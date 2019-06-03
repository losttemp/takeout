package com.baidu.iov.dueros.waimai.model;

import android.util.ArrayMap;
import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;
import com.baidu.iov.dueros.waimai.net.entity.request.ArriveTimeReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderOwnerReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.PoifoodListReq;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderOwnerBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.Map;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListModel implements IPoifoodListModel {
    private static final String TAG = PoifoodListModel.class.getSimpleName();

    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestPoifoodList(ArrayMap<String, String> params, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        if (params != null) {
            Lg.getInstance().d(TAG, "areaId:" + params.get(Constant.AREA_ID) + " aoiId:" + params.get(Constant.AOI_ID)
                    + " brandId:" + params.get(Constant.BRAND_ID));
        }
        PoifoodListReq poifoodListReq = new PoifoodListReq();

        poifoodListReq.setLongitude(Integer.valueOf(params.get("longitude")));
        poifoodListReq.setLatitude(Integer.valueOf(params.get("latitude")));
        poifoodListReq.setWm_poi_id(Long.parseLong(params.get(Constant.STORE_ID)));
        ApiUtils.getPoifoodList(poifoodListReq, new ApiCallBack<PoifoodListBean>() {
            @Override
            public void onSuccess(PoifoodListBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "requestPoifoodList getLogid: "+id);
                }
            }
        });
    }

    @Override
    public void requestPoidetailinfo(ArrayMap<String, String> params, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        if (params != null) {
            Lg.getInstance().d(TAG, "areaId:" + params.get(Constant.AREA_ID) + " aoiId:" + params.get(Constant.AOI_ID)
                    + " brandId:" + params.get(Constant.BRAND_ID));
        }
        PoifoodListReq poidetailinfoReq = new PoifoodListReq();
        poidetailinfoReq.setLongitude(Integer.valueOf(params.get("longitude")));
        poidetailinfoReq.setLatitude(Integer.valueOf(params.get("latitude")));
        poidetailinfoReq.setWm_poi_id(Long.parseLong(params.get(Constant.STORE_ID)));
        Lg.getInstance().d(TAG, "wm_poi_id = " + params.get(Constant.STORE_ID));
        ApiUtils.getPoidetailinfo(poidetailinfoReq, new ApiCallBack<PoidetailinfoBean>() {
            @Override
            public void onSuccess(PoidetailinfoBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "requestPoidetailinfo getLogid: "+id);
                }
            }
        });
    }

    @Override
    public void requestOrderPreview(OrderPreviewReqBean orderPreviewReqBean, final RequestCallback<OrderPreviewBean> callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getOrderPreview(orderPreviewReqBean, new ApiCallBack<OrderPreviewBean>() {
            @Override
            public void onSuccess(OrderPreviewBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "requestOrderPreview getLogid: "+id);
                }
            }
        });
    }


    @Override
    public void requestArriveTimeList(ArriveTimeReqBean arriveTimeReqBean, final RequestCallback<ArriveTimeBean> callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getArriveTimeList(arriveTimeReqBean, new ApiCallBack<ArriveTimeBean>() {
            @Override
            public void onSuccess(ArriveTimeBean data) {
                callback.onSuccess(data);

            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "requestArriveTimeList getLogid: "+id);
                }
            }
        });
    }

    @Override
    public void requesLogout(final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.logout(new RequestBase(), new ApiCallBack<LogoutBean>() {
            @Override
            public void onSuccess(LogoutBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "requesLogout getLogid: "+id);
                }
            }
        });
    }

    @Override
    public void requestCheckOrderOwner(OrderOwnerReq OrderOwnerReqBean, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getCheckOrderOwner(OrderOwnerReqBean, new ApiCallBack<OrderOwnerBean>() {
            @Override
            public void onSuccess(OrderOwnerBean data) {
                callback.onSuccess(data);

            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }

            @Override
            public void getLogid(String id) {
                if (callback!=null) {
                    callback.getLogid(id);
                    Lg.getInstance().d(TAG, "requestCheckOrderOwner getLogid: "+id);
                }
            }
        });
    }
}
