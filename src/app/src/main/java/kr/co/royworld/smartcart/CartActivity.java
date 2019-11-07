package kr.co.royworld.smartcart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kr.co.royworld.smartcart.adapter.CartAdapter;
import kr.co.royworld.smartcart.model.BeaconData;
import kr.co.royworld.smartcart.model.Cart;
import kr.co.royworld.smartcart.model.Category;
import kr.co.royworld.smartcart.model.Position;
import kr.co.royworld.smartcart.model.Product;
import kr.co.royworld.smartcart.utils.LogUtils;
import kr.co.royworld.smartcart.view.MapView;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private MyBeaconManager beaconManager;
    private MapView mapView;
    private ListView cartList;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference cartRef = database.getReference("cart");
    private DatabaseReference eventRef = database.getReference("event");

    private HashMap<String, Cart> carts = new HashMap<>();
    private List<String> cartKeys;
    private Cart curCart, nextCart;
    private Category curCate;
    private final Position POS_COUNTER = new Position(1, 7);

    private HashMap<String, Product> eventProducts = new HashMap<>();

    private AlertDialog.Builder posDialog, cartDialog, nextDialog, evtDialog, cmpltDialog;
    private float rateX = -1f;
    private float rateY = -1f;
    private int dialogCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        mapView = findViewById(R.id.mapView);
        mapView.setOnTouchListener(this);
        // mapView.setCpos(new Position(1,9));

        (findViewById(R.id.btnHome)).setOnClickListener(this);

        // ###BEACON
        beaconManager = new MyBeaconManager(this);
        // ###BEACON

        cartRef.child("nomember").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                if(cart != null) {
                    carts.put(dataSnapshot.getKey(), cart);

                    reloadCartList();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                if(cart != null){
                    carts.put(dataSnapshot.getKey(), cart);

                    reloadCartList();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                carts.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Calendar cal = Calendar.getInstance();
        DecimalFormat nf = new DecimalFormat("00");
        String today = cal.get(Calendar.YEAR) + nf.format(cal.get(Calendar.MONTH) + 1) + nf.format(cal.get(Calendar.DATE));
        eventRef.child(today).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                eventProducts.put(dataSnapshot.getKey(), dataSnapshot.getValue(Product.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                eventProducts.put(dataSnapshot.getKey(), dataSnapshot.getValue(Product.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                carts.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cartList = findViewById(R.id.cartList);
        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curCart = carts.get(cartKeys.get(position));
                curCate = curCart.getProduct().getCate();
                if(curCart != null){
                    if(!curCart.isCartYn()){
                        mapView.setTpos(curCart.getProduct().getPos());

                        if(mapView.isNear()){
                            addCart();
                        }
                    }else{
                        int remainCnt = 0;
                        for(String key:carts.keySet()){
                            Cart cart = carts.get(key);
                            if(cart != null){
                                if(!cart.isCartYn())    remainCnt++;
                            }
                        }
                        if(remainCnt < 1)    goToCounter();
                    }
                }
            }
        });
    }

    private void showNextDialog(String msg){
        if(nextDialog == null){
            nextDialog = new AlertDialog.Builder(CartActivity.this);
            nextDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mapView.setTpos(null);
                    nextCart.setCartYn(true);
                    cartRef.child("nomember").child(nextCart.getProduct().getPrdtCode()).setValue(nextCart);
                    checkEventProduct(nextCart);

                    nextCart = null;
                    dialogCnt--;
                }
            });
            nextDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogCnt--;

                }
            });
        }
        dialogCnt++;
        nextDialog.setMessage(msg);
        nextDialog.show();
    }

    public void reloadCartList(){
        List<Cart> list = new ArrayList<>();
        cartKeys = new ArrayList<>();

        int amt = 0;
        for(String key:carts.keySet()){
            cartKeys.add(key);
            Cart cart = carts.get(key);
            if(cart != null){
                list.add(cart);

                int quantity = cart.getQty();
                int dscntPrice = cart.getProduct().getDscntPrice();
                amt += (quantity * dscntPrice);
            }
        }

        CartAdapter adapter = new CartAdapter(list);
        cartList.setAdapter(adapter);

        ((TextView)findViewById(R.id.sumAmt)).setText(NumberFormat.getInstance(Locale.KOREA).format(amt)  + "원");
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.btnHome){
            finish();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId() == R.id.mapView){
            rateX = motionEvent.getX();
            rateY = motionEvent.getY();

            if(posDialog == null){
                posDialog = new AlertDialog.Builder(CartActivity.this);
                posDialog.setMessage("지금 현재 이 위치에 계십니까?");
                posDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mapView.setTouchCpos(rateX, rateY);

                        if(mapView.isNear()){
                            addCart();
                        }
                        dialogCnt--;
                    }
                });
                posDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialogCnt--;

                    }
                });
            }
            dialogCnt++;
            posDialog.show();
        }
        return false;
    }

    public void addCart(){
        if(cartDialog == null){
            cartDialog = new AlertDialog.Builder(CartActivity.this);
            cartDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mapView.setTpos(null);
                    curCart.setCartYn(true);
                    cartRef.child("nomember").child(curCart.getProduct().getPrdtCode()).setValue(curCart);

                    checkEventProduct(curCart);

                    curCart = null;
                    dialogCnt--;
                }
            });
            cartDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogCnt--;

                }
            });
        }
        dialogCnt++;
        cartDialog.setMessage(curCart.getProduct().getPrdtName() + "을(를) 카트에 담으시겠습니까?");
        cartDialog.show();
    }

    public void checkEventProduct(Cart prod){
        nextCart = null;
        int remainCnt = 0;
        HashMap<String, Product> evtProduct = new HashMap<>();

        String ctgrId = prod.getProduct().getCate().getCtgrId();

        for(String key:carts.keySet()){
            Cart cart = carts.get(key);
            if(!cart.isCartYn()){
                remainCnt++;

                if(cart.getProduct().getCate().getCtgrId().equals(ctgrId)){
                    nextCart = cart;
                }
            }
        }

        if(nextCart != null){
            this.showNextDialog("장바구니에 상품 중 [" + nextCart.getProduct().getPrdtName() + "]이(가) 같은 위치에 있습니다. 함께 담으시겠습니까?");
        }else{
            for(String prdtCode:eventProducts.keySet()){
                if(eventProducts.get(prdtCode).getCate().getCtgrId().equals(ctgrId)){
                    // 같은 코너에 담기지 않은 행사상품이 있는 경우
                    if(carts.get(prdtCode) == null){
                        LogUtils.info("PRDT_CODE:" + prdtCode);
                        evtProduct.put(prdtCode, eventProducts.get(prdtCode));
                    }
                }
            }

            if(evtProduct.size() > 0){
                if(evtDialog == null){
                    evtDialog = new AlertDialog.Builder(CartActivity.this);
                    evtDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(curCate != null){
                                Intent intent = new Intent(CartActivity.this, EventActivity.class);
                                intent.putExtra("ctgrId", curCate.getCtgrId());
                                startActivity(intent);
                                dialogCnt--;
                            }
                        }
                    });
                    evtDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            dialogCnt--;

                        }
                    });
                }
                dialogCnt++;
                evtDialog.setMessage("장바구니에 상품 중 같은 코너에서 행사중인 상품이 " + evtProduct.size() + "개 더 있습니다. 확인하시겠습니까?");
                evtDialog.show();
            }

            if(remainCnt < 1){
                goToCounter();
            }
        }
    }

    public boolean canCallback(){
        return (dialogCnt < 1);
    }

    public boolean callbackBeacon(BeaconData beaconData){
        if(nextCart == null){
            for(String key:cartKeys){
                if(!carts.get(key).isCartYn() && carts.get(key).getProduct().getCate().getCtgrId().equals(beaconData.getCate().getCtgrId())){
                    nextCart = carts.get(key);
                }
            }

            if(nextCart != null){
                mapView.setCpos(beaconData.getCate().getPos());
                this.showNextDialog("장바구니에 상품 중 [" + nextCart.getProduct().getPrdtName() + "]이(가) 현재 위치에 있습니다. 함께 담으시겠습니까?");
            }
            Toast.makeText(this, beaconData.getBeaconName() + " 부근에 있습니다.", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void goToCounter(){
        if(cmpltDialog == null){
            cmpltDialog = new AlertDialog.Builder(CartActivity.this);
            cmpltDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mapView.setTpos(POS_COUNTER);
                    dialogCnt--;
                }
            });
            cmpltDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogCnt--;

                }
            });
        }
        dialogCnt++;
        cmpltDialog.setMessage("카운터로 이동하시겠습니까?");
        cmpltDialog.show();
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
