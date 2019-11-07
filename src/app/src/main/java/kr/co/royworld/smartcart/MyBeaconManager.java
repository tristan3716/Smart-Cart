package kr.co.royworld.smartcart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kr.co.royworld.smartcart.model.BeaconData;
import kr.co.royworld.smartcart.utils.BeaconUtils;
import kr.co.royworld.smartcart.utils.LogUtils;
import kr.co.royworld.smartcart.view.MapView;

public class MyBeaconManager extends BeaconManager {
    private Context mContext;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference beaconRef = database.getReference("beacon");
    private HashMap<String, BeaconData> beacons= new HashMap<>();
    private HashMap<String, Region> regions= new HashMap<>();

    private BeaconData preBeacon;

    public MyBeaconManager(final Context context){
        super(context);

        this.mContext = context;

        setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    BeaconData data = null;
                    int minRssi = 99999;
                    for(Beacon beacon:list){
                        if(beacon.getRssi() != 0 && minRssi > Math.abs(beacon.getRssi())){
                            minRssi = beacon.getRssi();

                            int rssi = beacon.getRssi();
                            int power = beacon.getMeasuredPower();
                            double accuracy = BeaconUtils.calculateAccuracy(power, rssi);
                            if(BeaconUtils.calculateProximity(accuracy) == BeaconUtils.PROXIMITY_NEAR){
                                data = getBeaconData(beacon.getProximityUUID().toString(), beacon.getMajor(), beacon.getMinor());
                                if(data != null){
                                    LogUtils.info( data.getBeaconName() + "\t" + rssi + "\t" + power + "\t" + accuracy);
                                }
                            }
                        }
                    }

                    if(data != null && (preBeacon == null || !preBeacon.getBeaconName().equals(data.getBeaconName()))){
                        // 근처 비콘이 있고 이전 비콘과 같은 비콘이 아닌경우
                        if(mContext instanceof  CartActivity){
                            if(((CartActivity)mContext).canCallback()){
                                ((CartActivity)mContext).callbackBeacon(data);
                            }
                        }
                    }
                }
            }
        });

        beaconRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String beaconName = dataSnapshot.getKey();
                BeaconData beaconData = dataSnapshot.getValue(BeaconData.class);
                if(regions.get(beaconData.getBeaconName()) == null){
                    regions.put(beaconName, new Region(beaconName, UUID.fromString(beaconData.getUuid()), beaconData.getMajor(), beaconData.getMinor()));
                    startRanging(regions.get(beaconName));
                }
                beacons.put(beaconName, beaconData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String beaconName = dataSnapshot.getKey();
                BeaconData beaconData = dataSnapshot.getValue(BeaconData.class);
                if(regions.get(beaconData.getBeaconName()) == null){
                    regions.put(beaconName, new Region(beaconName, UUID.fromString(beaconData.getUuid()), beaconData.getMajor(), beaconData.getMinor()));
                    startRanging(regions.get(beaconName));
                }
                beacons.put(beaconName, beaconData);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                beacons.remove(dataSnapshot.getKey());
                stopRanging(regions.remove(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private BeaconData getBeaconData(String uuid, int major, int minor){
        for(String key:beacons.keySet()){
            BeaconData data = beacons.get(key);
            if(data.getUuid().toUpperCase().equals(uuid.toUpperCase()) && data.getMajor() == major && data.getMinor() == minor){
                return data;
            }
        }
        return null;
    }

    public void connect(){
        super.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for(String key:regions.keySet()){
                    startRanging(regions.get(key));
                }
            }
        });
    }

    public void stopRanging(){
        for(String key:regions.keySet()){
            stopRanging(regions.get(key));
        }
    }
}
