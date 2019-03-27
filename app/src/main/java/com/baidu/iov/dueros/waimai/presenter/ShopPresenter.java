package com.baidu.iov.dueros.waimai.presenter;
import android.content.Context;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.HomeModel;
import com.baidu.iov.dueros.waimai.model.IHomeModel;
public class ShopPresenter extends Presenter<ShopPresenter.ShopUi> {

	private static final String TAG = ShopPresenter.class.getSimpleName();

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

	public ShopPresenter() {
		mModel = new HomeModel();
	}

	@Override
	public void onUiReady(ShopUi ui) {
		super.onUiReady(ui);
		mModel.onReady();
	}

	@Override
	public void onUiUnready(ShopUi ui) {
		super.onUiUnready(ui);
		mModel.onDestroy();
	}




	public interface ShopUi extends Ui {

	}

}
