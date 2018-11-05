package com.baidu.iov.dueros.waimai.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.presenter.Presenter;

/**
 * Parent for all fragments that use Presenters and Ui design.
 */
public abstract class BaseFragment<T extends Presenter<U>, U extends Ui> extends Fragment {

    private T mPresenter;

    abstract T createPresenter();

    abstract U getUi();

    protected BaseFragment() {
        mPresenter = createPresenter();
    }

    /**
     * Presenter will be available after onActivityCreated().
     *
     * @return The presenter associated with this fragment.
     */
    public T getPresenter() {
        return mPresenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onUiReady(getUi());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().registerCmd(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().unregisterCmd(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onUiDestroy(getUi());
    }
}
