

package com.baidu.iov.dueros.waimai.presenter;

import android.os.Bundle;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;

import java.lang.ref.WeakReference;

/**
 * Base class for Presenters.
 */
public abstract class Presenter<U extends Ui> {

    private WeakReference<U> mUi;

    /**
     *
     * @param ui The Ui implementation that is now ready to be used.
     */
    public void onUiReady(U ui) {
        mUi = new WeakReference<U>(ui);
    }


    public final void onUiDestroy(U ui) {
        onUiUnready(ui);
        if (mUi != null) {
            mUi.clear();
            mUi = null;
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
