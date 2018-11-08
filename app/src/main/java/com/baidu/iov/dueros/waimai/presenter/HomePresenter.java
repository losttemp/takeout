package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.HomeModel;
import com.baidu.iov.dueros.waimai.model.IHomeModel;


public class HomePresenter extends Presenter<HomePresenter.HomeUi> {

	private static final String TAG = HomePresenter.class.getSimpleName();

	private IHomeModel mModel;

	@Override
	public void onCommandCallback(String cmd, String extra) {

	}

	@Override
	public void registerCmd(Context context) {

	}

	@Override
	public void unregisterCmd(Context context) {

	}

	public HomePresenter() {
		mModel = new HomeModel();
	}

	@Override
	public void onUiReady(HomeUi ui) {
		super.onUiReady(ui);
		mModel.onReady();
	}

	@Override
	public void onUiUnready(HomeUi ui) {
		super.onUiUnready(ui);
		mModel.onDestroy();
	}

	public interface HomeUi extends Ui {

	}

}
