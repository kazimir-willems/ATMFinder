package leif.com.atmfinder.proxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import leif.com.atmfinder.vo.GetAtmResponseVo;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GetAllATMProxy extends BaseProxy {

    public GetAtmResponseVo run(String latitude, String longitude, String country, String city) throws IOException {

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("latitude", latitude);
        formBuilder.add("longitude", longitude);
        formBuilder.add("country", country);
        formBuilder.add("city", city);

        RequestBody formBody = formBuilder.build();

        String contentString = postPlain("http://54.64.191.2/ATMFinder/GetAllATM.php", formBody);

        GetAtmResponseVo responseVo = new GetAtmResponseVo();

        try {
            JSONObject jsonResponse = new JSONObject(contentString);
            responseVo.atms = jsonResponse.getString("atms");
            responseVo.success = jsonResponse.getInt("success");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return responseVo;
    }
}
