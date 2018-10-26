package com.baidu.iov.dueros.waimai.presenter;
import android.content.Context;
import android.util.ArrayMap;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.BusinessModel;
import com.baidu.iov.dueros.waimai.model.IBusinessModel;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionsReq;
import com.baidu.iov.dueros.waimai.net.entity.request.PoilistReq;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionsResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;
import java.util.Map;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;

public class BusinessPresenter extends Presenter< BusinessPresenter.BusinessUi> {

    private static final String TAG = BusinessPresenter.class.getSimpleName();

    private IBusinessModel mBusinessModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (CMD_NO.equals(cmd) && null != getUi()) {
            getUi().close();
        }
    }

    @Override
    public void registerCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(CMD_NO);
            //mVoiceController.registerCmd(context, cmdList, mVoiceCallback);
            mVoiceManager.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mVoiceManager) {
            mVoiceManager.unregisterCmd(context, mVoiceCallback);
        }
    }

    public BusinessPresenter() {
        this.mBusinessModel = new BusinessModel();
    }

    @Override
    public void onUiReady(BusinessPresenter.BusinessUi ui) {
        super.onUiReady(ui);
        mBusinessModel.onReady();
    }

    public void requestFilterConditions(FilterConditionsReq filterConditionsReq) {
        mBusinessModel.requestFilterConditions(filterConditionsReq, new RequestCallback<FilterConditionsResponse>() {
            @Override
            public void onSuccess(FilterConditionsResponse data) {
                if ( getUi()!=null) {
                    getUi().onFilterConditionsSuccess(data);
                }
                Lg.getInstance().e(TAG,"msg:"+data);
            }

            @Override
            public void onFailure(String msg) {
                if ( getUi()!=null) {
                    getUi().onFilterConditionsError(msg);
                }
                Lg.getInstance().e(TAG,"msg:"+msg);
            }
        });
    }

    public void requestBusinessBean(PoilistReq poilistReq) {
        mBusinessModel.requestBusinessBean(poilistReq,new RequestCallback<Map<String,BusinessBean>>(){
            @Override
            public void onSuccess(Map<String,BusinessBean> data) {
                if ( getUi()!=null) {
                    getUi().onBusinessBeanSuccess(data);
                }
                Lg.getInstance().e(TAG,"msg:"+data);
            }

            @Override
            public void onFailure(String msg) {
                if ( getUi()!=null) {
                    getUi().onBusinessBeanError(msg);
                }
                Lg.getInstance().e(TAG,"msg:"+msg);
            }
        });
    }


    @Override
    public void onUiUnready(BusinessPresenter.BusinessUi ui) {
        super.onUiUnready(ui);
        mBusinessModel.onDestroy();
    }

    public interface BusinessUi extends Ui {
        void onBusinessBeanSuccess(Map<String,BusinessBean> data);
        void onBusinessBeanError(String error);
        void onFilterConditionsSuccess(FilterConditionsResponse data);
        void onFilterConditionsError(String error);
        void close();
    }
}
