package com.baidu.iov.dueros.waimai.net.entity.base;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net.entity
 * @time 2018/10/10 9:24
 * @change
 * @class describe
 */

public class ResponseBase <T>{
    /**error code*/
    private int errno;
    /**message*/
    private String err_msg;
    private T data;
    private String logid;
    private String spend_time;
    private String uuid;
    private String time;

    public ResponseBase() {
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }


    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    public String getSpend_time() {
        return spend_time;
    }

    public void setSpend_time(String spend_time) {
        this.spend_time = spend_time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBase{" +
                "errno=" + errno +
                ", err_msg='" + err_msg + '\'' +
                ", data=" + data +
                ", logid='" + logid + '\'' +
                ", spend_time='" + spend_time + '\'' +
                ", uuid='" + uuid + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
