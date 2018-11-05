package com.baidu.iov.dueros.waimai.presenter;

import android.content.Context;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.HomeModel;
import com.baidu.iov.dueros.waimai.model.IHomeModel;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;

import java.util.ArrayList;


public class HomePresenter extends Presenter<HomePresenter.HomeUi> {

	private static final String TAG = HomePresenter.class.getSimpleName();

	private IHomeModel mModel;

	@Override
	public void onCommandCallback(String cmd, String extra) {
		if (VoiceManager.CMD_NO.equals(cmd) && null != getUi()) {
			getUi().close();
		}
	}

	@Override
	public void registerCmd(Context context) {
		Lg.getInstance().d(TAG, "registerCmd");
		if (null != mVoiceManager) {
			ArrayList<String> cmdList = new ArrayList<String>();
			cmdList.add(VoiceManager.CMD_NO);
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
		void close();
	}

}
