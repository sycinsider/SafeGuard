
package com.example.mobileguard.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;

import org.apache.http.impl.conn.LoggingSessionInputBuffer;
import org.apache.http.message.BufferedHeader;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;
import com.example.mobileguard.utils.StreamUtils;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;

/**
 * ClassName:GPSService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 上午11:04:52 <br/>
 * 
 * @author dell
 * @version
 */
public class GPSService extends Service implements LocationListener {

    private LocationManager lm;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        new Thread(new Gpsrunnable(longitude, latitude)).start();
    }

    class Gpsrunnable implements Runnable {
        private double longitude;
        private double latitude;
        private String location;

        public Gpsrunnable(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://lbs.juhe.cn/api/getaddressbylngb?lngx=" + longitude
                        + "&lngy=" + latitude);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String result = "";
                    String temp = "";
                    while ((temp = reader.readLine()) != null) {
                        result += temp;
                    }
                    JSONObject json = new JSONObject(result);
                    JSONObject rowJson = json.getJSONObject("row");
                    JSONObject resJson = rowJson.getJSONObject("result");
                    location = resJson.getString("formatted_address");
                } else {
                    location = "获取失败";
                }
            } catch (Exception e) {
                location = "获取失败";
                e.printStackTrace();
            } finally {
                StreamUtils.close(reader);
                StreamUtils.close(inputStream);

            }
            String safenumber = SpUtils.getString(GPSService.this, Constantset.SAFE_NUMBER);
            if (TextUtils.isEmpty(safenumber)) {
                stopSelf();
                return;
            } else {
                SmsManager.getDefault().sendTextMessage(safenumber, null,
                        "经度是:" + longitude + "维度是" + latitude + "地址是:" + location, null, null);
                System.out.println(location);
                stopSelf();
            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        lm.removeUpdates(this);
    }
}
