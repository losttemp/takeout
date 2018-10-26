package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class FilterConditionsResponse {
    
    private MeituanBean meituan;

    private IovBean iov;

    public MeituanBean getMeituan() {
        return meituan;
    }

    public void setMeituan(MeituanBean meituan) {
        this.meituan = meituan;
    }

    @Override
    public String toString() {
        return "FilterConditionsResponse{" +
                "meituan=" + meituan +
                ", iov=" + iov +
                '}';
    }

    public IovBean getIov() {
        return iov;
    }

    public void setIov(IovBean iov) {
        this.iov = iov;
    }

    public  static  class MeituanBean{
       
        private int code;

        
        private String msg;

      
        private String errorInfo;

      
        private MeituanData data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }

        public MeituanData getData() {
            return data;
        }

        public void setData(MeituanData data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "MeituanBean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", errorInfo='" + errorInfo + '\'' +
                    ", data=" + data +
                    '}';
        }

        public static class MeituanData  {

            private List<CategoryFilter> category_filter_list;

            private List<SortType> sort_type_list;
            
            private List<ActivityFilter> activity_filter_list;

            public List<CategoryFilter> getCategory_filter_list() {
                return category_filter_list;
            }

            public void setCategory_filter_list(List<CategoryFilter> category_filter_list) {
                this.category_filter_list = category_filter_list;
            }

            public List<SortType> getSort_type_list() {
                return sort_type_list;
            }

            public void setSort_type_list(List<SortType> sort_type_list) {
                this.sort_type_list = sort_type_list;
            }

            public List<ActivityFilter> getActivity_filter_list() {
                return activity_filter_list;
            }

            public void setActivity_filter_list(List<ActivityFilter> activity_filter_list) {
                this.activity_filter_list = activity_filter_list;
            }

            @Override
            public String toString() {
                return "MeituanData{" +
                        "category_filter_list=" + category_filter_list +
                        ", sort_type_list=" + sort_type_list +
                        ", activity_filter_list=" + activity_filter_list +
                        '}';
            }

            public static class CategoryFilter {

                private long code;
                private String name;
                private int quantity;
                private List<SubCategory> sub_category_list;

                public long getCode() {
                    return code;
                }

                public void setCode(long code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }

                public List<SubCategory> getSub_category_list() {
                    return sub_category_list;
                }

                public void setSub_category_list(List<SubCategory> sub_category_list) {
                    this.sub_category_list = sub_category_list;
                }

                @Override
                public String toString() {
                    return "CategoryFilter{" +
                            "code=" + code +
                            ", name='" + name + '\'' +
                            ", quantity=" + quantity +
                            ", sub_category_list=" + sub_category_list +
                            '}';
                }

                public static class SubCategory {
                    private long code;
                    private String name;
                    private int quantity;
                    private String icon_url;

                    public long getCode() {
                        return code;
                    }

                    public void setCode(long code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getQuantity() {
                        return quantity;
                    }

                    public void setQuantity(int quantity) {
                        this.quantity = quantity;
                    }

                    public String getIcon_url() {
                        return icon_url;
                    }

                    public void setIcon_url(String icon_url) {
                        this.icon_url = icon_url;
                    }

                    @Override
                    public String toString() {
                        return "SubCategory{" +
                                "code=" + code +
                                ", name='" + name + '\'' +
                                ", quantity=" + quantity +
                                ", icon_url='" + icon_url + '\'' +
                                '}';
                    }
                }
            }

            public static class SortType {
                private long  code;
                private String  name;
                private String  short_name;
                private String icon_url;
                private String icon_url_click;
                private int  position;
                public  static final int TABPOS=0;
                public  static final int LISTPOS=1;
                public long getCode() {
                    return code;
                }

                public void setCode(long code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getShort_name() {
                    return short_name;
                }

                public void setShort_name(String short_name) {
                    this.short_name = short_name;
                }

                public String getIcon_url() {
                    return icon_url;
                }

                public void setIcon_url(String icon_url) {
                    this.icon_url = icon_url;
                }

                public String getIcon_url_click() {
                    return icon_url_click;
                }

                public void setIcon_url_click(String icon_url_click) {
                    this.icon_url_click = icon_url_click;
                }

                public int getPosition() {
                    return position;
                }

                public void setPosition(int position) {
                    this.position = position;
                }

                @Override
                public String toString() {
                    return "SortType{" +
                            "code=" + code +
                            ", name='" + name + '\'' +
                            ", short_name='" + short_name + '\'' +
                            ", icon_url='" + icon_url + '\'' +
                            ", icon_url_click='" + icon_url_click + '\'' +
                            ", position=" + position +
                            '}';
                }
            }
            
            public static class ActivityFilter {

                private String  group_title;

                private int  support_multi_choice;

                private int  display_style;

                private List<Item> items;

                public  static final int SINGLECHOICE=0;
                public  static final int MULTICHOICE=1;

                public String getGroup_title() {
                    return group_title;
                }

                public void setGroup_title(String group_title) {
                    this.group_title = group_title;
                }

                public int getSupport_multi_choice() {
                    return support_multi_choice;
                }

                public void setSupport_multi_choice(int support_multi_choice) {
                    this.support_multi_choice = support_multi_choice;
                }

                public int getDisplay_style() {
                    return display_style;
                }

                public void setDisplay_style(int display_style) {
                    this.display_style = display_style;
                }

                public List<Item> getItems() {
                    return items;
                }

                public void setItems(List<Item> items) {
                    this.items = items;
                }

                @Override
                public String toString() {
                    return "ActivityFilter{" +
                            "group_title='" + group_title + '\'' +
                            ", support_multi_choice=" + support_multi_choice +
                            ", display_style=" + display_style +
                            ", items=" + items +
                            '}';
                }

                public static class Item {
                    private String  code;
                    private String  name;
                    private String  icon;
                    private String  remarks;
                    private boolean chcked;

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String icon) {
                        this.icon = icon;
                    }

                    public String getRemarks() {
                        return remarks;
                    }

                    public void setRemarks(String remarks) {
                        this.remarks = remarks;
                    }

                    public boolean isChcked() {
                        return chcked;
                    }

                    public void setChcked(boolean chcked) {
                        this.chcked = chcked;
                    }

                    @Override
                    public String toString() {
                        return "Item{" +
                                "code='" + code + '\'' +
                                ", name='" + name + '\'' +
                                ", icon='" + icon + '\'' +
                                ", remarks='" + remarks + '\'' +
                                ", chcked='" + chcked + '\'' +
                                '}';
                    }
                }
            

           
              

              
                    
                }
                
                
                
            
        }
        
    }

    public static class IovBean {
    }
}
