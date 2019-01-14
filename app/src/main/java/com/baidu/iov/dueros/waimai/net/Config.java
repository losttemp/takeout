package com.baidu.iov.dueros.waimai.net;


import com.baidu.iov.dueros.waimai.utils.CacheUtils;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net
 * @time 2018/10/10 8:52
 * @change
 * @class describe
 */
public class Config {
    /**url type*/
    private static boolean isOfficial = true;

    private static final String OFFICIAL_HOST = "https://vehicle.baidu.com";
    private static final String TEST_HOST = "https://sandbox-vehicle.baidu.com";


    public static String getHost(){
           return  isOfficial?OFFICIAL_HOST:TEST_HOST;
    }

    public static final String CLIENT_OS ="a";

    public static final String APP_KEY ="fordedge";

    public static final String CN ="1006002002";


    public static final String PREFIX_SIGN ="fdS1Oa";

    public static final String KEY_SIGN ="Vs@dsP";

    public static final int Coordinate_Type_GB =2;

    public static final int Coordinate_Type_BAIDU =3;

    public static String COOKIE_VALUE = "BDUSS=" + CacheUtils.getBduss();
    
    public static final String COOKIE_KEY = "Cookie";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";


}
