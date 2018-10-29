package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.HomeModel;
import com.baidu.iov.dueros.waimai.model.IHomeModel;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;

import static com.baidu.iov.dueros.waimai.utils.VoiceManager.CMD_NO;

public class SearchPresenter extends Presenter<SearchPresenter.SearchUi> {

	private static final String TAG = SearchPresenter.class.getSimpleName();

	private IHomeModel mModel;

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
		mModel = new HomeModel();
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

	public interface SearchUi extends Ui {
		void close();

	}

}
