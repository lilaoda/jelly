package lhy.jelly.ui.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lhy.jelly.data.remote.ApiService;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Lihy on 2018/6/26 10:44
 * E-Mail ：liheyu999@163.com
 */
public class LoginActivityTest {

    @Inject
    ApiService apiService;

    @Test
    public void doLogin() throws JSONException {
        String json = "{\"apiKey\":\"PDA\",\"timestamp\":\"2018-07-26 16:50:32\",\"digest\":\"/augW3DLjvP8mGFC7oKYQg==\",\"data\":[{\"wbId\":\"123456\",\"creater\":\"6543210\",\"createName\":\"青浦中心\",\"barcodeList\":[{\"barcode\":\"TEST201807261650-11\",\"printedDt\":\"2018-07-26 16:50:32\"},{\"barcode\":\"TEST201807261650-12\",\"printedDt\":\"2018-07-26 16:50:32\"}]}]}";
//        String jsonAPP = "{\n" +
//                "\"apiKey\": \"PDA\",\n" +
//                "\"data\": [\n" +
//                "{\n" +
//                "\"barcodeList\": [\n" +
//                "{\n" +
//                "\"barcode\": \"843000234001\",\n" +
//                "\"printedDt\": \"2018-07-26 10:58:29\"\n" +
//                "}\n" +
//                "],\n" +
//                "\"createName\": \"张发康\",\n" +
//                "\"creater\": \"6412\",\n" +
//                "\"wbId\": \"84300023\"\n" +
//                "}\n" +
//                "],\n" +
//                "\"digest\": \"HAK+Yv93XGyHL2Q1tPgB9A==\",\n" +
//                " \"timestamp\": \"2018-07-26 10:58:36\"\n" +
//                "}";
        String jsonAPP = "{\"apiKey\":\"PDA\",\"data\":[{\"barcodeList\":[{\"barcode\":\"350050644001\",\"printedDt\":\"2018-07-26 17:34:30\"}],\"createName\":\"张发康\",\"creater\":\"6412\",\"wbId\":\"35005064\"}],\"timestamp\":\"2018-07-26 17:34:34\"}";
        JSONObject jsonObject = new JSONObject(jsonAPP);
        JSONArray dataAPP = jsonObject.getJSONArray("data");

        JSONObject jsonObjectServer = new JSONObject(json);
        JSONArray dataServer = jsonObjectServer.getJSONArray("data");
        System.out.println(dataAPP.toString());
        System.out.println(dataServer.toString());

    }

    @Test
    private void testUpload() {
        List<String> list = new ArrayList<>();
        String descriptionString = "This is a description";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
//        apiService.upload("android", "18", description, getMultipartBody()).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxObserver<JsonObject>() {
//                    @Override
//                    public void onSuccess(JsonObject value) {
//
//                    }
//                });
    }
}