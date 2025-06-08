package com.termux.termxide.log;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileLogger {
	private static final String TAG = "FileLogger";
	public final String filePath;
	private BufferedWriter writer;

	public FileLogger(String parent, String name) {
		File file = new File(parent, name);
		this.filePath = file.getAbsolutePath();

	}

	public FileLogger(String filePath) {
		this.filePath = filePath;
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				if (file.createNewFile()) {
					Log.e(TAG, "Error initializing FileLogger: " + filePath);
				}
			}
			this.writer = new BufferedWriter(new FileWriter(file, true));
		} catch (Exception e) {
			Log.e(TAG, "Error initializing FileLogger", e);
			this.writer = null;
		}
	}

	@NonNull
	private String getTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH).format(new Date());
	}

	public synchronized void log(String msg) {
		if (writer == null) return;
		try {
			writer.write(getTimestamp() + " " + msg);
			writer.newLine();
			writer.flush();
		} catch (Exception e) {
			Log.d(TAG, "Error writing log", e);
		}
	}

	public synchronized void log(byte[] b) {
		if (writer == null) return;
		try {
			writer.write(getTimestamp() + " " + new String(b));
			writer.newLine();
			writer.flush();
		} catch (Exception e) {
			Log.e(TAG, "Error writing log bytes", e);
		}
	}

	public synchronized void close() {
		if (writer == null) return;
		try {
			writer.flush();
			writer.close();
		} catch (Exception e) {
			Log.e(TAG, "Error closing log writer", e);
		}
	}
}