package leif.com.atmfinder.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import leif.com.atmfinder.R;
import leif.com.atmfinder.adapter.AtmAdapter;
import leif.com.atmfinder.model.ATMModel;

public class AtmListActivity extends AppCompatActivity {

    RecyclerView atmList;

    private ArrayList<ATMModel> atmItems = new ArrayList<>();
    private AtmAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;

    private Button btnBanner;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmlist);

        ButterKnife.bind(this);

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        atmList = (RecyclerView) findViewById(R.id.atm_list);
        btnBanner = (Button) findViewById(R.id.btn_banner);

        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coinmama.com/?ref=affwtm"));
                startActivity(browserIntent);
            }
        });

        atmItems = (ArrayList<ATMModel>) (getIntent().getSerializableExtra("atms"));

        atmList.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(AtmListActivity.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        atmList.setLayoutManager(mLinearLayoutManager);
        atmList.addItemDecoration(new DividerItemDecoration(AtmListActivity.this, DividerItemDecoration.VERTICAL_LIST));

        adapter = new AtmAdapter(AtmListActivity.this);
        atmList.setAdapter(adapter);

        refreshItems();
    }

    private void refreshItems() {
        adapter.addItems(atmItems);

        adapter.notifyDataSetChanged();
    }

    public void touchItem(int pos) {
        ATMModel item = atmItems.get(pos);

        Intent intent = new Intent(AtmListActivity.this, AtmDetailActivity.class);

        intent.putExtra("atms", atmItems);
        intent.putExtra("current_atm", item);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
