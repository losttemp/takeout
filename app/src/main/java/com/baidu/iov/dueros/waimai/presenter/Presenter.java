

package com.baidu.iov.dueros.waimai.presenter;

import android.os.Bundle;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import java.lang.ref.SoftReference;

/**
 * Base class for Presenters.
 */
public abstract class Presenter<U extends Ui> {

    private SoftReference<U> mUi;

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
