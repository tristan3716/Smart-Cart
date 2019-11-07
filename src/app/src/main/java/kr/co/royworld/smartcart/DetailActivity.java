package kr.co.royworld.smartcart;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

import kr.co.royworld.smartcart.model.Cart;
import kr.co.royworld.smartcart.model.Product;
import kr.co.royworld.smartcart.utils.LogUtils;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText qty;
    private TextView amt;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference cartRef = database.getReference("cart");
    private DatabaseReference prdtRef = database.getReference("product");

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        String prdtCode = getIntent().getStringExtra("prdtCode");

        (findViewById(R.id.btnHome)).setOnClickListener(this);

        prdtRef.child(prdtCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                product = dataSnapshot.getValue(Product.class);
                if (product != null) {
                    product.setPrdtCode(dataSnapshot.getKey());
                    reloadProduct();
                } else {
                    Toast.makeText(getApplicationContext(), "해당 상품이 존재하지않습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러가 날때마다 처리
                // Failed to read value
                LogUtils.error(error.getMessage());
            }
        });

        qty = findViewById(R.id.qty);
        qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String qtyStr = qty.getText().toString();
                if(!qtyStr.equals("")){
                    int quantity = Integer.parseInt(qtyStr);
                    if(quantity < 1){
                        Toast.makeText(getApplicationContext(), "최소 1개 이상은 입력해주어야 합니다.", Toast.LENGTH_SHORT).show();
                        qty.setText("1");
                        quantity = 1;
                    }
                    amt.setText(NumberFormat.getInstance(Locale.KOREA).format(product.getDscntPrice() * quantity)  + "원");
                }
                return false;
            }
        });
        (findViewById(R.id.btnMinus)).setOnClickListener(this);
        (findViewById(R.id.btnPlus)).setOnClickListener(this);
        (findViewById(R.id.btnMap)).setOnClickListener(this);
        (findViewById(R.id.btnBuy)).setOnClickListener(this);

        amt = findViewById(R.id.amt);
    }

    public void reloadProduct(){
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imgUrl = findViewById(R.id.imgUrl) ;
        TextView prdtName = findViewById(R.id.prdtName) ;
        TextView sellPrice = findViewById(R.id.sellPrice) ;
        TextView dscntPrice = findViewById(R.id.dscntPrice) ;

        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(this).load(product.getImgUrl()).into(imgUrl);
        prdtName.setText(product.getPrdtName());
        sellPrice.setText(NumberFormat.getInstance(Locale.KOREA).format(product.getSellPrice())  + "원");
        dscntPrice.setText(NumberFormat.getInstance(Locale.KOREA).format(product.getDscntPrice())  + "원");
        if(product.getSellPrice() != product.getDscntPrice()){
            sellPrice.setPaintFlags(sellPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dscntPrice.setVisibility(View.VISIBLE);
        }else{
            sellPrice.setPaintFlags(sellPrice.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            dscntPrice.setVisibility(View.GONE);
        }

        int quantity = Integer.parseInt(qty.getText().toString());
        amt.setText(NumberFormat.getInstance(Locale.KOREA).format(product.getDscntPrice() * quantity)  + "원");
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnHome:finish();break;
            case R.id.btnMinus:decrease();break;
            case R.id.btnPlus:increase();break;
            case R.id.btnMap:showMap();break;
            case R.id.btnBuy:buy();break;
        }
    }

    private void decrease(){
        int quantity = Integer.parseInt(qty.getText().toString());
        if(quantity <= 1){
            Toast.makeText(this, "최소 1개 이상은 입력해주어야 합니다.", Toast.LENGTH_SHORT).show();
        }else{
            quantity--;
            qty.setText(String.valueOf(quantity));

            amt.setText(NumberFormat.getInstance(Locale.KOREA).format(product.getDscntPrice() * quantity)  + "원");
        }
    }

    private void increase(){
        int quantity = Integer.parseInt(qty.getText().toString());
        quantity++;
        qty.setText(String.valueOf(quantity));

        amt.setText(NumberFormat.getInstance(Locale.KOREA).format(product.getDscntPrice() * quantity)  + "원");
    }

    private void showMap(){
        Intent intent = new Intent(DetailActivity.this, MapActivity.class);
        intent.putExtra("posX", product.getPos().getPosX());
        intent.putExtra("posY", product.getPos().getPosY());
        startActivity(intent);
    }

    private void buy(){
        int price = product.getDscntPrice();
        int quantity = Integer.parseInt(qty.getText().toString());
        cartRef.child("nomember").child(product.getPrdtCode()).setValue(new Cart(product, quantity, false));

        Toast.makeText(this, product.getPrdtName() + " " + quantity + "개가 장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show();
    }
}
