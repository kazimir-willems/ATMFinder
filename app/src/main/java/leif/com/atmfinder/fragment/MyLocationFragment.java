package leif.com.atmfinder.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import leif.com.atmfinder.AppContext;
import leif.com.atmfinder.R;
import leif.com.atmfinder.model.ATMModel;
import leif.com.atmfinder.proxy.GetATMProxy;
import leif.com.atmfinder.vo.GetAtmResponseVo;

public class MyLocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Button btnRadar;
    private GoogleMap mMap;
    MapView mapView;

    private String country;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MediaPlayer player;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getATM();
        }
    };

    private ArrayList<ATMModel> atmList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mylocation, container, false);

        btnRadar = (Button) v.findViewById(R.id.btn_radar);

        btnRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppContext.myLatitude != 0.0 && AppContext.myLongitude != 0.0) {
                    startAnimation();

                    GetAddressTask task = new GetAddressTask();
                    task.execute();
                } else {
                    Toast.makeText(getActivity(), "Cannot get your current location", Toast.LENGTH_SHORT).show();
                }

            }
        });

        player = new MediaPlayer();

        mapView = (MapView) v.findViewById(R.id.map_view);

        // Gets to GoogleMap from the MapView and does initialization stuff
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return v;
    }

    private void startAnimation() {
        btnRadar.setEnabled(false);
        String audioFileName = "radar_sound.mp3";

        if(player.isPlaying())
            player.stop();
        try {
            player.reset();
            AssetFileDescriptor afd = getActivity().getAssets().openFd(audioFileName);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        btnRadar.setBackgroundResource(R.drawable.radar_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) btnRadar.getBackground();

        frameAnimation.start();
    }

    private void stopAnimation() {
        btnRadar.setEnabled(true);
        if(player.isPlaying())
            player.stop();

        btnRadar.setBackgroundResource(R.drawable.radar_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) btnRadar.getBackground();

        frameAnimation.stop();

        btnRadar.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_radar_background));
    }

    private void getATM() {
        GetATMTask task = new GetATMTask();
        task.execute(String.valueOf(AppContext.myLatitude), String.valueOf(AppContext.myLatitude), country, "");
    }

    public static MyLocationFragment newInstance() {
        MyLocationFragment f = new MyLocationFragment();
        return f;
    }

    public void refreshComponent() {
        btnRadar.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
    }

    private void refreshMap(GetAtmResponseVo responseVo) {
        stopAnimation();
        if(responseVo != null && responseVo.success == 1) {
            atmList.clear();
            try {
                JSONArray jsonArray = new JSONArray(responseVo.atms);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ATMModel atmItem = new ATMModel();
                    atmItem.setOperName(jsonObject.getString("oper_name"));
                    atmItem.setLatitude(jsonObject.getDouble("latidue"));
                    atmItem.setLongitude(jsonObject.getDouble("longitude"));
                    atmItem.setAddress(jsonObject.getString("address"));
                    atmItem.setOpenHour(jsonObject.getString("open_hour"));
                    atmItem.setCountry(jsonObject.getString("country"));
                    atmItem.setCity(jsonObject.getString("city"));
                    atmItem.setPlace(jsonObject.getString("place"));
                    atmItem.setDistance(jsonObject.getString("distance"));

                    atmList.add(atmItem);

                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        LatLng moveMarker = new LatLng(0, 0);
        // Add a marker in Sydney and move the camera
        for (int i = 0; i < atmList.size(); i++) {
            ATMModel item = atmList.get(i);
            double latitude = item.getLatitude();
            double longitude = item.getLongitude();
            String operName = item.getOperName();
            LatLng marker = new LatLng(latitude, longitude);
            if(i == 0) {
                moveMarker = marker;
            }
            mMap.addMarker(new MarkerOptions().position(marker).title(operName));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveMarker, 12.0f));

        btnRadar.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.v("MarkerClicked", marker.getPosition().latitude + ", " + marker.getPosition().longitude);
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + AppContext.myLatitude + "," + AppContext.myLongitude + "&daddr=" + marker.getPosition().latitude + "," + marker.getPosition().longitude));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en")));
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public class GetATMTask extends AsyncTask<String, Void, GetAtmResponseVo> {

        private String latitude;
        private String longitude;
        private String country;
        private String city;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected GetAtmResponseVo doInBackground(String... params) {
            GetATMProxy simpleProxy = new GetATMProxy();
            latitude = params[0];
            longitude = params[1];
            country = params[2];
            city = params[3];
            try {
                final GetAtmResponseVo responseVo = simpleProxy.run(latitude, longitude, country, city);

                return responseVo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(GetAtmResponseVo responseVo) {
            stopAnimation();
            refreshMap(responseVo);
        }
    }

    public class GetAddressTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            Geocoder gcd = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            int retryCount = 0;
            while(retryCount < 3) {
                try {
                    addresses = gcd.getFromLocation(AppContext.myLatitude, AppContext.myLongitude, 1);

                    if (addresses.size() > 0) {
                        country = addresses.get(0).getCountryName();

                        retryCount = 3;
                    } else {
                        retryCount++;
                    }

                } catch (IOException e) {
                    retryCount++;
                    e.printStackTrace();
                }
            }

            return country;

        }

        @Override
        protected void onPostExecute(String address) {
            if(address == null) {
                stopAnimation();
                Toast.makeText(getActivity(), "Cannot get your current location", Toast.LENGTH_SHORT).show();
            } else {
                handler.postDelayed(runnable, 2000);
            }
        }
    }
}