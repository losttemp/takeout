package com.baidu.iov.dueros.waimai.net.entity.response;

/**
 * @author pengqm
 * @name film
 * @class name：com.baidu.iov.dueros.film.net.entity.response
 * @time 2018/10/12 13:45
 * @change
 * @class describe
 */

public class CinemaInfoResponse {
    private String startTime;
    private String endTime;
    private String roomInfo;
    private String language;
    private String price;
    private String vipPrice;
    private String remain;

    public CinemaInfoResponse() {
    }

    public CinemaInfoResponse(String startTime, String endTime, String roomInfo, String language,
                              String price, String vipPrice, String remain) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomInfo = roomInfo;
        this.language = language;
        this.price = price;
        this.vipPrice = vipPrice;
        this.remain = remain;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(String roomInfo) {
        this.roomInfo = roomInfo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    @Override
    public String toString() {
        return "CinemaInfoResponse{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", roomInfo='" + roomInfo + '\'' +
                ", language='" + language + '\'' +
                ", price='" + price + '\'' +
                ", vipPrice='" + vipPrice + '\'' +
                ", remain='" + remain + '\'' +
                '}';
    }
}
