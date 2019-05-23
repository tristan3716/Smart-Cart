package com.exam.zzz_other_menu;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;

import android.content.Intent;

import android.database.Cursor;

import android.os.Bundle;

import android.util.Log;

import android.widget.Toast;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;

// ���� ������ �߱� �� �ͽ������ ����Ʈ�信 �׸���� �����ϱ� ���� ��Ƽ��Ƽ
public class info extends Activity {

	MySQLiteHandler handler; // �����ͺ��̽� ����ϱ� ���� handler
	Cursor c; // �����ͺ��̽� ������� ���̺��� ã���� Ŀ��

	ArrayList<Info_save_class> item_list; // Parsing_data.java���� �Ѿ���� ��̸���Ʈ�� ������ ��̸���Ʈ

	int item_breakpoint = 0;
	int item_node_length = 0;
	String temp = ""; // 7�����̺� ����� �����̸��� �ߺ��Ǵ� �� �����ϱ� ���ؼ� ���

	// ��¥ �����ϱ� ���� ����
	int year;
	int month;
	int date;
	int hour;
	int minute;
	int second;
	
	double x[]; // x��ǥ
	double y[]; // y��ǥ

	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("info.java", "onCreate!!");
		super.onCreate(savedInstanceState);

		item_list = new ArrayList<Info_save_class>();

		// ��ǰ x��ǥ
		x = new double[]
				{258.0, 329.0,
				189.0, 132.0, 7.0, 190.0, 66.0, 7.0,
				262.0, 335.0, 338.0, 415.0,
				7.0, 7.0, 72.0, 62.0};

		// ��ǰ y��ǥ
		y = new double[]
				{110.0, 110.0,
				110.0, 110.0, 100.0, 43.0, 43.0, 43.0,
				515.0, 515.0, 580.0, 515.0,
				278.0, 475.0, 375.0, 580.0};

		Log.d("info.java", "����Ʈ�� �� �ǳ� ���ô�");
		Intent intent2 = getIntent();
		if(intent2!=null) {

			Log.d("info.java", "����Ʈ�� �� �˴ϴ�");
			item_list = (ArrayList<Info_save_class>)getIntent().getSerializableExtra("item_list");
			item_breakpoint = intent2.getIntExtra("length", 0);
			item_node_length = intent2.getIntExtra("item_length", 0);
		}

		handler = MySQLiteHandler.open(getApplicationContext());

		Log.d("info.java", "���⼭���� ������ �����մϴ�");

		Calendar cal = Calendar.getInstance();

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		date = cal.get(Calendar.DATE);
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
		second = cal.get(Calendar.SECOND);
/*
		Log.d("Ȯ��", Integer.toString(year));
		Log.d("Ȯ��", Integer.toString(month));
		Log.d("Ȯ��", Integer.toString(date));
		Log.d("Ȯ��", Integer.toString(hour));
		Log.d("Ȯ��", Integer.toString(minute));
		Log.d("Ȯ��", Integer.toString(second));
*/
		c = handler.select(9);
		c.moveToFirst();

		if(c.getCount()==0){
			handler.insert_date(year, month, date, hour, minute, second);

			Table_3();
			Table_5();
			Table_6();
			Table_7();
			Table_8();
		} else {
			if(c.getInt(1)==year && c.getInt(2)==month && c.getInt(3)==date/* && c.getInt(4)==hour && c.getInt(5)==minute*/){
				Log.d("���Ƽ� �׳� �Ѿ�ϴ�", "1");
			} else {
				handler.update(year, month, date, hour, minute, second);

				handler.delete_info();

				Toast.makeText(getApplicationContext(), "���� ó�� �����ϼ����Ƿ� ������ �ٽ� ���ϴ�", Toast.LENGTH_SHORT).show();

				Table_3();
				Table_5();
				Table_6();
				Table_7();
				Table_8();
			}
		}

		handler.close();

		Log.d("info.java", "���� ��Ƽ��Ƽ�� �Ѿ�� ����Ʈ�κ��Դϴ�");

		Intent intent = new Intent(getApplicationContext(), SelectMenu.class); // �����ͺ��̽��� ������ Main��Ƽ��Ƽ�� ��ȯ
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP); // ��Ƽ��Ƽ �����ϱ� ����
		startActivity(intent); // ����Ʈ ����
		finish();
	}

	public void Table_3(){
		c = handler.select(3);
		c.moveToFirst();

		if(c.getCount()==0) {

			String[] Name = new String[]{"����","��깰","��깰","���깰"};

			Log.d("info.java", "��ǰ��׷��� �����մϴ�");
			for(int i=0; i<Name.length; i++) {
				handler.insert("", Name[i], 0, 0, 0, 0, 0, 0, 0, 3);
			}
		}
	}

	public void Table_5(){
		c = handler.select(5);
		c.moveToFirst();

		int l = 0;
		
		if(c.getCount()==0){

			Log.d("info.java", "��ǰ�ұ׷��� �����մϴ�");
			for(int k=0; k<item_breakpoint; k++) {
				handler.insert(item_list.get(k).getMartName(), item_list.get(k).getName(), item_list.get(k).getPrice(), 1, x[l], y[l], 0, item_list.get(k).getLat(), item_list.get(k).getLon(), 5); // ���� �׸���� �����ͺ��̽��� ����
				
				if(l==15){
					l=0;
				} else {
					l++;
				}
			}
		}
	}

	public void Table_6(){

		String[] large_mart_name = new String[]{"�Ե���Ʈ","�̸�Ʈ","Ȩ�÷���","�ϳ��θ�Ʈ","�ż����ȭ��","�����ȭ��","�Ե���ȭ��"};

		c = handler.select(6);
		c.moveToFirst();

		if(c.getCount()==0){

			Log.d("info.java", "�����׷��� �����մϴ�");
			for(int i=0; i<large_mart_name.length; i++) {
				handler.insert(large_mart_name[i], "", 0, 0, 0, 0, 0, 0, 0, 6);
			}
		}
	}

	public void Table_7(){
		c = handler.select(7);
		c.moveToFirst();

		if(c.getCount()==0){
			Log.d("info.java", "����ұ׷��� �����մϴ�");
			
			for(int i=0; i<item_breakpoint; i++) {
				if(temp.equals(item_list.get(i).getMartName())){
					Log.d("info.java", "�ұ׷������ �ߺ�������");
				}
				else{
					handler.insert_bookmarker(item_list.get(i).getMartName(), 0);
					temp = item_list.get(i).getMartName();
				}
			}
		}
	}

	public void Table_8(){
		c = handler.select(8);
		c.moveToFirst();

		if(c.getCount()==0){
			handler.insert_length(item_breakpoint, item_node_length);
		}
	}
}