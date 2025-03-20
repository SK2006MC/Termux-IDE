package com.termux.termxide;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.termux.shared.logger.Logger;
import com.termux.shared.termux.TermuxConstants;
import com.termux.shared.termux.crash.TermuxCrashUtils;
import com.termux.shared.termux.settings.preferences.TermuxFloatAppSharedPreferences;

/**
 * Application class for TermXIDE that handles global application initialization
 * and crash analytics setup.
 * This class manages crash reporting and ensures proper cleanup during termination.
 */
public class TermXIDE extends Application {
	private static final String TAG = "TermXIDE";
	private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());
	private Thread.UncaughtExceptionHandler mPreviousExceptionHandler;

	public static void setLogConfig(Context context, boolean commitToFile) {
		Logger.setDefaultLogTag(TermuxConstants.TERMUX_FLOAT_APP_NAME.replaceAll("[: ]", ""));

		// Load the log level from shared preferences and set it to the {@link Logger.CURRENT_LOG_LEVEL}
		TermuxFloatAppSharedPreferences preferences = TermuxFloatAppSharedPreferences.build(context);
		if (preferences == null) return;
		preferences.setLogLevel(null, preferences.getLogLevel(true), commitToFile);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Context context = getApplicationContext();
		TermuxCrashUtils.setCrashHandler(context);
		setLogConfig(context, true);
//        try {
//            initCrashAnalytics();
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to initialize crash analytics", e);
//            // Fallback to default error handling
//            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(
//                Thread.currentThread(), e);
//        }
	}

	private void initCrashAnalytics() {
		try {
			// Store previous handler for fallback
			mPreviousExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

			CrashAnalyticsThreadHandler crashHandler = new CrashAnalyticsThreadHandler(this);
			if (crashHandler != null) {
				Thread.setDefaultUncaughtExceptionHandler(crashHandler);
				Log.d(TAG, "Crash analytics initialized successfully");

				// Post a check to main thread to verify handler is set
				MAIN_THREAD_HANDLER.post(new Runnable() {
					@Override
					public void run() {
						Thread.UncaughtExceptionHandler currentHandler =
								Thread.getDefaultUncaughtExceptionHandler();
						if (currentHandler instanceof CrashAnalyticsThreadHandler) {
							Log.d(TAG, "Crash handler verified on main thread");
						} else {
							Log.w(TAG, "Crash handler not set on main thread");
						}
					}
				});
			} else {
				Log.e(TAG, "Failed to create crash handler");
			}
		} catch (Exception e) {
			Log.e(TAG, "Error initializing crash analytics", e);
			// Restore previous handler if initialization fails
			if (mPreviousExceptionHandler != null) {
				Thread.setDefaultUncaughtExceptionHandler(mPreviousExceptionHandler);
			}
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "Application is terminating");
//
//        // Restore previous exception handler
//        if (mPreviousExceptionHandler != null) {
//            Thread.setDefaultUncaughtExceptionHandler(mPreviousExceptionHandler);
//            Log.d(TAG, "Restored previous exception handler");
//        }
	}
}
