package com.baidu.iov.dueros.waimai.utils;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;

/**
 * Created by v_chenxuran on 2019/1/24
 * Describe: 模拟触控工具类
 */
public final class VoiceTouchUtils {

    private VoiceTouchUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String DESCRIPTION_SEPARATOR = "^";

    public static void setVoiceTouchSupport(@NonNull View view, @StringRes int description) {
        setVoiceTouchSupport(view, ResUtils.getString(description));
    }

    public static void setVoiceTouchSupport(@NonNull View view, String description) {
        view.setContentDescription(description);
    }

    public static void setVoicesTouchSupport(@NonNull View view, @StringRes int... descriptions) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < descriptions.length; i++) {
            stringBuilder.append(ResUtils.getString(descriptions[i]));
            if (i < descriptions.length - 1) {
                stringBuilder.append(DESCRIPTION_SEPARATOR);
            }
        }
        setVoiceTouchSupport(view, stringBuilder.toString());
    }

    public static void setVoicesTouchSupport(@NonNull View view, String... descriptions) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < descriptions.length; i++) {
            stringBuilder.append(descriptions[i]);
            if (i < descriptions.length - 1) {
                stringBuilder.append(DESCRIPTION_SEPARATOR);
            }
        }
        setVoiceTouchSupport(view, stringBuilder.toString());
    }

    public static void setVoicesTouchSupport(@NonNull View view, @ArrayRes int descriptions) {
        setVoicesTouchSupport(view, ResUtils.getStringArray(descriptions));
    }

    public static void setItemVoiceTouchSupport(@NonNull View view, int index, String description) {
        index += 1;
        try {
            description = String.format(description, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVoiceTouchSupport(view, description);
    }

    public static void setItemVoiceTouchSupport(@NonNull View view, int index, @StringRes int description) {
        index += 1;
        String descriptionStr;
        try {
            descriptionStr = ResUtils.getString(description, index);
        } catch (Exception e) {
            e.printStackTrace();
            descriptionStr = ResUtils.getString(description);
        }
        setVoiceTouchSupport(view, descriptionStr);
    }

    public static void setItemVoicesTouchSupport(@NonNull View view, int index, @ArrayRes int descriptions) {
        setItemVoicesTouchSupport(view, index, ResUtils.getStringArray(descriptions));
    }

    public static void setItemVoicesTouchSupport(@NonNull View view, int index, String... descriptions) {
        index += 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < descriptions.length; i++) {
            String description;
            try {
                description = String.format(descriptions[i], index);
            } catch (Exception e) {
                description = descriptions[i];
            }
            stringBuilder.append(description);
            if (i < descriptions.length - 1) {
                stringBuilder.append(DESCRIPTION_SEPARATOR);
            }
        }
        setVoiceTouchSupport(view, stringBuilder.toString());
    }

    public static void setVoiceTouchTTSSupport(@NonNull View view, @StringRes int tts) {
        setVoiceTouchTTSSupport(view, ResUtils.getString(tts));
    }

    public static void setVoiceTouchTTSSupport(@NonNull View view, String tts) {
        setVoiceTouchTTSSupport(view, createTTSAccessibilityDelegate(tts));
    }

    public static void setVoiceTouchTTSSupport(@NonNull View view, View.AccessibilityDelegate delegate) {
        view.setAccessibilityDelegate(delegate);
    }

    public static void clearVoiceTouchSupport(@NonNull View view) {
        view.setContentDescription(null);
    }

    public static void clearVoiceTouchTTSSupport(@NonNull View view) {
        view.setAccessibilityDelegate(null);
    }

    public static View.AccessibilityDelegate createTTSAccessibilityDelegate(@StringRes int tts) {
        return createTTSAccessibilityDelegate(ResUtils.getString(tts));
    }

    public static View.AccessibilityDelegate createTTSAccessibilityDelegate(final String tts) {
        return new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action == AccessibilityNodeInfo.ACTION_CLICK) {
                    VoiceManager.getInstance().playTTS(WaiMaiApplication.getInstance().getApplicationContext(), tts);
                }
                return super.performAccessibilityAction(host, action, args);
            }
        };
    }
}
