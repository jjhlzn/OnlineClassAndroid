package com.jinjunhang.onlineclass.service;

/**
 * Created by lzn on 16/3/23.
 */
public class ServiceConfiguration {
    //public final static String serverName = "192.168.1.50";

    private final static boolean isUseConfig = false;
    public static String LOCATOR_HTTP = "";
    public static String LOCATOR_SERVERNAME = "";
    public static int LOCATOR_PORT = 0;
    private final static String serverName1 = "jjhaudio.hengdianworld.com";
    private final static int port1 = 80;

    private final static String serverName2 = "192.168.31.146";
    private final static int port2 = 3000;

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

    public static String GetAlbumsUrl() {
        return httpMethod() + "://"+ serverName() + ":" + port() +"/albums";
    }

    public static String GetAlbumSongsUrl() {
        return httpMethod() + "://" + serverName() + ":" + port() + "/album/songs";
    }

}


