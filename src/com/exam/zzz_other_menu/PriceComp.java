package com.exam.zzz_other_menu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ActionBar;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.content.Context;
import android.content.IntentFilter.MalformedMimeTypeException;

import android.database.Cursor;

import android.nfc.NfcAdapter;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;

import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView;
import android.widget.Toast;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;
import com.example.n_mart.R;

public class PriceComp extends Activity {

	private static final String TAG = "nfcinventory_simple";

	// nfc���� ���
	NfcAdapter mNfcAdapter; // ���� nfc�ϵ������� �ٸ� ������ ��� // nfc������ üũ�Ǿ� �ִ��� ���� ���� // nfc�±׿��� ndef�����͸� �о� ���ų� �ݴ�� ���� �� �� ��� ����
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	IntentFilter[] mWriteTagFilters;

	MySQLiteHandler handler;
	Cursor c;
	Cursor cur;
	Cursor d;

	ListView list;

	ArrayList<PriceCompData_small> alist;
	ArrayList<PriceCompData_small> blist;
	ArrayList<PriceCompData> clist;
	ArrayList<PriceCompData> dlist;
	ArrayList<Integer> pricelist;
	ArrayList<String> mart_name;

	ArrayList<Integer> applelist;
	ArrayList<Integer> pearlist;
	ArrayList<Integer> boochoolist;
	ArrayList<Integer> moolist;
	ArrayList<Integer> onionlist;
	ArrayList<Integer> sangchoolist;
	ArrayList<Integer> oelist;
	ArrayList<Integer> pumpkinlist;
	ArrayList<Integer> beeflist;
	ArrayList<Integer> porklist;
	ArrayList<Integer> chickenlist;
	ArrayList<Integer> egglist;
	ArrayList<Integer> jogilist;
	ArrayList<Integer> myungtaelist;
	ArrayList<Integer> ojinglist;
	ArrayList<Integer> gofishlst;

	ArrayAdapter<String> Adapter;
	DataAdapter adapter;


	public ArrayList<String> dl = new ArrayList<String>();
	Parsing_data pd;

	int Lotte_seoul = 0, Lotte_gangbyeon = 0, Lotte_geumcheon = 0;
	int Home_gangdong = 0, Home_banghak = 0, Home_dongdaemun = 0, Home_myenmok = 0, Home_yongdungpo = 0, Home_syhung = 0, Home_jamsil = 0;
	int E_seongsu = 0, E_eunpyeong = 0, E_mokdong = 0, E_yougsan = 0, E_chunggye = 0, E_myeongil = 0, E_sindorim = 0, E_changdong = 0, E_jayang = 0, E_sangbong = 0, E_mia = 0, E_gayang = 0, E_yeouido = 0, E_wangsimni = 0;
	int Hanaro_mokdong = 0, Hanaro_mia = 0, Hanaro_yangjae = 0, Hanaro_yongsan = 0;
	int Sin_gangnam = 0, Sin_bon = 0;
	int HD_mia = 0, HD_sinchon = 0;
	int LD_gangnam = 0, LD_khwanak = 0, LD_nowon = 0, LD_mia = 0, LD_bon = 0, LD_yongdungpo = 0, LD_jamsil = 0, LD_chungryangri = 0;

	String mart_name_intent = "";
	String price_mart = "";
	String price_mart_name = "";
	int comp_price1;

	public void onCreate(Bundle savedInstanceState) {

		Log.e("PriceComp.java", "1");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_comp);

		ActionBar actionbar = getActionBar();
		actionbar.hide();

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null)
		{/*
			Log.v("list.java", "NFC�غ� �ȵ�");
			Toast.makeText(this,
					"Your device does not support NFC. Cannot run demo.",
					Toast.LENGTH_LONG).show();
			finish();
			return;*/
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




		pd = new Parsing_data();


		Intent intent = getIntent();
		mart_name_intent = intent.getExtras().get("select_mart_name").toString();
		Log.d("mart_name_intent", mart_name_intent);

		Log.e("PriceComp.java", "2");
		list = (ListView) findViewById(R.id.list1);

		Log.e("PriceComp.java", "3");
		handler = MySQLiteHandler.open(getApplicationContext());

		Log.e("PriceComp.java", "4");
		alist = new ArrayList<PriceCompData_small>();
		blist = new ArrayList<PriceCompData_small>();
		clist = new ArrayList<PriceCompData>();
		dlist = new ArrayList<PriceCompData>();
		pricelist = new ArrayList<Integer>();
		mart_name = new ArrayList<String>();

		applelist = new ArrayList<Integer>();
		pearlist = new ArrayList<Integer>();
		boochoolist = new ArrayList<Integer>();
		moolist = new ArrayList<Integer>();
		onionlist = new ArrayList<Integer>();
		sangchoolist = new ArrayList<Integer>();
		oelist = new ArrayList<Integer>();
		pumpkinlist = new ArrayList<Integer>();
		beeflist = new ArrayList<Integer>();
		porklist = new ArrayList<Integer>();
		chickenlist = new ArrayList<Integer>();
		egglist = new ArrayList<Integer>();
		jogilist = new ArrayList<Integer>();
		myungtaelist = new ArrayList<Integer>();
		ojinglist = new ArrayList<Integer>();
		gofishlst = new ArrayList<Integer>();
		/*
		View header = getLayoutInflater().inflate(R.layout.comp_header,null,false);
		TextView pricemart = (TextView) header.findViewById(R.id.Price_mart_name1);
		TextView pricemart_name = (TextView) header.findViewById(R.id.Price_mart_name2);
		TextView price_all = (TextView) header.findViewById(R.id.Price_all);
		RelativeLayout rl = (RelativeLayout) header.findViewById(R.id.rl);*/

		//	list.addHeaderView(header);

		RelativeLayout header_rellay = (RelativeLayout) findViewById(R.id.headlist1);
		ImageView header_img = (ImageView) findViewById(R.id.header_image);
		TextView header_mart_name = (TextView) findViewById(R.id.header_mart_name);
		TextView header_small_name = (TextView) findViewById(R.id.header_small_name);
		TextView header_allprice = (TextView) findViewById(R.id.header_allprice);


		for(int i=1; i<41; i++){
			applelist.add((1+((i-1)*16)));
			pearlist.add((2+((i-1)*16)));
			boochoolist.add((3+((i-1)*16)));
			moolist.add((4+((i-1)*16)));
			onionlist.add((5+((i-1)*16)));
			sangchoolist.add((6+((i-1)*16)));
			oelist.add((7+((i-1)*16)));
			pumpkinlist.add((8+((i-1)*16)));
			beeflist.add((9+((i-1)*16)));
			porklist.add((10+((i-1)*16)));
			chickenlist.add((11+((i-1)*16)));
			egglist.add((12+((i-1)*16)));
			jogilist.add((13+((i-1)*16)));
			myungtaelist.add((14+((i-1)*16)));
			ojinglist.add((15+((i-1)*16)));
			gofishlst.add((16+((i-1)*16)));
			// list.get(i-1)�ΰ� ����ϱ�
		}

		c = handler.select(1);
		c.moveToFirst();

		for(int i=0; i<c.getCount(); i++){
			Log.d("�����ͺ��̽� ����", Integer.toString(c.getInt(0)));
			alist.add(new PriceCompData_small(c.getString(2).toString(), c.getInt(4)));

			c.moveToNext();
		}

		Log.d("���䰡??", "1");
		for(int i=0; i<alist.size(); i++){
			String a = alist.get(i).getName();
			if(a.contains("���")){
				blist.add(new PriceCompData_small("���", alist.get(i).getAmount()));
			} else if(a.contains("��")){
				if(a.contains("����")){
					blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
				} else {
					blist.add(new PriceCompData_small("��", alist.get(i).getAmount()));
				}
			} else if(a.contains("��")){
				blist.add(new PriceCompData_small("��", alist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
			} else if(a.contains("ȣ��")){
				blist.add(new PriceCompData_small("ȣ��", alist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
			} else if(a.contains("�������")){
				blist.add(new PriceCompData_small("�������", alist.get(i).getAmount()));
			} else if(a.contains("�߰��")){
				blist.add(new PriceCompData_small("�߰��", alist.get(i).getAmount()));
			} else if(a.contains("�ް�")){
				blist.add(new PriceCompData_small("�ް�", alist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
			} else if(a.contains("����") || a.contains("����")){
				blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
			} else if(a.contains("��¡��")){
				blist.add(new PriceCompData_small("��¡��", alist.get(i).getAmount()));
			} else if(a.contains("����")){
				blist.add(new PriceCompData_small("����", alist.get(i).getAmount()));
			}
		}

		Log.d("���䰡??", "2");
		c = handler.select(5);
		c.moveToFirst();

		for(int i=0; i<c.getCount(); i++){
			Log.d("adsfsadfasd", c.getString(1).toString() + "   " + Integer.toString(c.getInt(3)));
		}

		for(int i=0; i<blist.size(); i++){
			if(blist.get(i).getName().equals("���")){
				for(int j=0; j<applelist.size(); j++){
					c.moveToPosition((applelist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("��")){
				for(int j=0; j<pearlist.size(); j++){
					c.moveToPosition((pearlist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<boochoolist.size(); j++){
					c.moveToPosition((boochoolist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("��")){
				for(int j=0; j<moolist.size(); j++){
					c.moveToPosition((moolist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<onionlist.size(); j++){
					c.moveToPosition((onionlist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<sangchoolist.size(); j++){
					c.moveToPosition((sangchoolist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<oelist.size(); j++){
					c.moveToPosition((oelist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("ȣ��")){
				for(int j=0; j<pumpkinlist.size(); j++){
					c.moveToPosition((pumpkinlist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<beeflist.size(); j++){
					c.moveToPosition((beeflist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("�������")){
				for(int j=0; j<porklist.size(); j++){
					c.moveToPosition((porklist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("�߰��")){
				for(int j=0; j<chickenlist.size(); j++){
					c.moveToPosition((chickenlist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("�ް�")){
				for(int j=0; j<egglist.size(); j++){
					c.moveToPosition((egglist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<jogilist.size(); j++){
					c.moveToPosition((jogilist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<myungtaelist.size(); j++){
					c.moveToPosition((myungtaelist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("��¡��")){
				for(int j=0; j<ojinglist.size(); j++){
					c.moveToPosition((ojinglist.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			} else if(blist.get(i).getName().equals("����")){
				for(int j=0; j<gofishlst.size(); j++){
					c.moveToPosition((gofishlst.get(j)-1));
					clist.add(new PriceCompData(c.getString(1).toString(), c.getString(2).toString(), c.getInt(3), blist.get(i).getAmount()));
				}
			}
		}

		Log.d("���䰡??", "3");
		for(int i=0; i<clist.size(); i++){
			if(clist.get(i).getMartName().equals("�Ե���Ʈ ������")){
				Log.e("���� ���� ���ƿ�~~", Integer.toString(clist.get(i).getPrice()) + "  " + Integer.toString(clist.get(i).getAmount()));
				Lotte_gangbyeon = Lotte_gangbyeon + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���Ʈ ��õ��")){
				Lotte_geumcheon = Lotte_geumcheon + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���Ʈ ���￪��")){
				Lotte_seoul = Lotte_seoul + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ ������")){
				E_gayang = E_gayang + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ ������")){
				E_myeongil = E_myeongil + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ ����")){
				E_mokdong = E_mokdong + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ �̾���")){
				E_mia = E_mia + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ �����")){
				E_sangbong= E_sangbong + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ ������")){
				E_seongsu = E_seongsu + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ �ŵ�����")){
				E_sindorim = E_sindorim + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ ���ǵ���")){
				E_yeouido = E_yeouido + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ �սʸ���")){
				E_wangsimni = E_wangsimni + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ �����")){
				E_yougsan = E_yougsan + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ ������")){
				E_eunpyeong = E_eunpyeong + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ �ھ���")){
				E_jayang = E_jayang + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ â����")){
				E_changdong = E_changdong + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�̸�Ʈ û����")){
				E_chunggye = E_chunggye + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("Ȩ�÷��� ������")){
				Home_gangdong = Home_gangdong + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("Ȩ�÷��� ���빮��")){
				Home_dongdaemun = Home_dongdaemun + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("Ȩ�÷��� �����")){
				Home_myenmok = Home_myenmok + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("Ȩ�÷��� ������")){
				Home_banghak = Home_banghak + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("Ȩ�÷��� ������")){
				Home_syhung = Home_syhung + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("Ȩ�÷��� ��������")){
				Home_yongdungpo = Home_yongdungpo + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("Ȩ�÷��� �����")){
				Home_jamsil = Home_jamsil + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�ϳ��θ�Ʈ ����")){
				Hanaro_mokdong = Hanaro_mokdong + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�ϳ��θ�Ʈ �̾���")){
				Hanaro_mia = Hanaro_mia + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�ϳ��θ�Ʈ ������")){
				Hanaro_yangjae = Hanaro_yangjae + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�ϳ��θ�Ʈ �����")){
				Hanaro_yongsan = Hanaro_yongsan + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�ż����ȭ�� ������")){
				Sin_gangnam = Sin_gangnam + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�ż����ȭ�� ����")){
				Sin_bon = Sin_bon + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�����ȭ�� �̾���")){
				HD_mia = HD_mia + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�����ȭ�� ������")){
				HD_sinchon = HD_sinchon + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� ������")){
				LD_gangnam = LD_gangnam + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� ������")){
				LD_khwanak = LD_khwanak + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� �����")){
				LD_nowon = LD_nowon + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� �̾���")){
				LD_mia = LD_mia + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� ����")){
				LD_bon = LD_bon + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� ��������")){
				LD_yongdungpo = LD_yongdungpo + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� �����")){
				LD_jamsil = LD_jamsil + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else if(clist.get(i).getMartName().equals("�Ե���ȭ�� û������")){
				LD_chungryangri = LD_chungryangri + (clist.get(i).getPrice()*clist.get(i).getAmount());
			} else {
			}
		}

		Log.d("���䰡??", "4");
		pricelist.add(0, Lotte_gangbyeon);
		pricelist.add(1, Lotte_geumcheon);
		pricelist.add(2, Lotte_seoul);

		pricelist.add(3, E_gayang);
		pricelist.add(4, E_myeongil);
		pricelist.add(5, E_mokdong);
		pricelist.add(6, E_mia);
		pricelist.add(7, E_sangbong);
		pricelist.add(8, E_seongsu);
		pricelist.add(9, E_sindorim);
		pricelist.add(10, E_yeouido);
		pricelist.add(11, E_wangsimni);
		pricelist.add(12, E_yougsan);
		pricelist.add(13, E_eunpyeong);
		pricelist.add(14, E_jayang);
		pricelist.add(15, E_changdong);		
		pricelist.add(16, E_chunggye);		

		pricelist.add(17, Home_gangdong);		
		pricelist.add(18, Home_dongdaemun);
		pricelist.add(19, Home_myenmok);
		pricelist.add(20, Home_banghak);
		pricelist.add(21, Home_syhung);
		pricelist.add(22, Home_yongdungpo);		
		pricelist.add(23, Home_jamsil);

		pricelist.add(24, Hanaro_mokdong);
		pricelist.add(25, Hanaro_mia);
		pricelist.add(26, Hanaro_yangjae);
		pricelist.add(27, Hanaro_yongsan);

		pricelist.add(28, Sin_gangnam);
		pricelist.add(29, Sin_bon);

		pricelist.add(30, HD_mia);
		pricelist.add(31, HD_sinchon);

		pricelist.add(32, LD_gangnam);
		pricelist.add(33, LD_khwanak);
		pricelist.add(34, LD_nowon);
		pricelist.add(35, LD_mia);
		pricelist.add(36, LD_bon);
		pricelist.add(37, LD_yongdungpo);
		pricelist.add(38, LD_jamsil);
		pricelist.add(39, LD_chungryangri);








		mart_name.add(0, "�Ե���Ʈ ������"); 
		mart_name.add(1, "�Ե���Ʈ ��õ��");
		mart_name.add(2, "�Ե���Ʈ ���￪��");

		mart_name.add(3, "�̸�Ʈ ������"); 
		mart_name.add(4, "�̸�Ʈ ������"); 
		mart_name.add(5, "�̸�Ʈ ����"); 
		mart_name.add(6, "�̸�Ʈ �̾���");
		mart_name.add(7, "�̸�Ʈ �����"); 
		mart_name.add(8, "�̸�Ʈ ������"); 
		mart_name.add(9, "�̸�Ʈ �ŵ�����");
		mart_name.add(10, "�̸�Ʈ ���ǵ���");
		mart_name.add(11, "�̸�Ʈ �սʸ���");
		mart_name.add(12, "�̸�Ʈ �����"); 
		mart_name.add(13, "�̸�Ʈ ������"); 
		mart_name.add(14, "�̸�Ʈ �ھ���"); 
		mart_name.add(15, "�̸�Ʈ â����"); 
		mart_name.add(16, "�̸�Ʈ û����");		

		mart_name.add(17, "Ȩ�÷��� ������"); 		 
		mart_name.add(18, "Ȩ�÷��� ���빮��");
		mart_name.add(19, "Ȩ�÷��� �����");
		mart_name.add(20, "Ȩ�÷��� ������");
		mart_name.add(21, "Ȩ�÷��� ������");
		mart_name.add(22, "Ȩ�÷��� ��������");
		mart_name.add(23, "Ȩ�÷��� �����"); 

		mart_name.add(24, "�ϳ��θ�Ʈ ����");
		mart_name.add(25, "�ϳ��θ�Ʈ �̾���");
		mart_name.add(26, "�ϳ��θ�Ʈ ������");
		mart_name.add(27, "�ϳ��θ�Ʈ �����");

		mart_name.add(28, "�ż����ȭ�� ������");
		mart_name.add(29, "�ż����ȭ�� ����");

		mart_name.add(30, "�����ȭ�� �̾���");
		mart_name.add(31, "�����ȭ�� ������");

		mart_name.add(32, "�Ե���ȭ�� ������");
		mart_name.add(33, "�Ե���ȭ�� ������");
		mart_name.add(34, "�Ե���ȭ�� �����");
		mart_name.add(35, "�Ե���ȭ�� �̾���");
		mart_name.add(36, "�Ե���ȭ�� ����");
		mart_name.add(37, "�Ե���ȭ�� ��������");
		mart_name.add(38, "�Ե���ȭ�� �����");
		mart_name.add(39, "�Ե���ȭ�� û������");	

		Log.d("���䰡??", "1");
		for(int i=0; i<mart_name.size(); i++){
			dlist.add(new PriceCompData(mart_name.get(i), "", pricelist.get(i), 0));
		}

		for(int i=0; i<dlist.size(); i++){
			Log.d("Ȯ�ο�", dlist.get(i).getMartName() + " " + dlist.get(i).getPrice());
		}

		for(int i=0; i<dlist.size()-1; i++){
			if(dlist.get(i).getPrice()>dlist.get(i+1).getPrice()){

				Log.d("dlist", dlist.get(i).getMartName() + "   " + Integer.toString(dlist.get(i).getPrice()));
				Log.d("dlist", dlist.get(i+1).getMartName() + "   " + Integer.toString(dlist.get(i+1).getPrice()));

				PriceCompData pcd = new PriceCompData(dlist.get(i+1).getMartName(), "", dlist.get(i+1).getPrice(), 0);
				PriceCompData pcd2 = new PriceCompData(dlist.get(i).getMartName(), "", dlist.get(i).getPrice(), 0);
				dlist.set(i, pcd);
				dlist.set(i+1, pcd2);

				Log.d("dlist", dlist.get(i).getMartName() + "   " + Integer.toString(dlist.get(i).getPrice()));
				Log.d("dlist", dlist.get(i+1).getMartName() + "   " + Integer.toString(dlist.get(i+1).getPrice()));


				i = -1;
			} else {
				Log.d("����", "���׿�~");
			}
		}

		c = handler.select(1);
		c.moveToFirst();


		if(c.getCount()!=0){

			if(c.getString(1).toString().equals("�Ե���Ʈ ������")){

				header_img.setBackgroundResource(R.drawable.l_gangbyncomp);
				header_mart_name.setText("�Ե���Ʈ");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(0));




			}else if(c.getString(1).toString().equals("�Ե���Ʈ ��õ��")){
				header_img.setBackgroundResource(R.drawable.l_keumchoncomp);
				header_mart_name.setText("�Ե���Ʈ");
				header_small_name.setText("��õ��");
				header_allprice.setText("" + pricelist.get(1));
			}else if(c.getString(1).toString().equals("�Ե���Ʈ ���￪��")){
				header_img.setBackgroundResource(R.drawable.l_seoulstationcomp);
				header_mart_name.setText("�Ե���Ʈ");
				header_small_name.setText("���￪��");
				header_allprice.setText("" + pricelist.get(2));
			}else if(c.getString(1).toString().equals("�̸�Ʈ ������")){
				header_img.setBackgroundResource(R.drawable.e_gayangcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(3));
			}else if(c.getString(1).toString().equals("�̸�Ʈ ������")){
				header_img.setBackgroundResource(R.drawable.e_myungilcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(4));
			}else if(c.getString(1).toString().equals("�̸�Ʈ ����")){
				header_img.setBackgroundResource(R.drawable.e_mokdongcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("����");
				header_allprice.setText("" + pricelist.get(5));
			}else if(c.getString(1).toString().equals("�̸�Ʈ �̾���")){
				header_img.setBackgroundResource(R.drawable.e_miacomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("�̾���");
				header_allprice.setText("" + pricelist.get(6));
			}else if(c.getString(1).toString().equals("�̸�Ʈ �����")){
				header_img.setBackgroundResource(R.drawable.e_sangbongcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("�����");
				header_allprice.setText("" + pricelist.get(7));
			}else if(c.getString(1).toString().equals("�̸�Ʈ ������")){
				header_img.setBackgroundResource(R.drawable.e_sungsoocomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(8));
			}else if(c.getString(1).toString().equals("�̸�Ʈ �ŵ�����")){
				header_img.setBackgroundResource(R.drawable.e_sindorimcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("�ŵ�����");
				header_allprice.setText("" + pricelist.get(9));
			}else if(c.getString(1).toString().equals("�̸�Ʈ ���ǵ���")){
				header_img.setBackgroundResource(R.drawable.e_yuidocomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("���ǵ���");
				header_allprice.setText("" + pricelist.get(10));
			}else if(c.getString(1).toString().equals("�̸�Ʈ �սʸ���")){
				header_img.setBackgroundResource(R.drawable.e_wangsiricomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("�սʸ���");
				header_allprice.setText("" + pricelist.get(11));
			}else if(c.getString(1).toString().equals("�̸�Ʈ �����")){
				header_img.setBackgroundResource(R.drawable.e_yongsancomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("�����");
				header_allprice.setText("" + pricelist.get(12));
			}else if(c.getString(1).toString().equals("�̸�Ʈ ������")){
				header_img.setBackgroundResource(R.drawable.e_eunpyungcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(13));
			}else if(c.getString(1).toString().equals("�̸�Ʈ �ھ���")){
				header_img.setBackgroundResource(R.drawable.e_jayangcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("�ھ���");
				header_allprice.setText("" + pricelist.get(14));
			}else if(c.getString(1).toString().equals("�̸�Ʈ â����")){
				header_img.setBackgroundResource(R.drawable.e_changdongcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("â����");
				header_allprice.setText("" + pricelist.get(15));
			}else if(c.getString(1).toString().equals("�̸�Ʈ û����")){
				header_img.setBackgroundResource(R.drawable.e_changdongcomp);
				header_mart_name.setText("�̸�Ʈ");
				header_small_name.setText("û����");
				header_allprice.setText("" + pricelist.get(16));
			}else if(c.getString(1).toString().equals("Ȩ�÷��� ������")){
				header_img.setBackgroundResource(R.drawable.h_gangdongcomp);
				header_mart_name.setText("Ȩ�÷���");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(17));
			}else if(c.getString(1).toString().equals("Ȩ�÷��� ���빮��")){
				header_img.setBackgroundResource(R.drawable.h_dongdaemooncomp);
				header_mart_name.setText("Ȩ�÷���");
				header_small_name.setText("���빮��");
				header_allprice.setText("" + pricelist.get(18));
			}else if(c.getString(1).toString().equals("Ȩ�÷��� �����")){
				header_img.setBackgroundResource(R.drawable.h_myunmokcomp);
				header_mart_name.setText("Ȩ�÷���");
				header_small_name.setText("�����");
				header_allprice.setText("" + pricelist.get(19));
			}else if(c.getString(1).toString().equals("Ȩ�÷��� ������")){
				header_img.setBackgroundResource(R.drawable.h_banghakcomp);
				header_mart_name.setText("Ȩ�÷���");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(20));
			}else if(c.getString(1).toString().equals("Ȩ�÷��� ������")){
				header_img.setBackgroundResource(R.drawable.h_siheungcomp);
				header_mart_name.setText("Ȩ�÷���");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(21));
			}else if(c.getString(1).toString().equals("Ȩ�÷��� ��������")){
				header_img.setBackgroundResource(R.drawable.h_youngdeungpocomp);
				header_mart_name.setText("Ȩ�÷���");
				header_small_name.setText("��������");
				header_allprice.setText("" + pricelist.get(22));
			}else if(c.getString(1).toString().equals("Ȩ�÷��� �����")){
				header_img.setBackgroundResource(R.drawable.h_jamsilcomp);
				header_mart_name.setText("Ȩ�÷���");
				header_small_name.setText("�����");
				header_allprice.setText("" + pricelist.get(23));
			}else if(c.getString(1).toString().equals("�ϳ��θ�Ʈ ����")){
				header_img.setBackgroundResource(R.drawable.n_mokdongcomp);
				header_mart_name.setText("�ϳ��θ�Ʈ");
				header_small_name.setText("����");
				header_allprice.setText("" + pricelist.get(24));
			}else if(c.getString(1).toString().equals("�ϳ��θ�Ʈ �̾���")){
				header_img.setBackgroundResource(R.drawable.n_miacomp);
				header_mart_name.setText("�ϳ��θ�Ʈ");
				header_small_name.setText("�̾���");
				header_allprice.setText("" + pricelist.get(25));
			}else if(c.getString(1).toString().equals("�ϳ��θ�Ʈ ������")){
				header_img.setBackgroundResource(R.drawable.n_yangjaecomp);
				header_mart_name.setText("�ϳ��θ�Ʈ");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(26));
			}else if(c.getString(1).toString().equals("�ϳ��θ�Ʈ �����")){
				header_img.setBackgroundResource(R.drawable.n_yongsancomp);
				header_mart_name.setText("�ϳ��θ�Ʈ");
				header_small_name.setText("�����");
				header_allprice.setText("" + pricelist.get(27));
			}else if(c.getString(1).toString().equals("�ż����ȭ�� ������")){
				header_img.setBackgroundResource(R.drawable.s_gangnamcomp);
				header_mart_name.setText("�ż����ȭ��");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(28));
			}else if(c.getString(1).toString().equals("�ż����ȭ�� ����")){
				header_img.setBackgroundResource(R.drawable.s_boncomp);
				header_mart_name.setText("�ż����ȭ��");
				header_small_name.setText("����");
				header_allprice.setText("" + pricelist.get(29));
			}else if(c.getString(1).toString().equals("�����ȭ�� �̾���")){
				header_img.setBackgroundResource(R.drawable.hd_miacomp);
				header_mart_name.setText("�����ȭ��");
				header_small_name.setText("�̾���");
				header_allprice.setText("" + pricelist.get(30));
			}else if(c.getString(1).toString().equals("�����ȭ�� ������")){
				header_img.setBackgroundResource(R.drawable.hd_sinchoncomp);
				header_mart_name.setText("�����ȭ��");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(31));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� ������")){
				header_img.setBackgroundResource(R.drawable.ld_gangnamcomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(32));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� ������")){
				header_img.setBackgroundResource(R.drawable.ld_khwanakcomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("������");
				header_allprice.setText("" + pricelist.get(33));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� �����")){
				header_img.setBackgroundResource(R.drawable.ld_nowoncomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("�����");
				header_allprice.setText("" + pricelist.get(34));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� �̾���")){
				header_img.setBackgroundResource(R.drawable.ld_miacomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("�̾���");
				header_allprice.setText("" + pricelist.get(35));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� ����")){
				header_img.setBackgroundResource(R.drawable.ld_boncomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("����");
				header_allprice.setText("" + pricelist.get(36));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� ��������")){
				header_img.setBackgroundResource(R.drawable.ld_youngdeungpocomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("��������");
				header_allprice.setText("" + pricelist.get(37));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� �����")){
				header_img.setBackgroundResource(R.drawable.ld_jamsilcomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("�����");
				header_allprice.setText("" + pricelist.get(38));
			}else if(c.getString(1).toString().equals("�Ե���ȭ�� û������")){
				header_img.setBackgroundResource(R.drawable.ld_chungryangricomp);
				header_mart_name.setText("�Ե���ȭ��");
				header_small_name.setText("û������");
				header_allprice.setText("" + pricelist.get(39));
			}
		}
		comp_price1 = Integer.parseInt(header_allprice.getText().toString());

		Log.e("PriceComp.java", "9");

		price_mart = header_mart_name.getText().toString();
		price_mart_name = header_small_name.getText().toString();

		c = handler.select(1);
		c.moveToFirst();

		d = handler.select(5);
		d.moveToFirst();

		//		for(int i=0; i<dlist.size(); i++){
		//
		//			if(dlist.get(i).getMartName().equals(price_mart + " " + price_mart_name)){
		//				dlist.remove(i);
		//			}
		//		}

		Log.e("������ �α�", Integer.toString(pd.latitude.size()));
		Log.e("������ �α�2", Integer.toString(pd.longitude.size()));


		for(int j = pd.latitude.size()-1; j>-1; j--){


			//			Double f= Math.sqrt(Math.pow(pd.latitude.get(j)-c.getDouble(8),2) + Math.pow(pd.longitude.get(j)-c.getDouble(9),2));
			//			Log.d("�����浵������",""+f);

			if(Math.sqrt( Math.pow(pd.latitude.get(j)- c.getDouble(8),2)  + 
					Math.pow(pd.longitude.get(j)- c.getDouble(9),2)	)>0.042591996){
				for(int i = 0; i<dlist.size();i++){
					if(dlist.get(i).getMartName() == mart_name.get(j).toString()){
						dlist.remove(i);
					}
				}
			}	
		}

		/*for(int i=0; i<mart_name.size(); i++){
			Log.e("��Ʈ����Ȯ��", ""+ mart_name.get(i).toString());
		}*/

		//		for(int i=0; i<dlist.size(); i++){
		//			if(dlist.get(i).getMartName() == mart_name.get(3).toString()){
		//				Log.e("�𸮽�Ʈ3", ""+i+"  "+ dlist.get(i).getMartName());
		//				dlist.remove(i);
		//			}
		//			
		//		}


		/*	dlist.remove(3);
		for(int i=0; i< dlist.size(); i++){

			Log.e("�𸮽�Ʈ ����Ȯ��", ""+i+"  "+ dlist.get(i).getMartName());
		}*/


		for(int i=0; i<dlist.size(); i++){

			if(dlist.get(i).getMartName().equals(price_mart + " " + price_mart_name)){
				dlist.remove(i);
			}
		}


		adapter = new DataAdapter(this, dlist); // Ŀ���Ҿ���Ϳ� ������¿� ��̸���Ʈ ����
		adapter.notifyDataSetChanged(); // ����Ʈ�� ������ ����Ǿ��ٰ� ����Ϳ��� �˸�
		list.setAdapter(adapter); // ����Ʈ��� ����� ����

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

				Builder dlg = new AlertDialog.Builder(PriceComp.this);
				dlg.setTitle("����")
				.setMessage(adapter.getItem(position).getMartName() + " �� ���� �ϼ̽��ϴ� ������ Ȯ�� �ƴϸ� ��Ҹ� �����ϼ���")
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Log.e("������Ȯ��", Integer.toString(position));
						Intent intent = new Intent(getApplicationContext(), list.class); // �庼 ��� �����ִ� list.java���� ����Ʈ ����
						intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP); // ����Ʈ �� ��Ƽ��Ƽ ���� ����
						intent.putExtra("select_mart_name", adapter.getItem(position).getMartName());
						intent.putExtra("select_mart_name2", mart_name_intent);
						startActivity(intent); // ����Ʈ ����
					//	finish();

					}
				})
				.setNegativeButton("���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
			}
		});

		header_rellay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Builder dlg = new AlertDialog.Builder(PriceComp.this);
				dlg.setTitle("����")
				.setMessage(price_mart + " " + price_mart_name + " �� ���� �ϼ̽��ϴ� ������ Ȯ�� �ƴϸ� ��Ҹ� �����ϼ���")
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

//						Toast.makeText(getApplicationContext(), "�Ⱦ�Ⱦ�~~~", Toast.LENGTH_SHORT).show();

						Intent intent2 = new Intent(getApplicationContext(), list.class); // �庼 ��� �����ִ� list.java���� ����Ʈ ����
						intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP); // ����Ʈ �� ��Ƽ��Ƽ ���� ����
						intent2.putExtra("select_mart_name", price_mart + " " + price_mart_name);
						intent2.putExtra("select_mart_name2", mart_name_intent);
						startActivity(intent2); // ����Ʈ ����
					//	finish();
					}
				})
				.setNegativeButton("���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
			}
		});


		handler.close();
		Log.e("PriceComp.java", "10");
	}

	private class DataAdapter extends ArrayAdapter<PriceCompData> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<PriceCompData> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {

			Log.e("getView", "1");
			View view = null;
			if (v == null) {
				view = mInflater.inflate(R.layout.comp_list, null);
			} else {
				view = v;
			}

			Log.e("getView", "2");
			PriceCompData pcd = this.getItem(position);
			//			adapter = new DataAdapter(this, dlist);
			Log.e("getView", "3");
			TextView MartNameText = (TextView) view.findViewById(R.id.marttext);
			TextView PriceText = (TextView) view.findViewById(R.id.pricetext);
			TextView Smallname = (TextView) view.findViewById(R.id.smallname);
			ImageView comp_arrow = (ImageView) view.findViewById(R.id.comp_arrow);

			ImageView mart_picture = (ImageView) view.findViewById(R.id.Comp_mart5);



			if(pcd.getMartName().equals("�Ե���Ʈ ������")){



				MartNameText.setText("�Ե���Ʈ");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());
				mart_picture.setBackgroundResource(R.drawable.l_gangbyun);

				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���Ʈ ��õ��")){
				MartNameText.setText("�Ե���Ʈ");
				Smallname.setText("��õ��");
				PriceText.setText("" + pcd.getPrice());
				mart_picture.setBackgroundResource(R.drawable.l_keumchon);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���Ʈ ���￪��")){
				MartNameText.setText("�Ե���Ʈ");
				Smallname.setText("���￪��");
				PriceText.setText("" + pcd.getPrice());	
				mart_picture.setBackgroundResource(R.drawable.l_seoulstation);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ ������")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("������");
				mart_picture.setBackgroundResource(R.drawable.e_gayang);
				PriceText.setText("" + pcd.getPrice());		
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ ������")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());		
				mart_picture.setBackgroundResource(R.drawable.e_myungil);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ ����")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("����");
				PriceText.setText("" + pcd.getPrice());		
				mart_picture.setBackgroundResource(R.drawable.e_mokdong);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ �̾���")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("�̾���");
				PriceText.setText("" + pcd.getPrice());		
				mart_picture.setBackgroundResource(R.drawable.e_mia);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ �����")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("�����");
				PriceText.setText("" + pcd.getPrice());	
				mart_picture.setBackgroundResource(R.drawable.e_sangbong);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ ������")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());		
				mart_picture.setBackgroundResource(R.drawable.e_sungsoo);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ �ŵ�����")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("�ŵ�����");
				PriceText.setText("" + pcd.getPrice());		
				mart_picture.setBackgroundResource(R.drawable.e_sindorim);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ ���ǵ���")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("���ǵ���");
				PriceText.setText("" + pcd.getPrice());		
				mart_picture.setBackgroundResource(R.drawable.e_yuido);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ �սʸ���")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("�սʸ���");
				PriceText.setText("" + pcd.getPrice());				
				mart_picture.setBackgroundResource(R.drawable.e_wangsiri);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ �����")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("�����");
				mart_picture.setBackgroundResource(R.drawable.e_yongsan);
				PriceText.setText("" + pcd.getPrice());		
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ ������")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.e_eunpyung);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ �ھ���")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("�ھ���");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.e_jayang);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ â����")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("â����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.e_changdong);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�̸�Ʈ û����")){
				MartNameText.setText("�̸�Ʈ");
				Smallname.setText("û����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.e_chungye);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("Ȩ�÷��� ������")){
				MartNameText.setText("Ȩ�÷���");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.h_gangdong);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("Ȩ�÷��� ���빮��")){
				MartNameText.setText("Ȩ�÷���");
				Smallname.setText("���빮��");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.h_dongdaemoon);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("Ȩ�÷��� �����")){
				MartNameText.setText("Ȩ�÷���");
				Smallname.setText("�����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.h_myunmok);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("Ȩ�÷��� ������")){
				MartNameText.setText("Ȩ�÷���");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.h_banghak);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("Ȩ�÷��� ������")){
				MartNameText.setText("Ȩ�÷���");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.h_siheung);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("Ȩ�÷��� ��������")){
				MartNameText.setText("Ȩ�÷���");
				Smallname.setText("��������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.h_youngdeungpo);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("Ȩ�÷��� �����")){
				MartNameText.setText("Ȩ�÷���");
				Smallname.setText("�����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.h_jamsil);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�ϳ��θ�Ʈ ����")){
				MartNameText.setText("�ϳ��θ�Ʈ");
				Smallname.setText("����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.n_mokdong);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�ϳ��θ�Ʈ �̾���")){
				MartNameText.setText("�ϳ��θ�Ʈ");
				Smallname.setText("�̾���");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.n_mia);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�ϳ��θ�Ʈ ������")){
				MartNameText.setText("�ϳ��θ�Ʈ");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.n_yangjae);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�ϳ��θ�Ʈ �����")){
				MartNameText.setText("�ϳ��θ�Ʈ");
				Smallname.setText("�����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.n_yongsan);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�ż����ȭ�� ������")){
				MartNameText.setText("�ż����ȭ��");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.s_gangnam);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�ż����ȭ�� ����")){
				MartNameText.setText("�ż����ȭ��");
				Smallname.setText("����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.s_bon);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�����ȭ�� �̾���")){
				MartNameText.setText("�����ȭ��");
				Smallname.setText("�̾���");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.hd_mia);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�����ȭ�� ������")){
				MartNameText.setText("�����ȭ��");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.hd_sinchon);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� ������")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_gangnam);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� ������")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_khwanak);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� �����")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("�����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_nowon);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� �̾���")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("�̾���");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_mia);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� ����")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_bon);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� ��������")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("��������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_youngdeungpo);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� �����")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("�����");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_jamsil);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			} else if(pcd.getMartName().equals("�Ե���ȭ�� û������")){
				MartNameText.setText("�Ե���ȭ��");
				Smallname.setText("û������");
				PriceText.setText("" + pcd.getPrice());			
				mart_picture.setBackgroundResource(R.drawable.ld_chungryangri);
				if(comp_price1>pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_down);
				}else if(comp_price1<pcd.getPrice()){
					comp_arrow.setBackgroundResource(R.drawable.comp_up);
				}else{
					comp_arrow.setBackgroundResource(R.drawable.comp_equal);
				}
			}



			Log.e("getView", "5");
			return view;
		}
	}
	// �� �Ʒ����ʹ� NFC���� �Լ�

	// ����׶��� ����ġ Ȱ��ȭ
	@Override
	protected void onResume()
	{
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

class PriceCompData_small{

	private String Name;
	private int Amount;

	public PriceCompData_small(String pName, int pAmount){

		this.Name = pName;
		this.Amount = pAmount;
	}

	public String getName(){
		return Name;
	}

	public int getAmount(){
		return Amount;
	}
}

class PriceCompData{

	private String MartName;
	private String Name;
	private int Price;
	private int Amount;

	public PriceCompData(String pMartName, String pName, int pPrice, int pAmount){

		this.MartName = pMartName;
		this.Name = pName;
		this.Price = pPrice;
		this.Amount = pAmount;
	}

	public String getMartName(){
		return MartName;
	}

	public String getName(){
		return Name;
	}

	public int getPrice(){
		return Price;
	}

	public int getAmount(){
		return Amount;
	}
}

