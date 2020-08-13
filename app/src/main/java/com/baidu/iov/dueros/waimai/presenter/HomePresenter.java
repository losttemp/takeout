package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.HomeModel;
import com.baidu.iov.dueros.waimai.model.IHomeModel;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.utils.Lg;
public class HomePresenter extends Presenter<HomePresenter.HomeUi> {

	private static final String TAG = HomePresenter.class.getSimpleName();

	private IHomeModel mModel;



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


	public void requesLogout() {

		mModel.requesLogout(new RequestCallback<LogoutBean>() {
			@Override
			public void onSuccess(LogoutBean data) {
				if (getUi() != null) {
					getUi().update(data);
				}
			}

			@Override
			public void onFailure(String msg) {
				if (getUi() != null) {
					getUi().failure(msg);
				}
			}

			@Override
			public void getLogid(String id) {
				Lg.getInstance().d(TAG, "requesLogout getLogid: "+id);
			}
		});
	}

	public interface HomeUi extends Ui {

		void update(LogoutBean data);

		void failure(String msg);

	}

}
