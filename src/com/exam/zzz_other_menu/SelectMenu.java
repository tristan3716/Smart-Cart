package com.exam.zzz_other_menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;
import com.exam.zzz_other_menu_nfc.nfcexam;
import com.example.developer_info.develope_activity;
import com.example.n_mart.MainActivity;
import com.example.n_mart.R;

public class SelectMenu extends Activity implements View.OnClickListener{

	private static final String TAG = "nfcinventory_simple";

	// nfc���� ���
	NfcAdapter mNfcAdapter; // ���� nfc�ϵ������� �ٸ� ������ ��� // nfc������ üũ�Ǿ� �ִ��� ���� ���� // nfc�±׿��� ndef�����͸� �о� ���ų� �ݴ�� ���� �� �� ��� ����
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	AlertDialog mWriteTagDialog;

	MySQLiteHandler handler; // �����ͺ��̽� ������� handler
	Cursor c;

	ConnectivityManager cManager;
	NetworkInfo mobile;
	NetworkInfo wifi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectmenu);
		Log.e("SelectMenu.java", "onCreate!!");

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
		} else {

			// check if NFC is enabled
			checkNfcEnabled();

			mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
					getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

			IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
			try	{
				ndefDetected.addDataType("application/root.gast.playground.nfc");
			} catch (MalformedMimeTypeException e) {
				throw new RuntimeException("Could not add MIME type.", e);
			}
		}

		ActionBar actionbar = getActionBar();
		actionbar.hide();



		cManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		Button button1 = (Button) findViewById(R.id.map);
		Button button2 = (Button) findViewById(R.id.listmap);
		Button button3 = (Button) findViewById(R.id.delete);
		Button button4 = (Button) findViewById(R.id.help);
		Button button5 = (Button) findViewById(R.id.info);

		handler = MySQLiteHandler.open(getApplicationContext());

		c = handler.select(5);
		c.moveToFirst();

		if(c.getCount()==0 || ((!mobile.isConnected() && !wifi.isConnected()))){
			Builder dlg = new AlertDialog.Builder(SelectMenu.this);
			dlg.setTitle("����")
			.setMessage("���ͳ� ������ �Ǿ����� �ʾ� ��Ʈ������ ����� ������� �ʾҰų� \n" +
					"��Ʈ���� ������Ʈ�� ����� ���� �ʾҽ��ϴ�." +
					"���ͳ� ���� �� �ٽ� �����Ͽ� �ּ���")
					// ���̾�α��� �ƴϿ� ��ư
					.setNegativeButton("Ȯ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							// ignore, just dismiss
							// �������� �ܼ����Ḹ �Ǹ� �ٸ� ������ �ְ�ʹٸ� ���⿡ �߰���Ű�� ��
						}
					}).show(); // ���̾�α� ����������
		} else {
			button1.setOnClickListener(this);
			button2.setOnClickListener(this);
			button3.setOnClickListener(this);
			button4.setOnClickListener(this);
			button5.setOnClickListener(this);
		}
		handler.close();

	}

	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.map:
			Log.e("SelectMenu.java", "������ư�� �������ϴ�");
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.listmap:
			Log.e("SelectMenu.java", "���弱���� �������ϴ�");

			handler = MySQLiteHandler.open(getApplicationContext()); // �����ͺ��̽� �ڵ鷯

			c = handler.select(1);
			c.moveToFirst();

			if(c.getCount()!=0){

				Builder dlg = new AlertDialog.Builder(SelectMenu.this);
				dlg.setTitle("����")
				.setMessage(c.getString(1).toString() + " ���� ������ �� �� ��ǰ�� �����ֽ��ϴ�.\n" +
						"�ٸ����忡�� ���� ���÷��� ��\n" +
						"�������忡�� �������÷��� �ƴϿ��� Ŭ���ϼ���")
						.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() {
							// ���̾�α��� �� ��ư�� ������ ����
							@Override
							public void onClick(DialogInterface dialog,	int whichButton) {
								handler.delete_table_1_4();

								Intent intent2 = new Intent(getApplicationContext(), Select_mart.class);
								intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent2);
							}
						})
						// ���̾�α��� �ƴϿ� ��ư
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								// ignore, just dismiss
								// �������� �ܼ����Ḹ �Ǹ� �ٸ� ������ �ְ�ʹٸ� ���⿡ �߰���Ű�� ��
								handler.delete_table_1_4();

								Intent intent3 = new Intent(getApplicationContext(), Main.class);
								intent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent3.putExtra("select_mart_name", c.getString(1).toString());
								Log.d("����Ʈ�޴����� ��Ʈ����Ʈ", c.getString(1).toString());
								startActivity(intent3);

							}
						}).show(); // ���̾�α� ����������

				handler.close();

			} else{
				Intent intent4 = new Intent(getApplicationContext(), Select_mart.class);
				intent4.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent4);
				break;
			}

			break;
		case R.id.info:
			//Intent intent5 = new Intent(this, develope_activity.class);
			Intent intent5 = new Intent(this, nfcexam.class);
			intent5.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent5);
			break;
		case R.id.help:
			Intent intent6 = new Intent(this, Bookmark.class);
			intent6.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent6);
			break;
		case R.id.delete:
			Log.e("SelectMenu.java", "������ư�� �������ϴ�");
			handler = MySQLiteHandler.open(getApplicationContext()); // �����ͺ��̽� �ڵ鷯
			handler.delete_table_1_4();
			//handler.deleteAll();
			handler.close(); // �����ͺ��̽� ����
			Toast.makeText(getApplicationContext(), "��ٱ��ϰ� �����Ǿ����ϴ�", Toast.LENGTH_SHORT).show();
//			// Ʋ�ڸ��� ���Ḧ ������ �������µ� �� ������ itemlist���� list.java�� �پ��ִµ� �̶��� ����Ʈ�� ���� �ȵ�����
//			if(list.itemlist.size()==0){
//				for(int i=0; i<list.itemlist_x.size(); i++){
//					list.itemlist_x.remove(i);
//				}
//				for(int i=0; i<list.itemlist_y.size(); i++){
//					list.itemlist_y.remove(i);
//				}
//				for(int i=0; i<list.itemlist.size(); i++) {
//					list.itemlist.remove(i);
//				}
//				for(int i=0; i<list.itemstate.size(); i++) {
//					list.itemstate.remove(i);
//				}
//				Log.d("close", "3");
//			}
//			Log.d("close", "4");
			//finish();
		}
	}

	// �� �Ʒ����ʹ� NFC���� �Լ�

	// ����׶��� ����ġ Ȱ��ȭ
	@Override
	protected void onResume() {
		super.onResume();
		if(mNfcAdapter!=null){
			Log.d(TAG, "onResume: " + getIntent());

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

	// ���� nfc�� �����ִٸ� ����ڿ��� nfc����� Ȱ��ȭ �ϴ� ����� ǥ���ϴ� �κ�
	private void checkNfcEnabled()
	{
		Boolean nfcEnabled = mNfcAdapter.isEnabled();
		if (!nfcEnabled) {
			new AlertDialog.Builder(SelectMenu.this)
			.setTitle(getString(R.string.warning_nfc_is_off))
			.setMessage(getString(R.string.NFC_NO_SETTING))
			.setCancelable(false)
			.setPositiveButton("Update Settings", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)	{
					startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				}
			}).create().show();
		} else {
			Toast.makeText(getApplicationContext(), "NFC���� �Ǿ����ϴ�", Toast.LENGTH_SHORT).show();
		}
	}
}
