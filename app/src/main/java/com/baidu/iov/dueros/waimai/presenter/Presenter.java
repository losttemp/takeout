

package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.os.Bundle;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;

import java.lang.ref.SoftReference;

/**
 * Base class for Presenters.
 */
public abstract class Presenter<U extends Ui> {

    private SoftReference<U> mUi;
    protected VoiceManager mVoiceManager;

    protected VoiceManager.CmdCallback mVoiceCallback;

    public abstract void onCommandCallback(String cmd, String extra);
    public abstract void registerCmd(Context context);
    public abstract void unregisterCmd(Context context);

    public Presenter() {
        if (null == mVoiceManager) {
            mVoiceManager = VoiceManager.getInstance();
        }
        if (null == mVoiceCallback) {
            mVoiceCallback = new VoiceManager.CmdCallback(){

                @Override
                public void onCmdCallback(String cmd, String extra) {
                    onCommandCallback(cmd, extra);
                }
            };
        }
    }
    /**
     *
     * @param ui The Ui implementation that is now ready to be used.
     */
    public void onUiReady(U ui) {
        mUi = new SoftReference<U>(ui);
    }


    public final void onUiDestroy(U ui) {
        onUiUnready(ui);
        if (mUi != null) {
            mUi.clear();
        }
    }

    public void onUiUnready(U ui) {
    }

    public void onSaveInstanceState(Bundle outState) {}

    public void onRestoreInstanceState(Bundle savedInstanceState) {}

    public U getUi() {
        return mUi.get();
    }
}
