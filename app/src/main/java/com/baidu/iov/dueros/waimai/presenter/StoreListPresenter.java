package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IStoreListModel;
import com.baidu.iov.dueros.waimai.model.StoreListModel;
import com.baidu.iov.dueros.waimai.net.entity.request.FilterConditionReq;
import com.baidu.iov.dueros.waimai.net.entity.request.StoreReq;
import com.baidu.iov.dueros.waimai.net.entity.response.FilterConditionResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;

import java.util.ArrayList;

public class StoreListPresenter extends Presenter<StoreListPresenter.StoreListUi> {

	private static final String TAG = StoreListPresenter.class.getSimpleName();

	private IStoreListModel mModel;

	@Override
	public void onCommandCallback(String cmd, String extra) {
		if (getUi() == null) {
			return;
		}

		switch (cmd) {
			case VoiceManager.CMD_SELECT:
				getUi().selectListItem(Integer.parseInt(extra));
				break;
			case VoiceManager.CMD_NEXT:
				getUi().nextPage();
				break;
			default:
				break;
		}
	}

	@Override
	public void registerCmd(Context context) {
		Lg.getInstance().d(TAG, "registerCmd");
		if (null != mVoiceManager) {
			ArrayList<String> cmdList = new ArrayList<String>();
			cmdList.add(VoiceManager.CMD_NO);
			cmdList.add(VoiceManager.CMD_SELECT);
			cmdList.add(VoiceManager.CMD_NEXT);
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
		});
	}

	public interface StoreListUi extends Ui {
		void update(StoreResponse data);

		void failure(String msg);

		void updateFilterCondition(FilterConditionResponse data);

		void failureFilterCondition(String msg);

		void selectListItem(int index);

		void nextPage();
	}

}
