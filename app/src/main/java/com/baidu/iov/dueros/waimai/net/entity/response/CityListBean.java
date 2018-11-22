package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

/**
 * Created by ubuntu on 18-10-15.
 */

public class CityListBean {

    private List<HotBean> hot;
    private List<AllBean> all;

    public List<HotBean> getHot() {
        return hot;
    }

    public void setHot(List<HotBean> hot) {
        this.hot = hot;
    }

    public List<AllBean> getAll() {
        return all;
    }

    public void setAll(List<AllBean> all) {
        this.all = all;
    }

    public static class HotBean {
        /**
         * id : 131
         * name : 北京
         * pinyin : beijing
         */

        private int id;
        private String name;
        private String pinyin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        @Override
        public String toString() {
            return "HotBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    '}';
        }
    }

    public static class AllBean {
        public AllBean(int id, String name, String pinyin) {
            this.id = id;
            this.name = name;
            this.pinyin = pinyin;
        }

        /**
         * id : 320
         * name : 鞍山
         * pinyin : anshan
         */


        private int id;
        private String name;
        private String pinyin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        @Override
        public String toString() {
            return "AllBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    '}';
        }
    }
}
