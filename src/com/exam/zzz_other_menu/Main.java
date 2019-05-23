package com.exam.zzz_other_menu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;

import android.database.Cursor;

import android.nfc.NfcAdapter;

import android.os.Bundle;

import android.util.Log;

import android.view.Window;
import android.view.View;
//import android.view.KeyEvent;

import android.widget.Button;
import android.widget.ExpandableListView;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;
import com.example.n_mart.R;

public class Main extends Activity {

	private static final String TAG = "nfcinventory_simple";

	// nfc���� ���
	NfcAdapter mNfcAdapter; // ���� nfc�ϵ������� �ٸ� ������ ��� // nfc������ üũ�Ǿ� �ִ��� ���� ���� // nfc�±׿��� ndef�����͸� �о� ���ų� �ݴ�� ���� �� �� ��� ����
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	IntentFilter[] mWriteTagFilters;

	MySQLiteHandler handler; // �����ͺ��̽� ������� �ڵ鷯 ����
	Cursor c; // ���ϴ� �����ͺ��̽� ���̺� ������� Ŀ��

	ExpandableListView lv; // �ͽ������ ����Ʈ�� ����
	ArrayList<ArrayList<Model>> childList; // �ұ׷쿡 ������ ����Ʈ ����
	ArrayList<GM> groups; // ��׷쿡 ������ ����Ʈ ����
	Button b1; // ��ư����
	Adapter adapter; // �������� ����

	int length = 100;
	String temp[] = new String[length];

	int fruit = 0;
	int farm = 0;
	int animal = 0;
	int fish = 0;
	int item_length = 0;
	int item_size = 0;

	String mart_name;

	public void onCreate(Bundle savedInstanceState)
	{
		Log.w("Main.java", "oncreate!!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 	 // �׼ǹ� ���ֱ�
		setContentView(R.layout.main_exlist);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null){
		} else {
			mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
					getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

			IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
			try	{
				ndefDetected.addDataType("application/root.gast.playground.nfc");
			} catch (MalformedMimeTypeException e) {
				throw new RuntimeException("Could not add MIME type.", e);
			}
		}
		
		Intent intent = getIntent();
		mart_name = intent.getExtras().get("select_mart_name").toString();

		Log.w("Main.java", "Handler ������� ����");
		handler = MySQLiteHandler.open(getApplicationContext()); // �����ͺ��̽� ������� open ����

		Log.w("Main.java", "1�� ���̺��� �ҷ��ɴϴ�");
		c = handler.select(1);
		c.moveToFirst();

		for(int i=0; i<length; i++){
			temp[i]="";
		}

		for(int i=0; i<c.getCount(); i++){
			temp[i] = c.getString(2).toString();
			c.moveToNext();
		}

		lv = (ExpandableListView) findViewById(R.id.expandableListView1); // �ͽ������ ����Ʈ�� ����
		groups = new ArrayList<GM>(); // ��׷쿡 ������ ����Ʈ ����

		Log.w("Main.java", "3�� ���̺��� �ҷ��ɴϴ�");
		c = handler.select(3);
		c.moveToFirst();

		if(c.getCount()!=0){ // �����ͺ��̽��� ������ �־�߸� ����ǵ��� ����
			Log.w("Main.java", "��ǰ ��׷����� ���� ����");
			for(int i=0; i<c.getCount(); i++) {
				GM g = new GM(); // ��׷츮��Ʈ�� ������ü
				g.setName(c.getString(1).toString()); // ��׷츮��Ʈ�� ��������->�׷��̸�
				groups.add(g); // ��׷��̸���Ʈ�� ����

				c.moveToNext();
			}
			Log.w("Main.java", "��ǰ ��׷����� ���� ��");
		}

		Log.w("Main.java", "��ǰ �ұ׷����� �����ϱ� ���� �غ�");
		childList = new ArrayList<ArrayList<Model>>(); // �ұ׷츮��Ʈ ����
		ArrayList<Model> list = new ArrayList<Model>(); // �ұ׷츮��Ʈ�� ���� ��������Ʈ ����
		Model m = new Model(); // �ұ׷���������Ʈ ������ü

		Log.w("Main.java", "8�����̺��� �ҷ��ɴϴ�");
		c = handler.select(8);
		c.moveToFirst();

		item_length = c.getInt(2);
		item_size = c.getInt(1);

		Log.w("Main.java", "5�����̺��� �ҷ��ɴϴ�");
		c = handler.select(5);
		c.moveToFirst();

		if(c.getCount()!=0){ // �����ͺ��̽��� ������ �־�߸� ����ǵ��� ����

			Log.w("Main.java", "��ǰ�ұ׷����� �����غ� ����");
			for(int i=0; i<item_size; i++){
				if(c.getString(1).toString().equals(mart_name)){
					for(int j=0; j<item_length; j++) {
						if((c.getString(2).toString().contains("���"))||
								(c.getString(2).toString().contains("��"))){
							if(c.getString(2).toString().contains("����")){
								c.moveToNext();
								farm++;
							}
							else {
								c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
								fruit++;
							}
						}
						else if((c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("��"))||
								(c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("ȣ��"))){
							c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
							farm++;
						}
						else if((c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("�������"))||
								(c.getString(2).toString().contains("�߰��"))||
								(c.getString(2).toString().contains("�ް�"))){
							c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
							animal++;
						}
						else if((c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("����"))||
								(c.getString(2).toString().contains("��¡��"))||
								(c.getString(2).toString().contains("����"))){
							c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
							fish++;
						}
						else{
							Log.i("Main.java", "�� ���Դ��� Ȯ���غ���~~");
							break;
						}
					}
					break;
				}
				else{
					c.moveToNext();
				}
			}
			Log.w("Main.java", "��ǰ �ұ׷����� �����غ� ��");
		}

		if(c.getCount()!=0) {
			Log.i("Main.java", "��ǰ �ұ׷������� �����ϴ� �κ� ����");
			c = handler.select(5);
			c.moveToFirst();

			for(int i=0; i<item_size; i++){
				if(c.getString(1).toString().equals(mart_name)){
					for(int j=0; j<fruit; j++){
						m.setItemName(c.getString(2).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
						m.setPrice(c.getInt(3));
						m.setAmount(0);
						m.setLat(c.getDouble(8));
						m.setLon(c.getDouble(9));
						list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
						m = new Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
						c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					}

					childList.add(list); // �ұ׷츮��Ʈ�� �߰�
					list = new ArrayList<Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

					for(int j=0; j<farm; j++){
						m.setItemName(c.getString(2).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
						m.setPrice(c.getInt(3));
						m.setAmount(0);
						m.setLat(c.getDouble(8));
						m.setLon(c.getDouble(9));
						list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
						m = new Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
						c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					}

					childList.add(list); // �ұ׷츮��Ʈ�� �߰�
					list = new ArrayList<Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

					for(int j=0; j<animal; j++){
						m.setItemName(c.getString(2).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
						m.setPrice(c.getInt(3));
						m.setAmount(0);
						m.setLat(c.getDouble(8));
						m.setLon(c.getDouble(9));
						list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
						m = new Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
						c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					}

					childList.add(list); // �ұ׷츮��Ʈ�� �߰�
					list = new ArrayList<Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...


					for(int j=0; j<fish; j++){
						m.setItemName(c.getString(2).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
						m.setPrice(c.getInt(3));
						m.setAmount(0);
						m.setLat(c.getDouble(8));
						m.setLon(c.getDouble(9));
						list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
						m = new Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
						c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					}

					childList.add(list); // �ұ׷츮��Ʈ�� �߰�
					list = new ArrayList<Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

					break;
				}
				else{
					c.moveToNext();
				}
			}
			Log.i("Main.java", "���� �ұ׷������� �����ϴ� �κ� ��");
		}
		
		c = handler.select(7);
		c.moveToFirst();
		
		for(int i=0; i<c.getCount(); i++){
			Log.d("7�����̺�˻�~~", c.getString(1).toString() + " " + Integer.toString(c.getInt(2)));
			c.moveToNext();
		}

		handler.close(); // �����ͺ��̽� ��� ����

		adapter = new Adapter(getApplicationContext(), groups, childList); // Ŀ���� �ͽ������ ����Ʈ�信 ����
		adapter.notifyDataSetChanged();
		lv.setAdapter(adapter); // ����Ϳ� �ͽ��Ҵ��� ����Ʈ�� ����
		b1 = (Button) findViewById(R.id.button1); // ���� ������ ��ǰ���� ������ִ� list.java���Ϸ� ������ ����Ʈ ���� ���� ��ư

		// ��ư�� Ŭ������ ���� �̺�Ʈ ������ ���� ����
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				ArrayList<ArrayList<Model>> list = adapter.getAllList(); // ��׷츮��Ʈ ��ü�� �׵��� ������ ������ ����
				int size = list.size(); // ��׷츮��Ʈ�� ���� ���
				for (int i = 0; i < size; i++)
				{
					int subSize = list.get(i).size(); // ��׷츮��Ʈ ���� �ұ׷츮��Ʈ�� ���̰��
					ArrayList<Model> subList = new ArrayList<Model>(); // �ұ׷츮��Ʈ ����
					subList = list.get(i); // �ұ׷츮��Ʈ�� ���� ����
					for (int j = 0; j < subSize; j++)
					{
						if (subList.get(j).getAmount()!=0) {
							for(int k=0; k<length; k++) {
								if(temp[k].equals(subList.get(j).getItemName())) {
									Log.i("Main.java", "�̹� �ִ� ��ǰ �Դϴ�");
									break;
								}
								else if(isNull(temp[k])) {
							//		Log.i("Main.java", "�߰��Ǵ� ��ǰ �Դϴ�");

									Cursor cs = handler.selecteach(subList.get(j).getItemName()); // �Ѿ�� �̸������� �����ͺ��̽� �˻��Ͽ� Ŀ���� ����
									cs.moveToFirst(); // �˻��� Ŀ�� ����ϱ� ���ؼ� ��� // �Ⱦ��� Ŀ����ġ�� -1
									double x = cs.getDouble(5); // �˻��� Ŀ���� ��ġ�� ���� �������� ����
									double y = cs.getDouble(6);
									handler.insert(mart_name, subList.get(j).getItemName(), subList.get(j).getPrice(), subList.get(j).getAmount(), x, y, 0, subList.get(j).getLat(), subList.get(j).getLon(), 1); // �����ͺ��̽� ���̺� mart���� �߰�
									handler.close(); // �����ͺ��̽� ��� ����
									break;
								}
								else {
									Log.d("if, else if pass", "temp");
								}
							}
						}
					}
				}

				Intent intent = new Intent(getApplicationContext(), PriceComp.class); // �庼 ��� �����ִ� list.java���� ����Ʈ ����
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP); // ����Ʈ �� ��Ƽ��Ƽ ���� ����
				intent.putExtra("select_mart_name", mart_name);
				startActivity(intent); // ����Ʈ ����
			//	finish();
			}
		});
	}
/*	
	// �ܸ��� �޴�, Ȩ, �ڷΰ��� ��ư �̺�Ʈ �����κ�
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BACK:
				finish();
				// �ܸ����� BACK��ư
				return true;
			case KeyEvent.KEYCODE_MENU:
				// �ܸ����� �޴���ư
				return true;
					case KeyEvent.KEYCODE_HOME:
				// �ܸ����� HOME��ư -> ���۾���
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}*/

	public boolean isNull(String value){
		if(value==null || value.trim().length()==0){
			return true;
		} else {
			return false;
		}
	}

	// �� �Ʒ����ʹ� NFC���� �Լ�

	// ����׶��� ����ġ Ȱ��ȭ
	@Override
	protected void onResume() {
		super.onResume();
		if(mNfcAdapter!=null){
			mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mReadTagFilters, null);
		}
	}

	// ����׶��� ����ġ ��Ȱ��ȭ
	@Override
	protected void onPause()
	{
		super.onPause();
		if(mNfcAdapter!=null){
			Log.d(TAG, "onPause: " + getIntent());

			mNfcAdapter.disableForegroundDispatch(this);
		}
	}
}