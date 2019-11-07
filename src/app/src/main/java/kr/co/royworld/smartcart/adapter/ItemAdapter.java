package kr.co.royworld.smartcart.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.co.royworld.smartcart.R;
import kr.co.royworld.smartcart.model.Product;

public class ItemAdapter extends BaseAdapter {
    private List<Product> products;

    public ItemAdapter(List<Product> products) {
        this.products = products;
        if(this.products == null)    this.products = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return products.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vi_item01, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imgUrl = convertView.findViewById(R.id.imgUrl) ;
        TextView prdtName = convertView.findViewById(R.id.prdtName) ;
        TextView sellPrice = convertView.findViewById(R.id.sellPrice) ;
        TextView dscntPrice = convertView.findViewById(R.id.dscntPrice) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Product product = products.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(context).load(product.getImgUrl()).into(imgUrl);
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

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return products.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Product item) {
        products.add(item);
    }
}