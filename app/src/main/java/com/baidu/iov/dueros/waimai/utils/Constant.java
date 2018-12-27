
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

    public static String UUID = "uuid";

    public static int  EVENT_EXIT= 31300078;
    public static int  EVENT_BACK= 31300077;
    
    public static int  EVENT_FOOD_CLICK= 31300039;
    public static int  EVENT_FLOWER_CLICK= 31300040;
    public static int  EVENT_CAKE_CLICK= 31300041;
    public static int  EVENT_HISTORY_ITEM_CLICK= 31300038;
    public static int  EVENT_HISTORY_ITEM_VOIVE= 31300037;
    
    public static int  EVENT_INPUT_SEARCH= 31300036;
    public static int  EVENT_OPEN_ORDER_LIST= 31300044;
    public static int  EVENT_OPEN_ADDRESS_SELECT= 31300045;
    public static int  EVENT_OPEN_SEARCH_FROM_HOME= 31300046;
    public static int  EVENT_OPEN_SEARCH_FROM_FLOWER= 31300079;
    public static int  EVENT_OPEN_SEARCH_FROM_CAKE= 31300087;
    
    public static int  EVENT_NEXT_PAGE_VOICE_FROM_HOME= 31300051;
    public static int  EVENT_NEXT_PAGE_VOICE_FROM_FLOWER= 31300084;
    public static int  EVENT_NEXT_PAGE_VOICE_FROM_CAKE= 31300092;

    public static int  EVENT_SELECTE_STORE_CLICK_FROM_HOME= 31300043;
    public static int  EVENT_SELECTE_STORE_CLICK_FROM_FLOWER= 31300085;
    public static int  EVENT_SELECTE_STORE_CLICK_FROM_CAKE= 31300095;

    public static int  EVENT_SELECTE_STORE_VOICE_FROM_HOME= 31300042;
    public static int  EVENT_SELECTE_STORE_VOICE_FROM_FLOWER= 31300084;
    public static int  EVENT_SELECTE_STORE_VOICE_FROM_CAKE= 31300094;

    public static int  EVENT_CLICK_FILTER_FROM_HOME= 31300050;
    public static int  EVENT_CLICK_FILTER_FROM_FLOWER= 31300083;
    public static int  EVENT_CLICK_FILTER_FROM_CAKE= 31300091;

    public static int  EVENT_CLICK_SORT_FROM_HOME= 31300049;
    public static int  EVENT_CLICK_SORT_FROM_FLOWER= 31300080;
    public static int  EVENT_CLICK_SORT_FROM_CAKE= 31300088;

    public static int  EVENT_CLICK_SALES_FROM_HOME= 31300047;
    public static int  EVENT_CLICK_SALES_FROM_FLOWER= 31300081;
    public static int  EVENT_CLICK_SALES_FROM_CAKE= 31300089;

    public static int  EVENT_CLICK_DISTANCE_FROM_HOME= 31300047;
    public static int  EVENT_CLICK_DISTANCE_FROM_FLOWER= 31300082;
    public static int  EVENT_CLICK_DISTANCE_FROM_CAKE= 31300048;



}