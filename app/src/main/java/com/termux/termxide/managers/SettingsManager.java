package com.termux.termxide.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SettingsManager {
	private static final String TAG = "SettingsManager";
	private static final String PREFS_NAME = "TermXIDE_Prefs";
	private static final String KEY_THEME = "theme";
	private static final String KEY_FONT_SIZE = "font_size";
	private static final String KEY_TAB_SIZE = "tab_size";
	private static final String KEY_SHOW_LINE_NUMBERS = "show_line_numbers";
	private static final String KEY_SHOW_WHITESPACE = "show_whitespace";
	private static final String KEY_HOME_PATH = "home_path";
	private static final String KEY_IS_FIRST = "is_first";

	private final SharedPreferences prefs;

	public SettingsManager(Context context) {
		prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	// Theme settings
	public String getTheme() {
		return prefs.getString(KEY_THEME, "dark");
	}

	public void setTheme(String theme) {
		prefs.edit().putString(KEY_THEME, theme).apply();
	}

	// Editor settings
	public int getFontSize() {
		return prefs.getInt(KEY_FONT_SIZE, 14);
	}

	public void setFontSize(int size) {
		prefs.edit().putInt(KEY_FONT_SIZE, size).apply();
	}

	public int getTabSize() {
		return prefs.getInt(KEY_TAB_SIZE, 4);
	}

	public void setTabSize(int size) {
		prefs.edit().putInt(KEY_TAB_SIZE, size).apply();
	}

	public boolean isShowLineNumbers() {
		return prefs.getBoolean(KEY_SHOW_LINE_NUMBERS, true);
	}

	public void setShowLineNumbers(boolean show) {
		prefs.edit().putBoolean(KEY_SHOW_LINE_NUMBERS, show).apply();
	}

	public boolean isShowWhitespace() {
		return prefs.getBoolean(KEY_SHOW_WHITESPACE, false);
	}

	public void setShowWhitespace(boolean show) {
		prefs.edit().putBoolean(KEY_SHOW_WHITESPACE, show).apply();
	}

	// File manager settings
	public boolean isShowHiddenFiles() {
		return prefs.getBoolean("show_hidden_files", false);
	}

	public void setShowHiddenFiles(boolean show) {
		prefs.edit().putBoolean("show_hidden_files", show).apply();
	}

	public String getDefaultDirectory() {
		return prefs.getString("default_directory", "/storage/emulated/0");
	}

	public void setDefaultDirectory(String path) {
		prefs.edit().putString("default_directory", path).apply();
	}

	// Terminal settings
	public String getTerminalFont() {
		return prefs.getString("terminal_font", "monospace");
	}

	public void setTerminalFont(String font) {
		prefs.edit().putString("terminal_font", font).apply();
	}

	public int getTerminalFontSize() {
		return prefs.getInt("terminal_font_size", 12);
	}

	public void setTerminalFontSize(int size) {
		prefs.edit().putInt("terminal_font_size", size).apply();
	}

	// General settings
	public boolean isAutoSaveEnabled() {
		return prefs.getBoolean("auto_save", false);
	}

	public void setAutoSave(boolean enabled) {
		prefs.edit().putBoolean("auto_save", enabled).apply();
	}

	public int getAutoSaveInterval() {
		return prefs.getInt("auto_save_interval", 30); // in seconds
	}

	public void setAutoSaveInterval(int seconds) {
		prefs.edit().putInt("auto_save_interval", seconds).apply();
	}

	// Reset all settings to defaults
	public void resetToDefaults() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.apply();
		Log.d(TAG, "Settings reset to defaults");
	}

	public void setHomePath(String homePath) {
		prefs.edit().putString(KEY_HOME_PATH, homePath).apply();
	}

	public boolean getIsFirst() {
		return prefs.getBoolean(KEY_IS_FIRST, false);
	}

	public void putIsFirst(boolean b) {
		prefs.edit().putBoolean(KEY_IS_FIRST, b).apply();
	}
}
