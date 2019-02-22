

package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;
import android.os.Bundle;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;

import java.lang.ref.SoftReference;

/**
 * Base class for Presenters.
 */
public abstract class Presenter<U extends Ui> {

    private SoftReference<U> mUi;
    protected StandardCmdClient mStandardCmdClient;

    protected StandardCmdClient.CmdCallback mVoiceCallback;

    public abstract void onCommandCallback(String cmd, String extra);
    public abstract void registerCmd(Context context);
    public abstract void unregisterCmd(Context context);

    public Presenter() {
        if (null == mStandardCmdClient) {
            mStandardCmdClient = StandardCmdClient.getInstance();
        }
        if (null == mVoiceCallback) {
            mVoiceCallback = new StandardCmdClient.CmdCallback(){

                @Override
                public void onCmdCallback(String cmd, String extra) {
                    if (cmd.equals(StandardCmdClient.CMD_SELECT) && Integer.parseInt(extra) < 0)
                        return;
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
