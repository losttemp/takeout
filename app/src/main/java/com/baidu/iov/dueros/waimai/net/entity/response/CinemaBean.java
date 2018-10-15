package com.baidu.iov.dueros.waimai.net.entity.response;

import java.io.Serializable;
import java.util.List;

public class CinemaBean {
    private FilterBean filter;
    private List<ListBean> list;

    public FilterBean getFilter() {
        return filter;
    }

    public void setFilter(FilterBean filter) {
        this.filter = filter;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class FilterBean {
        private List<AreasBean> areas;
        private List<BrandsBean> brands;
        private List<ExtraBean> extra;

        public List<AreasBean> getAreas() {
            return areas;
        }

        public void setAreas(List<AreasBean> areas) {
            this.areas = areas;
        }

        public List<BrandsBean> getBrands() {
            return brands;
        }

        public void setBrands(List<BrandsBean> brands) {
            this.brands = brands;
        }

        public List<ExtraBean> getExtra() {
            return extra;
        }

        public void setExtra(List<ExtraBean> extra) {
            this.extra = extra;
        }

        public static class AreasBean extends DataBean {
            private List<ChildrenBean> children;

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            public static class ChildrenBean extends DataBean {
            }
        }

        public static class BrandsBean extends DataBean {
        }

        public static class ExtraBean extends DataBean {
            private List<ChildrenBeanX> children;

            public List<ChildrenBeanX> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBeanX> children) {
                this.children = children;
            }

            public static class ChildrenBeanX extends DataBean {
            }
        }
    }

    public static class ListBean implements Serializable {
        private int cinemaId;
        private String name;
        private String address;
        private String minPrice;
        private String distance;
        private List<TagBean> tag;

        public int getCinemaId() {
            return cinemaId;
        }

        public void setCinemaId(int cinemaId) {
            this.cinemaId = cinemaId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(String minPrice) {
            this.minPrice = minPrice;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public List<TagBean> getTag() {
            return tag;
        }

        public void setTag(List<TagBean> tag) {
            this.tag = tag;
        }

        public static class TagBean {
            private int id;
            private String name;

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
        }
    }


    public static class DataBean {
        private int id;
        private String name;
        private int value;

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

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
