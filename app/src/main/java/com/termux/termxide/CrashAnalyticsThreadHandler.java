package com.termux.termxide;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Custom uncaught exception handler that logs crash information and provides
 * analytics about crashes in the application.
 */
public class CrashAnalyticsThreadHandler implements Thread.UncaughtExceptionHandler {
	private static final String TAG = "CrashAnalytics";
	private final Application mApplication;
	private final Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

	public CrashAnalyticsThreadHandler(Application application) {
		if (application == null) {
			throw new IllegalArgumentException("Application cannot be null");
		}
		mApplication = application;
		mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			// Get stack trace as string
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			ex.printStackTrace(printWriter);
			String stackTrace = result.toString();
			printWriter.close();

			// Log crash details
			Log.e(TAG, "Uncaught exception in thread: " + thread.getName(), ex);
			Log.e(TAG, "Stack trace:\n" + stackTrace);

			// Additional crash analytics - you can add more analytics here
			String deviceInfo = "Device: " + android.os.Build.MODEL;
			String androidVersion = "Android Version: " + android.os.Build.VERSION.RELEASE;
			String appVersion = "App Version: N/A";
			try {
				appVersion = "App Version: " + mApplication.getPackageManager()
						.getPackageInfo(mApplication.getPackageName(), 0).versionName;
			} catch (Exception ignored) {
			}

			Log.e(TAG, deviceInfo);
			Log.e(TAG, androidVersion);
			Log.e(TAG, appVersion);

			// Save crash log to file
			saveCrashLogToFile(thread, stackTrace, deviceInfo, androidVersion, appVersion);

		} catch (Exception e) {
			Log.e(TAG, "Error while logging crash", e);
		}

		// Continue with default exception handling
		if (mDefaultExceptionHandler != null) {
			mDefaultExceptionHandler.uncaughtException(thread, ex);
		} else {
			System.exit(1);
		}
	}

	private void saveCrashLogToFile(Thread thread, String stackTrace, String deviceInfo, String androidVersion, String appVersion) {
		String fileName = "crash_log_" + System.currentTimeMillis() + ".txt";
		File crashFile = new File(mApplication.getObbDir(), fileName);
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append("Thread: ").append(thread.getName()).append("\n");
		logBuilder.append(deviceInfo).append("\n");
		logBuilder.append(androidVersion).append("\n");
		logBuilder.append(appVersion).append("\n");
		logBuilder.append("Stack trace:\n").append(stackTrace);
		try (FileOutputStream fos = new FileOutputStream(crashFile)) {
			fos.write(logBuilder.toString().getBytes());
			fos.flush();
		} catch (IOException e) {
			Log.e(TAG, "Failed to write crash log to file", e);
		}
	}
}
