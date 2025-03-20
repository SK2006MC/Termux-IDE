package com.termux.termxide.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.termux.termxide.R;
import com.termux.termxide.databinding.ActivityCodeEditorBinding;
import com.termux.termxide.ui.CodeEditorView;

import java.io.File;

public class CodeEditorActivity extends BaseActivity {
	private ActivityCodeEditorBinding binding;
	private CodeEditorView codeEditor;
	private File currentFile;
	private String currentFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityCodeEditorBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		// Initialize UI components
		setupUI();

		// Get file path from intent
		currentFilePath = getIntent().getStringExtra("file_path");
		if (currentFilePath != null) {
			currentFile = new File(currentFilePath);
			loadFile();
		}
	}

	private void setupUI() {
		// Setup toolbar
//        setSupportActionBar(binding.toolbar);

		// Setup code editor
		codeEditor = binding.codeEditor;

		// Setup bottom app bar
		setSupportActionBar(binding.bottomToolbar);

		// Setup FABs
		FloatingActionButton fabSave = binding.fabSave;
		FloatingActionButton fabUndo = binding.fabUndo;
		FloatingActionButton fabRedo = binding.fabRedo;

		fabSave.setOnClickListener(v -> saveFile());
		fabUndo.setOnClickListener(v -> codeEditor.undo());
		fabRedo.setOnClickListener(v -> codeEditor.redo());
	}

	private void loadFile() {
		if (currentFile == null || !currentFile.exists()) {
			alert("File not found");
			return;
		}

		// TODO: Implement file loading
		alert("Loading file: " + currentFile.getName());
	}

	private void saveFile() {
		if (currentFile == null) {
			alert("No file selected");
			return;
		}

		// TODO: Implement file saving
		alert("Saving file: " + currentFile.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_code_editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_search) {
			showSearchDialog();
			return true;
		} else if (id == R.id.action_replace) {
			showReplaceDialog();
			return true;
		} else if (id == R.id.action_settings) {
			startMyActivity(SettingsActivity.class);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	void showSearchDialog() {
		// TODO: implement it
	}

	void showReplaceDialog() {
		// TODO: implement it
	}
}
