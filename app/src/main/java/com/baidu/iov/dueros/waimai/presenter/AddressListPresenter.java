package com.baidu.iov.dueros.waimai.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressListImpl;
import com.baidu.iov.dueros.waimai.model.IAddressList;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEndBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_NEXT;
import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_PRE;

public class AddressListPresenter extends Presenter<AddressListPresenter.AddressListUi> {
    private static final String TAG = AddressListPresenter.class.getSimpleName();

    private IAddressList mAddressList;
    private List<AddressListBean.IovBean.DataBean> mDataBeans;
    private AddressListBean.IovBean.DataBean mDesBean;
    private boolean isOnSuccess;
    private MReceiver mReceiver;

    public AddressListPresenter() {
        mAddressList = new AddressListImpl();
        mDesBean = new AddressListBean.IovBean.DataBean();
    }

    @Override
    public void onUiUnready(AddressListUi ui) {
        super.onUiUnready(ui);
        mAddressList.onReady();
    }

    @Override
    public void onUiReady(AddressListUi ui) {
        super.onUiReady(ui);
        mAddressList.onDestroy();
    }

    public void requestData(long wm_poi_id) {
        mAddressList.requestAddressList(wm_poi_id, new RequestCallback<AddressListBean>() {

            @Override
            public void onSuccess(AddressListBean data) {
                if (mDataBeans.contains(mDesBean)) {
                    mDataBeans.clear();
                    mDataBeans.add(mDesBean);
                } else {
                    mDataBeans.clear();
                }
                mDataBeans.addAll(data.getIov().getData());
                if (null != getUi()) {
                    if (data != null && data.getIov().getData().size() > 0) {
                        for (int i = 0; i < data.getIov().getData().size(); i++) {
                            if (data.getIov().getData().get(i).isIs_hint()){
                                data.getIov().getData().remove(i);
                                i--;
                            }
                        }
                        setAutocompleteData(data);
                        isOnSuccess = true;
                        getUi().onGetAddressListSuccess(mDataBeans);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()) {
                    getUi().onGetAddressListFailure(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "getLogid: " + id);
            }
        });

    }

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (getUi() == null) {
            return;
        }

        switch (cmd) {
            case StandardCmdClient.CMD_SELECT:
                getUi().selectListItem(Integer.parseInt(extra));
                break;
            case StandardCmdClient.CMD_NEXT:
                getUi().nextPage(true);
                break;
            case StandardCmdClient.CMD_PRE:
                getUi().nextPage(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void registerCmd(Context context) {
        Lg.getInstance().d(TAG, "registerCmd");
        if (null != mStandardCmdClient) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(StandardCmdClient.CMD_SELECT);
            cmdList.add(CMD_NEXT);
            cmdList.add(CMD_PRE);
            mStandardCmdClient.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        Lg.getInstance().d(TAG, "unregisterCmd");
        if (null != mStandardCmdClient) {
            mStandardCmdClient.unregisterCmd(context, mVoiceCallback);
        }
    }

    public interface AddressListUi extends Ui {

        void onRegisterReceiver(BroadcastReceiver mReceiver, IntentFilter intentFilter);

        void onGetAddressListSuccess(List<AddressListBean.IovBean.DataBean> data);

        void onGetAddressListFailure(String msg);

        void selectListItem(int i);

        void nextPage(boolean isNextPage);
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
                    if (MyApplicationAddressBean.USER_PHONES.size() > 0) {
                        mDesBean.setUser_phone(Encryption.encrypt(MyApplicationAddressBean.USER_PHONES.get(0)));
                    }
                    if (MyApplicationAddressBean.USER_NAMES.size() > 0) {
                        mDesBean.setUser_name(Encryption.encrypt(MyApplicationAddressBean.USER_NAMES.get(0)));
                    }
                    mDesBean.setLatitude((int) (Double.parseDouble(addressEndBean.getLat()) * LocationManager.SPAN));
                    mDesBean.setLongitude((int) (Double.parseDouble(addressEndBean.getLng()) * LocationManager.SPAN));
                    mDesBean.setType(context.getResources().getString(R.string.address_destination));
                    mDesBean.setCanShipping(1);
                    mDataBeans.add(0, mDesBean);
                }
                if (mDataBeans != null && null != getUi() && mDataBeans.size() > 0 && isOnSuccess) {
                    getUi().onGetAddressListSuccess(mDataBeans);
                }
            } else if (intent.getAction() == Constant.OPEN_API_EXIT_NAVI) {
//                Lg.getInstance().d(TAG, "onReceive exit navi mDataBeans:" + mDataBeans);
                removeDesBean();
            }
        }

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

    private void removeDesBean() {
        if (mDataBeans.contains(mDesBean)) {
            mDataBeans.remove(mDesBean);
            if (null != getUi()) {
                getUi().onGetAddressListSuccess(mDataBeans);
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

    private void setAutocompleteData(AddressListBean data){
        List<AddressListBean.IovBean.DataBean>  mDataListBean = data.getIov().getData();
        MyApplicationAddressBean.USER_NAMES.clear();
        MyApplicationAddressBean.USER_PHONES.clear();
        StringBuilder baiduName = new StringBuilder();
        StringBuilder baiduPhone = new StringBuilder();
        for (int i = 0; i < mDataListBean.size(); i++) {
            try {
                AddressListBean.IovBean.DataBean dataInfo = mDataListBean.get(i);
                if (!TextUtils.isEmpty(dataInfo.getUser_name())) {
                    String user_name = Encryption.desEncrypt(dataInfo.getUser_name());
                    if (TextUtils.isEmpty(user_name))break;
                    if (null != dataInfo.getMt_address_id() &&
                            null == dataInfo.getAddress_id()) {
                        if (MyApplicationAddressBean.USER_NAMES.contains(user_name)) {
                            MyApplicationAddressBean.USER_NAMES.remove(user_name);
                        }
                        //mt
                        MyApplicationAddressBean.USER_NAMES.add(0, user_name);
                    } else if (null == dataInfo.getMt_address_id() &&
                            null != dataInfo.getAddress_id()) {
                        //baidu
                        baiduName.append(user_name);
                    } else if (null == dataInfo.getMt_address_id() &&
                            null == dataInfo.getAddress_id()) {
                        //baidu
                        baiduName.append(user_name);
                    } else {
                        if (!MyApplicationAddressBean.USER_NAMES.contains(user_name)) {
                            //app
                            MyApplicationAddressBean.USER_NAMES.add(user_name);
                        }
                    }
                }
                if (!TextUtils.isEmpty(dataInfo.getUser_phone())) {
                    String user_phone = Encryption.desEncrypt(dataInfo.getUser_phone());
                    if (TextUtils.isEmpty(user_phone))break;
                    if (!user_phone.contains("*")) {
                        if (null != dataInfo.getMt_address_id() &&
                                null == dataInfo.getAddress_id()) {
                            if (MyApplicationAddressBean.USER_PHONES.contains(user_phone)) {
                                MyApplicationAddressBean.USER_PHONES.remove(user_phone);
                            }
                            //mt
                            MyApplicationAddressBean.USER_PHONES.add(0, user_phone);
                        } else if (null == dataInfo.getMt_address_id() &&
                                null != dataInfo.getAddress_id()) {
                            //baidu
                            baiduPhone.append(user_phone);
                        } else if (null == dataInfo.getMt_address_id() &&
                                null == dataInfo.getAddress_id()) {
                            //baidu
                            baiduPhone.append(user_phone);
                        } else {
                            if (!MyApplicationAddressBean.USER_PHONES.contains(user_phone)) {
                                //app
                                MyApplicationAddressBean.USER_PHONES.add(user_phone);
                            }

                        }
                    }
                }
                if (!TextUtils.isEmpty(baiduName)) {
                    if (MyApplicationAddressBean.USER_NAMES.contains(baiduName.toString())) {
                        MyApplicationAddressBean.USER_NAMES.remove(baiduName.toString());
                    }
                    MyApplicationAddressBean.USER_NAMES.add(0, baiduName.toString());
                }
                if (!TextUtils.isEmpty(baiduPhone)) {
                    if (MyApplicationAddressBean.USER_PHONES.contains(baiduPhone.toString())) {
                        MyApplicationAddressBean.USER_PHONES.remove(baiduPhone.toString());
                    }
                    MyApplicationAddressBean.USER_PHONES.add(0, baiduPhone.toString());
                }
                baiduName.delete(0, baiduName.length());
                baiduPhone.delete(0, baiduPhone.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(data.getIov().getUser_phone())) {
            try {
                String personalPhone = Encryption.desEncrypt(data.getIov().getUser_phone());
                if (StringUtils.isChinaPhoneLegal(personalPhone)) {
                    if (MyApplicationAddressBean.USER_PHONES.contains(personalPhone)) {
                        MyApplicationAddressBean.USER_PHONES.remove(personalPhone);
                    }
                    MyApplicationAddressBean.USER_PHONES.add(0, personalPhone);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
