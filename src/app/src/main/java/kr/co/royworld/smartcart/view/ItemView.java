package kr.co.royworld.smartcart.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.Locale;

import kr.co.royworld.smartcart.R;
import kr.co.royworld.smartcart.model.Product;

public class ItemView extends LinearLayout {
    private Product mProduct;
    public ItemView(Context context) {
        super(context); // 부모의 인자값이 있는 생성자를 호출한다

        initView();
    }

    public ItemView(Context context, Product product) {
        super(context); // 부모의 인자값이 있는 생성자를 호출한다

        mProduct = product;

        initView();
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs);

        initView();
    }

    private void initView() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.vi_item02, this, false);
        addView(v);

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imgUrl = v.findViewById(R.id.imgUrl) ;
        TextView prdtName = v.findViewById(R.id.prdtName) ;
        TextView sellPrice = v.findViewById(R.id.sellPrice) ;
        TextView dscntPrice = v.findViewById(R.id.dscntPrice) ;

        if(mProduct != null){
            // 아이템 내 각 위젯에 데이터 반영
            Glide.with(getContext()).load(mProduct.getImgUrl()).into(imgUrl);
            prdtName.setText(mProduct.getPrdtName());
            sellPrice.setText(NumberFormat.getInstance(Locale.KOREA).format(mProduct.getSellPrice())  + "원");
            dscntPrice.setText(NumberFormat.getInstance(Locale.KOREA).format(mProduct.getDscntPrice())  + "원");
            if(mProduct.getSellPrice() != mProduct.getDscntPrice()){
                sellPrice.setPaintFlags(sellPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dscntPrice.setVisibility(View.VISIBLE);
            }else{
                sellPrice.setPaintFlags(sellPrice.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                dscntPrice.setVisibility(View.GONE);
            }
        }
    }

    public void setProduct(Product product){
        this.mProduct = product;

        if(mProduct != null){
            ImageView imgUrl = findViewById(R.id.imgUrl) ;
            TextView prdtName = findViewById(R.id.prdtName) ;
            TextView sellPrice = findViewById(R.id.sellPrice) ;
            TextView dscntPrice = findViewById(R.id.dscntPrice) ;

            // 아이템 내 각 위젯에 데이터 반영
            Glide.with(getContext()).load(mProduct.getImgUrl()).into(imgUrl);
            prdtName.setText(mProduct.getPrdtName());
            sellPrice.setText(NumberFormat.getInstance(Locale.KOREA).format(mProduct.getSellPrice())  + "원");
            dscntPrice.setText(NumberFormat.getInstance(Locale.KOREA).format(mProduct.getDscntPrice())  + "원");
            if(mProduct.getSellPrice() != mProduct.getDscntPrice()){
                sellPrice.setPaintFlags(sellPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dscntPrice.setVisibility(View.VISIBLE);
            }else{
                sellPrice.setPaintFlags(sellPrice.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                dscntPrice.setVisibility(View.GONE);
            }
        }
    }

    public Product getProduct() {
        return this.mProduct;
    }
}
