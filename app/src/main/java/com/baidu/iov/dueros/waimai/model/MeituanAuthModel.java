package com.baidu.iov.dueros.waimai.model;


import com.baidu.iov.dueros.waimai.interfacedef.AccountCallback;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.GuidingReq;
import com.baidu.iov.dueros.waimai.net.entity.request.MeituanAuthorizeReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.GuideBean;
import com.baidu.iov.dueros.waimai.net.entity.response.MeituanAuthorizeResponse;
import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.Lg;

/**
 * Created by ubuntu on 18-10-27.
 */

public class MeituanAuthModel implements IMeituanAuthModel {
    private static final String TAG = MeituanAuthModel.class.getSimpleName();
    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }

    private void getGuidingData (){
        ApiUtils.getGuiding(new GuidingReq(), new ApiCallBack<GuideBean>() {
//            @Override
//            public void onSuccess(GuidingBean data) {
//                Lg.getInstance().d("GuidingAppear", GsonUtil.toJson(data.getList().getWaimai()));
//                if (data.getList().getWaimai() != null) {
//                    WaiMaiApplication.getInstance().setmWaimaiBean(data.getList().getWaimai());
//                    Lg.getInstance().d("GuidingAppear", data.getList().getWaimai().getAddress().getMe().get(0));
//                }
//            }

            @Override
            public void onSuccess(GuideBean data) {
                Lg.getInstance().d("GuidingAppear", data.getData().getGeneralList() + "");
                if (data.getData().getGeneralList().getTakeout() != null) {
                    WaiMaiApplication.getInstance().setmWaimaiBean(data.getData().getGeneralList().getTakeout());
                    Lg.getInstance().d("GuidingAppear", data.getData().getGeneralList().getTakeout().getTakeout_add().getHints().get(0).getHint());
                }
            }

            @Override
            public void onFailed(String msg) {
                Lg.getInstance().e("GuidingAppear", "Failed to get the guide. Error MSG: " + msg);
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d("GuidingAppear", id);
            }
        });
    }


    @Override
    public void requestMeituanAuth(MeituanAuthorizeReq meituanAuthReq, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getMeituanAuth(meituanAuthReq, new ApiCallBack<MeituanAuthorizeResponse>() {
            @Override
            public void onSuccess(MeituanAuthorizeResponse data) {
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
                    Lg.getInstance().d(TAG, "requestMeituanAuth getLogid: "+id);
                }
            }
        });

    }

    @Override
    public void requestAdressList(AddressListReqBean reqBean, final RequestCallback callback) {
        if (callback == null) {
            return;
        }

        ApiUtils.getAddressList(reqBean, new ApiCallBack<AddressListBean>() {
            @Override
            public void onSuccess(AddressListBean data) {
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
                    Lg.getInstance().d(TAG, "requestAdressList getLogid: "+id);
                }
            }
        });
    }
}
