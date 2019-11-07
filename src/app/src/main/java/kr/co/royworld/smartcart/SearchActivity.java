package kr.co.royworld.smartcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.royworld.smartcart.adapter.ItemAdapter;
import kr.co.royworld.smartcart.model.Product;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnHome;
    private EditText searchTerm;
    private GridView srchList;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference prdtRef = database.getReference("product");

    private HashMap<String, Product> products = new HashMap<>();
    private List<String> prdtKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

        searchTerm = findViewById(R.id.searchTerm);
        searchTerm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_UP){
                    reloadProductList();
                }
                return false;
            }
        });

        prdtRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product = dataSnapshot.getValue(Product.class);
                if(product != null){
                    product.setPrdtCode(dataSnapshot.getKey());
                    products.put(dataSnapshot.getKey(), product);

                    reloadProductList();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product = dataSnapshot.getValue(Product.class);
                if(product != null){
                    product.setPrdtCode(dataSnapshot.getKey());
                    products.put(dataSnapshot.getKey(), product);

                    reloadProductList();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                products.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        srchList = findViewById(R.id.srchList);
        srchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = products.get(prdtKeys.get(position));
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("prdtCode", product.getPrdtCode());
                startActivity(intent);
            }
        });
    }

    public void reloadProductList(){
        String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
        List<Product> list = new ArrayList<>();
        prdtKeys = new ArrayList<>();
        for(String key:products.keySet()){
            String prdtName = products.get(key).getPrdtName();
            if(searchTerm.equals("") || prdtName.toUpperCase().indexOf(searchTerm.toUpperCase()) >= 0){
                prdtKeys.add(key);
                list.add(products.get(key));
            }
        }

        ItemAdapter adapter = new ItemAdapter(list);
        srchList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnHome:finish();break;
        }
    }
}
