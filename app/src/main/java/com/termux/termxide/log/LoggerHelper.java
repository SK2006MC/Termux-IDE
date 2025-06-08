package com.termux.termxide.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LoggerHelper {
	private final FileLogger myfileLogger;

	private final ExecutorService loggingExecutor;

	public LoggerHelper(android.content.Context context, String rootPath) {
		this.myfileLogger = new FileLogger(rootPath, "log.txt");
		this.loggingExecutor = Executors.newSingleThreadExecutor(new LoggingThreadFactory());
	}

	public void log(String msg) {
		loggingExecutor.execute(() -> myfileLogger.log(msg));
	}

	public void shutdown() {
		loggingExecutor.shutdown();

		myfileLogger.close();

	}

	// Thread factory for logging tasks (lower priority)
	private static class LoggingThreadFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "MyUtils-Logging-Thread");
			t.setPriority(Thread.MIN_PRIORITY);
			return t;
		}
	}
}