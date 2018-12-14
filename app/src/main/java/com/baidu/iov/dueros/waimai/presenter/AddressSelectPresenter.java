package com.baidu.iov.dueros.waimai.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressSelectModel;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEndBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_SELECT;

public class AddressSelectPresenter extends Presenter<AddressSelectPresenter.AddressSelectUi> {
    private static final String TAG = AddressSelectPresenter.class.getSimpleName();

    private AddressSelectModel mAdressSelectModel;
    private List<AddressListBean.IovBean.DataBean> mDataBeans;
    private AddressListBean.IovBean.DataBean mDesBean;
    private MReceiver mReceiver;

    public AddressSelectPresenter() {
        this.mAdressSelectModel = new AddressSelectModel();
        mDesBean = new AddressListBean.IovBean.DataBean();
    }

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (getUi() == null) {
            return;
        }

        switch (cmd) {
            case CMD_SELECT:
                getUi().selectListItem(Integer.parseInt(extra));
                break;
            default:
                break;
        }
    }

    @Override
    public void registerCmd(Context context) {
        if (null != mVoiceManager) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(CMD_SELECT);
            mVoiceManager.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        if (null != mVoiceManager) {
            mVoiceManager.unregisterCmd(context, mVoiceCallback);
        }
    }

    @Override
    public void onUiReady(AddressSelectUi ui) {
        super.onUiReady(ui);
    }

    @Override
    public void onUiUnready(AddressSelectUi ui) {
        super.onUiUnready(ui);
    }

    public void requestData(AddressListReqBean reqBean) {

        mAdressSelectModel.requestAdressList(reqBean, new RequestCallback<AddressListBean>() {

            @Override
            public void onSuccess(AddressListBean data) {
                if (mDataBeans.contains(mDesBean)) {
                    mDataBeans.clear();
                    mDataBeans.add(mDesBean);
                } else {
                    mDataBeans.clear();
                }
                mDataBeans.addAll(data.getIov().getData());
                Lg.getInstance().d(TAG, "onSuccess:" + mDataBeans.toString());
                if (null != getUi()) {
                    getUi().onSuccess(mDataBeans);
                }
                String baiduName = null;
                String baiduPhone = null;
                for (int i = 0; i < mDataBeans.size(); i++) {
                    try {
                        AddressListBean.IovBean.DataBean dataBean = mDataBeans.get(i);
                        String user_phone = Encryption.desEncrypt(dataBean.getUser_phone());
                        String user_name = Encryption.desEncrypt(dataBean.getUser_name());
                        if (null != mDataBeans.get(i).getMt_address_id() &&
                                null == mDataBeans.get(i).getAddress_id()) {
                            if (MyApplicationAddressBean.USER_NAMES.contains(user_name)) {
                                MyApplicationAddressBean.USER_NAMES.remove(user_name);
                            }
                            if (MyApplicationAddressBean.USER_PHONES.contains(user_phone)) {
                                MyApplicationAddressBean.USER_PHONES.remove(user_phone);
                            }
                            //mt
                            MyApplicationAddressBean.USER_PHONES.add(0, user_phone);
                            MyApplicationAddressBean.USER_NAMES.add(0, user_name);
                        } else if (null == mDataBeans.get(i).getMt_address_id() &&
                                null != mDataBeans.get(i).getAddress_id()) {
                            //baidu
                            baiduPhone = user_phone;
                            baiduName = user_name;
                        } else {
                            if (MyApplicationAddressBean.USER_NAMES.contains(user_name)) {
                                MyApplicationAddressBean.USER_NAMES.remove(user_name);
                            }
                            if (MyApplicationAddressBean.USER_PHONES.contains(user_phone)) {
                                MyApplicationAddressBean.USER_PHONES.remove(user_phone);
                            }
                            //app
                            MyApplicationAddressBean.USER_PHONES.add(user_phone);
                            MyApplicationAddressBean.USER_NAMES.add(user_name);
                        }
//TODO set desBeanDada: username userphone
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //baidu>mt>app
                if (!TextUtils.isEmpty(baiduName) && !TextUtils.isEmpty(baiduPhone)) {
                    MyApplicationAddressBean.USER_NAMES.add(0, baiduName);
                    MyApplicationAddressBean.USER_PHONES.add(0, baiduPhone);
                }
                if (mDataBeans.contains(mDesBean)) {
                    mDataBeans.get(0).setUser_phone(MyApplicationAddressBean.USER_PHONES.get(0));
                    mDataBeans.get(0).setUser_name(MyApplicationAddressBean.USER_NAMES.get(0));
                }
            }

            @Override
            public void onFailure(String msg) {
                if (null != getUi()) {
                    getUi().onFailure(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Log.d(TAG, "getLogid: "+id);
            }
        });
    }

    public void initDesBeans() {
        mDataBeans = new ArrayList<>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.OPEN_API_BAIDU_MAP);
        intentFilter.addAction(Constant.OPEN_API_EXIT_NAVI);
        mReceiver = new MReceiver();
        if (null != getUi()) {
            getUi().onRegisterReceiver(mReceiver, intentFilter);
        }
    }


    public interface AddressSelectUi extends Ui {
        void onRegisterReceiver(MReceiver mReceiver, IntentFilter intentFilter);

        void onSuccess(List<AddressListBean.IovBean.DataBean> data);

        void onFailure(String msg);

        void selectListItem(int i);
    }

    public class MReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constant.OPEN_API_BAIDU_MAP) {
                String s = intent.getStringExtra("dest_json");
                Lg.getInstance().d(TAG, "onReceive open baidu map mDataBeans:" + mDataBeans);
                if (mDataBeans.contains(mDesBean)) {
                    mDataBeans.remove(mDesBean);
                }
                if (!TextUtils.isEmpty(s)) {
                    AddressEndBean addressEndBean = parseJSON(s);
                    mDesBean.setAddress(Encryption.encrypt(addressEndBean.getName()));
                    if (MyApplicationAddressBean.USER_PHONES.size() > 0
                            && MyApplicationAddressBean.USER_NAMES.size() > 0) {
                        mDesBean.setUser_phone(MyApplicationAddressBean.USER_PHONES.get(0));
                        mDesBean.setUser_name(MyApplicationAddressBean.USER_NAMES.get(0));
                    }
                    mDesBean.setLatitude((int) (Double.parseDouble(addressEndBean.getLat()) * LocationManager.SPAN));
                    mDesBean.setLongitude((int) (Double.parseDouble(addressEndBean.getLng()) * LocationManager.SPAN));
                    mDesBean.setType(context.getResources().getString(R.string.address_destination));
                    mDataBeans.add(0, mDesBean);
                }
                if (null != getUi()) {
                    getUi().onSuccess(mDataBeans);
                }
            } else if (intent.getAction() == Constant.OPEN_API_EXIT_NAVI) {
                Lg.getInstance().d(TAG, "onReceive exit navi mDataBeans:" + mDataBeans);
                removeDesBean();
            }
        }

    }

    private void removeDesBean() {
        if (mDataBeans.contains(mDesBean)) {
            mDataBeans.remove(mDesBean);
            if (null != getUi()) {
                getUi().onSuccess(mDataBeans);
            }
        }
    }

    private AddressEndBean parseJSON(String jsonData) {
        Gson gson = new Gson();
        AddressEndBean natiBean = gson.fromJson(jsonData,
                new TypeToken<AddressEndBean>() {
                }.getType());
        return natiBean;
    }
}
