package com.baidu.iov.dueros.waimai.net.entity.base;

import android.os.Build;

import com.baidu.iov.dueros.waimai.net.Config;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net.entity
 * @time 2018/10/10 9:24
 * @change
 * @class describe
 */

public class RequestBase {
    /**api version*/
    public int av;
    /**APP KEY*/
    public String ak;
    /**channel*/
    public String cn;
    public String uuid;
    public String sign;
    /**client os*/
    public String c;


    public RequestBase() {
        this.c = Config.CLIENT_OS;
        this.ak = Config.APP_KEY;
        this.cn = Config.CN;
        this.av = Build.VERSION_CODES.HONEYCOMB_MR1;
    }


    @Override
    public String toString() {
        return "RequestBase{" +
                ", av=" + av +
                ", ak='" + ak + '\'' +
                ", cn='" + cn + '\'' +
                ", uuid='" + uuid + '\'' +
                ", sign='" + sign + '\'' +
                ", c='" + c + '\'' +
                '}';
    }



}
