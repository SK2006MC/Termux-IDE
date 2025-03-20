package com.termux.termxide.data;

public class ItemFile {
	private final String name;
	private final String path;
	private final boolean isDirectory;
	private final long size;
	private final long lastModified;

	public ItemFile(String name, String path, boolean isDirectory, long size, long lastModified) {
		this.name = name;
		this.path = path;
		this.isDirectory = isDirectory;
		this.size = size;
		this.lastModified = lastModified;
	}

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