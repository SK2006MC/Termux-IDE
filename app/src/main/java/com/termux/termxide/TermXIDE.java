package com.termux.termxide;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Application class for TermXIDE that handles global application initialization
 * and crash analytics setup.
 * This class manages crash reporting and ensures proper cleanup during termination.
 */
public class TermXIDE extends Application {
    private static final String TAG = "TermXIDE";
    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());
    private Thread.UncaughtExceptionHandler mPreviousExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            initCrashAnalytics();
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize crash analytics", e);
            // Fallback to default error handling
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(
                Thread.currentThread(), e);
        }
    }

    /**
     * Initializes crash analytics by setting up a custom uncaught exception handler.
     * This method ensures that any uncaught exceptions are properly reported.
     */
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

    /**
     * Cleans up resources and restores the previous exception handler when the
     * application is terminating.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "Application is terminating");
        
        // Restore previous exception handler
        if (mPreviousExceptionHandler != null) {
            Thread.setDefaultUncaughtExceptionHandler(mPreviousExceptionHandler);
            Log.d(TAG, "Restored previous exception handler");
        }
    }
}
