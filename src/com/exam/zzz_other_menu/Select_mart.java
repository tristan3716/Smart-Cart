package com.exam.zzz_other_menu;

import java.util.*;

import android.app.*;
import android.app.AlertDialog.Builder;
import android.content.*;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.database.*;
import android.nfc.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;

import com.exam.zzz_other_menu_mysql.*;
import com.example.n_mart.*;

public class Select_mart extends Activity {

	private static final String TAG = "nfcinventory_simple";

	// nfc���� ���
	NfcAdapter mNfcAdapter; // ���� nfc�ϵ������� �ٸ� ������ ��� // nfc������ üũ�Ǿ� �ִ��� ���� ���� // nfc�±׿��� ndef�����͸� �о� ���ų� �ݴ�� ���� �� �� ��� ����
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	IntentFilter[] mWriteTagFilters;

	MySQLiteHandler handler;
	Cursor c;
	ExpandableListView Select_mart;

	ArrayList<ArrayList<Select_mart_Model>> childList; // �ұ׷쿡 ������ ����Ʈ ����
	ArrayList<Select_mart_GM> groups; // ��׷쿡 ������ ����Ʈ ����
	Select_mart_Adapter adapter; // �������� ����

	String[] temp;
	int Lotte = 0;
	int Home = 0;
	int E_m = 0;
	int Hanaro = 0;
	int Sin = 0;
	int Lotte_dep = 0;
	int H_dep = 0;

	String MartName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 	 // �׼ǹ� ���ֱ�
		setContentView(R.layout.select_mart);

		Log.i("Select_mart.java", "oncreate!!");

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
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
		Select_mart = (ExpandableListView) findViewById(R.id.mart_expandableListView2); // ��Ʈ���� ��Ÿ�� layout

		Select_mart.setFocusable(false);

		Log.i("Select_mart.java", "handler�� �����մϴ�");
		handler = MySQLiteHandler.open(getApplicationContext()); // �����ͺ��̽� ������� open ����

		Log.i("Select_mart.java", "6�����̺��� �ҷ��ɴϴ�");
		c = handler.select(6); // infoŬ������ �ҷ������� ���ƾ� ���� ���ϴ� getCount�� ���ؼ� ����
		c.moveToFirst(); // ������ getCount ���� �ȵ�

		groups = new ArrayList<Select_mart_GM>(); // ��׷쿡 ������ ����Ʈ ����

		Log.i("Select_mart.java", "�����׷� ������ ȭ�鿡 ����ϱ� ���� ����ϴ� �κ��Դϴ�");
		if(c.getCount()!=0){ // �����ͺ��̽��� ������ �־�߸� ����ǵ��� ����
			Log.i("Select_mart.java", "�����׷� ���� ���� ����");
			for(int i=0; i<c.getCount(); i++) // i�� ������ �Ϲ������� ������ ��... // �ұ׷���� ������ �׻� �´� ���� �ƴҵ�...?? // ��Ŵ� 5�� ��Ŵ� 7�� �̷�����...
			{
				Select_mart_GM g = new Select_mart_GM(); // ��׷츮��Ʈ�� ������ü

				g.setName(c.getString(1).toString()); // ��׷츮��Ʈ�� ��������->�׷��̸�
				groups.add(g); // ��׷��̸���Ʈ�� ����

				c.moveToNext();
			}
			Log.i("Select_mart.java", "�����׷� ���� ���� ��");
		}

		Log.i("Select_mart.java", "����ұ׷� ������ ȭ�鿡 ����ϱ� ���� ����ϴ� �κ��Դϴ�");
		childList = new ArrayList<ArrayList<Select_mart_Model>>(); // �ұ׷츮��Ʈ ����
		ArrayList<Select_mart_Model> list = new ArrayList<Select_mart_Model>(); // �ұ׷츮��Ʈ�� ���� ��������Ʈ ����
		Select_mart_Model m = new Select_mart_Model(); // �ұ׷���������Ʈ ������ü

		Log.i("Select_mart.java", "7�� ���̺��� �ҷ��ɴϴ�");
		c = handler.select(7); // �����ͺ��̽� ���̺� mart2�� ������ ���� �˻���
		c.moveToFirst(); // ������ �Ʒ� getCount ���� �ȵ�

		Log.i("Select_mart.java", "�� ��Ʈ�� ����� ������ Ȯ���ϱ� ���ؼ� ����ϴ� �κ��Դϴ�");
		if(c.getCount()!=0){ // �����ͺ��̽��� ������ �־�߸� ����ǵ��� ����

			for(int i = 0; i<c.getCount(); i++) // i�� ������ �Ϲ������� ������ ��... // �ұ׷���� ������ �׻� �´� ���� �ƴҵ�...?? // ��Ŵ� 5�� ��Ŵ� 7�� �̷�����...
			{
				if((c.getString(1).toString().contains("�Ե���Ʈ"))){
					c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					Lotte++;
				}
				else if((c.getString(1).toString().contains("Ȩ�÷���"))){
					c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					Home++;
				}
				else if((c.getString(1).toString().contains("�̸�Ʈ"))){
					c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					E_m++;
				}
				else if((c.getString(1).toString().contains("�ϳ��θ�Ʈ"))){
					c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					Hanaro++;
				}
				else if((c.getString(1).toString().contains("�ż����ȭ��"))){
					c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					Sin++;
				}
				else if((c.getString(1).toString().contains("�Ե���ȭ��"))){
					c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					Lotte_dep++;
				}
				else if((c.getString(1).toString().contains("�����ȭ��"))){
					c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
					H_dep++;
				}
				else{
					Log.i("Select_mart.java", "�� ���Դ��� Ȯ���غ���~~");
				}
			}
		}

		if(c.getCount()!=0) {
			Log.i("Select_mart.java", "���� �ұ׷������� �����ϴ� �κ� ����");
			c = handler.select(7);
			c.moveToFirst();

			for(int j=0; j<Lotte; j++){
				m.setMartName(c.getString(1).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
				m.setBookMarker(c.getInt(2));
				list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
				m = new Select_mart_Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}

			childList.add(list); // �ұ׷츮��Ʈ�� �߰�
			list = new ArrayList<Select_mart_Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

			for(int j=0; j<E_m; j++){
				m.setMartName(c.getString(1).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
				m.setBookMarker(c.getInt(2));
				list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
				m = new Select_mart_Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}

			childList.add(list); // �ұ׷츮��Ʈ�� �߰�
			list = new ArrayList<Select_mart_Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

			for(int j=0; j<Home; j++){
				m.setMartName(c.getString(1).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
				m.setBookMarker(c.getInt(2));
				list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
				m = new Select_mart_Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}

			childList.add(list); // �ұ׷츮��Ʈ�� �߰�
			list = new ArrayList<Select_mart_Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

			for(int j=0; j<Hanaro; j++){
				m.setMartName(c.getString(1).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
				m.setBookMarker(c.getInt(2));
				list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
				m = new Select_mart_Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}

			childList.add(list); // �ұ׷츮��Ʈ�� �߰�
			list = new ArrayList<Select_mart_Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

			for(int j=0; j<Sin; j++){
				m.setMartName(c.getString(1).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
				m.setBookMarker(c.getInt(2));
				list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
				m = new Select_mart_Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}

			childList.add(list); // �ұ׷츮��Ʈ�� �߰�
			list = new ArrayList<Select_mart_Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

			for(int j=0; j<H_dep; j++){
				m.setMartName(c.getString(1).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
				m.setBookMarker(c.getInt(2));
				list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
				m = new Select_mart_Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}

			childList.add(list); // �ұ׷츮��Ʈ�� �߰�
			list = new ArrayList<Select_mart_Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

			for(int j=0; j<Lotte_dep; j++){
				m.setMartName(c.getString(1).toString()); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� ��µǴ� �̸�
				m.setBookMarker(c.getInt(2));
				list.add(m); // �ұ׷츮��Ʈ�� ���� ��������Ʈ�� �߰�
				m = new Select_mart_Model(); // ���ο� �ұ׷�������ü�� ���� // ���ϸ� ��� ��������� �� ������ �����͸� �� ��µ�
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}

			childList.add(list); // �ұ׷츮��Ʈ�� �߰�
			list = new ArrayList<Select_mart_Model>(); // ���ο� �ұ׷� ��������Ʈ�� ���� // Ȯ���� ���غ����� ���ϸ� ��� ��������ų� ��������...

			Log.i("Select_mart.java", "���� �ұ׷������� �����ϴ� �κ� ��");
		}

		adapter = new Select_mart_Adapter(getApplicationContext(), groups, childList); // Ŀ���� �ͽ������ ����Ʈ�信 ����
		adapter.notifyDataSetChanged();

		Select_mart.setAdapter(adapter); // ����Ϳ� �ͽ��Ҵ��� ����Ʈ�� ����

		Log.i("Select_mart.java", "������ ���������� �ߴ� ���̾�α׸� �����մϴ�");

		Select_mart.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				final Select_mart_Model model = adapter.getChild(groupPosition, childPosition);

				Builder dlg = new AlertDialog.Builder(Select_mart.this);
				dlg.setTitle("����")
				.setMessage(model.getMartName() + " �� ���� �ϼ̽��ϴ� ������ Ȯ�� �ƴϸ� ��Ҹ� �����ϼ���")
				.setNeutralButton("�ٷν���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getApplicationContext(), list.class); // �庼 ��� �����ִ� list.java���� ����Ʈ ����
						intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP); // ����Ʈ �� ��Ƽ��Ƽ ���� ����
						intent.putExtra("select_mart_name", model.getMartName());
						intent.putExtra("select_mart_name2", model.getMartName());
						startActivity(intent); // ����Ʈ ����
						finish(); // ����Ʈ �� ��Ƽ��Ƽ ����
					}
				})
				.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() {
					// ���̾�α��� �� ��ư�� ������ ����

					@Override
					public void onClick(DialogInterface dialog,	int whichButton) {
						Intent intent2 = new Intent(getApplicationContext(), Main.class); // �庼 ��� �����ִ� list.java���� ����Ʈ ����
						intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP); // ����Ʈ �� ��Ƽ��Ƽ ���� ����
						intent2.putExtra("select_mart_name", model.getMartName());
						startActivity(intent2); // ����Ʈ ����
						finish(); // ����Ʈ �� ��Ƽ��Ƽ ����
					}
				})
				// ���̾�α��� �ƴϿ� ��ư
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// ignore, just dismiss
						// �������� �ܼ����Ḹ �Ǹ� �ٸ� ������ �ְ�ʹٸ� ���⿡ �߰���Ű�� ��
					}
				}).show(); // ���̾�α� ����������

				return false;
			}
		});
		handler.close(); // �����ͺ��̽� ��� ����
	}

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
	protected void onPause() {
		super.onPause();
		if(mNfcAdapter!=null){
			Log.d(TAG, "onPause: " + getIntent());

			mNfcAdapter.disableForegroundDispatch(this);
		}
	}
}