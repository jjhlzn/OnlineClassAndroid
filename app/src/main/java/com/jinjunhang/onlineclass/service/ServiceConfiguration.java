package com.jinjunhang.onlineclass.service;

import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;

/**
 * Created by lzn on 16/3/23.
 */
public class ServiceConfiguration {
    //public final static String serverName = "192.168.1.50";

    public static final String DEFAULT_HTTP = "http";
    public static final String DEFAULT_HOST = "114.215.236.171";
    public static final String DEFAULT_PORT = "6012";

    private final static boolean isUseConfig = true;
    public static String LOCATOR_HTTP = "";
    public static String LOCATOR_SERVERNAME = "";
    public static int LOCATOR_PORT = 0;
    private final static String serverName1 = "jjhaudio.hengdianworld.com";
    private final static int port1 = 80;

    private final static String serverName2 = "114.215.236.171";
    private final static int port2 = 6012;

    static {
        KeyValueDao keyValueDao = KeyValueDao.getInstance(CustomApplication.get());
        LOCATOR_HTTP = keyValueDao.getValue(KeyValueDao.SERVER_HTTP, ServiceConfiguration.DEFAULT_HTTP);
        LOCATOR_SERVERNAME = keyValueDao.getValue(KeyValueDao.SERVER_HOST, ServiceConfiguration.DEFAULT_HOST);
        try {
            LOCATOR_PORT = Integer.parseInt(keyValueDao.getValue(KeyValueDao.SERVER_PORT, ServiceConfiguration.DEFAULT_PORT));
        } catch (Exception ex) {
            LOCATOR_PORT = 80;
        }
    }

    public static String httpMethod() {
        if (isUseConfig) {
            return LOCATOR_HTTP;
        }
        return "http";
    }

    public static String serverName() {
        if (isUseConfig) {
            return LOCATOR_SERVERNAME;
        }
        return serverName1;
    }

    public static int port() {
        if (isUseConfig) {
            return LOCATOR_PORT;
        }
        return port1;
    }

    //用户
    public static String LoginUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/login";
    }

    public static  String LogoutUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/logout";
    }

    public static  String SingupUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/signup";
    }

    public static  String GetPhoneCheckCodeUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/getPhoneCheckCode";
    }

    public static String ForgetPasswordUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/getPassword";
    }

    public static String GetUserStatData() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/getstatdata";
    }

    public static String GetUserProfileImage(String userid) {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/getprofileimage?userid="+userid;
    }

    public static String UploadProfileImageUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/uploadprofileimage";
    }

    public static String SetNameUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/setName";
    }

    public static String SetNickNameUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/setnickname";
    }

    public static String ResetPassword() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/resetPassword";
    }

    public static String UpdateTokenUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/user/updatetoken";
    }


    //课程
    public static String GetAlbumsUrl() {
        return httpMethod() + "://"+ serverName() + ":" + port() +"/albums";
    }

    public static String GetHotSearchWordsUrl() {
        return httpMethod() + "://"+ serverName() + ":" + port() +"/album/getHotSearchWords";
    }

    public static String SearchCourseUrl() {
        return httpMethod() + "://"+ serverName() + ":" + port() +"/album/search";
    }

    public static String GetAlbumSongsUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/album/songs";
    }

    public static String GetAdsUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getAds";
    }

    public static String GetSongCommentsUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/song/comments";
    }

    public static String GetLiveSongListener() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/song/livelistener";
    }

    //Comment
    public static String SendCommentUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/comment/add";
    }

    public static String GetLiveCommentsUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/song/livecomments";
    }

    public static String SendLiveCommentUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/comment/addLive";
    }

    //other
    public static String CheckUpgradeUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/checkUpgrade";
    }


    //ServiceLocator
    public static String GetServiceLocatorUrl() {
        return "http://servicelocator.hengdianworld.com:9000/servicelocator";
    }

}


