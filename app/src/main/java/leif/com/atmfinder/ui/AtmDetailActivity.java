package leif.com.atmfinder.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.ButterKnife;
import leif.com.atmfinder.AppContext;
import leif.com.atmfinder.R;
import leif.com.atmfinder.adapter.AtmAdapter;
import leif.com.atmfinder.model.ATMModel;

public class AtmDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    RecyclerView atmList;

    private ArrayList<ATMModel> atmItems = new ArrayList<>();
    private ATMModel currentATM;

    private Button btnBanner;

    private TextView tvOperName;
    private TextView tvAddress;
    private TextView tvOpenHour;
    private TextView tvPlace;
    private TextView tvDistance;

    private GoogleMap mMap;
    MapView mapView;

    private double latitude;
    private double longitude;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        atmList = (RecyclerView) findViewById(R.id.atm_list);
        btnBanner = (Button) findViewById(R.id.btn_banner);

        tvOperName = (TextView) findViewById(R.id.tv_oper_name);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvOpenHour = (TextView) findViewById(R.id.tv_open_hour);
        tvPlace = (TextView) findViewById(R.id.tv_place);
        tvDistance = (TextView) findViewById(R.id.tv_distance);

        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coinmama.com/?ref=affwtm"));
                startActivity(browserIntent);
            }
        });

        atmItems = (ArrayList<ATMModel>) (getIntent().getSerializableExtra("atms"));
        currentATM = (ATMModel) getIntent().getSerializableExtra("current_atm");

        tvOperName.setText(currentATM.getOperName());
        tvAddress.setText(currentATM.getAddress());
        tvOpenHour.setText(currentATM.getOpenHour());
        tvPlace.setText(currentATM.getPlace());
        tvDistance.setText(String.format("%.2f", Double.valueOf(currentATM.getDistance())) + " km");

        mapView = (MapView) findViewById(R.id.map_view);

        // Gets to GoogleMap from the MapView and does initialization stuff
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    private void refreshItems() {
        LatLng moveMarker = new LatLng(0, 0);
        // Add a marker in Sydney and move the camera
        for (int i = 0; i < atmItems.size(); i++) {
            ATMModel item = atmItems.get(i);
            double latitude = item.getLatitude();
            double longitude = item.getLongitude();
            String operName = item.getOperName();
            LatLng marker = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(marker).title(operName));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentATM.getLatitude(), currentATM.getLongitude()), 12.0f));
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

        refreshItems();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.v("MarkerClicked", marker.getPosition().latitude + ", " + marker.getPosition().longitude);
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + marker.getPosition().latitude + "," + marker.getPosition().longitude));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
