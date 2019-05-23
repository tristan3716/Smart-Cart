package com.exam.zzz_other_menu;

import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONException;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;
import com.example.n_mart.R;

public class list extends Activity {

	private static final String TAG = "nfcinventory_simple";

	// nfc���� ���
	NfcAdapter mNfcAdapter; // ���� nfc�ϵ������� �ٸ� ������ ��� // nfc������ üũ�Ǿ� �ִ��� ���� ���� // nfc�±׿��� ndef�����͸� �о� ���ų� �ݴ�� ���� �� �� ��� ����
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	IntentFilter[] mWriteTagFilters;
	private boolean mWriteMode = false;

	ListView list; // ����Ʈ��
	TextView AllPrice; // �� �ݾ� ��Ÿ���� �ؽ�Ʈ��
	TableLayout tl; // �޴���ư�� ���� ��ư��....
	TextView NowAllPrice; // ����ݾ׿�

	int length = 100; // �迭�� ����
	int menu_button_visible_count = 0; // �޴���ư Ŭ���� ��Ÿ���� �Ǵ� ������� �ϱ����ؼ� ����ϴ� ����
	int info_control = 0; // info���� ������

	int allprice = 0; // �Ѿ��� ��Ÿ���� ����
	int allprice_cal = 0; // �Ѿ��� ����ϱ� ���ؼ� ����ϴ� ����
	int nowallprice = 0; // ����ݾ��� ��Ÿ���� ����
	int nowallprice_cal = 0; // ����ݾ��� ����ϱ� ���ؼ� ����ϴ� ����

	MySQLiteHandler handler; // �����ͺ��̽� ������� handler
	Cursor c; // �����ͺ��̽� ������� Ŀ��
	Cursor cur; // table4�� ���� Ŀ�� // nfc������� �����ٴ� ǥ������ �����ϴ� table��

	int pos = 0; // nfc���� �� ����Ʈ �籸���� �� ���� �ö󰡴°��� �������� ��Ÿ���� �Ϸ��� pos

	String intentmart_name; // nfc���� ����� save, save2�� �����ϱ� ���� nfc���� �̸�����
	String intentname; // nfc���� ����� save, save2�� �����ϱ� ���� nfc���� �̸�����
	int intentprice = 0; // nfc���� ����� save, save2�� �����ϱ� ���� nfc���� �̸�����

	// Ŀ���� numberpicker���� ����ϴ� ����
	int uprange = 20; // �ִ����
	int downrange = 0; // �ּҼ���
	int values = 1; // �������

	DataAdapter adapter; // Ŀ���� �����
	ArrayList<cdata> alist; // ����Ʈ�� �Ѹ��� ������ ��� ��̸���Ʈ���� �Ǵٽ� �߰��Ǵ� �� ���� ���� ����ϴ� ��̸���Ʈ
	ArrayList<cdata> alist2; // NFC�� ���� ��ǰ�� ������ ��� ��̸���Ʈ���� �Ǵٽ� �߰��Ǵ� �� ���� ���� ����ϴ� ��̸���Ʈ

	static ArrayList<String> itemlist; // ��ü�� �̸�
	static ArrayList<Double> itemlist_x; // ��ü��x��ǥ
	static ArrayList<Double> itemlist_y; // ��ü��y��ǥ
	static ArrayList<Integer> itemstate; // ��ü������ ���� ������ ����

	static int cpzm = 0; // nfc�߰��� ��쿡 ���
	static int cpzm2 = 0; // nfc�� �����ٴ� ���� ǥ���ϴ� ��쿡 ��� // �� ����� �� �ٽ� �����Ű�� �ʰ� �����Ƿ� �ʿ���� ��???

	String comp_mart_name;
	String comp_mart_name2;

	ArrayList<PriceCompData_small> selectitemlist;
	ArrayList<PriceCompData_small> blist;

	int selectcpzm = 0;

	//��ġ ��ġ�� ���� ǥ�õǴ� ��������� �� ����
	public TipsView m_tips_view;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.v("list.java", "oncreate!!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 	 // �׼ǹ� ���ֱ�
		setContentView(R.layout.activity_main);

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

			IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

			// create IntentFilter arrays:
			mWriteTagFilters = new IntentFilter[] { tagDetected };
			mReadTagFilters = new IntentFilter[] { ndefDetected, tagDetected };

		}
		AllPrice = (TextView) findViewById(R.id.text2); // �Ѿװ����� ����ϱ����� �ؽ�Ʈ�� ����
		tl = (TableLayout) findViewById(R.id.tl); // �޴���ư ������ �� ������ �κ�
		NowAllPrice = (TextView) findViewById(R.id.text8);

		// ���ҽ� ���Ͽ� ���ǵ� id_view ��� ID �� ��������Ǻ並 ���´�.
		m_tips_view = (TipsView)findViewById(R.id.id_view1);

		tl.setVisibility(View.GONE); // �޴���ư�� ����

		handler = MySQLiteHandler.open(getApplicationContext()); // �����ͺ��̽� �ڵ鷯 ����
		alist = new ArrayList<cdata>(); // ��̸���Ʈ ��ü����
		alist2 = new ArrayList<cdata>(); // ��̸���Ʈ ��ü����
		itemlist = new ArrayList<String>(); // ��ü�� �̸�
		itemlist_x = new ArrayList<Double>(); // ��ü��x��ǥ
		itemlist_y = new ArrayList<Double>(); // ��ü��y��ǥ
		itemstate = new ArrayList<Integer>(); // ��ü������ ���� ������ ����

		selectitemlist = new ArrayList<PriceCompData_small>();
		blist = new ArrayList<PriceCompData_small>();

		Log.v("list.java", "�����ʱ�ȭ �� �� �� ����Ϸ�");

		Intent intent = getIntent();

		comp_mart_name = intent.getExtras().get("select_mart_name").toString(); // ó���� ������ ��Ʈ
		comp_mart_name2 = intent.getExtras().get("select_mart_name2").toString(); // ���ݺ񱳿��� ������ ��Ʈ

		c = handler.select(1);
		c.moveToFirst();

		for(int i=0; i<c.getCount(); i++){
			selectitemlist.add(new PriceCompData_small(c.getString(2).toString(), c.getInt(4)));

			c.moveToNext();
		}

		for(int i=0; i<selectitemlist.size(); i++){
			String a = selectitemlist.get(i).getName();
			if(a.contains("���")){
				blist.add(new PriceCompData_small("���", selectitemlist.get(i).getAmount()));
			} else if(a.contains("��")){
				if(a.contains("����")){
					blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
				} else {
					blist.add(new PriceCompData_small("��", selectitemlist.get(i).getAmount()));
				}
			} else if(a.contains("��")){
				blist.add(new PriceCompData_small("��", selectitemlist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
			} else if(a.contains("ȣ��")){
				blist.add(new PriceCompData_small("ȣ��", selectitemlist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
			} else if(a.contains("�������")){
				blist.add(new PriceCompData_small("�������", selectitemlist.get(i).getAmount()));
			} else if(a.contains("�߰��")){
				blist.add(new PriceCompData_small("�߰��", selectitemlist.get(i).getAmount()));
			} else if(a.contains("�ް�")){
				blist.add(new PriceCompData_small("�ް�", selectitemlist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
			} else if(a.contains("����") || a.contains("����")){
				blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
			} else if(a.contains("��¡��")){
				blist.add(new PriceCompData_small("��¡��", selectitemlist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", selectitemlist.get(i).getAmount()));
			}
		}

		if(comp_mart_name.equals(comp_mart_name2)){
			c = handler.select(1); // main.java���� ������ ��ǰ���� ����� �����ͺ��̽� ��� ��ȸ
			c.moveToFirst(); // Ŀ���� ��ġ�� ó������ ��ġ

			// main.java���� ������ ��ǰ���� ����� �����ͺ��̽��� ������ �˻�� �迭 temp�� �ű�� �κ�
			// ���ÿ� ����Ʈ�並 ������ ��̸���Ʈ���� ������� �̸�, ����, ��������
			for (int i = 0; i < c.getCount(); i++) {
				// ����Ʈ�並 ������ ��̸���Ʈ�� ������� �̸�, ����, ��������
				alist.add(new cdata(getApplicationContext(), c.getString(2).toString(), c.getInt(3), c.getInt(4)));
				itemlist_x.add(i, c.getDouble(5)); // x��ǥ
				itemlist_y.add(i, c.getDouble(6)); // y��ǥ
				itemlist.add(i, c.getString(2).toString()); // �ش��ϴ� ��ǰ �̸�����
				itemstate.add(i, c.getInt(7)); // ��ǰ�� �������� ������������ ǥ���Ͽ� ������ ��Ÿ���� ���� ����
				c.moveToNext(); // Ŀ���� �������� �̵���Ŵ
				// nfc������� ���¹�ǰ�̸� �߰��Լ��� �߻��ϰ� �߰��� �� �� �߰����� �ʱ����� �迭�� �����ϱ� ���� ������ �ε��� ���ϴ� �κ�
			}
		} else {

			handler.delete_table_1_4();

			c = handler.select(5);
			c.moveToFirst();

			for(int i=0; i<c.getCount(); i++){
				if(c.getString(1).toString().equals(comp_mart_name)){
					for(int j=0; j<blist.size(); j++){
						if(c.getString(2).toString().contains(blist.get(j).getName())){
							if(blist.get(j).getName().equals("����")){
								selectcpzm = 1;
								processadd(comp_mart_name, c.getString(2).toString(), c.getInt(3), blist.get(j).getAmount());
							} else {
								if(c.getString(2).toString().contains("����") && blist.get(j).getName().equals("��")){

								} else {
									selectcpzm = 1;
									processadd(comp_mart_name, c.getString(2).toString(), c.getInt(3), blist.get(j).getAmount());
								}
							}
						}
					}
				}
				c.moveToNext();
			}
		}

		// �޴����� ��ٱ��ϻ����� �����۸���Ʈ�κ� �����ؾ��ϳ� ���ƾ��ϳ� Ȯ�ο�
		for(int i=0; i<itemlist.size(); i++){
			Log.e("�����۸���Ʈ", itemlist.get(i));
		}
		
		Log.v("list.java", "���޹��� ���õ� ��ǰ���� ������� ��������Ϸ�");

		list = (ListView) findViewById(R.id.list); // main���� ������ ��ǰ�� ��µǴ� ����Ʈ, Ŀ���Ҹ���Ʈ�䰡 �� ����Ʈ�信 ��µ�
		list.setFocusable(false); // ����Ʈ��� ��ư�� ���� �������� ���ϰ�  ������ �Ѵ� �ǵ��� ���ϸ� �ϳ��� ���õ�

		cur = handler.select(4); // nfc���� ��ǰ�� ����Ǵ� �����ͺ��̽� ��� ��ȸ
		cur.moveToFirst(); // Ŀ���� ó������ ��ġ

		// nfc���� ��ǰ���� ����� �����ͺ��̽��� ������ �˻�� �迭�� �����ϴ� �κ�
		if (cur.getCount() != 0) {
			for (int i = 0; i < cur.getCount(); i++) {
				alist2.add(new cdata(getApplicationContext(), cur.getString(1).toString(), cur.getInt(2), cur.getInt(3)));
				cur.moveToNext(); // Ŀ���� �������� �̵���Ŵ
			}
		}

		Log.v("list.java", "NFC���� ��ǰ���� �������� ��������Ϸ�");

		priceupdata(); // �Ѿ��� ��Ÿ���� �Լ�
		priceUpdata2(); // ����ݾ��� ��Ÿ���� �Լ�
		adapter = new DataAdapter(this, alist); // Ŀ���Ҿ���Ϳ� ������¿� ��̸���Ʈ ����
		adapter.notifyDataSetChanged(); // ����Ʈ�� ������ ����Ǿ��ٰ� ����Ϳ��� �˸�
		list.setAdapter(adapter); // ����Ʈ��� ����� ����
		// ����Ʈ�� ����� ��� ������ �������̾�αװ� �߰� �ϱ����� ����
		list.setOnItemLongClickListener(new ListViewItemLongClickListener());
		// ��� ��ǰ�� nfc�� �������� ������̾�αװ� �߰��ϴ� �Լ�
		//exit();
		handler.close(); // �����ͺ��̽� ����

		Log.v("list.java", "oncreate�κ� ����");
	}

	// ��ƽ
	public void myfunction(){

		Log.v("list.java", "��ƽ�κн���");
		m_tips_view.performHapticFeedback(1);
	}

	// �߰��Ұ��� ����� ���̾�α� // ���� �����ϸ� �߰��Լ��� ���� �ƴϿ��� �����ϸ� �׳� ���̾�α׸� ����
	public void add(final String pmart_name, final String pname, final int pprice, final int pamount) {
		Log.v("list.java", "�߰��Ұ��� ����� ���̾�α�");
		cpzm++;
		new AlertDialog.Builder(this) // ���̾�α� ������
		.setTitle(R.string.add_singer) // ���̾�α� ����
		// ���̾�α� �� ��ư
		.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,	int whichButton) {
				Log.v("list.java", "�߰����̾�α��� ���� ������");
				// ���̾�α��� �� ��ư�� ������ ����
				processadd(pmart_name, pname, pprice, pamount); // �����ͺ��̽��� ��ǰ �߰� ��
				priceupdata(); // ��ǰ�� �߰��Ǹ鼭 ������ �ö󰡹Ƿ� �Ѿ׺����Լ�
				priceUpdata2();
				Log.d("add", Integer.toString(nowallprice));
			}
		})
		// ���̾�α��� �ƴϿ� ��ư
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			// ���̾�α��� �ƴϿ� ��ư�� ������ �۵�
			public void onClick(DialogInterface dialog,	int whichButton) {
				Log.v("list.java", "�߰����̾�α��� �ƴϿ��� ������");
				// ignore, just dismiss
				// �������� �ܼ����Ḹ �Ǹ� �ٸ� ������ �ְ�ʹٸ� ���⿡ �߰���Ű�� ��
			}
		}).show(); // ���̾�α� ����������
	}

	// �����Ұ��� ����� ���̾�α� // ���� �����ϸ� �����Լ��� ���� �ƴϿ��� �����ϸ� �׳� ���̾�α׸� ����
	private void delete(final int rowId) {
		Log.v("list.java", "�����Ұ��� ����� ���̾�α�");
		// ����Ʈ�䰡 ��Ÿ���� ����Ʈ�� position�� 0 ���� Ŭ���� �۵�
		if (rowId >= 0) {
			new AlertDialog.Builder(this) // ���̾�α� ������
			.setTitle(R.string.delete_singer) // ���̾�α� ����
			// ���̾�α� �� ��ư
			.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() {
				// ���̾�α��� �� ��ư�� ������ ����
				public void onClick(DialogInterface dialog,	int whichButton) {
					Log.v("list.java", "���Ŵ��̾�α��� ���� ������");
					processDelete(rowId); // ���޹��� ����Ʈ���� ��ġ��  processDelete �Լ��� ����
					priceupdata(); // ��ǰ�� �����Ǹ� �� �� �Ѿ׺����
					priceUpdata2();
				}
			})
			// ���̾�α��� �ƴϿ� ��ư
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int whichButton) {
					Log.v("list.java", "���Ŵ��̾�α��� �ƴϿ��� ������");
					// ignore, just dismiss
					// �������� �ܼ����Ḹ �Ǹ� �ٸ� ������ �ְ�ʹٸ� ���⿡ �߰���Ű�� ��
				}
			}).show(); // ���̾�α� ����������
		}
	}

	// nfc������� �ش� nfc�� ����ִ� ������ �̸��� ���ݰ� ������ ǥ�����ִ� ����
	private void info(final String infoname) {

		Log.v("list.java", "NFC������� ������ �ִ� info�Լ�����");
		Cursor cs = handler.selectinfo(infoname); // �Ѿ�� �̸������� �����ͺ��̽� �˻��Ͽ� Ŀ���� ���� select(1)
		cs.moveToFirst(); // �˻��� Ŀ�� ����ϱ� ���ؼ� ��� // �Ⱦ��� Ŀ����ġ�� -1
		values = cs.getInt(4); // �˻��� Ŀ���� ��ġ�� ���� �������� ����
		// ���̾�α׿� ����� �޼��� ���̾�α׿��� �� �� �����ϸ� ������ �����ϰ� �ǰ� ��� ��ġ�� ����Ʈ�� �����ϰ� �Ǵ��� ��Ÿ���� ��ġ������ ã������ �κ�
		String str_info = " �̹� " + values + " �� �����̽��ϴ�. \r\n\r\n �߰��Ͽ� �����ðڽ��ϱ�?"; 
		for (int i = 0; i < length; i++) {
			// �Ѿ�� ������ �̸��� �庸�� ����Ʈ�� ������ ��ġ�ϰԵǸ� �װ� ������ �����Ű�� ���� ��ġ�̹Ƿ� �׶��� i����  pos�� ���� �� for�� Ż��
			if (infoname.equals(alist.get(i).getName())) {
				pos = i;
				priceUpdata2();
				break;
			}
		}

		// ���̾�α� ��ºκ�
		new AlertDialog.Builder(this) // ���̾�α� ������
		.setTitle(infoname) // ���̾�α� ����
		.setMessage(str_info) // ���̾�α� ���� ��¹���
		// ���̾�α� �� ��ư
		.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() {
			// ���̾�α� �� ��ư ���ý� �۵�
			public void onClick(DialogInterface dialog,	int whichButton) {
				Log.v("list.java", "info���̾�α��� ���� ������");
				// values=���簪, downrange=���������ּҰ�(0), uprange=���������ִ밪(20)
				if (values >= downrange && values < uprange) {

					values++; // ������ values�� 1����
					processUpdata(infoname, values, pos); // �����ͺ��̽� ������Ʈ�� ���� ���� �Լ�(�̸�,����,���������� ������ġ)
					priceUpdata2();
					list.setSelection(pos - 1); // ���������ϰ� ���� ����Ʈ��ó�� �̵��ϴ� �κ�  nfc����� ���� ���� �̵��ϱ� ������ ���							
				} else if (values > uprange) {
					values = downrange;
					processUpdata(infoname, values, pos);
					// ���������ϰ� ���� ����Ʈ��ó�� �̵��ϴ� �κ�, nfc����� ���� ���� �̵��ϱ� ������ ���
					list.setSelection(pos - 1);}
				priceupdata(); // �Ѿ� ���� �Լ�
				priceUpdata2();
			}
		})
		// �ƴϿ� ��ư
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,	int whichButton) {
				Log.v("list.java", "info���̾�α��� �ƴϿ��� Ŭ����");
				processUpdata(infoname, values, pos); // �����ͺ��̽� ������Ʈ�� ���� ���� �Լ�(�̸�,����,���������� ������ġ)
				priceUpdata2();
				// ignore, just dismiss
			}
		}).setCancelable(true) // ��Ű ������ ���̾�α� ���ᰡ�� // �ƴϿ� ��ư��
		// ����
		.setIcon(R.drawable.ic_launcher) // ���̾�α� ���� �׸�
		.show(); // ���౸�� // ������ ���� �ȵ�
	}

	//������ ���� �����µ� ���� �ð� ������ ��������....
	private void exit(){

		Log.v("list.java", "NFC�� �̿��Ͽ� ��� ��ǰ�� ������� ������ exit���̾�α�");
		c = handler.select(1);
		cur = handler.select(4);

		// mart4���̺� �ƹ��ǹ̾��°� �� ����.. ��������� �Ⱦ��� cur�߰��� �� �ȵż�
		if(c.getCount() == (cur.getCount()-1)){
			new AlertDialog.Builder(this) // ���̾�α� ������
			.setTitle(R.string.delete_singer) // ���̾�α� ����
			// ���̾�α� �� ��ư
			.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() {
				// ���̾�α��� �� ��ư�� ������ ����
				public void onClick(DialogInterface dialog,	int whichButton) {
					Log.v("list.java", "exit���̾�α��� ���� ������");
					//finish();
				}
			})
			// ���̾�α��� �ƴϿ� ��ư
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Log.v("list.java", "exit���̾�α��� �ƴϿ��� ������");
					// ignore, just dismiss
					// �������� �ܼ����Ḹ �Ǹ� �ٸ� ������ �ְ�ʹٸ� ���⿡ �߰���Ű�� ��
				}
			}).show(); // ���̾�α� ����������
		}
	}

	// main���� �������� �ʾҴ� ��ǰ nfc ������ �߰��Ǵ� �߰��Լ�
	private void processadd(String pmart_name, String pname, int pprice, int pamount) {

		Log.v("list.java", "�߰����̾�α׿��� ���� �����ϸ� �Ѿ�ͼ� ������ �߰���Ű�� �Լ�����");
		Cursor cs = handler.selecteach(pname); // �Ѿ�� �̸������� �����ͺ��̽� �˻��Ͽ� Ŀ���� ����
		cs.moveToFirst(); // �˻��� Ŀ�� ����ϱ� ���ؼ� ���, �Ⱦ��� Ŀ����ġ�� -1
		double x = cs.getDouble(5); // �˻��� Ŀ���� ��ġ�� ���� �������� ����
		double y = cs.getDouble(6);
		Cursor cs2 = handler.selecteach_martname(pmart_name);
		cs2.moveToFirst();
		double lat = cs2.getDouble(8);
		double lon = cs2.getDouble(9);

		if(selectcpzm==0){
			handler.insert(pmart_name, pname, pprice, pamount, x, y, 1, lat, lon, 1); // main���� �Ѿ�� ��ǰ�� �ִ� �����ͺ��̽��� ��������
		} else {
			handler.insert(pmart_name, pname, pprice, pamount, x, y, 0, lat, lon, 1); // main���� �Ѿ�� ��ǰ�� �ִ� �����ͺ��̽��� ��������
		}
		// �߰��Ǵ� �̸�, ���� ,������ ��̸���Ʈ�� �߰��ϴ� �κ�
		alist.add(new cdata(getApplicationContext(), pname, pprice, pamount)); 

		if(selectcpzm==0){
			// �߰��Ǹ� ���� �Ʒ��� ����Ʈ�� �����ǰ� �߰��Ǵ� ����Ʈ ���󰡴� �κ�
			list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); 
			adapter.notifyDataSetChanged(); // ����Ʈ ������ �����ٰ� �˷��ִ� �κ�
		}

		itemlist_x.add(x);
		itemlist_y.add(y);
		itemlist.add(pname);
		if(selectcpzm==0){
			itemstate.add(1);
		} else {
			itemstate.add(0);
		}

		Log.v("list.java", "���⼭ �����ΰ�??9");

		m_tips_view.invalidate(); // ���������� ����Ǿ����Ƿ� ����

		if(selectcpzm==0){
			save2();
		} else {
			selectcpzm = 0;
		}

		Log.v("list.java", "�߰����̾�α׿��� ���� �����ϸ� �Ѿ�ͼ� ������ �߰���Ű�� �Լ���");
	}

	// 3���̻� �����ų� ������ 1�϶� -��ư �������� ���Ǵ� �����Լ�
	private void processDelete(int rowId) {

		Log.v("list.java", "���Ŵ��̾�α׿��� ���� �����ϸ� �Ѿ�ͼ� ������ ���Ž�Ű�� �Լ�����");
		c = handler.select(1); // ������ ������ table1�� �������� Ŀ���� ����� ������ �ʱ�ȭ
		c.moveToPosition(rowId); // ������� �ϴ� ����Ʈ�� ��ġ�� �޾Ƽ� Ŀ���� ���ϴ� ��ġ�� �̵��ñ�ū ����
		String[] args = { c.getString(2).toString() };
		//dbhandler�� �ִ� delete������ �̸��迭�� ������ �޾� �˻��Ͽ� �����ϱ� ������ �޾Ƶ��� ��ġ���� String������ ����
		handler.deletetemp(args); // mart4�� �������

		handler.delete(args); // String�迭 ������ ��ġ���� delete�Լ��� �̵�, mart�� �������
		adapter.notifyDataSetChanged(); // ������ ����Ǿ��ٴ� ���� �˷��ִ� ����
		alist.remove(rowId);

		for(int i=0; i<alist2.size(); i++){
			if(c.getString(2).toString().equals(alist2.get(i).getName())){
				alist2.remove(i);
			} else{
				Log.d("delete", "������???");
			}
		}

		itemlist.remove(rowId);
		itemlist_x.remove(rowId);
		itemlist_y.remove(rowId);
		itemstate.remove(rowId);

		m_tips_view.invalidate(); // ���� ������ ����Ǿ����Ƿ� ����
		Log.v("list.java", "���Ŵ��̾�α׿��� ���� �����ϸ� �Ѿ�ͼ� ������ ���Ž�Ű�� �Լ���");
	}

	// +,- ��ư ������ �� ���� ����� �Լ�
	private void processUpdata(String name, int values, int position) {

		Log.v("list.java", "�������������ϴ� �Լ�����");
		handler.update(name, values, 1); // �̸��� �������� update�Լ��� ����
		c = handler.select(1); // ������ ����� ������ Ŀ���� �˷��ְ� �ʱ�ȭ
		handler.update(name, values, 2); // �̸��� �������� update�Լ��� ����
		cur = handler.select(4); // ������ ����� ������ Ŀ���� �˷��ְ� �ʱ�ȭ
		c.moveToPosition(position); // �޾ƿ� ��ġ���� �̿��Ͽ� Ŀ���� ���ϴ� ��ġ�� �̵���Ŵ
		// �����Ͱ� ����Ǿ����Ƿ� ����Ʈ�� �ٲ�����ϴµ� ��̸���Ʈ�� �������Լ��� ����ϱ� ���ؼ� ���� cdate ��ü
		cdata data = new cdata(getApplicationContext(), c.getString(2).toString(), c.getInt(3), c.getInt(4));
		alist.set(position, data); // ����� ������ �����Ϸ��� ��ġ�� �����ϴ� ����
		adapter.notifyDataSetChanged(); // ������ ����Ǿ��ٴ� ���� �˷��ִ� ����
		Log.v("list.java", "�������������ϴ� �Լ���");
	}

	// ������ nfc�� �����ٴ� ǥ�ø� ���� �׸��� ���� �ٲ�µ� �׶� ���¸� �̿��ϹǷ� �� ���¸� ��ȯ��Ű�� �Լ�
	private void processUpdataState(String name, int state, int position) {

		Log.v("list.java", "���峻�������� ǥ�ð� NFC�� �������� �ٲ�µ� �׶��� ��Ÿ���� �Լ�����");
		switch(state){
		case 0:
			handler.updatestate(name, 1);
			c = handler.select(1);
			itemstate.set(position, 1);
			break;
		case 1:
			break;
		}
		Log.v("list.java", "���峻�������� ǥ�ð� NFC�� �������� �ٲ�µ� �׶��� ��Ÿ���� �Լ���");
	}

	// �Ѿ� ����� �Լ�
	private void priceupdata() {

		Log.v("list.java", "�Ѿ׺����Լ� ����");
		c = handler.select(1);
		// processUpdata�Լ����� �ٷ� �Ѿ���� �Լ��̹Ƿ� Ŀ���� ������ �����ϰ��� �ϴ� �����ͺ��̽��� ����Ű�� ������ �� Ŀ���� �� ���� �̵���Ŵ
		c.moveToFirst(); 

		// tabel1�� ����� ������ �� ó������ �о table1 �� ���ݰ� ������ �����ͼ� ���� ���Ͽ� �Ѿ��� ��Ÿ��
		for (int i = 0; i < c.getCount(); i++) {

			allprice_cal = c.getInt(3) * c.getInt(4); // Ŀ���� ����Ű�� ���� ���ݰ� ������ ���Ͽ�  price2�� ����
			c.moveToNext(); // Ŀ���� �������� �̵�

			// Allprice�� �Ѿ��� ��Ÿ��
			allprice = allprice + allprice_cal; // Allprice �� price�� ���� ���� Allprice�� ����
		}
		AllPrice.setText("" + allprice); // text3�� ���
		allprice = 0; // 0���� �ʱ�ȭ ���ϸ� �ٲٱ� ���� �������� ���� over�Ǽ� ����
		allprice_cal = 0; // 0���� �ʱ�ȭ ���ϸ� �ٲٱ� ���� �������� ���� over�Ǽ� ����

		Log.v("list.java", "�Ѿ׺����Լ� ��");
	}

	// ����ݾ� ��¿�
	private void priceUpdata2() {

		Log.v("list.java", "����ݾ׺����Լ� ����");
		c = handler.select(4);
		c.moveToFirst();

		for (int j = 0; j < c.getCount(); j++) {

			nowallprice_cal = c.getInt(2) * c.getInt(3);
			c.moveToNext();

			nowallprice = nowallprice + nowallprice_cal;
		}

		NowAllPrice.setText("" + nowallprice);
		nowallprice = 0;
		nowallprice_cal = 0;

		Log.v("list.java", "����ݾ׺����Լ� ��");
	}


	private class DataAdapter extends ArrayAdapter<cdata> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<cdata> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			Log.v("list.java", "getview ����");
			View view = null;
			if (v == null) {
				view = mInflater.inflate(R.layout.list2, null);
			} else {
				view = v;
			}final TextView text1 = (TextView) view.findViewById(R.id.textView1);

			cdata data = this.getItem(position);


			final TextView text2 = (TextView) view.findViewById(R.id.textView2);
			final TextView text3 = (TextView) view.findViewById(R.id.textView3);
			final TextView text4 = (TextView) view.findViewById(R.id.textView4);
			Button upButton = (Button) view.findViewById(R.id.upButton);
			Button downButton = (Button) view.findViewById(R.id.downButton);
			ImageView image = (ImageView) view.findViewById(R.id.imageView1);

			view.setBackgroundResource(R.drawable.back0);


			text1.setText(data.getName());
			text2.setText("" + data.getPrice() + "��");
			text3.setText("" + data.getAmount());

			upButton.setFocusable(false);
			downButton.setFocusable(false);

			upButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Log.v("list.java", "getview���� +��ư �������� �۵��Լ�");
					String a = text1.getText().toString();

					for (int i = 0; i < alist.size(); i++) {
						if (a.equals(alist.get(i).getName())) {
							values = Integer.parseInt(text3.getText().toString());
							break;
						}
					}
					if (values >= downrange && values < uprange) {
						values++;
						processUpdata(a, values, position);
					} else if (values > uprange) {
						values = downrange;
						processUpdata(a, values, position);

					}
					priceupdata();
					priceUpdata2();
					text3.setText("" + values);
				}

			});

			downButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Log.v("list.java", "getview���� -��ư �������� �۵��Լ�");
					String a = text1.getText().toString();

					for (int i = 0; i < alist.size(); i++) {
						if (a.equals(alist.get(i).getName())) {
							values = Integer.parseInt(text3.getText().toString());
							break;
						}
					}

					if (values > downrange && values <= uprange) {

						if (values == 1) {
							delete(position);
						} else {
							values--;
							processUpdata(a, values, position);
						}

					} else if (values < downrange) {
						values = uprange;
						processUpdata(a, values, position);
					}
					priceupdata();
					priceUpdata2();
					text3.setText(values + "");
				}
			});
			// �� ���� �ϴ� �κ�
			// �δ�� "" ������ ��

			Log.v("list.java", "getview���� ����Ʈ�� ��������κ�");

			if (data.getName().contains("���")) {
				image.setImageResource(R.drawable.apple);
			} else if (data.getName().contains("��")) {
				if (data.getName().contains("����")) {
					image.setImageResource(R.drawable.bechoo);
				} else{
					image.setImageResource(R.drawable.pear);
				}
			} else if (data.getName().contains("��")) {
				image.setImageResource(R.drawable.moo);
			} else if (data.getName().contains("����")) {
				image.setImageResource(R.drawable.onion);
			} else if (data.getName().contains("����")) {
				image.setImageResource(R.drawable.sangchoo);
			} else if (data.getName().contains("����")) {
				image.setImageResource(R.drawable.oe);
			} else if (data.getName().contains("ȣ��")) {
				image.setImageResource(R.drawable.pumpkin);
			} else if (data.getName().contains("����")) {
				image.setImageResource(R.drawable.beef);
			} else if (data.getName().contains("�������")) {
				image.setImageResource(R.drawable.pork);
			} else if (data.getName().contains("�߰��")) {
				image.setImageResource(R.drawable.chicken);
			} else if (data.getName().contains("�ް�")) {
				image.setImageResource(R.drawable.egg);
			} else if (data.getName().contains("����")) {
				image.setImageResource(R.drawable.jogi);
			} else if (data.getName().contains("����")) {
				image.setImageResource(R.drawable.myungtae);
			} else if (data.getName().contains("����")) {
				// �׸� �ٲ�� �ϳ�???
				image.setImageResource(R.drawable.myungtae);
			} else if (data.getName().contains("��¡��")) {
				image.setImageResource(R.drawable.ojing);
			} else if (data.getName().contains("����")) {
				image.setImageResource(R.drawable.gofish);
			}

			Log.v("list.java", "getview���� NFC������� ����Ʈ ������ �� ����κ�");


			// �������ϴ� �ڵ尡 ��������
			// ������ ������ PASS ������ �������Ű�� �ɷ� �����ؾ���

			for (int j = 0; j < alist2.size(); j++) {
				if (data != null) {
					String dlatl;
					if (c.getCount() == 0) {
						dlatl = intentname;
					} else if (isNull(alist2.get(j).getName())) {
						dlatl = "";
					} else {
						dlatl = alist2.get(j).getName();
					}

					if (dlatl.equals(data.getName())) {
						view.setBackgroundColor(Color.CYAN);
						notifyDataSetChanged();
					} else if (isNull(dlatl)) {
						break;
					} else {
					}
				}
			}

			int oneprice = data.getPrice() * data.getAmount();
			text4.setText("" + oneprice + "��");
			oneprice = 0;

			Log.v("list.java", "getview ����");
			return view;
		}
	}

	// ����Ʈ�� ����� �ϳ��� ��Ŭ������ �� �������̾�αװ� �߰��ϴ� �κ�
	private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			delete(position);
			return false;
		}
	}

	// ���ڿ��� ����ִ��� �˷��ִ� �Լ�
	public boolean isNull(String value) {
		if (value == null || value.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	// �ܸ��� �޴�, Ȩ, �ڷΰ��� ��ư �̺�Ʈ �����κ�
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BACK:
				{
					Intent intent = new Intent(getApplicationContext(), SelectMenu.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
//					finish();
					// �ܸ����� BACK��ư
					return true;
				}
			case KeyEvent.KEYCODE_MENU:
				// �ܸ����� �޴���ư
				if (menu_button_visible_count == 0) {
					tl.setVisibility(View.VISIBLE);
					menu_button_visible_count++;
				} else {
					tl.setVisibility(View.GONE);
					menu_button_visible_count = 0;
				}
				return true;
				/*	case KeyEvent.KEYCODE_HOME:
				// �ܸ����� HOME��ư -> ���۾���
				return true;*/
			}
		}
		return super.dispatchKeyEvent(event);
	}

	// �� �Ʒ����ʹ� NFC���� �Լ�

	// ����׶��� ����ġ Ȱ��ȭ
	@Override
	protected void onResume()
	{
		super.onResume();

		if(mNfcAdapter!=null){
			Log.d(TAG, "onResume: " + getIntent());

			if (getIntent().getAction() != null)
			{
				if (getIntent().getAction().equals(
						NfcAdapter.ACTION_NDEF_DISCOVERED))
				{
					NdefMessage[] msgs = getNdefMessagesFromIntent(getIntent());
					NdefRecord record = msgs[0].getRecords()[0];
					byte[] payload = record.getPayload();
					setTextFieldValues(new String(payload));
				}
			}
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


	@Override
	protected void onNewIntent(Intent intent)
	{
		if(mNfcAdapter!=null){
			Log.d(TAG, "onNewIntent: " + intent);

			if (!mWriteMode)
			{
				// Currently in tag READING mode
				if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
				{
					NdefMessage[] msgs = getNdefMessagesFromIntent(intent);
					String payload = new String(msgs[0].getRecords()[0].getPayload());
					setTextFieldValues(payload);
				} else if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED))
				{
					Toast.makeText(this,
							"This NFC tag currently has no inventory NDEF data.",
							Toast.LENGTH_LONG).show();
				}
			} else
			{
				Log.d("list.java", "mWriteMode �Դϴ�");
			}
		}
	}

	// READING MODE METHODS

	NdefMessage[] getNdefMessagesFromIntent(Intent intent) {
		NdefMessage[] msgs = null;
		if(mNfcAdapter!=null){
			// Parse the intent
			String action = intent.getAction();
			if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)
					|| action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
			{
				//EXTRA_NDEF_MESSAGES�� �̿��Ͽ� �����͸� parcels�� ���ü��ִ�
				Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
				// �����Ͱ� ������� �ʴٸ� parcelable ��ü ���θ� ��ȸ�ϸ鼭 ���ο� ndefmessage��ü�� ����� �ִ�
				if (rawMsgs != null) {
					msgs = new NdefMessage[rawMsgs.length];
					for (int i = 0; i < rawMsgs.length; i++) {
						msgs[i] = (NdefMessage) rawMsgs[i];
					}
				} else {
					// Unknown tag type
					byte[] empty = new byte[] {};
					NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
					NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
					msgs = new NdefMessage[] { msg };
				}
			} else {
				Log.e(TAG, "Unknown intent.");
				finish();
			}
			return msgs;
		} else {
			return msgs;
		}
	}

	private void setTextFieldValues(String jsonString)
	{
		if(mNfcAdapter!=null){
			JSONObject inventory = null;
			String mart_name2 = "";
			String name2 = "";
			String price2 = "";
			try
			{
				inventory = new JSONObject(jsonString);
				mart_name2 = inventory.getString("mart_name");
				name2 = inventory.getString("name");
				price2 = inventory.getString("ram");
			} catch (JSONException e)
			{
				Log.e(TAG, "Couldn't parse JSON: ", e);
			}

			intentmart_name = mart_name2;
			intentname = name2;
			intentprice = Integer.parseInt(price2);
			m_tips_view.invalidate();
			save();
		}
	}

	// �ߺ��߰� ������
	public void save(){

		if(intentmart_name.equals(comp_mart_name)){
			Log.d("save", "ó���κ�");
			if(alist.size()==0){
				Log.d("save", "size=0");
				add(intentmart_name, intentname, intentprice, 1);
			} else{
				for(int i=0; i<alist.size(); i++){
					if(intentname.equals(alist.get(i).getName())){
						Log.d("alist", "������ �����");
						save2();
						break;
					} else if(i==(alist.size()-1)){
						add(intentmart_name, intentname, intentprice, 1);
						break;
					} else{
						Log.d("if, else if pass", "alist");
					}
				}
			}
		} else {
			Toast.makeText(this, "NFC�� ����� �Ͱ� ������ �ٸ��ϴ�  \n " +
					"NFC�� ���� �Ǿ� �ִ� ��Ʈ�̸� : " + intentmart_name +
					"\n ���õ� ��Ʈ�̸� : " + comp_mart_name, Toast.LENGTH_SHORT).show();
		}
	}


	//NFC �ߺ��߰� ������
	public void save2(){
		Log.e("save2", "1");
		//j�� 0�̸� �ΰ��̱� ������ ��� ����� ���ư��� ���� // ó���� �ζ����� else if����ǰ� 
		if(alist2.size()==0){

			Cursor cs = handler.selectinfo(intentname); // �Ѿ�� �̸������� �����ͺ��̽� �˻��Ͽ� Ŀ���� ����
			cs.moveToFirst(); // �˻��� Ŀ�� ����ϱ� ���ؼ� ��� // �Ⱦ��� Ŀ����ġ�� -1
			double x = cs.getDouble(5); // �˻��� Ŀ���� ��ġ�� ���� �������� ����
			double y = cs.getDouble(6);
			int state = cs.getInt(7);
			processUpdataState(intentname, state, cpzm2);
			state = 1;
			handler.insert("", intentname, intentprice, 1, x, y, state, 0, 0, 4); // nfc�� ó��������� �����ٰ� �����ͺ��̽��� ����
			alist2.add(new cdata(getApplicationContext(), intentname, intentprice, 1)); 

			priceUpdata2();
			adapter.notifyDataSetChanged();
		} else{
			for (int j = 0; j < alist2.size(); j++) {
				if (intentname.equals(alist2.get(j).getName())) {
					info(intentname);
					// �̹� nfc�ѹ� ����ٸ� temp2�迭�� �̸� ����Ǿ��ְ� �ƹ��͵� ���ϰ� for�� Ż��
					break;
				} else if(j==(alist2.size()-1)){
					c = handler.select(1);
					c.moveToFirst();

					for(int k=0; k<c.getCount(); k++) {
						if(intentname.equals(c.getString(2).toString())){
							for(int l=0; l<itemlist.size(); l++){
								if(intentname.equals(itemlist.get(l))){
									cpzm2 = l;
									break;
								}
							}
							Log.e("save2", "5");
							if(cpzm==0) {
								Log.d("cpzm=0�� ��", "start");
								Cursor cs = handler.selectinfo(intentname); // �Ѿ�� �̸������� �����ͺ��̽� �˻��Ͽ� Ŀ���� ����
								cs.moveToFirst(); // �˻��� Ŀ�� ����ϱ� ���ؼ� ��� // �Ⱦ��� Ŀ����ġ�� -1
								double x = cs.getDouble(5); // �˻��� Ŀ���� ��ġ�� ���� �������� ����
								double y = cs.getDouble(6);
								int nfcamount = cs.getInt(4); // �˻��� Ŀ���� ��ġ�� ���� �������� ����
								int state = cs.getInt(7);
								processUpdataState(intentname, state, cpzm2);
								state = itemstate.get(cpzm2);
								handler.insert("", intentname, intentprice, nfcamount, x, y, state, 0, 0, 4); // nfc�� ó��������� �����ٰ� �����ͺ��̽��� ����
								alist2.add(new cdata(getApplicationContext(), intentname, intentprice, nfcamount)); 
								info_control = 1;
								break;
							} else {
								Log.d("cpzm=0�ƴ� ��", "start");
								Cursor cs = handler.selecteach(intentname); // �Ѿ�� �̸������� �����ͺ��̽� �˻��Ͽ� Ŀ���� ����
								cs.moveToFirst(); // �˻��� Ŀ�� ����ϱ� ���ؼ� ��� // �Ⱦ��� Ŀ����ġ�� -1
								double x = cs.getDouble(5); // �˻��� Ŀ���� ��ġ�� ���� �������� ����
								double y = cs.getDouble(6);
								int state = cs.getInt(7);
								processUpdataState(intentname, state, cpzm2);
								state = itemstate.get(cpzm2);

								handler.insert("", intentname, intentprice, 1, x, y, state, 0, 0, 4); // nfc�� ó��������� �����ٰ� �����ͺ��̽��� ����
								alist2.add(new cdata(getApplicationContext(), intentname, intentprice, 1)); 
								info_control = 1;
								break;
							}
						} else{
							c.moveToNext();
						}
					}
				} else{
					Log.d("if, else if pass", "alist2");
				}

				if(info_control==1){
					info_control = 0;
					priceUpdata2();
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}
}

class cdata {
	private String name;
	private int price;
	private int amount;

	public cdata(Context context, String pname, int pprice, int pamount) {
		name = pname;
		price = pprice;
		amount = pamount;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getAmount() {
		return amount;
	}
}