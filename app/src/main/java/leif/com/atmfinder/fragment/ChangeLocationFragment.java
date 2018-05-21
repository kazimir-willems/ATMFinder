package leif.com.atmfinder.fragment;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import leif.com.atmfinder.AppContext;
import leif.com.atmfinder.R;
import leif.com.atmfinder.model.ATMModel;
import leif.com.atmfinder.proxy.GetATMProxy;
import leif.com.atmfinder.proxy.GetAllATMProxy;
import leif.com.atmfinder.proxy.GetLocationProxy;
import leif.com.atmfinder.ui.AtmListActivity;
import leif.com.atmfinder.vo.GetAtmResponseVo;
import leif.com.atmfinder.vo.GetLocationResponseVo;

public class ChangeLocationFragment extends Fragment {
    private String countries[] = {"United States", "Canada", "Austria", "United Kingdom", "Spain", "Russian Federation", "Czech Republic", "Australia", "Finland", "Switzerland", "Italy", "Netherlands", "Slovakia", "Mexico", "Panama", "Japan", "Slovenia", "Hong Kong", "Philippines", "Greece", "Dominican Republic", "Taiwan", "Belgium", "Sigapore", "Israel", "Romania", "Kosovo", "Croatia", "Poland", "Serbia", "VietNam", "Malta", "Kazakhstan", "Colombia", "Estonia", "Guam", "Denmark", "Costa Rica", "Chile", "Hungary", "Bulgaria", "France", "Norway", "New Zealand", "Thailand", "Malaysia", "China", "Sweden", "Ukraine", "Lithuania", "Aruba", "Brazil", "South Korea", "Barbados", "Cyprus", "Portugal", "Iceland", "Peru", "Liechtenstein", "Saudi Arabia", "Indonesia", "Mongolia", "Jamaica", "Anguilla"};
    private String cities[][] = {
            {"AgouraHills","Albuquerque","Alhambra","ArlingtonHeights","Artesia","Aspen","Athens","Atlanta","AtlanticHighlands","Austin","Azusa","Baltimore","Bangor","BensalemTownship","BeverlyHills","Birmingham,US","Boise","Boston","BoundBrook","Bountiful","Bourbonnais","Buffalo","Cartersville","Cary","ChapelHill","Charlotte","Cheboygan","Chicago","Chico","Chicopee","ChinoHills","Cincinnati","CityofOrange","Cleveland","Coachella","Columbia","Columbus","Cotati","Dallas","Denver","Detroit","Dolton","Dover","Dublin","ElCajon","ElPaso","Fargo","Fitchburg","FortMyers","FortWayne","Freehold","Fresno","Garland","Glenview","GrandForks","GrandRapids","GreenBay","Gurnee","HaddonTownship","Hazlet","Hillside","Hollywood","Honolulu","Hopatcong","Houston","Howell","Huntsville","Indianapolis","Irvine","Jacksonville","JerseyCity","Joplin","KansasCity,MO","Kapolei","Kissimmee","LagunaNiguel","LakeOrion","LakeWorth","Lancaster","Langhorne","LasVegas","Lexington","Lincoln","LincolnPark","LittleRock","LongBranch","LosAngeles","Louisville","Lunenburg","ManalapanTownship","Manchester(USA)","Mansfield","McAllen","Medford","Memphis","Miami","MiamiBeach","Milwaukee","Minneapolis","Minot","Modesto","Nashua","Nashville","NevadaCity","Newark","NewOrleans","NewportBeach","NewYork","Norcross","NorthBergen","NorthRichlandHills","Norwalk","OklahomaCity","Olympia","Omaha","Orlando","OrlandPark","Parlin","Passaic","Peoria","Philadelphia","Phoenix","Pittsburgh","Portland,ME","Portland,OR","Providence","RedBank","Reno","Richmond,VA,US","RockIsland","Rockwall","Rosedale","Sacramento","SaintJoseph","SaltLakeCity","SanAntonio","SanDiego","SanFrancisco","San José","SanJose,Ca","SanLeandro","Savannah","Schaumburg","Seattle","Secaucus","Sedona","Somerdale","SouthChicagoHeights","SouthPlainfield","SouthSiouxCity","Spencer","Springfield","Springfield,IL","St.Louis","StCharles","Suwanee","Tampa","Tarzana","Titusville","TomsRiver","Tustin","Verona","Vienna,VA","Waco","Washington","Wayne","WestJordan","Westmont","Willingboro","Willowick","Wilmington"},
            {"Antigonish","Calgary","Charlottetown","Edmonton","Fredericton","Gatineau","GrandPrairie,AB","Halifax","Kelowna","London,CA","MapleRidge","MedicineHat","Moncton","Montreal","Nanaimo","NorthBay","Ottawa","QuebecCity","RedDeer","Regina","SaintJohn","Saskatoon","SaultSte.Marie","St.John's","StAlbert","Sudbury","Toronto","VancouverCA","Victoria","Whistler","Winnipeg"},
            {"Amstetten","Attersee","Braunau","Dornbirn","Graz","HaidbeiAnsfelden","Horn","Imst","Innsbruck","Judenburg","Königswiesen","Linz","Nauders","Oberwart","Poggersdorf","Salzburg","SpittalanderDrau","Steyr","Vienna","Villach","Wels","Wieselburg","Zemendorf"},
            {"Belfast","Birmingham,UK","BrightonUK","Bristol","Cardiff","Chelmsford","Derby","Edinburgh","Glasgow","Harrow","Hastings","KingstonuponHull","Leeds","Leicester","London,UK","Manchester(UK)","Penzance","RoyalTunbridgeWells"},
            {"ACoruña","Barcelona","Bilbao","Gijón","Girona","Ibiza","LasPalmas","Lleida","Madrid","Malaga","Marbella","Murcia","Oviedo","PalmadeMallorca","SanCristóbaldeLaLaguna","Tarragona","Valencia","Valladolid","Vigo","Vitoria-Gasteiz","Zaragoza"},
            {"Chelyabinsk","Ivanovo","Izhevsk","Krasnodar","Krasnoyarsk","Moscow","Novosibirsk","Ufa","Yekaterinburg","Yoshkar-Ola"},
            {"Brno","Liberec","Ostrava","Prague","ŽeleznáRuda"},
            {"Brisbane","Launceston","Melbourne","Perth","Sydney"},
            {"Helsinki","Jyväskylä","Kouvola","Kuopio","Lahti","Tampere","Turku","Vaasa"},
            {"Basel","Bern","Biel/Bienne","Genève","Gottlieben","Herisau","Lausanne","Lugano","Luzern","Müstair","Neuchâtel","St.Gallen","Zurich"},
            {"Alba","Bolzano","Brenner","Florence","Genova","Milan","Rovereto","Torino","Trento","Udine"},
            {"Amersfoort","Amsterdam","Arnhem","Bavel","Eindhoven","Maastricht","Oudenbosch","Tilburg","Utrecht"},
            {"BanskáŠtiavnica","Bratislava","Košice","Michalovce","Poprad","Trenčín","Trnava"},
            {"Culiacán","Guadalajara","Mérida","MexicoCity","Monterrey","SantiagodeQuerétaro","Silao","Tijuana"},
            {"Panamá"},
            {"Fukuoka","Hiroshima","Okayama","Osaka","Tokyo"},
            {"Celje","Koper","Ljubljana","Maribor","Novomesto"},
            {"HongKong"},
            {"Manila","Pasay","QuezonCity","TaguigCity"},
            {"Athens","Heraklion","Thessaloniki"},
            {"PuntaCana","SantiagoDeLosCaballeros","SantoDomingo"},
            {"TainanCity","Taipei","TaipeiCity"},
            {"Antwerp","Brussels","Ghent","Hasselt"},
            {"Singapore"},
            {"Jerusalem","TelAviv"},
            {"Bucharest","ClujNapoca"},
            {"Prishtina"},
            {"Pula","Rijeka","Split","Zagreb"},
            {"Bialystok","Katowice","Warsaw"},
            {"Belgrade","NoviSad"},
            {"HoChiMinhCity"},
            {"Sliema","StJulian's"},
            {"Almaty","Astana"},
            {"Bogota","Pereira"},
            {"Tallinn"},
            {"Dededo","Tamuning"},
            {"Copenhagen"},
            {"SanJosé"},
            {"Santiago"},
            {"Budapest"},
            {"Sofia"},
            {"Montpellier"},
            {"Kristiansand","Oslo"},
            {"Auckland","Dunedin"},
            {"ChiangMai"},
            {"KualaLumpur"},
            {"Shanghai"},
            {"Stockholm"},
            {"Odessa"},
            {"Vilnius"},
            {"Oranjestad"},
            {"SãoPaulo"},
            {"Seoul"},
            {"Bridgetown"},
            {"Limassol"},
            {"Funchal"},
            {"Reykjavík"},
            {"Lima"},
            {"Vaduz"},
            {"Jubail"},
            {"Bali"},
            {"Ulaanbaatar"},
            {"Kingston"},
            {"TheValley"}
    };
    private Spinner countrySpinner;
    private Spinner citySpinner;

    private LinearLayout searchLayout;

    private Button btnRadar;
    private Button btnSearch;

    private EditText edtAddress;

    private String selectedCountry = "United States";
    private String selectedCity = "AgouraHills";

    private double latitude = 0.0;
    private double longitude = 0.0;

    private ArrayList<ATMModel> atmList = new ArrayList<>();

    private MediaPlayer player;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String address = "";
            address = edtAddress.getText().toString();
            if(address.isEmpty()) {
                GetAllATMTask task = new GetAllATMTask();
                task.execute(String.valueOf(AppContext.myLatitude), String.valueOf(AppContext.myLongitude), selectedCountry, selectedCity);
            } else {
                GetLocationTask task = new GetLocationTask();
                task.execute(address);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change, container, false);

        countrySpinner = (Spinner) v.findViewById(R.id.country_spin);
        citySpinner = (Spinner) v.findViewById(R.id.city_spin);
        btnRadar = (Button) v.findViewById(R.id.btn_radar);
        btnSearch = (Button) v.findViewById(R.id.btn_search);
        searchLayout = (LinearLayout) v.findViewById(R.id.search_layout);
        edtAddress = (EditText) v.findViewById(R.id.edt_address);

        player = new MediaPlayer();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, countries);
        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = countries[i];
                String[] selectedCities = cities[i];
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, selectedCities);

                citySpinner.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity = citySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();

                handler.postDelayed(runnable, 2000);
            }
        });
        return v;
    }

    public static ChangeLocationFragment newInstance() {
        ChangeLocationFragment f = new ChangeLocationFragment();
        return f;
    }

    private void startAnimation() {
        searchLayout.setVisibility(View.GONE);
        btnRadar.setVisibility(View.VISIBLE);

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
        searchLayout.setVisibility(View.VISIBLE);
        btnRadar.setVisibility(View.GONE);

        if(player.isPlaying())
            player.stop();

        btnRadar.setBackgroundResource(R.drawable.radar_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) btnRadar.getBackground();

        frameAnimation.stop();

        btnRadar.setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_radar_background));
    }

    private void getATM() {
        GetATMTask task = new GetATMTask();
        task.execute(String.valueOf(latitude), String.valueOf(longitude), selectedCountry, selectedCity);
    }

    private void refreshATMs(GetAtmResponseVo responseVo) {
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

                Intent intent = new Intent(getActivity(), AtmListActivity.class);

                intent.putExtra("atms", atmList);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);

                startActivity(intent);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Cannot get ATMs", Toast.LENGTH_SHORT).show();
        }
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
            refreshATMs(responseVo);
        }
    }

    public class GetAllATMTask extends AsyncTask<String, Void, GetAtmResponseVo> {

        private String latitude;
        private String longitude;
        private String country;
        private String city;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected GetAtmResponseVo doInBackground(String... params) {
            GetAllATMProxy simpleProxy = new GetAllATMProxy();
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
            refreshATMs(responseVo);
        }
    }

    public class GetLocationTask extends AsyncTask<String, Void, GetLocationResponseVo> {

        private String address;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected GetLocationResponseVo doInBackground(String... params) {
            GetLocationProxy simpleProxy = new GetLocationProxy();
            address = params[0];
            try {
                final GetLocationResponseVo responseVo = simpleProxy.run(address);

                return responseVo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(GetLocationResponseVo responseVo) {
            if(responseVo != null) {
                try {
                    JSONArray jsonArray = new JSONArray(responseVo.results);
                    JSONObject geoItem = jsonArray.getJSONObject(0);
                    JSONObject geometry = geoItem.getJSONObject("geometry");
                    JSONObject geoLocation = geometry.getJSONObject("location");
                    latitude = geoLocation.getDouble("lat");
                    longitude = geoLocation.getDouble("lng");

                    getATM();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } else {
                stopAnimation();
                Toast.makeText(getActivity(), "Cannot get address detail", Toast.LENGTH_SHORT).show();
            }
        }
    }

}