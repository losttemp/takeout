
package com.baidu.iov.dueros.waimai.utils;

public class Constant {

    public static final boolean LEAKCANARY_DEBUG = true;
    public static final String AREA_ID = "areaId";
    public static final String AOI_ID = "aoiId";
    public static final String BRAND_ID = "brandId";
    public static final String FEATURE_ID = "featureId";
    public static final String EXTRA_ID = "extra";
    public static final int SPECIAL_HALL_ID = 5;

    public static final int CINEMA = 1024;

    public static String GPS = "GPS";
    public static Integer LATITUDE = -1;
    public static Integer LONGITUDE = -1;
    public static String STREET = "street";
    public static String STREET_NUMBER = "street_number";
    public static String LBS_DISTRICT = "district";
    public static String CITY = "city";
    public static String CITYCODE = "citycode";
    public static String PROVINCE = "province";
    public static String COUNTRY = "country";
    public static String DETAIL = "detail";
    public static String OPERATORS = "Operators";

    public static final String STORE_FRAGMENT_FROM_PAGE_TYPE = "page_type";
    public static final int STORE_FRAGMENT_FROM_HOME = 0;
    public static final int STORE_FRAGMENT_FROM_SEARCH = 1;
    public static final int STORE_FRAGMENT_FROM_FLOWER = 2;
    public static final int STORE_FRAGMENT_FROM_CAKE = 3;
    public static final int STORE_FRAGMENT_FROM_FOOD = 4;

    public static final String STORE_ID = "store_id";
    public static final int STROE_STATUS_NORMAL = 1;
    public static final int STROE_STATUS_BUSY = 2;
    public static final int STROE_STATUS_BREAK = 3;
    public static final String ORDER_ID = "order_id";
    public static final String USER_PHONE = "user_phone";
    public static String ACCOUNT_LOGIN_SUCCESS = "login_success";
    public static String ACCOUNT_LOGIN_FAIL = "login_fail";
    public static String ACCOUNT_AUTH_SUCCESS = "auth_success";
    public static String ACCOUNT_AUTH_FAIL = "auth_fail";
    public static String ACCOUNT_PAID_SUCCESS = "paid_success";
    public static String ACCOUNT_PAID_FAIL = "paid_fail";
    public static final int SEARCH_STATUS_HISTORY = 0;
    public static final int SEARCH_STATUS_SUGGEST = 1;
    public static final int SEARCH_STATUS_FRAGMENT = 2;
    public static final int COMPREHENSIVE = 0;
    public static final int SALES = 1;
    public static final int DISTANCE = 5;
    public static String  PULL_LOCATION = "com.baidu.iov.dueros.waimai.pulllocation";


    public static final int ADDRESS_SEARCH_ACTIVITY_RESULT_CODE = 1;
    public static String ADDRESS_EDIT_INTENT_EXTRE_CITY = "address_edit_location_city";
    public static String ADDRESS_SEARCCH_INTENT_EXTRE_ADDSTR = "address_search_addStr";
    public static String ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT = "address_add_or_edit_model";
    public static String ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS = "address_select_address_edit";
    public static String ADDRESS_DATA = "address_data";
    public static String ADDRESS_SELECTED_TIME = "address_time";
    public static String ADDRESS_SELECTED = "ADDRESS_SELECTED";
    public static String OPEN_API_EXIT_NAVI = "com.baidu.naviauto.open.api.exitnavi";
    public static String OPEN_API_BAIDU_MAP = "com.baidu.naviauto.open.api";
    public static String WM_POI_ID = "wm_poi_id";

    public static final int CITY_REQUEST_CODE_CHOOSE = 506;
    public static final int CITY_RESULT_CODE_CHOOSE = 606;

    public static String ONE_MORE_ORDER = "one_more_order";
    public static String ORDER_LSIT_EXTRA_STRING = "order_list_extra_string";
    public static String ORDER_LSIT_BEAN = "order_lsit_bean";

    public final static String TOTAL_COST = "total_cost";
    public final static String SHOP_NAME = "shop_name";
    public final static String PAY_URL = "pay_url";
    public final static String PIC_URL = "pic_url";
    public final static String EXPECTED_TIME = "expected_time";

    public final static String USER_NAME = "user_name";
    public final static String USER_ADDRESS = "user_address";
    public final static String STORE_NAME = "store_name";
    public final static String PRODUCT_COUNT = "product_count";
    public final static String PRODUCT_NAME = "product_name";
    public final static String TO_SHOW_SHOP_CART = "show_shop_card";
    public final static int PAY_STATUS_SUCCESS = 3;

    public final static int SELECT_DELIVERY_ADDRESS = 100;
    public final static int ORDER_PREVIEW_SUCCESS = 0;
    public final static int SUBMIT_ORDER_SUCCESS = 0;
    public final static int STORE_CANT_NOT_BUY = 2;
    public final static int FOOD_CANT_NOT_BUY = 3;
    public final static int FOOD_COST_NOT_BUY = 5;
    public final static int FOOD_COUNT_NOT_BUY = 15;
    public final static int FOOD_LACK_NOT_BUY = 20;
    public final static int BEYOND_DELIVERY_RANGE = 9;
    public final static int SERVICE_ERROR = 26;

    public static final String IS_NEED_VOICE_FEEDBACK = "isNeedVoiceFeedback";
    public static final String START_APP = "start_app";
    public static final int START_APP_CODE = 0x123;
    public static final String GET_CITY_ERROR = "失败";
    public static String UUID = "uuid";

    public final static int  EVENT_EXIT= 31300078;
    public final static int  EVENT_BACK= 31300077;

    public final static int  EVENT_FOOD_CLICK= 31300039;
    public final static int  EVENT_FLOWER_CLICK= 31300040;
    public final static int  EVENT_CAKE_CLICK= 31300041;
    public final static int  EVENT_HISTORY_ITEM_CLICK= 31300038;
    public final static int  EVENT_HISTORY_ITEM_VOIVE= 31300037;

    public final static int  EVENT_INPUT_SEARCH= 31300036;
    public final static int  EVENT_OPEN_ORDER_LIST= 31300044;
    public final static int  EVENT_OPEN_ADDRESS_SELECT= 31300045;
    public final static int  EVENT_OPEN_SEARCH_FROM_HOME= 31300046;
    public final static int  EVENT_OPEN_SEARCH_FROM_FLOWER= 31300079;
    public final static int  EVENT_OPEN_SEARCH_FROM_CAKE= 31300087;

    public final static int  EVENT_NEXT_PAGE_VOICE_FROM_HOME= 31300051;
    public final static int  EVENT_NEXT_PAGE_VOICE_FROM_FLOWER= 31300084;
    public final static int  EVENT_NEXT_PAGE_VOICE_FROM_CAKE= 31300092;

    public final static int  EVENT_SELECTE_STORE_CLICK_FROM_HOME= 31300043;
    public final static int  EVENT_SELECTE_STORE_CLICK_FROM_FLOWER= 31300085;
    public final static int  EVENT_SELECTE_STORE_CLICK_FROM_CAKE= 31300095;

    public final static int  EVENT_SELECTE_STORE_VOICE_FROM_HOME= 31300042;
    public final static int  EVENT_SELECTE_STORE_VOICE_FROM_FLOWER= 31300084;
    public final static int  EVENT_SELECTE_STORE_VOICE_FROM_CAKE= 31300094;

    public final static int  EVENT_CLICK_FILTER_FROM_HOME= 31300050;
    public final static int  EVENT_CLICK_FILTER_FROM_FLOWER= 31300083;
    public final static int  EVENT_CLICK_FILTER_FROM_CAKE= 31300091;

    public final static int  EVENT_CLICK_SORT_FROM_HOME= 31300049;
    public final static int  EVENT_CLICK_SORT_FROM_FLOWER= 31300080;
    public final static int  EVENT_CLICK_SORT_FROM_CAKE= 31300088;

    public final static int  EVENT_CLICK_SALES_FROM_HOME= 31300047;
    public final static int  EVENT_CLICK_SALES_FROM_FLOWER= 31300081;
    public final static int  EVENT_CLICK_SALES_FROM_CAKE= 31300089;

    public final static int  EVENT_CLICK_DISTANCE_FROM_HOME= 31300048;
    public final static int  EVENT_CLICK_DISTANCE_FROM_FLOWER= 31300082;
    public final static int  EVENT_CLICK_DISTANCE_FROM_CAKE= 31300090;




    //address
    public final static int ENTRY_LOGIN_OS = 31300016;
    public final static int ENTRY_LOGIN_MEITUAN = 31300017;
    public final static int ENTRY_ADDRESS_NEWACT_SAVE = 31300018;
    public final static int ENTRY_ADDRESS_NEWACT_START_POI = 31300019;
    public final static int ENTRY_ADDRESS_NEWACT_EDIT_DATA = 31300020;
    public final static int ENTRY_ADDRESS_EDITACT_SAVE = 31300029;
    public final static int ENTRY_ADDRESS_EDITACT_DELETE = 31300030;
    public final static int ENTRY_ADDRESS_EDITACT_START_POI = 31300031;
    public final static int ENTRY_ADDRESS_EDITACT_EDIT_DATA = 31300032;
    public final static int ENTRY_ADDRESS_LIST_SELECT_MUDIDI = 31300021;
    public final static int ENTRY_ADDRESS_LIST_SELECT_FIRM = 31300022;
    public final static int ENTRY_ADDRESS_LIST_SELECT_HOME = 31300023;
    public final static int ENTRY_ADDRESS_LIST_SELECT_OTHER = 31300024;
    public final static int ENTRY_ADDRESS_LIST_START_EDIT = 31300026;
    public final static int ENTRY_ADDRESS_LIST_START_ADD_NEW = 31300027;
    public final static int ENTRY_ADDRESS_POIACT_EDIT = 31300033;
    public final static int ENTRY_ADDRESS_POIACT_SELECT = 31300035;

    public static final int POIFOODLIST_CLICK_ON_THE_TYPE_OF_GOODS = 31300052;
    public static final int POIFOODLIST_ADD_GOODS_BY_VOICE = 31300053;
    public static final int POIFOODLIST_CLICK_ON_INCREASE_OR_DECREASE = 31300054;
    public static final int POIFOODLIST_SEE_NOTICE = 31300055;
    public static final int POIFOODLIST_VOICE_PAGING = 31300056;
    public static final int POIFOODLIST_CONFIRM_THE_ORDER = 31300057;
    public static final int POIFOODLIST_CHECK_THE_SHOPPING_CART = 31300058;
    public static final int POIFOODLIST_EMPTY_CART = 31300059;
    public static final int POIFOODLIST_CLOSE_THE_SHOPPING_CART = 31300060;
    public static final int POIFOODLIST_SPECIFICATION_CLICK = 31300061;
    public static final int POIFOODLIST_FINISH = 31300077;

    public final static int ORDERSUBMIT_TOPAY=31300062;
    public final static int ORDERSUBMIT_TIME_DIALOG=31300063;
    public final static int ORDERSUBMIT_ADDRESS_DIALOG=31300064;
    public final static int ORDERSUBMIT_CHANGE_ADDRESS=31300065;
    public final static int ORDERSUBMIT_UPDATE_ADDRESS=31300066;
    public final static int ORDERSUBMIT_ALERT_ADDRESS=31300067;
    public final static int ORDERSUBMIT_CHANGE_ADDRESS_VOICE=31300068;
    public final static int ORDERSUBMIT_CHANGE_TIME=31300069;
    public final static int PAYSUCCESS_TO_ORDERDETAIL=31300070;
    public final static int CALL_FOR_CANCLE_ORDER=31300072;
    public final static int ORDERLIST_TO_REPEAT_VOCIE=31300073;
    public final static int ORDERLIST_TO_ORDERDETAIL=31300074;
    public final static int ORDERLIST_TO_ORDERDETAIL_VOICE=31300075;
    public final static int ORDERLIST_REFRESH_VOICE=31300076;
    public final static int GOBACK_TO_PREACTIVITY=31300077;

}