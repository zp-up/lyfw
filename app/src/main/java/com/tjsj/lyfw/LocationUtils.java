package com.tjsj.lyfw;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * 位置信息的Utils
 *
 * @author zsl
 *
 */
public class LocationUtils {

    // 纬度
    public static double latitude = 0.0;
    // 经度
    public static double longitude = 0.0;

    /**
     * 初始化位置信息
     *
     * @param context
     */
    public static void initLocation(Context context) {
        try{




        LocationManager locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
              //  Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                List<String> providers = locationManager.getAllProviders();
                Location bestLocation = null;
                for (String provider : providers) {

                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }

                if (bestLocation != null) {
                    latitude = bestLocation.getLatitude();
                    longitude = bestLocation.getLongitude();
                }
            }

        } else {
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {

                    }
                }
            };
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                Location location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude(); // 经度
                    longitude = location.getLongitude(); // 纬度
                }
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

