package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net.entity.response
 * @time 2018/10/10 14:06
 * @change
 * @class describe
 */

public class CityListResponse {
    private List<City> hot;
    private List<City> all;

    public List<City> getHot() {
        return hot;
    }

    public void setHot(List<City> hot) {
        this.hot = hot;
    }

    public List<City> getAll() {
        return all;
    }

    public void setAll(List<City> all) {
        this.all = all;
    }
}
