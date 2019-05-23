package com.exam.zzz_other_menu;

import java.net.URL;

import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.AsyncTask;

import android.util.Log;

import android.view.Window;

import android.widget.Toast;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;
import com.example.n_mart.R;

public class Parsing_data extends Activity {

	public ArrayList<String> mart_name; // ��Ʈ�̸� �ѱ�
	public ArrayList<String> mart_name_url; // ��Ʈ�̸� URL
	static public ArrayList<Double> latitude; // ��Ʈ ����
	static public ArrayList<Double> longitude; // ��Ʈ �浵

	public ArrayList<Info_save_class> item_list; // �̸��� ������ �����ϴ� ArrayList
	//Info_save_class item; // ���������Ŭ����

	int l=0; // �ҽ��ڵ忡�� ���
	int m=0; // �۵��ϴ��� ���ϴ��� Ȯ�ο�

	Document doc = null;
	//LinearLayout layout = null;

	MySQLiteHandler handler; // �����ͺ��̽� ����ϱ� ���� handler
	Cursor c; // �����ͺ��̽� ������� ���̺��� ã���� Ŀ��

	// ��¥�� �����ؼ� �Ľ����� ���� ���ϴ� ��
	int year;
	int month;
	int date;
	int hour;
	int minute;
	int second;

	// ���ͳ� ������ �Ǿ��ִ��� Ȯ���ϱ� ���� ����
	ConnectivityManager cManager;
	NetworkInfo mobile;
	NetworkInfo wifi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Ÿ��Ʋ�� ����
		setContentView(R.layout.parsing_data);

		Log.v("Parsing_data.java","onCreate!!"); 
		
		cManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3g �Ǵ� LTE ���� �Ǿ��ִ��� Ȯ���ϴ� ����
		wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // WIFI ���� �Ǿ��ִ��� Ȯ���ϴ� ����
		
		mart_name = new ArrayList<String>(); // ��Ʈ�̸� �ѱ�
		mart_name_url = new ArrayList<String>(); // ��Ʈ�̸� URL
		latitude = new ArrayList<Double>(); // ��Ʈ ����
		longitude = new ArrayList<Double>(); // ��Ʈ �浵

		// �Ľ��� ���� �������� �ε�ȭ�� �����ֱ� ���� �ڵ鷯
		Handler hd = new Handler();
		hd.postDelayed(new Runnable() {

			@Override
			public void run() {

				add_mart_name(); // �ѱ� ��Ʈ �̸� ADD
				add_mart_name_url(); // URL ��Ʈ �̸� ADD
				add_mart_lat(); // ��Ʈ ����
				add_mart_lon(); // ��Ʈ �浵

				item_list = new ArrayList<Info_save_class>(); // item_list �ʱ�ȭ
				//item = new Info_save_class(null, null, 0, 0, 0); // item ��ü ���� �� �ʱ�ȭ

				handler = MySQLiteHandler.open(getApplicationContext());

				Calendar cal = Calendar.getInstance();

				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				date = cal.get(Calendar.DATE);
				hour = cal.get(Calendar.HOUR_OF_DAY);
				minute = cal.get(Calendar.MINUTE);
				second = cal.get(Calendar.SECOND);

				c = handler.select(9);
				c.moveToFirst();

				if((mobile.isConnected() || wifi.isConnected()) && (c.getCount()==0 || (c.getInt(1)!=year || c.getInt(2)!=month || c.getInt(3)!=date/* || c.getInt(4)!=hour || c.getInt(5)!=minute*/))){
					Log.v("Parsing_data.java", "XML�Ľ� �ϴ� �κ��Դϴ�");
					for(int i=0; i<40; i++){
						FUNCTION();
						m++;
					}
				}
				else {
					Log.v("Parsing_data.java", "XML�Ľ� �ǳ� �پ����....?");
					Intent intent = new Intent(getApplicationContext(), SelectMenu.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			}
		}, 3000);
	}
	
	public void add_mart_lat(){
		Log.v("Parsing_data.java", "��Ʈ�� ������ �����ϴ� �Լ��Դϴ�");
		
		latitude.add(0, 37.5349372); // �Ե���Ʈ ������
		latitude.add(1, 37.4704705); // �Ե���Ʈ ��õ��
		latitude.add(2, 37.555042);  // �Ե���Ʈ ���￪��
		
		latitude.add(3, 37.5578966); // �̸�Ʈ ������
		latitude.add(4, 37.5546516); // �̸�Ʈ ������
		latitude.add(5, 37.5257763); // �̸�Ʈ ����
		latitude.add(6, 37.6108648); // �̸�Ʈ �̾���
		latitude.add(7, 37.596438); // �̸�Ʈ �����
		latitude.add(8, 37.5399389); // �̸�Ʈ ������
		latitude.add(9, 37.5070431); // �̸�Ʈ �ŵ�����
		latitude.add(10, 37.5181371); // �̸�Ʈ ���ǵ���
		latitude.add(11, 37.5399389); // �̸�Ʈ �սʸ���
		latitude.add(12, 37.5288539); // �̸�Ʈ �����
		latitude.add(13, 37.6003893); // �̸�Ʈ ������
		latitude.add(14, 37.5376127); // �̸�Ʈ �ھ���
		latitude.add(15, 37.6516821); // �̸�Ʈ â����
		latitude.add(16, 37.5708733); // �̸�Ʈ û����
		
		latitude.add(17, 37.5456582); // Ȩ�÷��� ������
		latitude.add(18, 37.5745108); // Ȩ�÷��� ���빮��
		latitude.add(19, 37.5808178); // Ȩ�÷��� �����
		latitude.add(20, 37.6648183); // Ȩ�÷��� ������
		latitude.add(21, 37.4523405); // Ȩ�÷��� ������
		latitude.add(22, 37.5181712); // Ȩ�÷��� ��������
		latitude.add(23, 37.5162275); // Ȩ�÷��� �����
		
		latitude.add(24, 37.6057062); // �ϳ��θ�Ʈ ����
		latitude.add(25, 37.6215289); // �ϳ��θ�Ʈ �̾���
		latitude.add(26, 37.4629917); // �ϳ��θ�Ʈ ������
		latitude.add(27, 37.5331353); // �ϳ��θ�Ʈ �����

		latitude.add(28, 37.5041299); // �ż����ȭ�� ������
		latitude.add(29, 37.5609008); // �ż����ȭ�� ����

		latitude.add(30, 37.6083223); // �����ȭ�� �̾���
		latitude.add(31, 37.5560736); // �����ȭ�� ������

		latitude.add(32, 37.4969255); // �Ե���ȭ�� ������
		latitude.add(33, 37.4905756); // �Ե���ȭ�� ������
		latitude.add(34, 37.6587754); // �Ե���ȭ�� �����
		latitude.add(35, 37.6145502); // �Ե���ȭ�� �̾���
		latitude.add(36, 37.5649903); // �Ե���ȭ�� ����
		latitude.add(37, 37.5154709); // �Ե���ȭ�� ��������
		latitude.add(38, 37.5112348); // �Ե���ȭ�� �����
		latitude.add(39, 37.5792928); // �Ե���ȭ�� û������
	}
	
	public void add_mart_lon(){
		Log.v("Parsing_data.java", "��Ʈ�� �浵�� �����ϴ� �Լ��Դϴ�");
		
		longitude.add(0, 127.0957088); // �Ե���Ʈ ������
		longitude.add(1, 126.8956692); // �Ե���Ʈ ��õ��
		longitude.add(2, 126.9695336);  // �Ե���Ʈ ���￪��
		
		longitude.add(3, 126.8622978); // �̸�Ʈ ������
		longitude.add(4, 127.1557517); // �̸�Ʈ ������
		longitude.add(5, 126.8703272); // �̸�Ʈ ����
		longitude.add(6, 127.0296051); // �̸�Ʈ �̾���
		longitude.add(7, 127.0935903); // �̸�Ʈ �����
		longitude.add(8, 127.0531674); // �̸�Ʈ ������
		longitude.add(9, 126.8902185); // �̸�Ʈ �ŵ�����
		longitude.add(10, 126.9261297); // �̸�Ʈ ���ǵ���
		longitude.add(11, 127.0531674); // �̸�Ʈ �սʸ���
		longitude.add(12, 126.9640447); // �̸�Ʈ �����
		longitude.add(13, 126.9202568); // �̸�Ʈ ������
		longitude.add(14, 127.0726183); // �̸�Ʈ �ھ���
		longitude.add(15, 127.046933); // �̸�Ʈ â����
		longitude.add(16, 127.0213284); // �̸�Ʈ û����
		
		longitude.add(17, 127.1422439); // Ȩ�÷��� ������
		longitude.add(18, 127.0387578); // Ȩ�÷��� ���빮��
		longitude.add(19, 127.0798833); // Ȩ�÷��� �����
		longitude.add(20, 127.0435994); // Ȩ�÷��� ������
		longitude.add(21, 126.9062253); // Ȩ�÷��� ������
		longitude.add(22, 126.8959156); // Ȩ�÷��� ��������
		longitude.add(23, 127.1029989); // Ȩ�÷��� �����
		
		longitude.add(24, 127.0789505); // �ϳ��θ�Ʈ ����
		longitude.add(25, 127.0264632); // �ϳ��θ�Ʈ �̾���
		longitude.add(26, 127.0434134); // �ϳ��θ�Ʈ ������
		longitude.add(27, 126.9647209); // �ϳ��θ�Ʈ �����

		longitude.add(28, 127.0030692); // �ż����ȭ�� ������
		longitude.add(29, 126.9809889); // �ż����ȭ�� ����

		longitude.add(30, 127.0287626); // �����ȭ�� �̾���
		longitude.add(31, 126.9357977); // �����ȭ�� ������

		longitude.add(32, 127.0532731); // �Ե���ȭ�� ������
		longitude.add(33, 126.9249911); // �Ե���ȭ�� ������
		longitude.add(34, 127.0684857); // �Ե���ȭ�� �����
		longitude.add(35, 127.0305098); // �Ե���ȭ�� �̾���
		longitude.add(36, 126.981369); // �Ե���ȭ�� ����
		longitude.add(37, 126.9066823); // �Ե���ȭ�� ��������
		longitude.add(38, 127.0980274); // �Ե���ȭ�� �����
		longitude.add(39, 127.0479007); // �Ե���ȭ�� û������
	}

	public void add_mart_name(){
		Log.v("Parsing_data.java", "��Ʈ�̸��� �ѱ۷� �����ϴ� �Լ��Դϴ�");
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
	}

	public void add_mart_name_url(){
		Log.v("Parsing_data.java", "��Ʈ�̸��� URL�� �����ϴ� �κ��Դϴ�");
		mart_name_url.add(0, "%EB%A1%AF%EB%8D%B0%EB%A7%88%ED%8A%B8%EA%B0%95%EB%B3%80%EC%A0%90");
		mart_name_url.add(1, "%EB%A1%AF%EB%8D%B0%EB%A7%88%ED%8A%B8%EA%B8%88%EC%B2%9C%EC%A0%90");
		mart_name_url.add(2, "%EB%A1%AF%EB%8D%B0%EB%A7%88%ED%8A%B8%EC%84%9C%EC%9A%B8%EC%97%AD%EC%A0%90");

		mart_name_url.add(3, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EA%B0%80%EC%96%91%EC%A0%90");
		mart_name_url.add(4, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EB%AA%85%EC%9D%BC%EC%A0%90");
		mart_name_url.add(5, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EB%AA%A9%EB%8F%99%EC%A0%90");
		mart_name_url.add(6, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EB%AF%B8%EC%95%84%EC%A0%90");
		mart_name_url.add(7, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%83%81%EB%B4%89%EC%A0%90");
		mart_name_url.add(8, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%84%B1%EC%88%98%EC%A0%90");
		mart_name_url.add(9, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%8B%A0%EB%8F%84%EB%A6%BC%EC%A0%90");
		mart_name_url.add(10, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%97%AC%EC%9D%98%EB%8F%84%EC%A0%90");
		mart_name_url.add(11, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%99%95%EC%8B%AD%EB%A6%AC%EC%A0%90");
		mart_name_url.add(12, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EB%AA%A9%EB%8F%99%EC%A0%90");      
		mart_name_url.add(13, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%9D%80%ED%8F%89%EC%A0%90");
		mart_name_url.add(14, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%9E%90%EC%96%91%EC%A0%90");
		mart_name_url.add(15, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%B0%BD%EB%8F%99%EC%A0%90");
		mart_name_url.add(16, "%EC%9D%B4%EB%A7%88%ED%8A%B8%EC%B2%AD%EA%B3%84%EC%A0%90");

		mart_name_url.add(17, "%ED%99%88%ED%94%8C%EB%9F%AC%EC%8A%A4%EA%B0%95%EB%8F%99%EC%A0%90");
		mart_name_url.add(18, "%ED%99%88%ED%94%8C%EB%9F%AC%EC%8A%A4%EB%8F%99%EB%8C%80%EB%AC%B8%EC%A0%90");
		mart_name_url.add(19, "%ED%99%88%ED%94%8C%EB%9F%AC%EC%8A%A4%EB%A9%B4%EB%AA%A9%EC%A0%90");
		mart_name_url.add(20, "%ED%99%88%ED%94%8C%EB%9F%AC%EC%8A%A4%EB%B0%A9%ED%95%99%EC%A0%90");
		mart_name_url.add(21, "%ED%99%88%ED%94%8C%EB%9F%AC%EC%8A%A4%EC%8B%9C%ED%9D%A5%EC%A0%90");
		mart_name_url.add(22, "%ED%99%88%ED%94%8C%EB%9F%AC%EC%8A%A4%EC%98%81%EB%93%B1%ED%8F%AC%EC%A0%90");
		mart_name_url.add(23, "%ED%99%88%ED%94%8C%EB%9F%AC%EC%8A%A4%EC%9E%A0%EC%8B%A4%EC%A0%90");   

		mart_name_url.add(24, "%ED%96%89%EB%B3%B5%ED%95%9C%EC%84%B8%EC%83%81%EB%AA%A9%EB%8F%99%EC%A0%90%28%ED%95%98%EB%82%98%EB%A1%9C%EB%A7%88%ED%8A%B8%29");
		mart_name_url.add(25, "%ED%95%98%EB%82%98%EB%A1%9C%ED%81%B4%EB%9F%BD%EB%AF%B8%EC%95%84%EC%A0%90");
		mart_name_url.add(26, "%ED%95%98%EB%82%98%EB%A1%9C%ED%81%B4%EB%9F%BD%EC%96%91%EC%9E%AC%EC%A0%90");
		mart_name_url.add(27, "%EB%86%8D%ED%98%91%ED%95%98%EB%82%98%EB%A1%9C%EB%A7%88%ED%8A%B8%EC%9A%A9%EC%82%B0%EC%A0%90");

		mart_name_url.add(28, "%EC%8B%A0%EC%84%B8%EA%B3%84%EB%B0%B1%ED%99%94%EC%A0%90%EA%B0%95%EB%82%A8%EC%A0%90");
		mart_name_url.add(29, "%EC%8B%A0%EC%84%B8%EA%B3%84%EB%B0%B1%ED%99%94%EC%A0%90");

		mart_name_url.add(30, "%ED%98%84%EB%8C%80%EB%B0%B1%ED%99%94%EC%A0%90%EB%AF%B8%EC%95%84%EC%A0%90");
		mart_name_url.add(31, "%ED%98%84%EB%8C%80%EB%B0%B1%ED%99%94%EC%A0%90%EC%8B%A0%EC%B4%8C%EC%A0%90");
		
		mart_name_url.add(32, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90%EA%B0%95%EB%82%A8%EC%A0%90");
		mart_name_url.add(33, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90%EA%B4%80%EC%95%85%EC%A0%90");
		mart_name_url.add(34, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90%EB%85%B8%EC%9B%90%EC%A0%90");
		mart_name_url.add(35, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90%EB%AF%B8%EC%95%84%EC%A0%90");
		mart_name_url.add(36, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90");
		mart_name_url.add(37, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90%EC%98%81%EB%93%B1%ED%8F%AC%EC%A0%90");
		mart_name_url.add(38, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90%EC%9E%A0%EC%8B%A4%EC%A0%90");
		mart_name_url.add(39, "%EB%A1%AF%EB%8D%B0%EB%B0%B1%ED%99%94%EC%A0%90%EC%B2%AD%EB%9F%89%EB%A6%AC%EC%A0%90");
}

	public void FUNCTION(){
		Log.v("Parsing_data.java", "XML�Ľ��ϵ��� ��û�ϴ� �κ��Դϴ�");

		GetXMLTask task = new GetXMLTask();
		task.execute("http://openapi.seoul.go.kr:8088/6b616e673530323632313333/xml/ListNecessariesPricesService/1/16/" +
				mart_name_url.get(m));
	}

	//private inner class extending AsyncTask
	private class GetXMLTask extends AsyncTask<String, Void, Document>{   

		@Override
		protected Document doInBackground(String... urls) {
			Log.v("Parsing_data.java", "�̰����� �Ľ��� �Ͼ�ϴ�");
			URL url;
			try {
				url = new URL(urls[0]);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder(); //XML���� ���� ��ü�� ����
				doc = db.parse(new InputSource(url.openStream())); //XML������ �Ľ��Ѵ�.
				doc.getDocumentElement().normalize();

			} catch (Exception e) {
				Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
			}
			return doc;
		}

		@Override
		protected void onPostExecute(Document doc) {

			Log.v("Parsing_data.java", "���⼭ �Ľ� ����� ������ �����ϰ� ���� ��Ƽ��Ƽ�� �Ѱ��ִ� �κ��Դϴ�");

			String s = "";
			String ss = "";
			String sss = "";

			NodeList nodeList = doc.getElementsByTagName("row"); 

			//int count = 0;

			for(int i = 0; i< nodeList.getLength(); i++){

				s += "["+i+"]" ;
				Node node = nodeList.item(i); //data������Ʈ ���    ù��°<row>
				Element fstElmnt = (Element) node;

				NodeList nameList  = fstElmnt.getElementsByTagName("M_NAME"); //
				Element nameElement = (Element) nameList.item(0);
				nameList = nameElement.getChildNodes();
				s += "��Ʈ�̸�=" + ((Node) nameList.item(0)).getNodeValue() +" \n";

				NodeList i_name = fstElmnt.getElementsByTagName("A_NAME");
				ss = i_name.item(0).getChildNodes().item(0).getNodeValue();

				NodeList i_price = fstElmnt.getElementsByTagName("A_PRICE");
				sss = i_price.item(0).getChildNodes().item(0).getNodeValue();

			//	Log.e("�Ľ̼��ڰ˻���", "l="+Integer.toString(l));
			//	Log.e("��Ʈ�̸��˻�", mart_name.get(l));
				item_list.add(new Info_save_class(mart_name.get(l), ss, Integer.parseInt(sss), latitude.get(l), longitude.get(l)));
			//	Log.e("�Ľ̵����Ͱ˻�", ss);
			//	Log.e("�Ľ̼��ڰ˻�", Integer.toString(count));
				//count++;
			}

			l++;
		//	Log.e("�Ľ̼��ڰ˻���", "l="+Integer.toString(l));

			Log.e("asdfjkladsfjldksafjkldsajfdlka", toString().valueOf(item_list.size()) + " " + toString().valueOf(mart_name.size()*nodeList.getLength()));
			if(item_list.size()==((mart_name.size())*nodeList.getLength())) {

				Log.v("Pasing_data.java", "���� ��Ƽ��Ƽ�� �Ѿ�ϴ�");
				Log.v("����Ʈ���� ����m", Integer.toString(m));
				Log.v("����Ʈ���� ����node", Integer.toString(nodeList.getLength()));
				Intent intent = new Intent(getApplicationContext(), info.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("item_list", item_list);
				intent.putExtra("length", item_list.size());
				intent.putExtra("item_length", nodeList.getLength());
				startActivity(intent);
				finish();
			}
			super.onPostExecute(doc);
		}
	}//end inner class - GetXMLTask
}