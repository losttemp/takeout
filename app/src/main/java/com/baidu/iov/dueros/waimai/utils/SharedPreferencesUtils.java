package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;

import java.util.Arrays;
import java.util.List;

public class SharedPreferencesUtils {

	private static final String SEARCH_HISTORY = "search_history";
	private static final String SEARCH_KEYWORD = "search_keyword";
	private static final int SEARCH_HISTORY_MAX = 20;


	public static void getSearchHistory(List<String> historys) {
		SharedPreferences pref = WaiMaiApplication.getInstance().getSharedPreferences
				(SEARCH_HISTORY, Context.MODE_PRIVATE);
		historys.clear();
		String result = pref.getString(SEARCH_KEYWORD, "");

		if (TextUtils.isEmpty(result) || result.length() < 3) {
			return;
		}

		result = result.substring(1, result.length() - 1);
		String str[] = result.split(", ");
		List list = Arrays.asList(str);
		if (list != null) {
			historys.addAll(list);
		}
	}

	public static void saveSearchHistory(String word, List<String>
			historys) {
		if (historys == null) {
			return;
		}

		SharedPreferences pref = WaiMaiApplication.getInstance().getSharedPreferences
				(SEARCH_HISTORY, Context
						.MODE_PRIVATE);
		getSearchHistory(historys);
		//Delete duplicate historys
		if (historys.contains(word)) {
			historys.remove(word);
		}
		//save date 20 historys
		if (historys.size() >= SEARCH_HISTORY_MAX) {
			historys.remove(historys.size() - 1);
		}

		historys.add(0, word);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(SEARCH_KEYWORD, historys.toString());
		editor.commit();
	}

	public static void clearSearchHistory() {
		SharedPreferences pref = WaiMaiApplication.getInstance().getSharedPreferences
				(SEARCH_HISTORY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(SEARCH_KEYWORD);
		editor.commit();
	}

	public static void deleteSearchHistory(List<String> historys) {
		SharedPreferences pref = WaiMaiApplication.getInstance().getSharedPreferences
				(SEARCH_HISTORY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(SEARCH_KEYWORD, historys.toString());
		editor.commit();
	}

}
