package kr.co.royworld.smartcart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kr.co.royworld.smartcart.adapter.ItemAdapter;
import kr.co.royworld.smartcart.model.Product;
import kr.co.royworld.smartcart.view.ItemView;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnHome;
    private ListView evtList01;
    private ScrollView evtList02;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference eventRef = database.getReference("event");
    private DatabaseReference prodRef = database.getReference("product");

    private HashMap<String, List<Product>> events = new HashMap<>();
    private List<String> eventKeys;
    private List<Product> listLeft;

    private String ctgrId = null;
    private String today = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);

        ctgrId = getIntent().getStringExtra("ctgrId");
        Calendar cal = Calendar.getInstance();
        DecimalFormat nf = new DecimalFormat("00");
        today = cal.get(Calendar.YEAR) + nf.format(cal.get(Calendar.MONTH) + 1) + nf.format(cal.get(Calendar.DATE));

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String date = dataSnapshot.getKey();
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    List<Product> list = new ArrayList<>();
                    while(iter.hasNext()){
                        DataSnapshot snap = iter.next();
                        Product product = snap.getValue(Product.class);
                        if(ctgrId == null || ctgrId.equals("")|| product.getCate().getCtgrId().equals(ctgrId)) {
                            list.add(product);
                        }
                    }
                    events.put(date, list);
                reloadEventList();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String date = dataSnapshot.getKey();
                if(date.compareTo(today) >= 0){
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    List<Product> list = new ArrayList<>();
                    while(iter.hasNext()){
                        DataSnapshot snap = iter.next();
                        Product product = snap.getValue(Product.class);
                        if(ctgrId == null || ctgrId.equals("")|| product.getCate().getCtgrId().equals(ctgrId)){
                            list.add(product);
                        }
                    }
                    events.put(date, list);
                }
                reloadEventList();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                events.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        evtList01 = findViewById(R.id.evtList01);
        evtList01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = listLeft.get(position);
                Intent intent = new Intent(EventActivity.this, DetailActivity.class);
                intent.putExtra("prdtCode", product.getPrdtCode());
                startActivity(intent);
            }
        });

        evtList02 = findViewById(R.id.evtList02);
    }

    public void reloadEventList(){
        eventKeys = new ArrayList<>();
        for(String key:events.keySet()){
            eventKeys.add(key);
        }

        Collections.sort(eventKeys, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);

            }
        });
        // Collections.reverse(eventKeys);

        loadProductLeft();
        loadProductRight();
    }

    public void loadProductLeft(){
        listLeft = null;
        if(eventKeys.size() > 0) {
            String date = eventKeys.get(0);
            listLeft = events.get(date);
        }
        ItemAdapter adapter = new ItemAdapter(listLeft);
        evtList01.setAdapter(adapter);
    }

    public void loadProductRight(){
        if(evtList02 != null)    evtList02.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(events.size() > 1){
            // sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.VERTICAL);

            for(int i = 1; i < eventKeys.size(); i++){
                String date = eventKeys.get(i);

                TextView tit = new TextView(this);
                String titTxt = date.substring(4, 6) + "/" + date.substring(6, 8) + " 행사상품";
                tit.setLayoutParams(params);
                tit.setText(titTxt);
                tit.setPadding(20, 20, 20, 20);
                tit.setBackgroundColor(Color.LTGRAY);
                ll.addView(tit);


                List<Product> products = events.get(date);
                for(int j = 0; j < products.size(); j=j+2){
                    LinearLayout lv = new LinearLayout(this);
                    lv.setLayoutParams(params);
                    lv.setPadding(20, 20, 20, 20);
                    lv.setOrientation(LinearLayout.HORIZONTAL);

                    for(int k = j; k < products.size() && k < j+2; k++){
                        ItemView item = new ItemView(this, products.get(k));
                        item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ItemView item = (ItemView)view;
                                Product product = item.getProduct();

                                Intent intent = new Intent(EventActivity.this, DetailActivity.class);
                                intent.putExtra("prdtCode", product.getPrdtCode());
                                startActivity(intent);
                            }
                        });
                        lv.addView(item);
                    }
                    ll.addView(lv);
                }
            }

            evtList02.addView(ll);
        }
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnHome:finish();break;
        }
    }
}
