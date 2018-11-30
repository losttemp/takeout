package com.baidu.iov.dueros.waimai.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;


public class VoiceManager {
    private static final String TAG = VoiceManager.class.getSimpleName();
    private static final String PACKAGE_NAME = "com.baidu.che.codriver";
    private static final String ACTION_REGISTER = "com.baidu.duerosauto.app_controler.register";
    private static final String ACTION_UNREGISTER = "com.baidu.duerosauto.app_controler.unregister";
    private static final String ACTION_CALLBACK = "com.baidu.duerosauto.app_controler.callback";
    private static final String ACTION_TTS = "com.baidu.duerosauto.app_controler.tts";
    private static final String KEY_CMD = "cmd";
    private static final String KEY_PKG_NAME = "pkg_name";
    public static final String KEY_EXTRA = "extra";
    private static final String KEY_TTS_TEXT = "tts_text";

    public static final String CMD_NEXT = "next";
    public static final String CMD_PRE = "pre";
    public static final String CMD_YES = "yes";
    public static final String CMD_NO = "no";
    public static final String CMD_SELECT = "select";
    public static final String CMD_VIDEO_NEXT = "video_next";
    public static final String CMD_VIDEO_PRE = "video_pre";
    public static final String CMD_VIDEO_PAUSE = "video_pause";
    public static final String CMD_VIDEO_RESUME = "video_resume";

    private static class SingletonVoiceHolder {
        private static final VoiceManager VOICE_MANAGER = new VoiceManager();
    }

    public static VoiceManager getInstance() {
        return SingletonVoiceHolder.VOICE_MANAGER;
    }

    /*
     * Register instruction
     */
    public void registerCmd(Context context, ArrayList<String> cmdList, CmdCallback callback) {
        if (cmdList == null || cmdList.size() == 0
                || context == null || callback == null) {
            Lg.getInstance().d(TAG, "registerCmd() return");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_REGISTER);
        intent.putExtra(KEY_PKG_NAME, context.getPackageName());
        intent.putStringArrayListExtra(KEY_CMD, cmdList);
        intent.setPackage(PACKAGE_NAME);
        context.sendBroadcast(intent);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CALLBACK);
        context.registerReceiver(callback, filter);
        Lg.getInstance().d(TAG, "registerCmd() succeed");
    }

    /*
     * Cancellation of registration instruction
     */
    public void unregisterCmd(Context context, CmdCallback callback) {
        if (context == null || callback == null) {
            Log.d(TAG, "unregisterCmd() context == null");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_UNREGISTER);
        intent.putExtra(KEY_PKG_NAME, context.getPackageName());
        intent.setPackage(PACKAGE_NAME);
        context.sendBroadcast(intent);

        context.unregisterReceiver(callback);
        Lg.getInstance().d(TAG, "unregisterCmd() succeed");
    }

    public abstract static class CmdCallback extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                Lg.getInstance().d(TAG, "onReceive() return");
                return;
            }
            String action = intent.getAction();
            if (!TextUtils.equals(ACTION_CALLBACK, action)) {
                return;
            }
            String cmd = intent.getStringExtra(KEY_CMD);
            String extra = intent.getStringExtra(KEY_EXTRA);
            Lg.getInstance().d(TAG, "onReceive() action = " + action + ", cmd = " + cmd + ", extra = " + extra);
            if (!TextUtils.isEmpty(cmd)) {
                onCmdCallback(cmd, extra);
            }
        }

        public abstract void onCmdCallback(String cmd, String extra);
    }

    public void playTTS(Context context, String text) {
        if (context == null || TextUtils.isEmpty(text)) {
            Lg.getInstance().d(TAG, "playTTS() context == null");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_TTS);
        intent.putExtra(KEY_PKG_NAME, context.getPackageName());
        intent.putExtra(KEY_TTS_TEXT, text);
        intent.setPackage(PACKAGE_NAME);
        context.sendBroadcast(intent);
        Lg.getInstance().d(TAG, "playTTS() text = " + text);
    }
}
