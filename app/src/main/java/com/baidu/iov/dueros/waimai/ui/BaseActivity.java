package com.baidu.iov.dueros.waimai.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.presenter.Presenter;

public abstract class BaseActivity<T extends Presenter<U>, U extends Ui> extends AppCompatActivity {

    private T mPresenter;

    abstract T createPresenter();

    abstract U getUi();

    protected BaseActivity() {
        mPresenter = createPresenter();
    }

    public T getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onUiDestroy(getUi());
    }
}
