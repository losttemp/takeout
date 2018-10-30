package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.ISearchModel;
import com.baidu.iov.dueros.waimai.model.SearchModel;
import com.baidu.iov.dueros.waimai.net.entity.request.SearchSuggestReq;
import com.baidu.iov.dueros.waimai.net.entity.response.SearchSuggestResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;

public class SearchPresenter extends Presenter<SearchPresenter.SearchUi> {

	private static final String TAG = SearchPresenter.class.getSimpleName();

	private ISearchModel mModel;

	@Override
	public void onCommandCallback(String cmd, String extra) {
		if (CMD_NO.equals(cmd) && null != getUi()) {
			getUi().close();
		}
	}

	@Override
	public void registerCmd(Context context) {
		Lg.getInstance().d(TAG, "registerCmd");
		if (null != mVoiceManager) {
			ArrayList<String> cmdList = new ArrayList<String>();
			cmdList.add(CMD_NO);
			//mVoiceController.registerCmd(context, cmdList, mVoiceCallback);
			mVoiceManager.registerCmd(context, cmdList, mVoiceCallback);
		}
	}

	@Override
	public void unregisterCmd(Context context) {
		Lg.getInstance().d(TAG, "registerCmd");
		if (null != mVoiceManager) {
			mVoiceManager.unregisterCmd(context, mVoiceCallback);
		}
	}

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
		});
	}

	public interface SearchUi extends Ui {
		void close();

		void onSuggestSuccess(SearchSuggestResponse data);

		void onSuggestFailure(String msg);

	}

}
