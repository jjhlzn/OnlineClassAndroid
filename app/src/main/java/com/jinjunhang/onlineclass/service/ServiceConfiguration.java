package com.jinjunhang.onlineclass.service;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;


/**
 * Created by lzn on 16/3/23.
 */
public class ServiceConfiguration {
    private static final String TAG = LogHelper.makeLogTag(ServiceConfiguration.class);
    //public final static String serverName = "192.168.1.50";

    public static final String DEFAULT_HTTP = "http";
    public static final String DEFAULT_HOST = "jf.yhkamani.com";
    public static final String DEFAULT_PORT = "80";

    private final static boolean isUseConfig = false;
    public static String LOCATOR_HTTP = "";
    public static String LOCATOR_SERVERNAME = "";
    public static int LOCATOR_PORT = 0;
    private final static String serverName1 =  "192.168.31.130";  //"192.168.64.126"; //  //"jf.yhkamani.com"; //"192.168.1.108";
    private final static int port1 = 4000;


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
        //LogHelper.d(TAG, "isUseConfig = " + isUseConfig);
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

    public static String GetSongInfoUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/song/getsonginfo";
    }

    public static String SendHeartbeatUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/song/heartbeat";
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

    public static String GetParameterInfoUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getparameterinfo";
    }

    public static String GetFunctionMessageUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getfunctionmessage";
    }

    public static String GetExtendFunctionInfoUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getfunctioninfos";
    }

    public static String ClearFunctionMessageUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/clearfunctionmessage";
    }

    public static String GetCourseNotifyUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getcoursenotify";
    }

    public static String GetLaunchAdvUrl() {
        return  httpMethod() + "://" + serverName() + ":" + port() + "/app/getlaunchadv";
    }

    //mainPage
    public static String GetHeaderAdvUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getheaderadvs";
    }

    public static String GetFootAdvUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getfooteradvs";
    }


    //ServiceLocator
    public static String GetServiceLocatorUrl() {
        return "http://servicelocator.hengdianworld.com:9000/servicelocator";
    }


    //other
    public static String GetMainPageAdsUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getMainPageAds";
    }

    public static String GetTouTiaoUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/app/getToutiao";
    }

    public static String GetTuijianCoursesUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/getTuijianCourses";
    }

    public static String GetCoursesUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/getCourses";
    }
}


