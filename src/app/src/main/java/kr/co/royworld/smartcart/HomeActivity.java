package kr.co.royworld.smartcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.estimote.sdk.SystemRequirementsChecker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kr.co.royworld.smartcart.model.BeaconData;
import kr.co.royworld.smartcart.model.Cart;
import kr.co.royworld.smartcart.model.Category;
import kr.co.royworld.smartcart.model.CategorySale;
import kr.co.royworld.smartcart.model.Event;
import kr.co.royworld.smartcart.model.Position;
import kr.co.royworld.smartcart.model.Product;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference cateRef = database.getReference("category");
    private DatabaseReference eventRef = database.getReference("event");
    private DatabaseReference prodRef = database.getReference("product");
    private DatabaseReference cartRef = database.getReference("cart");
    private DatabaseReference beaconRef = database.getReference("beacon");
    private DatabaseReference saleRef = database.getReference("sale");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        (findViewById(R.id.btnCart)).setOnClickListener(this);
        (findViewById(R.id.btnSearch)).setOnClickListener(this);
        (findViewById(R.id.btnEvent)).setOnClickListener(this);
        (findViewById(R.id.btnMap)).setOnClickListener(this);
        (findViewById(R.id.btnChart)).setOnClickListener(this);

        initDb();
    }

    private void initDb(){
        // 카테고리정보[s]
        cateRef.removeValue();
        Category c0001 = new Category("C0001", "음료", new Position(0, 4));
        Category c0002 = new Category("C0002", "과자류", new Position(11, 4));
        Category c0003 = new Category("C0003", "야채", new Position(7, 9));
        Category c0004 = new Category("C0004", "생선", new Position(4, 0));
        cateRef.child("C0001").setValue(c0001);
        cateRef.child("C0002").setValue(c0002);
        cateRef.child("C0003").setValue(c0003);
        // 카테고리정보[e]

        // 상품정보[s]
        Product[] products = {
                new Product("10001", "오감자 그라탕", 1200, 900, "https://firebasestorage.googleapis.com/v0/b/smartcart-d2246.appspot.com/o/thumb_prdt01.jpg?alt=media&token=d6440a86-71e1-4a91-9363-6d125da2c20c", c0002, new Position(11, 4))
                , new Product("10002", "오징어땅콩", 1500, 1000, "https://firebasestorage.googleapis.com/v0/b/smartcart-d2246.appspot.com/o/thumb_prdt02.jpg?alt=media&token=52856079-3afd-4abd-aeed-03636dfe806a", c0002, new Position(11, 4))
                , new Product("10003", "포카칩 양파", 1500, 1000, "https://firebasestorage.googleapis.com/v0/b/smartcart-d2246.appspot.com/o/thumb_prdt03.jpg?alt=media&token=501f3355-cd5f-46f0-9cd4-fdf5c83b344a", c0002, new Position(11, 4))
                , new Product("10004", "코카콜라", 1200, 1000, "https://firebasestorage.googleapis.com/v0/b/smartcart-d2246.appspot.com/o/thumb_prdt04.jpg?alt=media&token=1a04fc7d-626e-437b-8ead-e6fa0926bbea", c0001, new Position(0, 4))
                , new Product("10005", "당근", 1000, 900, "https://firebasestorage.googleapis.com/v0/b/smartcart-d2246.appspot.com/o/thumb_prdt05.jpg?alt=media&token=bb40cd48-0118-4d87-9545-85b4bb8d568f", c0003, new Position(7, 9))
        };
        prodRef.removeValue();
        for(Product product:products){
            prodRef.child(product.getPrdtCode()).setValue(product);
        }
        // 상품정보[e]

        // 이벤트정보[s]
        Event[] events = {
                new Event("20190614", new Product[]{products[0], products[1], products[2]})
                , new Event("20190615", new Product[]{products[2], products[3], products[4]})
                , new Event("20190616", new Product[]{products[0], products[1], products[2]})
                , new Event("20190617", new Product[]{products[1], products[3], products[4]})
                , new Event("20190618", new Product[]{products[0], products[2], products[4]})
        };
        eventRef.removeValue();
        for(Event event:events){
            for(Product prod:event.getProd()){
                eventRef.child(event.getDate()).child(prod.getPrdtCode()).setValue(prod);
            }
        }
        // 이벤트정보[e]

        // 비콘정보[s]
        BeaconData[] beacons = {
                new BeaconData("음료373", "FDA50693-A4E2-4FB1-AFCF-C6EB07647825", 37301, 20001, c0001)
                , new BeaconData("과자374", "FDA50693-A4E2-4FB1-AFCF-C6EB07647825", 37401, 21700, c0002)
                , new BeaconData("야채379", "FDA50693-A4E2-4FB1-AFCF-C6EB07647825", 37901, 20001, c0003)
                , new BeaconData("카운터376", "FDA50693-A4E2-4FB1-AFCF-C6EB07647825", 37601, 20001, c0004)
        };
        beaconRef.removeValue();
        for(BeaconData beaconData : beacons){
            beaconRef.child(beaconData.getBeaconName()).setValue(beaconData);
        }

        // 비콘정보[e]

        // 장바구니정보[s]
        Cart[] carts = {
                new Cart(products[0], 1, false)
                // , new Cart(products[1], 3, false)
                , new Cart(products[2], 2, false)
                , new Cart(products[3], 4, false)
                , new Cart(products[4], 2, false)
        };

        cartRef.removeValue();
        for(Cart cart:carts){
            cartRef.child("nomember").child(cart.getProduct().getPrdtCode()).setValue(cart);
        }
        // 장바구니정보[e]


        // 매출정보[s]
        CategorySale[] sales = {
                new CategorySale(c0001, 10, 100000)
                , new CategorySale(c0002, 40, 440000)
                , new CategorySale(c0003, 33, 520000)
                , new CategorySale(c0004, 20, 120000)
        };
        for(CategorySale sale:sales){
            saleRef.child("20190617").child(sale.getCate().getCtgrId()).setValue(sale);
        }

        CategorySale[] sales01 = {
                new CategorySale(c0001, 30, 120000)
                , new CategorySale(c0002, 20, 110000)
                , new CategorySale(c0003, 13, 440000)
                , new CategorySale(c0004, 30, 230000)
        };
        for(CategorySale sale:sales01){
            saleRef.child("20190616").child(sale.getCate().getCtgrId()).setValue(sale);
        }
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnCart:moveToIntent(CartActivity.class);break;
            case R.id.btnSearch:moveToIntent(SearchActivity.class);break;
            case R.id.btnEvent:moveToIntent(EventActivity.class);break;
            case R.id.btnMap:moveToIntent(MapActivity.class);break;
            case R.id.btnChart:moveToIntent(ChartActivity.class);break;
        }
    }

    /**
     * Activity이동
     * @param activity 대상 Activity
     */
    private void moveToIntent(Class activity){
        startActivity(new Intent(HomeActivity.this, activity));
    }

    @Override
    protected void onResume(){
        super.onResume();

        // 블루투스 권한 및 활성화 코드
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }
}