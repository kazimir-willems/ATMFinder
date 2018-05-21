package leif.com.atmfinder.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import leif.com.atmfinder.AppContext;
import leif.com.atmfinder.fragment.ChangeLocationFragment;
import leif.com.atmfinder.fragment.MapFragment;
import leif.com.atmfinder.fragment.MyLocationFragment;
import leif.com.atmfinder.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button btnMyLocation;
    private Button btnChangeLocation;
    private Button btnBanner;

    MapFragment mapFragment;
    MyLocationFragment myLocationFragment;
    ChangeLocationFragment changeLocationFragment;

    private LocationManager mLocationManager;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnBanner = (Button) findViewById(R.id.btn_banner);

        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coinmama.com/?ref=affwtm"));
                startActivity(browserIntent);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btnMyLocation = (Button) findViewById(R.id.btn_my_location);
        btnChangeLocation = (Button) findViewById(R.id.btn_change_location);

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
                myLocationFragment.refreshComponent();
                btnMyLocation.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_map_press));
                btnChangeLocation.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_location_normal));
            }
        });

        btnChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                btnMyLocation.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_map_normal));
                btnChangeLocation.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_location_press));
            }
        });

        mapFragment = MapFragment.newInstance();
        myLocationFragment = MyLocationFragment.newInstance();
        changeLocationFragment = ChangeLocationFragment.newInstance();

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mLocationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("MyLocatoin", "Allowed");
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
                    1, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    1, mLocationListener);
        } else {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.v("MyLocation", "Allowed");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v("MyLocation", "Allowed");
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
                                1, mLocationListener);
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                                1, mLocationListener);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Log.v("MyLocation", "Location");
            AppContext.myLatitude = location.getLatitude();
            AppContext.myLongitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch(pos) {
                case 0:
                    return myLocationFragment;
                case 1:
                    return changeLocationFragment;
                default:
                    return myLocationFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
