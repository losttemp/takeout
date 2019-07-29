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
import com.baidu.iov.dueros.waimai.model.AddressSelectModel;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
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

import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_SELECT;
import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_NEXT;
import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_PRE;

public class AddressSelectPresenter extends Presenter<AddressSelectPresenter.AddressSelectUi> {
    private static final String TAG = AddressSelectPresenter.class.getSimpleName();

    private AddressSelectModel mAdressSelectModel;
    private List<AddressListBean.IovBean.DataBean> mDataBeans;
    private AddressListBean.IovBean.DataBean mDesBean;
    private MReceiver mReceiver;
    private boolean isOnSuccess;

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
        if (null != mStandardCmdClient) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(CMD_SELECT);
            cmdList.add(CMD_NEXT);
            cmdList.add(CMD_PRE);
            mStandardCmdClient.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        if (null != mStandardCmdClient) {
            mStandardCmdClient.unregisterCmd(context, mVoiceCallback);
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
        isOnSuccess = false;
        mAdressSelectModel.requestAdressList(reqBean, new RequestCallback<AddressListBean>() {

            @Override
            public void onSuccess(AddressListBean data) {
                MyApplicationAddressBean.USER_NAMES.clear();
                MyApplicationAddressBean.USER_PHONES.clear();
                if (mDataBeans.contains(mDesBean)) {
                    mDataBeans.clear();
                    mDataBeans.add(mDesBean);
                } else {
                    mDataBeans.clear();
                }
                mDataBeans.addAll(data.getIov().getData());
                StringBuilder baiduName = new StringBuilder();
                StringBuilder baiduPhone = new StringBuilder();
                AddressListBean.IovBean.DataBean gs = null;
                AddressListBean.IovBean.DataBean jl = null;
                for (int i = 0; i < mDataBeans.size(); i++) {
                    try {
                        AddressListBean.IovBean.DataBean dataInfo = mDataBeans.get(i);
                        if (!TextUtils.isEmpty(dataInfo.getUser_name())) {
                            String user_name = Encryption.desEncrypt(dataInfo.getUser_name());
                            if (TextUtils.isEmpty(user_name)) break;
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
                            if (TextUtils.isEmpty(user_phone)) break;
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
                        if (dataInfo.isIs_hint()) {
                            mDataBeans.remove(i);
                            i--;
                        }
                        if (i >= 0 && !TextUtils.isEmpty(mDataBeans.get(i).getType())) {
                            if ("家里".equals(mDataBeans.get(i).getType())) {
                                jl = mDataBeans.get(i);
                                mDataBeans.remove(i);
                                i--;
                            }
                            if (i >= 0 && !TextUtils.isEmpty(mDataBeans.get(i).getType())) {
                                if ("公司".equals(mDataBeans.get(i).getType())) {
                                    gs = mDataBeans.get(i);
                                    mDataBeans.remove(i);
                                    i--;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(data.getIov().getUser_phone())) {
                    try {
                        String personalPhone = Encryption.desEncrypt(data.getIov().getUser_phone());
                        if (MyApplicationAddressBean.USER_PHONES.contains(personalPhone)) {
                            MyApplicationAddressBean.USER_PHONES.remove(personalPhone);
                        }
                        MyApplicationAddressBean.USER_PHONES.add(0, personalPhone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mDataBeans.contains(mDesBean)) {
                    if (MyApplicationAddressBean.USER_PHONES.size() > 0) {
                        mDataBeans.get(0).setUser_phone(Encryption.encrypt(MyApplicationAddressBean.USER_PHONES.get(0)));
                    }
                    if (MyApplicationAddressBean.USER_NAMES.size() > 0) {
                        mDataBeans.get(0).setUser_name(Encryption.encrypt(MyApplicationAddressBean.USER_NAMES.get(0)));
                    }
                }
                if (jl != null) {
                    if (mDataBeans.contains(mDesBean)) {
                        mDataBeans.add(1, jl);
                    } else {
                        mDataBeans.add(0, jl);
                    }
                }
                if (gs != null) {
                    if (mDataBeans.contains(mDesBean)) {
                        mDataBeans.add(1, gs);
                    } else {
                        mDataBeans.add(0, gs);
                    }
                }
                if (null != getUi()) {
                    isOnSuccess = true;
                    getUi().onSuccess(mDataBeans);
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
                Lg.getInstance().d(TAG, "getLogid: " + id);
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
                    if (MyApplicationAddressBean.USER_PHONES.size() > 0
                            && MyApplicationAddressBean.USER_NAMES.size() > 0) {
                        mDesBean.setUser_phone(MyApplicationAddressBean.USER_PHONES.get(0));
                        mDesBean.setUser_name(MyApplicationAddressBean.USER_NAMES.get(0));
                    }
                    mDesBean.setLatitude((int) (Double.parseDouble(addressEndBean.getLat()) * LocationManager.SPAN));
                    mDesBean.setLongitude((int) (Double.parseDouble(addressEndBean.getLng()) * LocationManager.SPAN));
                    mDesBean.setType(context.getResources().getString(R.string.address_destination));
                    mDesBean.setCanShipping(1);
                    mDataBeans.add(0, mDesBean);
                }
                if (null != getUi() && mDataBeans.size() > 0 && isOnSuccess) {
                    isOnSuccess = false;
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
