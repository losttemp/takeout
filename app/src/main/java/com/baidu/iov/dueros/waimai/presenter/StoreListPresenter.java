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

import java.util.ArrayList;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;

public class StoreListPresenter extends Presenter<StoreListPresenter.StoreListUi> {

	private static final String TAG = StoreListPresenter.class.getSimpleName();

	private IStoreListModel mModel;

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

		void close();

		void updateFilterCondition(FilterConditionResponse data);

		void failureFilterCondition(String msg);
	}

}
