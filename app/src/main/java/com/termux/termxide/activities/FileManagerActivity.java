package com.termux.termxide.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.termux.termxide.R;
import com.termux.termxide.adapter.FileAdapter;
import com.termux.termxide.services.FileOperationService;

import java.util.List;

public class FileManagerActivity extends AppCompatActivity {
	private RecyclerView leftFileList, rightFileList;
	private FileAdapter leftAdapter, rightAdapter;
	private FileOperationService fileOperationService;
	private String currentLeftPath = "/storage/emulated/0";
	private String currentRightPath = "/storage/emulated/0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_manager);

		// Initialize services
		fileOperationService = new FileOperationService(this, null); // TODO: Initialize TermuxBridgeService

		// Initialize UI components
		setupUI();
		setupRecyclerViews();
		loadFiles(currentLeftPath, currentRightPath);
	}

	private void setupUI() {
		// Setup bottom app bar
		BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
		setSupportActionBar(bottomAppBar);

		// Setup FAB
		FloatingActionButton fab = findViewById(R.id.fabNew);
		fab.setOnClickListener(v -> showNewFileDialog());
	}

	private void setupRecyclerViews() {
		// Left pane
		leftFileList = findViewById(R.id.leftFileList);
		leftAdapter = new FileAdapter(this, fileOperationService);
		leftFileList.setLayoutManager(new LinearLayoutManager(this));
		leftFileList.setAdapter(leftAdapter);

		// Right pane
		rightFileList = findViewById(R.id.rightFileList);
		rightAdapter = new FileAdapter(this, fileOperationService);
		rightFileList.setLayoutManager(new LinearLayoutManager(this));
		rightFileList.setAdapter(rightAdapter);

		// Set click listeners
		leftAdapter.setOnItemClickListener((file, position) -> {
			if (file.isDirectory()) {
				currentLeftPath = file.getAbsolutePath();
				loadFiles(currentLeftPath, currentRightPath);
			}
		});

		rightAdapter.setOnItemClickListener((file, position) -> {
			if (file.isDirectory()) {
				currentRightPath = file.getAbsolutePath();
				loadFiles(currentLeftPath, currentRightPath);
			}
		});
	}

	private void loadFiles(String leftPath, String rightPath) {
		// Load files for left pane
		List<FileOperationService.FileMetadata> leftFiles = fileOperationService.listFiles(leftPath);
		leftAdapter.setFiles(leftFiles);

		// Load files for right pane
		List<FileOperationService.FileMetadata> rightFiles = fileOperationService.listFiles(rightPath);
		rightAdapter.setFiles(rightFiles);
	}

	private void showNewFileDialog() {
		// TODO: Implement new file/folder creation dialog
		Toast.makeText(this, "Create new file/folder", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_file_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			loadFiles(currentLeftPath, currentRightPath);
			return true;
		} else if (id == R.id.action_settings) {
			// TODO: Open settings
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}
