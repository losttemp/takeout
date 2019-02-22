package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.AddressListImpl;
import com.baidu.iov.dueros.waimai.model.IAddressList;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressListPresenter extends Presenter<AddressListPresenter.AddressListUi> {
    private static final String TAG = AddressListPresenter.class.getSimpleName();

    private IAddressList mAddressList;

    public AddressListPresenter() {
        mAddressList = new AddressListImpl();
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
                if (null != getUi()) {
                    if (data != null && data.getIov().getData().size() > 0) {
                        for (int i = 0; i < data.getIov().getData().size(); i++) {
                            if (data.getIov().getData().get(i).isIs_hint()){
                                data.getIov().getData().remove(i);
                                i--;
                            }
                        }
                        setAutocompleteData(data);
                        getUi().onGetAddressListSuccess(data);
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

        void onGetAddressListSuccess(AddressListBean data);

        void onGetAddressListFailure(String msg);

        void selectListItem(int i);
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
