package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.HomeModel;
import com.baidu.iov.dueros.waimai.model.IHomeModel;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;

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

	public void requestStoreList(StoreReq storeReq) {

		mModel.requestStoreList(storeReq, new RequestCallback<StoreResponse>() {
			@Override
			public void onSuccess(StoreResponse data) {
				if (getUi() != null) {
					getUi().update(data);
				}
			}

			@Override
			public void onFailure(String error) {
				if (getUi() != null) {
					getUi().failure(error);
				}
			}
		});
	}

	public interface HomeUi extends Ui {
		void update(StoreResponse data);

		void failure(String error);
	}

}
