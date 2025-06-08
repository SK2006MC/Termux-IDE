package com.termux.termxide.services;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileOperationService {
	private static final String TAG = "FileOperationService";
	private final Context context;
	private final TermuxBridgeService termuxBridgeService;

	public FileOperationService(Context context, TermuxBridgeService termuxBridgeService) {
		this.context = context;
		this.termuxBridgeService = termuxBridgeService;
	}

	/**
	 * List files in a directory
	 *
	 * @param path Directory path
	 * @return List of files with their metadata
	 */
	public List<FileMetadata> listFiles(String path) {
		List<FileMetadata> files = new ArrayList<>();
		try {
			File directory = new File(path);
			if (!directory.exists() || !directory.isDirectory()) {
				return files;
			}

			File[] filesArray = directory.listFiles();
			if (filesArray != null) {
				for (File file : filesArray) {
					files.add(new FileMetadata(
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

	/**
	 * Get file metadata
	 *
	 * @param path File path
	 * @return FileMetadata object
	 */
	public FileMetadata getFileMetadata(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			return new FileMetadata(
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
	public boolean createDirectory(String path) {
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
	public boolean delete(String path) {
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

	private boolean deleteDirectory(File directory) {
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

	/**
	 * File metadata class
	 */
	public static class FileMetadata {
		private final String name;
		private final String path;
		private final boolean isDirectory;
		private final long size;
		private final long lastModified;

		public FileMetadata(String name, String path, boolean isDirectory, long size, long lastModified) {
			this.name = name;
			this.path = path;
			this.isDirectory = isDirectory;
			this.size = size;
			this.lastModified = lastModified;
		}

		// Getters
		public String getName() {
			return name;
		}

		public String getPath() {
			return path;
		}

		public boolean isDirectory() {
			return isDirectory;
		}

		public long getSize() {
			return size;
		}

		public long getLastModified() {
			return lastModified;
		}
	}
}
