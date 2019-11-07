package kr.co.royworld.smartcart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import kr.co.royworld.smartcart.adapter.SaleDetailAdapter;
import kr.co.royworld.smartcart.adapter.SaleSummaryAdapter;
import kr.co.royworld.smartcart.model.CategorySale;
import kr.co.royworld.smartcart.model.Constants;
import kr.co.royworld.smartcart.utils.LogUtils;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference saleRef = database.getReference("sale");
    private List<CategorySale> sales = new ArrayList<>();
    private PieChartView pieChart;
    private TextView txtTotal;
    private ListView listTotal, listDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        Calendar cal = Calendar.getInstance();
        DecimalFormat nf = new DecimalFormat("00");
        String today = cal.get(Calendar.YEAR) + nf.format(cal.get(Calendar.MONTH) + 1) + nf.format(cal.get(Calendar.DATE));
        saleRef.child(today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sales = new ArrayList<>();

                int totalQty = 0;
                int totalAmt = 0;

                Iterable<DataSnapshot> iter01 = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iter02 = iter01.iterator();
                while(iter02.hasNext()){
                    CategorySale sale = iter02.next().getValue(CategorySale.class);
                    totalAmt += sale.getAmt();
                    totalQty += sale.getQty();
                    sales.add(sale);
                }

                // 파이차트
                List<SliceValue> pieData = new ArrayList<>();
                for(int i = 0; i < sales.size(); i++){
                    CategorySale sale = sales.get(i);
                    SliceValue value = new SliceValue(sale.getAmt(), Color.parseColor(Constants.COLORS_STR[i % Constants.COLORS_STR.length]));
                    value.setLabel(sale.getCate().getCtgrName());
                    pieData.add(value);
                }
                PieChartData data = new PieChartData(pieData);
                data.setHasLabels(true).setValueLabelTextSize(14);
                data.setHasCenterCircle(true)
                        .setCenterText1("DAILY SALES")
                        .setCenterText1FontSize(20)
                        .setCenterText1Color(Color.parseColor("#0097A7"));
                pieChart.setPieChartData(data);

                txtTotal.setText(NumberFormat.getInstance(Locale.KOREA).format(totalAmt)  + "원");

                listTotal.setAdapter(new SaleSummaryAdapter(sales));

                // ScrollView에서 List뷰를 제대로 설정 못하므로 높이를 재산정해서 강제로 넣어줌
                SaleDetailAdapter adapterDtl = new SaleDetailAdapter(sales);
                listDetail.setAdapter(adapterDtl);
                int totalHeight = 0;
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(listDetail.getWidth(), View.MeasureSpec.AT_MOST);
                for (int i = 0; i < adapterDtl.getCount(); i++) {
                    View listItem = adapterDtl.getView(i, null, listDetail);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight() + listItem.getPaddingTop();
                }

                ViewGroup.LayoutParams params = listDetail.getLayoutParams();
                params.height = totalHeight + (listDetail.getDividerHeight() * (adapterDtl.getCount() - 1));
                listDetail.setLayoutParams(params);
                listDetail.requestLayout();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러가 날때마다 처리
                // Failed to read value
                LogUtils.error(error.getMessage());
            }
        });

        (findViewById(R.id.btnHome)).setOnClickListener(this);

        pieChart = findViewById(R.id.chart);
        txtTotal = findViewById(R.id.txtTotal);
        listTotal = findViewById(R.id.listTotal);
        listTotal.setVerticalScrollBarEnabled(false);
        listTotal.setHorizontalScrollBarEnabled(false);
        listDetail = findViewById(R.id.listDetail);
        listDetail.setVerticalScrollBarEnabled(false);
        listDetail.setHorizontalScrollBarEnabled(false);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnHome:finish();break;
        }
    }
}
