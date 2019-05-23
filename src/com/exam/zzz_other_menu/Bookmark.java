package com.exam.zzz_other_menu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ActionBar;

import android.content.Intent;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.IntentFilter;
import android.content.Context;

import android.database.Cursor;

import android.nfc.NfcAdapter;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;
import com.example.n_mart.R;

public class Bookmark extends Activity {

	private static final String TAG = "nfcinventory_simple";

	// nfc���� ���
	NfcAdapter mNfcAdapter; // ���� nfc�ϵ������� �ٸ� ������ ��� �� nfc������ üũ�Ǿ� �ִ��� ���� ���� �� nfc�±׿��� ndef�����͸� �о� ���ų� �ݴ�� ���� �� �� ��� ����
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	IntentFilter[] mWriteTagFilters;

	ListView list;
	ArrayList<button_Data> alist;

	DataAdapter adapter;

	MySQLiteHandler handler;
	Cursor c;

	Button btn;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		ActionBar actionbar = getActionBar();
		actionbar.hide();

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


		handler = MySQLiteHandler.open(getApplicationContext());

		alist = new ArrayList<button_Data>();

		c = handler.select(7);
		c.moveToFirst();

		for(int i=0; i<c.getCount(); i++){
			if(c.getInt(2)==1){
				alist.add(new button_Data(c.getString(1).toString(), c.getInt(2)));
			}
			c.moveToNext();
		}

		adapter = new DataAdapter(this, alist);

		list = (ListView) findViewById(R.id.l1);
		list.setAdapter(adapter);

		handler.close();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), Main.class); // �庼 ��� �����ִ� list.java���� ����Ʈ ����
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP); // ����Ʈ �� ��Ƽ��Ƽ ���� ����
				intent.putExtra("select_mart_name", adapter.getItem(position).getBook_mart());
				startActivity(intent); // ����Ʈ ����
				finish();
			}
		});
	}

	private class DataAdapter extends ArrayAdapter<button_Data> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<button_Data> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {

			View view = null;
			if (v == null) {
				view = mInflater.inflate(R.layout.book_child, null);
			} else {
				view = v;
			}

			final button_Data BD = this.getItem(position);

			TextView Book_martname = (TextView) view.findViewById(R.id.Book_mart_childname);
			TextView Book_smallname = (TextView) view.findViewById(R.id.Book_smallname);
			ImageView Book_martpicture = (ImageView) view.findViewById(R.id.Book_martpic);

			btn = (Button) view.findViewById(R.id.Select_mart_button2);
			btn.setFocusable(false);

			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					c = handler.selectbookmarker(BD.getBook_mart());
					c.moveToFirst();

					if(c.getInt(2)==0){
						handler.updatebookmarker(BD.getBook_mart(), 1);
						BD.setBook_num(1);
						notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "���ã�⿡ �߰��Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
					} else {
						handler.updatebookmarker(BD.getBook_mart(), 0);
						BD.setBook_num(0);
						notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "���ã�⿡�� ��ü�Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
					}
				}
			});

			if(BD.getBook_num()==0){
				btn.setBackgroundResource(R.drawable.book_no);
				notifyDataSetChanged();
			} else {
				btn.setBackgroundResource(R.drawable.book_yes);
				notifyDataSetChanged();
			}

			c = handler.select(7);
			c.moveToFirst();

			for(int i=0;i<c.getCount();i++){

				if(BD.getBook_mart().equals("�Ե���Ʈ ������")){
					Book_martpicture.setBackgroundResource(R.drawable.l_gangbyun);
					Book_martname.setText("�Ե���Ʈ");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�Ե���Ʈ ��õ��")){
					Book_martpicture.setBackgroundResource(R.drawable.l_keumchon);
					Book_martname.setText("�Ե���Ʈ");
					Book_smallname.setText("��õ��");
				}else if(BD.getBook_mart().equals("�Ե���Ʈ ���￪��")){
					Book_martpicture.setBackgroundResource(R.drawable.l_seoulstation);
					Book_martname.setText("�Ե���Ʈ");
					Book_smallname.setText("���￪��");
				}else if(BD.getBook_mart().equals("�̸�Ʈ ������")){
					Book_martpicture.setBackgroundResource(R.drawable.e_gayang);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�̸�Ʈ ������")){
					Book_martpicture.setBackgroundResource(R.drawable.e_myungil);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�̸�Ʈ ����")){
					Book_martpicture.setBackgroundResource(R.drawable.e_mokdong);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("����");
				}else if(BD.getBook_mart().equals("�̸�Ʈ �̾���")){
					Book_martpicture.setBackgroundResource(R.drawable.e_mia);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("�̾���");
				}else if(BD.getBook_mart().equals("�̸�Ʈ �����")){
					Book_martpicture.setBackgroundResource(R.drawable.e_sangbong);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("�����");
				}else if(BD.getBook_mart().equals("�̸�Ʈ ������")){
					Book_martpicture.setBackgroundResource(R.drawable.e_sungsoo);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�̸�Ʈ �ŵ�����")){
					Book_martpicture.setBackgroundResource(R.drawable.e_sindorim);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("�ŵ�����");
				}else if(BD.getBook_mart().equals("�̸�Ʈ ���ǵ���")){
					Book_martpicture.setBackgroundResource(R.drawable.e_yuido);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("���ǵ���");
				}else if(BD.getBook_mart().equals("�̸�Ʈ �սʸ���")){
					Book_martpicture.setBackgroundResource(R.drawable.e_wangsiri);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("�սʸ���");
				}else if(BD.getBook_mart().equals("�̸�Ʈ �����")){
					Book_martpicture.setBackgroundResource(R.drawable.e_yongsan);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("�����");
				}else if(BD.getBook_mart().equals("�̸�Ʈ ������")){
					Book_martpicture.setBackgroundResource(R.drawable.e_eunpyung);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�̸�Ʈ �ھ���")){
					Book_martpicture.setBackgroundResource(R.drawable.e_jayang);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("�ھ���");
				}else if(BD.getBook_mart().equals("�̸�Ʈ â����")){
					Book_martpicture.setBackgroundResource(R.drawable.e_changdong);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("â����");
				}else if(BD.getBook_mart().equals("�̸�Ʈ û����")){
					Book_martpicture.setBackgroundResource(R.drawable.e_changdong);
					Book_martname.setText("�̸�Ʈ");
					Book_smallname.setText("û����");
				}else if(BD.getBook_mart().equals("Ȩ�÷��� ������")){
					Book_martpicture.setBackgroundResource(R.drawable.h_gangdong);
					Book_martname.setText("Ȩ�÷���");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("Ȩ�÷��� ���빮��")){
					Book_martpicture.setBackgroundResource(R.drawable.h_dongdaemoon);
					Book_martname.setText("Ȩ�÷���");
					Book_smallname.setText("���빮��");
				}else if(BD.getBook_mart().equals("Ȩ�÷��� �����")){
					Book_martpicture.setBackgroundResource(R.drawable.h_myunmok);
					Book_martname.setText("Ȩ�÷���");
					Book_smallname.setText("�����");
				}else if(BD.getBook_mart().equals("Ȩ�÷��� ������")){
					Book_martpicture.setBackgroundResource(R.drawable.h_banghak);
					Book_martname.setText("Ȩ�÷���");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("Ȩ�÷��� ������")){
					Book_martpicture.setBackgroundResource(R.drawable.h_siheung);
					Book_martname.setText("Ȩ�÷���");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("Ȩ�÷��� ��������")){
					Book_martpicture.setBackgroundResource(R.drawable.h_youngdeungpo);
					Book_martname.setText("Ȩ�÷���");
					Book_smallname.setText("��������");
				}else if(BD.getBook_mart().equals("Ȩ�÷��� �����")){
					Book_martpicture.setBackgroundResource(R.drawable.h_jamsil);
					Book_martname.setText("Ȩ�÷���");
					Book_smallname.setText("�����");
				}else if(BD.getBook_mart().equals("�ϳ��θ�Ʈ ����")){
					Book_martpicture.setBackgroundResource(R.drawable.n_mokdong);
					Book_martname.setText("�ϳ��θ�Ʈ");
					Book_smallname.setText("����");
				}else if(BD.getBook_mart().equals("�ϳ��θ�Ʈ �̾���")){
					Book_martpicture.setBackgroundResource(R.drawable.n_mia);
					Book_martname.setText("�ϳ��θ�Ʈ");
					Book_smallname.setText("�̾���");
				}else if(BD.getBook_mart().equals("�ϳ��θ�Ʈ ������")){
					Book_martpicture.setBackgroundResource(R.drawable.n_yangjae);
					Book_martname.setText("�ϳ��θ�Ʈ");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�ϳ��θ�Ʈ �����")){
					Book_martpicture.setBackgroundResource(R.drawable.n_yongsan);
					Book_martname.setText("�ϳ��θ�Ʈ");
					Book_smallname.setText("�����");
				}else if(BD.getBook_mart().equals("�ż����ȭ�� ������")){
					Book_martpicture.setBackgroundResource(R.drawable.s_gangnam);
					Book_martname.setText("�ż����ȭ��");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�ż����ȭ�� ����")){
					Book_martpicture.setBackgroundResource(R.drawable.s_bon);
					Book_martname.setText("�ż����ȭ��");
					Book_smallname.setText("����");
				}else if(BD.getBook_mart().equals("�����ȭ�� �̾���")){
					Book_martpicture.setBackgroundResource(R.drawable.hd_mia);
					Book_martname.setText("�����ȭ��");
					Book_smallname.setText("�̾���");
				}else if(BD.getBook_mart().equals("�����ȭ�� ������")){
					Book_martpicture.setBackgroundResource(R.drawable.hd_sinchon);
					Book_martname.setText("�����ȭ��");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� ������")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_gangnam);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� ������")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_khwanak);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("������");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� �����")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_nowon);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("�����");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� �̾���")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_mia);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("�̾���");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� ����")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_bon);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("����");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� ��������")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_youngdeungpo);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("��������");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� �����")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_jamsil);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("�����");
				}else if(BD.getBook_mart().equals("�Ե���ȭ�� û������")){
					Book_martpicture.setBackgroundResource(R.drawable.ld_chungryangri);
					Book_martname.setText("�Ե���ȭ��");
					Book_smallname.setText("û������");
					Log.d("1","û������������");
				}
				c.moveToNext();
			}
			handler.close();
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
			Log.d(TAG, "onResume: " + getIntent());

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

class button_Data {
	private String Book_mart_name;
	private int Book_mart_num;


	public button_Data(String Book_mart_name, int Book_mart_num){
		this.Book_mart_name=Book_mart_name;
		this.Book_mart_num=Book_mart_num;
	}

	public void setBook_mart(String Book_mart_name){
		this.Book_mart_name=Book_mart_name;
	}

	public void setBook_num(int Book_mart_num){
		this.Book_mart_num=Book_mart_num;
	}

	public String getBook_mart(){
		return Book_mart_name;
	}

	public int getBook_num(){
		return Book_mart_num;
	}

}