package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IStoreListModel;
import com.baidu.iov.dueros.waimai.model.StoreListModel;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class StoreListPresenter extends Presenter<StoreListPresenter.StoreListUi> {

	private static final String TAG = StoreListPresenter.class.getSimpleName();

	private IStoreListModel mModel;

	public StoreListPresenter() {
		mModel = new StoreListModel();
	}

	@Override
	public void onUiReady(StoreListUi ui) {
		super.onUiReady(ui);
		mModel.onReady();
	}

	@Override
	public void onUiUnready(StoreListUi ui) {
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
			public void onFailure(String msg) {
				if (getUi() != null) {
					getUi().failure(msg);
				}
			}

			@Override
			public void getLogid(String id) {
				Lg.getInstance().d(TAG, "requestStoreList getLogid: "+id);
			}
		});
	}

	public void requestFilterList(FilterConditionReq filterConditionReq) {

		mModel.requestFilterList(filterConditionReq, new RequestCallback<FilterConditionResponse>
				() {
			@Override
			public void onSuccess(FilterConditionResponse data) {
				if (getUi() != null) {
					getUi().updateFilterCondition(data);
				}
			}

			@Override
			public void onFailure(String msg) {
				if (getUi() != null) {
					getUi().failureFilterCondition(msg);
				}
			}

			@Override
			public void getLogid(String id) {
				Lg.getInstance().d(TAG, "requestFilterList getLogid: "+id);
			}
		});
	}

	public interface StoreListUi extends Ui {
		void update(StoreResponse data);

		void failure(String msg);

		void updateFilterCondition(FilterConditionResponse data);

		void failureFilterCondition(String msg);

		void selectListItem(int index);

		void nextPage(boolean isNextPage);
	}

}
