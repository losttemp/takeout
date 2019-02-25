package com.baidu.iov.dueros.waimai.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hegui on 2018/10/5.
 */

public class StandardCmdClient {
    private static final String TAG = "StandardCmdClient";
    private static final String PACKAGE_NAME = "com.baidu.che.codriver";
    private static final String ACTION_REGISTER = "com.baidu.duerosauto.standard_cmd.register";
    private static final String ACTION_UNREGISTER = "com.baidu.duerosauto.standard_cmd.unregister";
    private static final String ACTION_CALLBACK = "com.baidu.duerosauto.standard_cmd.callback";
    private static final String ACTION_TTS = "com.baidu.duerosauto.standard_cmd.tts";
    public static final String ACTION_EXIT_APK = "com.baidu.iov.dueros.waimai.action.close";
    private static final String KEY_CMD = "cmd";
    private static final String KEY_PKG_NAME = "pkg_name";
    public static final String KEY_EXTRA = "extra";
    private static final String KEY_TTS_TEXT = "tts_text";

    private static final String BUBBLE = "bubble";

    public static final String CMD_NEXT = "next";
    public static final String CMD_PRE = "pre";
    public static final String CMD_YES = "yes";
    public static final String CMD_NO = "no";
    public static final String CMD_SELECT = "select";
    public static final String CMD_VIDEO_NEXT = "video_next"; // 上一集
    public static final String CMD_VIDEO_PRE = "video_pre"; // 下一集
    public static final String CMD_VIDEO_PAUSE = "video_pause"; // 暂停
    public static final String CMD_VIDEO_RESUME = "video_resume"; // 继续播放
    public static final String CMD_VIDEO_SEEK_FORWARD = "video_forward"; // 快进
    public static final String CMD_VIDEO_SEEK_BACKWARD = "video_backward"; // 快退
    public static final String CMD_VIDEO_FULLSCREEN = "video_fullscreen"; // 全屏
    public static final String NEED_TTS = "needTts";

    private static class SingletonHolder {
        private static StandardCmdClient standardCmdClient = new StandardCmdClient();
    }

    public static StandardCmdClient getInstance() {
        return SingletonHolder.standardCmdClient;
    }

    /*
     * 注册指令
     * @context ：上下文
     * @cmdList ：要注册的指令集
     * @callback : 指令的回调，注册成功后，用户语音输入语音指令，就会有相应的指令回调到该callback
     */
    public void registerCmd(Context context, ArrayList<String> cmdList, CmdCallback callback) {
        if (cmdList == null || cmdList.size() == 0
                || context == null || callback == null) {
            Log.d(TAG, "registerCmd() cmdList == null");
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
        Log.d(TAG, "registerCmd() succeed");
    }

    /*
     * 取消注册指令
     * @context ：上下文
     * @callback : 该参数必须要与注册时的参数一致
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
        Log.d(TAG, "unregisterCmd() succeed");
    }

    /*
     * 取消注册指令
     * @context ：上下文
     * @text : 待播放的文本，该接口可以在未注册时使用。注意：如果文本是BUBBLE，则播放默认音效
     */
    public void playTTS(Context context, String text) {
        if (context == null || TextUtils.isEmpty(text)) {
            Log.d(TAG, "playTTS() context == null");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_TTS);
        intent.putExtra(KEY_PKG_NAME, context.getPackageName());
        intent.putExtra(KEY_TTS_TEXT, text);
        intent.setPackage(PACKAGE_NAME);
        context.sendBroadcast(intent);
        Log.d(TAG, "playTTS() text = " + text);
    }

    public abstract static class CmdCallback extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                Log.d(TAG, "onReceive() intent == null");
                return;
            }
            String action = intent.getAction();
            String cmd = intent.getStringExtra(KEY_CMD);
            String extra = intent.getStringExtra(KEY_EXTRA);
            Log.d(TAG, "onReceive() action = " + action + ", cmd = " + cmd + ", extra = " + extra);
            if (!TextUtils.equals(ACTION_CALLBACK, action)) {
                return;
            }
            if (!TextUtils.isEmpty(cmd)) {
                onCmdCallback(cmd, extra);
            }
        }

        public abstract void onCmdCallback(String cmd, String extra);
    }
}
