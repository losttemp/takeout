package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.ISearchModel;
import com.baidu.iov.dueros.waimai.model.SearchModel;
import com.baidu.iov.dueros.waimai.net.entity.request.SearchSuggestReq;
import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

public class SearchPresenter extends Presenter<SearchPresenter.SearchUi> {

	private static final String TAG = SearchPresenter.class.getSimpleName();

	private ISearchModel mModel;



	public SearchPresenter() {
		mModel = new SearchModel();
	}

	@Override
	public void onUiReady(SearchUi ui) {
		super.onUiReady(ui);
		mModel.onReady();
	}

	@Override
	public void onUiUnready(SearchUi ui) {
		super.onUiUnready(ui);
		mModel.onDestroy();
	}

	public void requestSuggestList(SearchSuggestReq searchSuggestReq) {

		mModel.requestSuggestList(searchSuggestReq, new RequestCallback<SearchSuggestResponse>() {
			@Override
			public void onSuccess(SearchSuggestResponse data) {
				if (getUi() != null) {
					getUi().onSuggestSuccess(data);
				}
			}

			@Override
			public void onFailure(String msg) {
				if (getUi() != null) {
					getUi().onSuggestFailure(msg);
				}
			}

			@Override
			public void getLogid(String id) {
				Lg.getInstance().d(TAG, "requestSuggestList getLogid: "+id);
			}
		});
	}

	public interface SearchUi extends Ui {
		void onSuggestSuccess(SearchSuggestResponse data);

		void onSuggestFailure(String msg);

		void close();

		void selectListItem(int index);

	}

}
