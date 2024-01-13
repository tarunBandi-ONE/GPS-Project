package com.example.gpsprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    LocationManager my_location;
    LocationListener locationListener_my;
    TextView location_text, distance2, time,locat;
    List<Address> addresses;
    ArrayList<Location> locationArrayList;
    MapView mapView;
    GoogleMap googleMap;
    Geocoder converter;
    double distance = 0;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView2);
        location_text = findViewById(R.id.location);
        distance2 = findViewById(R.id.distance);
        locat = findViewById(R.id.locat);
        converter =  new Geocoder(MainActivity.this, Locale.getDefault());
        locationArrayList = new ArrayList<>();
        time = findViewById(R.id.time);
       // location_text.setText(addresses.get(addresses.size()-1));
         locationListener_my = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("my_location",location.toString());

                try
                {
                    Double lattitude = location.getLatitude();
                    Double longitude = location.getLongitude();
                    DecimalFormat format = new DecimalFormat("0.000");
                    locat.setText("Latitude: "+format.format(lattitude)+" Longitude "+ format.format(longitude));
                    addresses  = converter.getFromLocation(lattitude, longitude, 1);
                    String address = addresses.get(addresses.size()-1).getAddressLine(0);
                    location_text.setText(address);
                    locationArrayList.add(location);
                    Log.d("arrayList",locationArrayList.toString());
                }
                catch (Exception e)
                {}
                if(locationArrayList!= null &&locationArrayList.size()>1) {
                    distance += location.distanceTo(locationArrayList.get(locationArrayList.size() - 2));
                    time.setText(((location.getElapsedRealtimeNanos()/1000000000) - (locationArrayList.get(locationArrayList.size() - 2).getElapsedRealtimeNanos()/1000000000)) + " Seconds");
                }
                DecimalFormat format = new DecimalFormat("0.00");
                distance2.setText(format.format(distance)+" Meters");
                Log.d("distance",distance+" ");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        my_location =   (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED )
            my_location.requestLocationUpdates(LocationManager.GPS_PROVIDER,(long)3000,(float)10,locationListener_my);
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            my_location.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 3000, (float) 10, locationListener_my);
        }
        Bundle map2 = null;
        if(savedInstanceState != null)
            map2 = savedInstanceState.getBundle("lol");
        mapView.onCreate(map2);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        my_location.removeUpdates(locationListener_my);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        my_location.removeUpdates(locationListener_my);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED )
            my_location.requestLocationUpdates(LocationManager.GPS_PROVIDER,(long)3000,(float)10,locationListener_my);
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            my_location.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 3000, (float) 10, locationListener_my);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle map_bundle = outState.getBundle("lol");
        if(map_bundle == null) {
            map_bundle = new Bundle();
            outState.putBundle("lol", map_bundle);
        }
        mapView.onSaveInstanceState(map_bundle);


    }
}
