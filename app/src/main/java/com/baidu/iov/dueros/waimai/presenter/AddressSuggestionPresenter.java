package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;

import java.util.ArrayList;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_SELECT;

public class AddressSuggestionPresenter extends Presenter<AddressSuggestionPresenter.AddressSuggestionUi>  {


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

    public interface AddressSuggestionUi extends Ui {

        void selectListItem(int i);
    }
}
