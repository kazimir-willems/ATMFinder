package leif.com.atmfinder.proxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import leif.com.atmfinder.vo.GetAtmResponseVo;
import leif.com.atmfinder.vo.GetLocationResponseVo;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GetLocationProxy extends BaseProxy {

    public GetLocationResponseVo run(String address) throws IOException {

        String params = "";
        params = "sensor = false&address=" + address + "&api_key=" + "AIzaSyAIOABdZlb4TbenYyF20YFWYA-DDNnoFSg";

        String contentString = getPlain("https://maps.googleapis.com/maps/api/geocode/json?", params);

        GetLocationResponseVo responseVo = new GetLocationResponseVo();

        try {
            JSONObject jsonResponse = new JSONObject(contentString);
            if(jsonResponse.getString("status").equals("OK")) {
                responseVo.results = jsonResponse.getString("results");
            } else {
                return null;
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return responseVo;
    }
}
