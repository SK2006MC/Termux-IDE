package com.termux.termxide.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TermuxBridgeService extends Service {
	private static final String TAG = "TermuxBridgeService";
	private ExecutorService executor;

	@Override
	public void onCreate() {
		super.onCreate();
		executor = Executors.newFixedThreadPool(4); // Thread pool for command execution
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Execute a command in Termux environment
	 *
	 * @param command  The command string to execute
	 * @param callback Callback to receive the output
	 */
	public void executeCommand(String command, CommandCallback callback) {
		executor.execute(() -> {
			try {
				Process process = Runtime.getRuntime().exec("su");
				OutputStream outputStream = process.getOutputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				// Send command to Termux
				outputStream.write(("TERMUX_PACKAGE_NAME=" + getPackageName() + " " + command + "\n").getBytes());
				outputStream.flush();
				outputStream.close();

				// Read and process output
				StringBuilder output = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line).append("\n");
				}

				reader.close();
				process.waitFor();

				// Return result through callback
				callback.onCommandComplete(output.toString(), process.exitValue() == 0);

			} catch (Exception e) {
				Log.e(TAG, "Error executing command", e);
				callback.onCommandError(e.getMessage());
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		executor.shutdown();
	}

	/**
	 * Interface for command execution callbacks
	 */
	public interface CommandCallback {
		void onCommandComplete(String output, boolean success);

		void onCommandError(String errorMessage);
	}
}
