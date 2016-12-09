package com.jinjunhang.framework.service;

import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import com.jinjunhang.onlineclass.service.LoginResponse;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.service.UpdateTokenRequest;
import com.jinjunhang.onlineclass.service.UpdateTokenResponse;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;

/**
 * Created by lzn on 16/3/23.
 */
public class BasicService {

    private final static String TAG = "BasicService";


    private String send(ServerRequest request) throws IOException {

        String method = "POST";
        String paramsString = request.getRequestJson();
        Log.d(TAG, "paramsString = " + paramsString);

        String url = request.getServiceUrl();

        if (method == "GET") {
            url = url + "?" + paramsString;
            Log.d(TAG, "send GET request: " + url);
            return get(url);
        } else {
            Log.d(TAG, "send POST request: " + url);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(request.getConnectionTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(request.getWriteTimeout(), TimeUnit.SECONDS)
                    .readTimeout(request.getReadTimeout(), TimeUnit.SECONDS)
                    .build();

            Request request1 = new Request.Builder()
                    .url(request.getServiceUrl())
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramsString))
                    .build();

            Response response = client.newCall(request1).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }



    private String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
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

            //token is invalid, need to try to get new token
            if (json.getInt("status") == ServerResponse.TOKEN_INVALID && !request.isResendForInvalidToken()) {
                UpdateTokenRequest updateTokenRequest  = new UpdateTokenRequest();
                LoginUser loginUser = new LoginUserDao(CustomApplication.get()).get();
                if (loginUser != null) {
                    updateTokenRequest.setUserName(loginUser.getUserName());
                    updateTokenRequest.setPassword(loginUser.getPassword());
                    UpdateTokenResponse updateTokenResponse = sendRequest(updateTokenRequest);
                    if (updateTokenResponse.isSuccess()) {
                        loginUser.setName( updateTokenResponse.getName() );
                        loginUser.setNickName( updateTokenResponse.getNickName() );
                        loginUser.setSex( updateTokenResponse.getSex());
                        loginUser.setCodeImageUrl( updateTokenResponse.getCodeImageUrl());
                        loginUser.setToken( updateTokenResponse.getToken());
                        loginUser.setLevel( updateTokenResponse.getLevel());
                        loginUser.setBoss( updateTokenResponse.getBoss());

                        request.setResendForInvalidToken(true);
                        request.setTest("resend");
                        resp = sendRequest(request);
                    } else {
                        resp.setStatus(ServerResponse.FAIL);
                        resp.setErrorMessage("请重新登录");
                    }
                }
            }

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
            resp.setErrorMessage("您的网络不给力，请检查网络是否正常");
            return resp;
        } catch (JSONException e) {
            Log.e(TAG, "Parse response of " + request.getServiceUrl() + " failed: ", e);
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("您的网络不给力，请检查网络是否正常");
            return resp;
        }
    }

    public ServerResponse upload(String url, File file) {
        OkHttpClient client = new OkHttpClient();
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
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject json =  new JSONObject(jsonString);
            resp.setStatus(json.getInt("status"));
            resp.setErrorMessage(json.getString("errorMessage"));
        }catch (Exception ex) {
            resp.setStatus(ServerResponse.FAIL);
            resp.setErrorMessage("您的网络不给力，请检查网络是否正常");
        }
        return resp;
    }



}

