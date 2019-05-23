package com.exam.zzz_other_menu;

import java.util.ArrayList;

import android.content.res.Resources;
import android.content.Context;

import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;

import android.util.AttributeSet;
import android.util.Log;

import android.view.View;
import android.view.MotionEvent;

import com.example.n_mart.R;

//View Ŭ������ ��ӹ��� ����� ���� ��
public class TipsView extends View
{
	public Paint m_paint = null;	 // ���� �׸� �� ����� �Ӽ� paint
	public float m_x = -1, m_y = -1;	 // ���� �׸� ��ǥ�� ������ ����
	Paint paint = new Paint();

	private ArrayList<bitdata> image;
	private ArrayList<bitdata> image2;

	public TipsView(Context c, AttributeSet attrs){	    // XML ���ҽ� ���Ϸ� �䰡 �����Ǹ� ȣ��Ǵ� ������
		super(c, attrs);
		Log.e("hjs", "create TipsView by XML");

		m_paint = new Paint();		// ������ �Ӽ��� ���� Paint ��ü ����
		m_paint.setAntiAlias(true);
		m_paint.setColor(Color.RED);

		image = new ArrayList<bitdata>();
		image2 = new ArrayList<bitdata>();
		
		Resources r = c.getResources();

		image.add(new bitdata("���", BitmapFactory.decodeResource(r, R.drawable.apple_notbuy)));
		image.add(new bitdata("��", BitmapFactory.decodeResource(r, R.drawable.pear_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.bechoo_notbuy)));
		image.add(new bitdata("��", BitmapFactory.decodeResource(r, R.drawable.moo_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.onion_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.sangchoo_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.oe_notbuy)));
		image.add(new bitdata("ȣ��", BitmapFactory.decodeResource(r, R.drawable.pumpkin_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.beef_notbuy)));
		image.add(new bitdata("�������", BitmapFactory.decodeResource(r, R.drawable.pork_notbuy)));
		image.add(new bitdata("�߰��", BitmapFactory.decodeResource(r, R.drawable.chicken_notbuy)));
		image.add(new bitdata("�ް�", BitmapFactory.decodeResource(r, R.drawable.egg_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.jogi_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.myungtae_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.myungtae_notbuy)));
		image.add(new bitdata("��¡��", BitmapFactory.decodeResource(r, R.drawable.ojing_notbuy)));
		image.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.gofish_notbuy)));

		image2.add(new bitdata("���", BitmapFactory.decodeResource(r, R.drawable.apple_buy)));
		image2.add(new bitdata("��", BitmapFactory.decodeResource(r, R.drawable.pear_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.bechoo_buy)));
		image2.add(new bitdata("��", BitmapFactory.decodeResource(r, R.drawable.moo_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.onion_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.sangchoo_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.oe_buy)));
		image2.add(new bitdata("ȣ��", BitmapFactory.decodeResource(r, R.drawable.pumpkin_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.beef_buy)));
		image2.add(new bitdata("�������", BitmapFactory.decodeResource(r, R.drawable.pork_buy)));
		image2.add(new bitdata("�߰��", BitmapFactory.decodeResource(r, R.drawable.chicken_buy)));
		image2.add(new bitdata("�ް�", BitmapFactory.decodeResource(r, R.drawable.egg_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.jogi_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.myungtae_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.myungtae_buy)));
		image2.add(new bitdata("��¡��", BitmapFactory.decodeResource(r, R.drawable.ojing_buy)));
		image2.add(new bitdata("����", BitmapFactory.decodeResource(r, R.drawable.gofish_buy)));
	}
	
	public void onDraw(Canvas canvas) {		// �信 �׸��׸��� ������ ����ϴ� �޼ҵ�	
		Log.e("TipsView","onDraw() IN");

		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);

		Log.e("TipsView","onDraw() IN2");
/*		// ��ġ ������ �߻��� ��� �ش� ��ġ�� ���� �׸���.
		if(m_x > 0 && m_y > 0) {
			// (x - 5, y - 5) �� �������� ������ 10�� ���� �׸���.
			canvas.drawCircle(m_x - 5, m_y - 5, 10, m_paint);
		}*/

		// �⺻ ����
		Log.e("TipsView","onDraw() IN3");
		for(int i=0; i<list.itemstate.size(); i++){
			if(list.itemstate.get(i) == 0){
				double x = list.itemlist_x.get(i);
				double y = list.itemlist_y.get(i);
				String name = list.itemlist.get(i);
				DRAW(canvas, name, x, y);
			}
		}
		Log.e("TipsView","onDraw() IN9");
		for(int i=0; i<list.itemstate.size(); i++){
			if(list.itemstate.get(i) == 1){
				double x = list.itemlist_x.get(i);
				double y = list.itemlist_y.get(i);
				String name = list.itemlist.get(i);
				DRAW2(canvas, name, x, y);
			}
		}

		Log.e("TipsView","onDraw() OUT");
	}

	public void DRAW(Canvas canvas, String name, double x, double y){

		Log.e("TipsView_DRAW","IN");

		for(int i=0; i<image.size(); i++){
			if(name.contains(image.get(i).getName())){
				if(image.get(i).getName().contains("����")){
					canvas.drawBitmap(image.get(i).getBitmap(), (float) x, (float) y, null);
				} else {
					canvas.drawBitmap(image.get(i).getBitmap(), (float) x, (float) y, null);
				}
			}
		}
	}
	public void DRAW2(Canvas canvas, String name, double x, double y){

		Log.e("TipsView_DRAW2","IN");

		for(int i=0; i<image2.size(); i++){
			if(name.contains(image2.get(i).getName())){
				if(image2.get(i).getName().contains("����")){
					canvas.drawBitmap(image2.get(i).getBitmap(), (float) x, (float) y, null);
				} else {
					canvas.drawBitmap(image2.get(i).getBitmap(), (float) x, (float) y, null);
				}
			}
		}
	}

	public void DRAW3(Canvas canvas, int n){ // ���� ����� �Լ�. n�� ���ڰ���
		Log.e("TipsView_DRAW3","IN");
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		for(int i=0; i<600; i+=n){
			canvas.drawLine(0, i, 500, i, paint);
			canvas.drawLine(i, 0, i, 400, paint);
		}
	}
	
	// ��ġ �̺�Ʈ�� ó���ϴ� �ݹ� �޼ҵ�
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("��ġ�̺�Ʈ","IN");
		// ���� Ŭ������ View Ŭ������ �߻��� �̺�Ʈ�� �����Ѵ�.
		super.onTouchEvent(event);

		// � �̺�Ʈ�� �߻��Ͽ������� ���� ó���� �޶�����.
		switch (event.getAction()) {
		// DOWN �̳� MOVE �� �߻��� ���
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			// ��ġ�� �߻��� X, Y �� �� ��ǥ�� ��´�.
			m_x = event.getX();
			m_y = event.getY();
			Log.e("hjs", Float.toString(m_x) + "  " + Float.toString(m_y));
			Log.e("hjs", "onTouchEvent");
			// �並 �����Ѵ�.
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			// UP �� �߻��� ��� �׸��� ��ǥ�� -1 �� �����ϰ� �並 �����Ѵ�.
			m_y = m_x = -1; 
			invalidate();
			break;

		}
		// true �� ��ȯ�Ͽ� ���̻��� �̺�Ʈ ó���� �̷������ �ʵ��� �̺�Ʈ ó���� �Ϸ��Ѵ�.
		return true;
	}
}

class bitdata{

	private String name;
	private Bitmap bit;

	public bitdata(String pname, Bitmap pbit){
		this.name = pname;
		this.bit = pbit;
	}

	public String getName(){
		return name;
	}

	public Bitmap getBitmap(){
		return bit;
	}
}