package com.antelope.goodbrother.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class MyLocationManager {

    private static volatile MyLocationManager myLocationManager;


    public static MyLocationManager getInstance() {

        if (myLocationManager == null) {
            synchronized (MyLocationManager.class) {
                if (myLocationManager == null) {
                    myLocationManager = new MyLocationManager();
                }
            }
        }
        return myLocationManager;
    }

    /**
     * @param activity    获取位置的activity
     * @param requestCode 请求码
     * @return 位置
     */
    public Location getLocation(Activity activity, int requestCode) throws RuntimeException {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//        String provider = null;
//        // 获取所有可用的位置提供器
//        if (locationManager==null){
//            throw new RuntimeException("the locationManager no available ");
//        }
//        List<String> providerList = locationManager.getProviders(true);
//        if (ListUtils.isEmpty(providerList)){
//            throw new RuntimeException("no location provider");
//        }
//        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
//            provider = LocationManager.GPS_PROVIDER;
//        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
//            provider = LocationManager.NETWORK_PROVIDER;
//        }else{
//           throw new RuntimeException("no location provider");
//        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        //从可用的位置提供器中，匹配以上标准的最佳提供器
        String locationProvider = locationManager.getBestProvider(criteria, true);
        Location location = null;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(locationProvider);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
            }
        } else {
            location = locationManager.getLastKnownLocation(locationProvider);
        }
        if (location != null) {
            Log.d("location", location.getLatitude() + "," + location.getLongitude());
        }
        return location;
    }
}
