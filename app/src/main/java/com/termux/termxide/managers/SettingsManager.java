package com.termux.termxide.managers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsManager {

	static final String KEY_IS_FIRST = "IS_FIRST";
	static final String KEY_HOME_PATH = "HOME_PATH";
	static final String KEY_LAST_LEFT_FILE_PATH = "LAST_LEFT_FILE_PATH";
	static final String KEY_LAST_RIGHT_FILE_PATH = "LAST_RIGHT_FILE_PATH";
	SharedPreferences preferences;

	public SettingsManager(Context context){
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public boolean getIsFirst() {
		return preferences.getBoolean(KEY_IS_FIRST,true);
	}
	public String getHomePath(){
		return preferences.getString(KEY_HOME_PATH,"");
	}
	public void setHomePath(String path) {
		preferences.edit().putString(KEY_HOME_PATH,path).apply();
	}
	public String getLastLeftFilePath(){
		return preferences.getString(KEY_LAST_LEFT_FILE_PATH,"");
	}
}
