package com.jinjunhang.framework.service;

import android.app.Application;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;
import com.jinjunhang.onlineclass.BuildConfig;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;

/**
 * Created by lzn on 16/3/23.
 */
public class BasicService {

    private final static String TAG = "BasicService";

    private OkHttpClient client = new OkHttpClient();

    private Map<String ,Object> getUserInfo() {
        Map<String, Object> userInfo = new LinkedHashMap<>();
        LoginUser loginUser = LoginUserDao.getInstance(CustomApplication.get()).get();
        if (loginUser == null) {
            loginUser = new LoginUser();
        }
        userInfo.put("userid", loginUser.getUserName());
        userInfo.put("token", loginUser.getPassword());
        return userInfo;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer + " " + model;
    }


    private Map<String, Object> getDeviceInfo() {
        Map<String, Object> deviceInfo = new LinkedHashMap<>();
        deviceInfo.put("platform", "android");
        deviceInfo.put("model",  getDeviceName());
        deviceInfo.put("osversion", getAndroidVersion());

        DisplayMetrics metrics = CustomApplication.get().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        deviceInfo.put("screensize", width+"*"+height);
        deviceInfo.put("appversion",  BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);
        return deviceInfo;
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }
    private Map<String, Object> addUserAndDeviceInfo(Map<String, Object> params) {
        Map<String, Object> newParams = new LinkedHashMap<>();
        newParams.put("request", params);
        newParams.put("client", getDeviceInfo());
        newParams.put("userInfo", getUserInfo());
        return newParams;

    }

    private String send(ServerRequest request) throws IOException {
        String method = "POST";
        Map<String, Object> params = addMoreRequestInfo(request.getParams());

        Gson gson = new Gson();

        params = addUserAndDeviceInfo(params);
        String paramsString = gson.toJson(params);
        Log.d(TAG, "paramsString = " + paramsString);

        String url = request.getServiceUrl();

        if (method == "GET") {
            url = url + "?" + paramsString;
            Log.d(TAG, "send request: " + url);
            return get(url);
        } else {
            Log.d(TAG, "send request: " + url);

            Request request1 = new Request.Builder()
                    .url(request.getServiceUrl())
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramsString))
                    .build();

            Response response = client.newCall(request1).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }

    private Map<String, Object> addMoreRequestInfo(Map<String, Object> params) {
        Map<String, Object> newParams = new LinkedHashMap<>();
        newParams.put("request", params);
        //newParams["client"] = getClientInfo()
        //newParams["userInfo"] = getUserInfo()
        return newParams;

    }

    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public <T extends ServerResponse> T sendRequest(ServerRequest request) {
        T resp = null;
        try {
            resp = (T)request.getServerResponseClass().newInstance();

            String httpMethod = "POST";
            Log.d(TAG, "httpMethod = " +httpMethod);
            String jsonString = send(request);

            Log.d(TAG, "get response: " + jsonString);
            JSONObject json =  new JSONObject(jsonString);
            resp.setStatus(json.getInt("status"));
            resp.setErrorMessage(json.getString("errorMessage"));
            if (json.getInt("status") != ServerResponse.SUCCESS) {
                return resp;
            }
            resp.parse(request, json);
            return resp;

        } catch (InstantiationException e) {
            return null;
        }
        catch (IllegalAccessException e){
            return null;
        }
        catch (IOException ioe) {
            Log.e(TAG, "Fetch " + request.getServiceUrl() + " failed: ", ioe);
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("IOException happen");
            return resp;
        } catch (JSONException e) {
            Log.e(TAG, "Parse response of " + request.getServiceUrl() + " failed: ", e);
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("JSON parse exception happen");
            return resp;
        }
    }



    public ServerResponse upload(String url, File file) {
        ServerResponse resp = new ServerResponse() {
            @Override
            public void parse(ServerRequest request, JSONObject json) throws JSONException {

            }
        };
        try {
            LoginUserDao loginUserDao = LoginUserDao.getInstance(CustomApplication.get());
            LoginUser user = loginUserDao.get();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("userimage", "userimage", RequestBody.create(MediaType.parse("image/png"), file))
                    .addFormDataPart("userid", user.getUserName())
                    .addFormDataPart("token", user.getToken())
                    .build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            Response response = this.client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject json =  new JSONObject(jsonString);
            resp.setStatus(json.getInt("status"));
            resp.setErrorMessage(json.getString("errorMessage"));
        }catch (Exception ex) {
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("服务器出错");
        }
        return resp;
    }

}

