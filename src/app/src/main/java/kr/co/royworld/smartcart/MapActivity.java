package kr.co.royworld.smartcart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.estimote.sdk.SystemRequirementsChecker;

import kr.co.royworld.smartcart.model.Position;
import kr.co.royworld.smartcart.view.MapView;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private MyBeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        (findViewById(R.id.btnHome)).setOnClickListener(this);

        int posX = getIntent().getIntExtra("posX", -1);
        int posY = getIntent().getIntExtra("posY", -1);
        if(posX != -1 && posY != -1){
            ((MapView)findViewById(R.id.mapView)).setTpos(new Position(posX, posY));
        }

        // ###BEACON
        beaconManager = new MyBeaconManager(this);
        // ###BEACON
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.btnHome){
            finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        // 블루투스 권한 및 활성화 코드
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect();
    }

    @Override
    protected void onPause(){
        beaconManager.stopRanging();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (beaconManager != null)    beaconManager.disconnect();

        super.onDestroy();
    }
}
