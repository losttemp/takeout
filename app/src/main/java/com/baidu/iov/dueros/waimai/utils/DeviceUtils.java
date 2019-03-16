package com.baidu.iov.dueros.waimai.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;

public class DeviceUtils {

	public static Point getScreenSize(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			return new Point(display.getWidth(), display.getHeight());
		} else {
			Point point = new Point();
			display.getSize(point);
			return point;
		}
	}

	public static boolean checkBluetooth(Activity context) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			ToastUtils.show(context.getApplicationContext(), context.getResources().getString(R.string.bluetooth_adapter_not_found), Toast.LENGTH_SHORT);
			return false;
		}
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//            BluetoothAdapter.ACTION_REQUEST_ENABLE
			enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(enableIntent);
			return false;
		}
		return true;
	}

}