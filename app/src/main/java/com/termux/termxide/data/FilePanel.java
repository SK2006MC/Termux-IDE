package com.termux.termxide.data;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.termux.termxide.R;
import com.termux.termxide.adapter.ItemFileAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilePanel {
	private final String TAG = "FilePanel";
	private String path;
	private final List<String> history;
	private final RecyclerView recyclerView;
	private final ItemFileAdapter fileAdapter;
	private final Context context;

	public FilePanel(String mPath, RecyclerView mRecyclerView) {
		this.context = mRecyclerView.getContext();
		this.path = mPath;
		this.history = new ArrayList<>();
		this.recyclerView = mRecyclerView;

		fileAdapter = new ItemFileAdapter(context);
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		recyclerView.setAdapter(fileAdapter);
		fileAdapter.setOnItemClickListener((file, position) -> {
			if (file.isDirectory()) {
				path = file.getAbsolutePath();
				openFolder(path);
			}
		});
	}

	public void sort(ItemFileAdapter.SortType type, boolean rev) {
		if (fileAdapter != null) {
			fileAdapter.sort(type, rev);
		}
	}

	public void openFolder(String path) {
		if (path == null || path.isEmpty()) {
			showError(context.getString(R.string.error_path_is_null));
			return;
		}

		this.path = path;
		history.add(path);
		openFolder2(path);
	}

	private void openFolder2(String path) {
		try {
			fileAdapter.loadFiles(path);
		} catch (IOException e) {
			showError(context.getString(R.string.error_loading_files) + " " + e.getMessage());
		}
	}

	private void showError(String message) {
		if (context != null) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	public boolean canGoBack() {
		return !history.isEmpty();
	}

	public String goBack() {
		if (history.isEmpty()) {
			return path;
		}
		String previousPath = history.remove(history.size() - 1);
		openFolder2(previousPath);
		return previousPath;
	}

	public String getCurrentPath() {
		return path;
	}
}