package com.termux.termxide.services;

import android.util.Log;

import com.termux.termxide.data.ItemFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileOperationService {
	private static final String TAG = "FileOperationService";

	public static List<ItemFile> listFiles(String path) {
		List<ItemFile> files = new ArrayList<>();
		try {
			File directory = new File(path);
			if (!directory.exists() || !directory.isDirectory()) {
				return files;
			}

			File[] filesArray = directory.listFiles();
			if (filesArray != null) {
				for (File file : filesArray) {
					files.add(new ItemFile(
							file.getName(),
							file.getAbsolutePath(),
							file.isDirectory(),
							file.length(),
							file.lastModified()
					));
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error listing files", e);
		}
		return files;
	}

	public static ItemFile getFileMetadata(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			return getFileMetadata(file);
		} catch (Exception e) {
			Log.e(TAG, "Invalid file path!");
			return null;
		}
	}

	public static ItemFile getFileMetadata(File file) {
		try {
			return new ItemFile(
					file.getName(),
					file.getAbsolutePath(),
					file.isDirectory(),
					file.length(),
					file.lastModified()
			);
		} catch (Exception e) {
			Log.e(TAG, "Error getting file metadata", e);
			return null;
		}
	}

	/**
	 * Create a new directory
	 *
	 * @param path Directory path
	 * @return true if successful
	 */
	public static boolean createDirectory(String path) {
		try {
			File directory = new File(path);
			return directory.mkdirs();
		} catch (Exception e) {
			Log.e(TAG, "Error creating directory", e);
			return false;
		}
	}

	/**
	 * Delete a file or directory
	 *
	 * @param path Path to delete
	 * @return true if successful
	 */
	public static boolean delete(String path) {
		try {
			File file = new File(path);
			if (file.isDirectory()) {
				return deleteDirectory(file);
			} else {
				return file.delete();
			}
		} catch (Exception e) {
			Log.e(TAG, "Error deleting", e);
			return false;
		}
	}

	private static boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						if (!deleteDirectory(file)) {
							return false;
						}
					} else {
						if (!file.delete()) {
							return false;
						}
					}
				}
			}
			return directory.delete();
		}
		return true;
	}
}
