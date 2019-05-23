package com.example.n_mart;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

//import com.example.xml01.R;

public class GetAddress extends Activity {
	
	double latitude, longitude;
	
	Document doc = null;
		
	public Double getAddress_to_la(String ssss) { // �����ڵ� ����(latitude)
		try {
			//Log.i("GeoCoding!!", "getAddress_to_la()");
			//ssss = "����Ư���� ��õ�� �� 919-7";
			Geocoder geo = new Geocoder(this, Locale.KOREAN);
			List<Address> addr = geo.getFromLocationName(ssss, 10);
		
			if (addr.size() > 0) {
				double country = addr.get(0).getLatitude();
				latitude = country;

			} else {}
		} catch (IOException e) {}
		return latitude;
	}
	
	public Double getAddress_to_lo(String ssss) { // �����ڵ� �浵(longitude)
		try {
			//Log.i("GeoCoding!!", "getAddress_to_lo()");
			//ssss = "����Ư���� ��õ�� �� 919-7";
			Geocoder geo = new Geocoder(this, Locale.KOREAN);
			List<Address> addr = geo.getFromLocationName(ssss, 10);
		
			if (addr.size() > 0) {
				double area = addr.get(0).getLongitude();
				longitude = area;
				
			} else {}
		} catch (IOException e) {}
		return longitude;
	}	
}
