/**
 * ���� 
 */

package com.example.n_mart;

import java.util.ArrayList;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.PendingIntent;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.Context;
import android.content.DialogInterface;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.database.Cursor;

import android.nfc.NfcAdapter;

import android.os.Bundle;

import android.util.Log;

import android.view.Window;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;

import com.exam.zzz_other_menu.Main;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay.OnStateChangeListener;


public class MainActivity extends NMapActivity implements OnMapStateChangeListener, OnStateChangeListener, LocationListener{

	private static final String TAG = "nfcinventory_simple";

	// nfc���� ���
	NfcAdapter mNfcAdapter; // ���� nfc�ϵ������� �ٸ� ������ ��� // nfc������ üũ�Ǿ� �ִ��� ���� ���� // nfc�±׿��� ndef�����͸� �о� ���ų� �ݴ�� ���� �� �� ��� ����
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	IntentFilter[] mWriteTagFilters;

	public static final String API_KEY = "df41e81814d5b62b088e616c54e1ef7b";	// API_KEY

	MySQLiteHandler handler;
	Cursor c;

	NMapView mMapView = null;	// ���̹� �� ��ü
	NMapController mMapController = null;	// �� ��Ʈ�ѷ�
	LinearLayout MapContainer;	// ���� �߰��� ���̾ƿ�

	NMapViewerResourceProvider mMapViewerResourceProvider = null;	// ���������� ���ҽ��� �����ϱ� ���� ��ü
	NMapOverlayManager mOverlayManager;	// �������� ������

	String select_mart_name;

	// ������ġ
	public LocationManager locManager;		
	public Location myLocation = null;
	public double latPoint, lngPoint;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // �׼ǹ� ��Ȱ��
		setContentView(R.layout.activity_main_map);

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



		MapContainer = (LinearLayout) findViewById(R.id.MapContainer);	// ���̹� ������ �ֱ� ���� LinearLayout ������Ʈ
		mMapView = new NMapView(this);	// ���̹� ���� ��ü ����
		mMapController = mMapView.getMapController();	// ���� ��ü�κ��� ��Ʈ�ѷ� ����
		mMapView.setApiKey(API_KEY);	// ���̹� ���� ��ü�� API_KEY ����
		MapContainer.addView(mMapView);		// ������ ���̹� ���� ��ü�� LinearLayout�� �߰���Ų��
		mMapView.setClickable(true);	// ������ ��ġ�� �� �ֵ��� �ɼ� Ȱ��ȭ
		mMapView.setBuiltInZoomControls(true, null);	// Ȯ��,��Ҹ� ���� �� ��Ʈ�ѷ� ǥ�� �ɼ� Ȱ��ȭ
		mMapView.setOnMapStateChangeListener(this);		// ������ ���� ���� ���� �̺�Ʈ ����


		//FUNCTION02(); �Ľ�
		loc01();
		FUNCTION03();

	}

	public void loc01(){		// ������ġ ã��.
		// LocationListener �ڵ�
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		// GPS�� ���� ��ġ ������ ������Ʈ ��û
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, this);  // 0�ʿ� �ٷ� �޾ƿ� 10��(1000)�ϸ� 10�ʵڿ� �޾ƿ�?
		// ���������κ��� ��ġ ������ ������Ʈ ��û
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, this);	// 100���� �Ÿ� �̵��ϸ� ������Ʈ

		//	getGeoLocation();
	}
	public void getGeoLocation() {

		if(myLocation != null) {
			latPoint = myLocation.getLatitude();
			lngPoint = myLocation.getLongitude();

			Log.d("didididididi", ""+latPoint+" "+lngPoint);
		}

		Log.d("����������ǥ",""+lngPoint+"  "+latPoint);
		NGeoPoint mGeoPoint = new NGeoPoint(lngPoint,latPoint);
		mMapController.setMapCenter(mGeoPoint, 11);

		FUNCTION01();
	}

	public void FUNCTION01(){	// ��Ŀ ����
		Log.e("tag", "FUNCTION01 ����");

		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);	// �������� ���ҽ� ������ü �Ҵ�
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);	// �������� ������ �߰�

		int markerId = NMapPOIflagType.PIN;		// �������̿� ǥ���ϱ� ���� ��Ŀ �̹����� id�� ����

		// ǥ���� ��ġ �����͸� �����Ѵ�. -- ������ ���ڰ� �������̸� �ν��ϱ� ���� id ��
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(100);

		for(int i=0; i<mart_address.size(); i++){
			String name = mart_name.get(i);
			double la = latitude.get(i);
			double lo = longitude.get(i);
			poiData.addPOIitem(lo, la, name, markerId, 0);
		}

		/*for(int i=0; i<row_count; i++){
			String name = mart_name_lot.get(i);
			double la = latitude_lot.get(i);
			double lo = longitude_lot.get(i);
			poiData.addPOIitem(lo, la, name, markerId, 0);
		}*/

		/*poiData.addPOIitem(127.0630205, 37.5091300, "��ġ1", markerId, 0);
		poiData.addPOIitem(127.061, 37.51, "��ġ2", markerId, 0);*/

		/*NMapPOIitem item = poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0); // ����ȭ��ǥ
			item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		NMapPOIitem item2 = poiData.addPOIitem(127.061, 37.51, "Pizza 777-111", markerId, 0); // ����ȭ��ǥ
			item2.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);*/

		Log.d("���1��Ŀ","������ǥ"+lngPoint+" "+latPoint);
		poiData.addPOIitem(lngPoint, latPoint, "����", markerId, 0);


		poiData.endPOIdata();


		// ��ġ �����͸� ����Ͽ� �������� ����
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		//poiDataOverlay.showAllPOIdata(0);	// id���� 0���� ������ ��� �������̰� ǥ�õǰ� �ִ� ��ġ�� ������ �߽ɰ� Zoom �缳��
		Log.e("tag", "FUNCTION01 ��");

		poiDataOverlay.setOnStateChangeListener(this);	// ������ ���� ���� ����, ��ǳ�� ���� �Ǵ� ��츦 ���� �̺�Ʈ ������ ���
	}

	/*
	public void FUNCTION02(){	// �Ľ� Ŭ���� ȣ�� �Լ�.
		//Log.e("tag","FUCNTION02 ����");
		XmlWork xmlWork = new XmlWork();
		xmlWork.execute("http://openapi.seoul.go.kr:8088/6b616e673530323632313333/xml/ListMarketInfoServer/1/30/%EB%A1%AF%EB%8D%B0%EB%A7%88%ED%8A%B8");
		//Log.e("tag","FUCNTION02 ��");

	}
	 */

	public ArrayList<Double> latitude = new ArrayList<Double>(); // ��ǥ ����
	public ArrayList<Double> longitude = new ArrayList<Double>(); // ��ǥ �浵
	public ArrayList<String> mart_address = new ArrayList<String>(); // �ּ�
	public ArrayList<String> mart_name = new ArrayList<String>(); // ��Ʈ�̸�
	public ArrayList<String> mart_mobile = new ArrayList<String>(); // ���ּ�

	public void FUNCTION03(){

		mart_mobile.add(0, "http://company.lottemart.com/branch/bc/main.do?brnchCd=0100012");
		mart_mobile.add(1, "http://company.lottemart.com/branch/bc/main.do?brnchCd=0100001");
		mart_mobile.add(2, "http://company.lottemart.com/branch/vm/main.do?brnchCd=0400006r");
		mart_mobile.add(3, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(4, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(5, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(6, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(7, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(8, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(9, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(10, "http://corporate.homeplus.co.kr/store/hypermarket.aspx");
		mart_mobile.add(11, "http://store.emart.com/branch/list.do");
		mart_mobile.add(12, "http://store.emart.com/branch/list.do");
		mart_mobile.add(13, "http://store.emart.com/branch/list.do");
		mart_mobile.add(14, "http://store.emart.com/branch/list.do");
		mart_mobile.add(15, "http://store.emart.com/branch/list.do");
		mart_mobile.add(16, "http://store.emart.com/branch/list.do");
		mart_mobile.add(17, "http://store.emart.com/branch/list.do");
		mart_mobile.add(18, "http://store.emart.com/branch/list.do");
		mart_mobile.add(19, "http://store.emart.com/branch/list.do");
		mart_mobile.add(20, "http://store.emart.com/branch/list.do");
		mart_mobile.add(21, "http://store.emart.com/branch/list.dor");
		mart_mobile.add(22, "http://store.emart.com/branch/list.do");
		mart_mobile.add(23, "http://store.emart.com/branch/list.do");
		mart_mobile.add(24, "http://store.emart.com/branch/list.do");

		mart_mobile.add(25, "https://www.nhhanaro.co.kr/utong/club/index.jsp");
		mart_mobile.add(26, "https://www.nhhanaro.co.kr/utong/club/index.jsp");
		mart_mobile.add(27, "https://www.nhhanaro.co.kr/utong/club/index.jsp");
		mart_mobile.add(28, "https://www.nhhanaro.co.kr/utong/club/index.jsp");
		mart_mobile.add(29, "http://www.shinsegae.com/store/main.jsp?storeCode=D02");
		mart_mobile.add(30, "http://www.shinsegae.com/store/main.jsp?storeCode=D01");
		mart_mobile.add(31, "http://www.ehyundai.com/newPortal/DP/DP000000_V.do?branchCd=B00141000");
		mart_mobile.add(32, "http://www.ehyundai.com/newPortal/DP/DP000000_V.do?branchCd=B00127000");
		mart_mobile.add(33, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=006");
		mart_mobile.add(34, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=005");
		mart_mobile.add(35, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=007");
		mart_mobile.add(36, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=024");
		mart_mobile.add(37, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=001");
		mart_mobile.add(38, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=003");
		mart_mobile.add(39, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=002");
		mart_mobile.add(40, "http://store.lotteshopping.com/handler/Main-Start?subBrchCd=004");


		mart_name.add(0, "�Ե���Ʈ ���￪��");
		mart_name.add(1, "�Ե���Ʈ ������");
		mart_name.add(2, "�Ե���Ʈ ��õ��");
		mart_name.add(3, "Ȩ�÷��� �����");
		mart_name.add(4, "Ȩ�÷��� ������");
		mart_name.add(5, "Ȩ�÷��� ������");
		mart_name.add(6, "Ȩ�÷��� ���빮��");
		mart_name.add(7, "Ȩ�÷��� �����");
		mart_name.add(8, "Ȩ�÷��� ��������");
		mart_name.add(9, "Ȩ�÷��� ������");
		mart_name.add(10, "Ȩ�÷��� ������");

		mart_name.add(11, "�̸�Ʈ ������");
		mart_name.add(12, "�̸�Ʈ ������");
		mart_name.add(13, "�̸�Ʈ ����");
		mart_name.add(14, "�̸�Ʈ �����");
		mart_name.add(15, "�̸�Ʈ û����");
		mart_name.add(16, "�̸�Ʈ ������");
		mart_name.add(17, "�̸�Ʈ �ŵ�����");
		mart_name.add(18, "�̸�Ʈ â����");
		mart_name.add(19, "�̸�Ʈ �ھ���");
		mart_name.add(20, "�̸�Ʈ �����");
		mart_name.add(21, "�̸�Ʈ �̾���");
		mart_name.add(22, "�̸�Ʈ ������");
		mart_name.add(23, "�̸�Ʈ ���ǵ���");
		mart_name.add(24, "�̸�Ʈ �սʸ���");

		mart_name.add(25, "�ϳ��θ�Ʈ ����");
		mart_name.add(26, "�ϳ��θ�Ʈ �̾���");
		mart_name.add(27, "�ϳ��θ�Ʈ ������");
		mart_name.add(28, "�ϳ��θ�Ʈ �����");

		mart_name.add(29, "�ż����ȭ�� ������");
		mart_name.add(30, "�ż����ȭ�� ����");

		mart_name.add(31, "�����ȭ�� �̾���");
		mart_name.add(32, "�����ȭ�� ������");

		mart_name.add(33, "�Ե���ȭ�� ������");
		mart_name.add(34, "�Ե���ȭ�� ������");
		mart_name.add(35, "�Ե���ȭ�� �����");
		mart_name.add(36, "�Ե���ȭ�� �̾���");
		mart_name.add(37, "�Ե���ȭ�� ����");
		mart_name.add(38, "�Ե���ȭ�� ��������");
		mart_name.add(39, "�Ե���ȭ�� �����");
		mart_name.add(40, "�Ե���ȭ�� û������");

		mart_address.add(0, "����Ư���� �߱� ������2�� 122-11");
		mart_address.add(1, "����Ư���� ������ ���ǵ� 546-4");
		mart_address.add(2, "����Ư���� ��õ�� ���굿 295-10");
		mart_address.add(3, "����Ư���� ���ı� ��õ�� 7-12");
		mart_address.add(4, "����Ư���� ������ õȣ�� 42");
		mart_address.add(5, "����Ư���� ������ ���е� 707-7");
		mart_address.add(6, "����Ư���� ���빮�� ��ε� 33-1");
		mart_address.add(7, "����Ư���� �߶��� ��� 168-2");
		mart_address.add(8, "����Ư���� �������� ������3�� 55-3");
		mart_address.add(9, "����Ư���� ��õ�� ���굿 1038-28");
		mart_address.add(10, "����Ư���� ��õ�� ���ﵿ 837-38");
		mart_address.add(11, "����Ư���� ������ ������2�� 333-16");
		mart_address.add(12, "����Ư���� ���� ���ϵ� 90-1");
		mart_address.add(13, "����Ư���� ��õ�� ����� 299");
		mart_address.add(14, "����Ư���� ��걸 �Ѱ���3�� 40-999");
		mart_address.add(15, "����Ư���� �߱� Ȳ�е� 2545");
		mart_address.add(16, "����Ư���� ������ ���ϵ� 46-4");
		mart_address.add(17, "����Ư���� ���α� ���ε� 3-25");
		mart_address.add(18, "����Ư���� ������ â�� 135-26");
		mart_address.add(19, "����Ư���� ������ �ھ絿 227-7");
		mart_address.add(20, "����Ư���� �߶��� ���쵿 506-1");
		mart_address.add(21, "����Ư���� ���ϱ� ������ 25-2");
		mart_address.add(22, "����Ư���� ������ ���絿 449-19");
		mart_address.add(23, "����Ư���� �������� ���ǵ��� 47");
		mart_address.add(24, "����Ư���� ������ ������2�� 333-16");

		mart_address.add(25, "����Ư���� �߶��� ���� 188-10");
		mart_address.add(26, "����Ư���� ���ϱ� �̾Ƶ� 318-5 ���������� 1��");
		mart_address.add(27, "����Ư���� ���ʱ� ���絿 230");
		mart_address.add(28, "����Ư���� ��걸 �Ѱ���2�� 15-19 ");
		mart_address.add(29, "����Ư���� ���ʱ� ������ 19-3");
		mart_address.add(30, "����Ư���� �߱� �湫��1�� 52-5");
		mart_address.add(31, "����Ư���� ���ϱ� ������ 20-1");
		mart_address.add(32, "����Ư���� ���빮�� âõ�� 30-33");
		mart_address.add(33, "����Ư���� ������ ��ġ�� 937");
		mart_address.add(34, "����Ư���� ���Ǳ� ��õ�� 729-22 ");
		mart_address.add(35, "����Ư���� ����� ���2�� 713");
		mart_address.add(36, "����Ư���� ���ϱ� �̾Ƶ� 70-6");
		mart_address.add(37, "����Ư���� �߱� �Ұ��� 1");
		mart_address.add(38, "����Ư���� �������� �������� 618-496");
		mart_address.add(39, "����Ư���� ���ı� ��ǵ� 40-1");
		mart_address.add(40, "����Ư���� ���빮�� ���� 591-53");

		longitude.add(0, 126.9695336); latitude.add(0, 37.555042);
		longitude.add(1, 127.0957088); latitude.add(1, 37.5349372);
		longitude.add(2, 126.8956692); latitude.add(2, 37.4704705);
		longitude.add(3, 127.1029989); latitude.add(3, 37.5162275);
		longitude.add(4, 127.1422439); latitude.add(4, 37.5456582);
		longitude.add(5, 127.0435994); latitude.add(5, 37.6648183);
		longitude.add(6, 127.0387578); latitude.add(6, 37.5745108);
		longitude.add(7, 127.0798833); latitude.add(7, 37.5808178);
		longitude.add(8, 126.8959156); latitude.add(8, 37.5181712);
		longitude.add(9, 126.9023778); latitude.add(9, 37.4657313);
		longitude.add(10, 126.9062253); latitude.add(10, 37.4523405);
		longitude.add(11, 127.0531674); latitude.add(11, 37.5399389);
		longitude.add(12, 126.9202568); latitude.add(12, 37.6003893);
		longitude.add(13, 126.8703272); latitude.add(13, 37.5257763);
		longitude.add(14, 126.9640447); latitude.add(14, 37.5288539);
		longitude.add(15, 127.0213284); latitude.add(15, 37.5708733);
		longitude.add(16, 127.1557517); latitude.add(16, 37.5546516);
		longitude.add(17, 126.8902185); latitude.add(17, 37.5070431);
		longitude.add(18, 127.046933); latitude.add(18, 37.6516821);
		longitude.add(19, 127.0726183); latitude.add(19, 37.5376127);
		longitude.add(20, 127.0935903); latitude.add(20, 37.596438);
		longitude.add(21, 127.0296051); latitude.add(21, 37.6108648);
		longitude.add(22, 126.8622978); latitude.add(22, 37.5578966);
		longitude.add(23, 126.9261297); latitude.add(23, 37.5181371);
		longitude.add(24, 127.0531674); latitude.add(24, 37.5399389);

		longitude.add(25, 127.0789505); latitude.add(25, 37.6057062);
		longitude.add(26, 127.0264632); latitude.add(26, 37.6215289);
		longitude.add(27, 127.0434134); latitude.add(27, 37.4629917);
		longitude.add(28, 126.9647209); latitude.add(28, 37.5331353);
		longitude.add(29, 127.0030692); latitude.add(29, 37.5041299);
		longitude.add(30, 126.9809889); latitude.add(30, 37.5609008);
		longitude.add(31, 127.0287626); latitude.add(31, 37.6083223);
		longitude.add(32, 126.9357977); latitude.add(32, 37.5560736);
		longitude.add(33, 127.0532731); latitude.add(33, 37.4969255);
		longitude.add(34, 126.9249911); latitude.add(34, 37.4905756);
		longitude.add(35, 127.0684857); latitude.add(35, 37.6587754);
		longitude.add(36, 127.0305098); latitude.add(36, 37.6145502);
		longitude.add(37, 126.981369); latitude.add(37, 37.5649903);
		longitude.add(38, 126.9066823); latitude.add(38, 37.5154709);
		longitude.add(39, 127.0980274); latitude.add(39, 37.5112348);
		longitude.add(40, 127.0479007); latitude.add(40, 37.5792928);

	}


	/**
	 * ���� �ִϸ��̼� ���� ���� �� ȣ��ȴ�.
	 * animType : ANIMATION_TYPE_PAN or ANIMATION_TYPE_ZOOM
	 * animState : ANIMATION_STATE_STARTED or ANIMATION_STATE_FINISHED
	 */
	@Override
	public void onAnimationStateChange(NMapView arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * ���� �߽� ���� �� ȣ��Ǹ� ����� �߽� ��ǥ�� �Ķ���ͷ� ���޵ȴ�.
	 */
	@Override
	public void onMapCenterChange(NMapView arg0, NGeoPoint arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapCenterChangeFine(NMapView arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * ������ �ʱ�ȭ�� �� ȣ��ȴ�.
	 * ���������� �ʱ�ȭ�Ǹ� errorInfo ��ü�� null�� ���޵Ǹ�, �ʱ�ȭ ���н� errorInfo��ü�� ���� ������ ���޵ȴ�.
	 * �� �̺�Ʈ �ڵ鷯���� �ΰ��� �ż��尡 ���� ��. ù °�� �̺�Ʈ�� �߻��� NMapView��ü �ڽ��̰� �ι�°�� ���� ������ ��� �ִ� ��ü.
	 */
	@Override
	public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
		// TODO Auto-generated method stub
		if(errorInfo == null){ // success
			mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);
		} else { // fail
			android.util.Log.e("NMAP", "onMapInitHandler: error=" + errorInfo.toString());
		}
	}

	/**
	 * ���� ���� ���� �� ȣ��Ǹ� ����� ���� ������ �Ķ���ͷ� ���޵ȴ�.
	 */
	@Override
	public void onZoomLevelChange(NMapView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public final String[] items = new String[] {"���弱��", "����"};
	int index_1 = 0;
	/**
	 * ��ǳ�� ��ġ�� �̺�Ʈ
	 */
	@Override
	public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, final NMapPOIitem item) {  //final
		// TODO Auto-generated method stub
		//Toast.makeText(this, "OnCalloutClick: " + item.getTitle(), Toast.LENGTH_SHORT).show();

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		//dialog.setIcon(R.drawable.ic_launcher);
		if(item.getTitle().equals("����"))
			return;
		
		dialog.setTitle(item.getTitle());
		dialog.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {//which==0 (���弱��) , which==1 ( ����)
				// TODO Auto-generated method stub
				//Toast.makeText(MainActivity.this, "items : "+items[which], Toast.LENGTH_SHORT).show();

				if(which == 0){ // ���弱�� ��ư 

					handler = MySQLiteHandler.open(getApplicationContext()); // �����ͺ��̽� �ڵ鷯

					for(int i=0; i < mart_name.size(); i++){
						if(item.getTitle().equals(mart_name.get(i))){
							Toast.makeText(MainActivity.this, item.getTitle() + " " + i, Toast.LENGTH_SHORT).show();
							index_1 = i;
							break;
						}else{
							//	Toast.makeText(MainActivity.this, "false!", Toast.LENGTH_SHORT).show();
						}
					}
					//Toast.makeText(MainActivity.this, "�ε���"+index_1, Toast.LENGTH_SHORT).show();


					c = handler.select(1);
					c.moveToFirst();

					if(c.getCount()!=0){

						Log.d("asdjlfjasklfjasdlfjsjfs", c.getString(1).toString());

						select_mart_name = c.getString(1).toString();

						Builder dlg = new AlertDialog.Builder(MainActivity.this);
						dlg.setTitle("����")
						.setMessage(c.getString(1).toString() + " ���� ������ �� �� ��ǰ�� �����ֽ��ϴ�.\n" +
								"�̹��� ������ ���忡�� ���� ���÷��� ��\n" +
								"������ ������ ���忡�� �������÷��� �ƴϿ��� Ŭ���ϼ���")
								.setPositiveButton(R.string.ok,	new DialogInterface.OnClickListener() {
									// ���̾�α��� �� ��ư�� ������ ����
									@Override
									public void onClick(DialogInterface dialog,	int whichButton) {
										handler.delete_table_1_4();

										Intent intent2 = new Intent(getApplicationContext(), Main.class);
										intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
										intent2.putExtra("select_mart_name", select_mart_name);
										startActivity(intent2);
										finish();
									}
								})
								// ���̾�α��� �ƴϿ� ��ư
								.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int whichButton) {
										// ignore, just dismiss
										// �������� �ܼ����Ḹ �Ǹ� �ٸ� ������ �ְ�ʹٸ� ���⿡ �߰���Ű�� ��

										Intent intent3 = new Intent(getApplicationContext(), Main.class);
										intent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
										intent3.putExtra("select_mart_name", select_mart_name);
										Log.d("����Ʈ�޴����� ��Ʈ����Ʈ", select_mart_name);
										startActivity(intent3);
										finish();

									}
								}).show(); // ���̾�α� ����������

					} else{
						Intent intent4 = new Intent(getApplicationContext(), Main.class);
						intent4.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent4.putExtra("select_mart_name", mart_name.get(index_1));
						startActivity(intent4);
						finish();
					}

					handler.close();
				}else{ // ���� ��ư
					//Toast.makeText(MainActivity.this, "items : " + items[1], Toast.LENGTH_SHORT).show();

					for(int i=0; i < mart_name.size(); i++){
						if(item.getTitle().equals(mart_name.get(i))){
							Toast.makeText(MainActivity.this, item.getTitle() + " " + i, Toast.LENGTH_SHORT).show();
							index_1 = i;
							break;
						}else{
							//	Toast.makeText(MainActivity.this, "false!", Toast.LENGTH_SHORT).show();
						}
					}
					//Toast.makeText(MainActivity.this, "�ε���"+index_1, Toast.LENGTH_SHORT).show();

					Intent intent_1 = new Intent(MainActivity.this, Map_info.class);
					intent_1.putExtra("mobile_address", mart_mobile.get(index_1));
					intent_1.putExtra("mobile_name", mart_name.get(index_1));
					startActivity(intent_1);
					finish();
				}
			}
		});
		dialog.setNeutralButton("�ݱ�", null);
		dialog.show();
	}

	/**
	 * �������� ���� -> �ٸ� �������� ���� ���� �� �̺�Ʈ. ���� ��ǥ�� �α� ȣ��
	 */
	@Override
	public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
		// TODO Auto-generated method stub
		if (item != null){
			Log.i("LOG_TAG", "onFocusChanged: " + item.toString());
		} else {
			Log.i("LOG_TAG", "onFocusChanged: " );
		}
	}

	public ArrayList<Double> latitude_lot = new ArrayList<Double>(); // ��ǥ ����
	public ArrayList<Double> longitude_lot = new ArrayList<Double>(); // ��ǥ �浵
	public ArrayList<String> mart_address_lot = new ArrayList<String>(); // �ּ�
	public ArrayList<String> mart_name_lot = new ArrayList<String>(); // ��Ʈ�̸�
	public int row_count;


	public void CHECK01(){
		for(int i=0; i<row_count; i++){
			Log.e("CHECK01 ��Ʈ�̸�", i +" "+ mart_name_lot.get(i));
			Log.e("CHECK01 �ּ�", i +" "+ mart_address_lot.get(i));
			Log.e("CHECK01 ����/�浵", i +" "+ latitude_lot.get(i)+"/"+longitude_lot.get(i));
		}
	}
	public void CHECK02(){
		for(int i=0; i<row_count; i++){
			Log.e("CHECK02", i +" "+ mart_address_lot.get(i));
		}
	}
	/**
	 *	�ּҸ� �޾ƿͼ� �����ڵ��� �ϱ� ���� Ŭ����
	 *
	 *
	 * �Ľ�
	 */
	/*	public class XmlWork extends AsyncTask<String, Void, Document> {

		Document doc = null;


		GetAddress getAddress = new GetAddress();

		@Override
		public Document doInBackground(String... urls) {
			URL url;
			try {
				url = new URL(urls[0]);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder(); // XML���� ���� ��ü�� ����
				doc = db.parse(new InputSource(url.openStream())); // XML������ �Ľ��Ѵ�.
				doc.getDocumentElement().normalize();
			} catch (Exception e) {
				Log.e("XmlWork", "Parsing Error");
				// Toast.makeText(getBaseContext(), "Parsing Error",
				// Toast.LENGTH_SHORT).show();
			}
			return doc;
		}

		@Override
		public void onPostExecute(Document doc) {
			Log.e("tag","xmlwork ����");

			String s = "";
			String ss = "";

			NodeList nodeList = doc.getElementsByTagName("row"); 

			for(int i = 0; i< nodeList.getLength(); i++){

				s += "["+i+"]" ;
				Node node = nodeList.item(i); //data������Ʈ ���    ù��°<row>
				Element fstElmnt = (Element) node;


				NodeList nameList  = fstElmnt.getElementsByTagName("M_MART_NAME"); //
				Element nameElement = (Element) nameList.item(0);
				nameList = nameElement.getChildNodes();
				s += "��Ʈ�̸�=" + ((Node) nameList.item(0)).getNodeValue() +" \n";



				NodeList m_name = fstElmnt.getElementsByTagName("M_MART_NAME");
				ss = m_name.item(0).getChildNodes().item(0).getNodeValue();
				mart_name_lot.add(i, ss);	// ��Ʈ �̸��� �����Ѵ�.



				NodeList m_address = fstElmnt.getElementsByTagName("M_MART_ADDR");
				ss = m_address.item(0).getChildNodes().item(0).getNodeValue();
				mart_address_lot.add(i, ss);	// ��Ʈ �ּҸ� �����Ѵ�.

				//	Log.e("�����ڵ� �� ss", ss);
				latitude_lot.add(i, getAddress.getAddress_to_la(ss)); 	// �ּҸ� ������ ��ȯ�Ͽ� ����.
				longitude_lot.add(i, getAddress.getAddress_to_lo(ss));  // �ּҸ� �浵�� ��ȯ�Ͽ� ����. 
				//	Log.e("�����ڵ��� ��ǥ", latitude_lot.get(i).toString());

				row_count = nodeList.getLength();
			}

			loc01();
			//FUNCTION01();	// ��Ŀǥ�� �κ�
			CHECK01();
			super.onPostExecute(doc);
		}
	}// end inner class - GetXMLTask


	 */

	// ������ġ
	@Override
	public void onLocationChanged(Location location) {	// �Լ� ȣ��
		// TODO Auto-generated method stub
		myLocation = location;
		getGeoLocation();
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

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
