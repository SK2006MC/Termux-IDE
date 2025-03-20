package com.termux.termxide.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.termux.termxide.R;
import com.termux.termxide.adapter.ItemFileAdapter;
import com.termux.termxide.data.FilePanel;
import com.termux.termxide.databinding.ActivityFileManagerBinding;

import java.util.Arrays;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class FileManagerActivity extends BaseActivity {

	private String currentLeftPath = "/storage/emulated/0";
	private String currentRightPath = "/storage/emulated/0";
	private FilePanel leftFilePanel, rightFilePanel;
	private ActivityFileManagerBinding binding;
	private final String[] sortTypes = Arrays.stream(ItemFileAdapter.SortType.values()).map(Enum::name).toArray(String[]::new);
	private ItemFileAdapter.SortType sortType = ItemFileAdapter.SortType.NAME;
	private boolean inReverse = false;
	private int lastSortTypeIndex = 0;

	/**
	 * Called when the activity is starting.
	 * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityFileManagerBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		setupUI();
		initOptionsMenu();
		initOnBackPressB(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				// Optionally handle back press
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Ensure panels are initialized before loading files
		if (leftFilePanel == null || rightFilePanel == null) {
			setupUI();
		}
		loadFiles(currentLeftPath, currentRightPath);
	}

	/**
	 * Returns the currently focused FilePanel.
	 * @return The focused FilePanel, or leftFilePanel by default.
	 */
	public FilePanel getCurrentPanel() {
		View v = getCurrentFocus();
		if (v != null) {
			if (binding.rightFileList.hasFocus()) return rightFilePanel;
			if (binding.leftFileList.hasFocus()) return leftFilePanel;
		}
		// Default to left panel if focus is unclear
		return leftFilePanel;
	}

	/**
	 * Loads files into both panels, ensuring panels are initialized.
	 * @param currentLeftPath Path for left panel
	 * @param currentRightPath Path for right panel
	 */
	private void loadFiles(String currentLeftPath, String currentRightPath) {
		if (rightFilePanel != null) {
			rightFilePanel.openFolder(currentRightPath);
		}
		if (leftFilePanel != null) {
			leftFilePanel.openFolder(currentLeftPath);
		}
	}

	/**
	 * Sets up the UI and initializes file panels.
	 */
	private void setupUI() {
		rightFilePanel = new FilePanel(currentRightPath, binding.rightFileList);
		leftFilePanel = new FilePanel(currentLeftPath, binding.leftFileList);
	}

	/**
	 * Shows a dialog to create a new file or folder.
	 */
	private void showNewFileDialog() {
		MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
		dialogBuilder
			.setTitle(R.string.new_file_folder_title)
			.setMessage(R.string.new_file_folder_message)
			.setCancelable(true);
		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * Initializes the options menu and handles menu item clicks.
	 */
	public void initOptionsMenu() {
		binding.toolbar.setOnMenuItemClickListener((item) -> {
			final int id = item.getItemId();
			if (id == R.id.action_refresh) {
				loadFiles(currentLeftPath, currentRightPath);
				return true;
			} else if (id == R.id.action_settings) {
				startMyActivity(SettingsActivity.class);
				finish();
				return true;
			} else if (id == R.id.action_new) {
				showNewFileDialog();
				return true;
			} else if (id == R.id.sort) {
				showSortDialog();
				return true;
			} else {
				return super.onOptionsItemSelected(item);
			}
		});
	}

	/**
	 * Shows a dialog to select sort type and order, remembers last selection, and applies sorting.
	 */
	private void showSortDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.sort_by_title);
		builder.setCancelable(true);

		builder.setSingleChoiceItems(sortTypes, lastSortTypeIndex, (di, i) -> {
			lastSortTypeIndex = i;
		});

		final CheckBox rev = new CheckBox(this);
		rev.setText(R.string.reverse);
		rev.setChecked(inReverse);
		rev.setOnCheckedChangeListener((v, s) -> {
			inReverse = s;
		});
		builder.setView(rev);

		builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
			try {
				sortType = ItemFileAdapter.SortType.valueOf(sortTypes[lastSortTypeIndex]);
				FilePanel panel = getCurrentPanel();
				if (panel != null) {
					panel.sort(sortType, inReverse);
				}
			} catch (IllegalArgumentException e) {
				Toast.makeText(this, "Invalid sort type selected", Toast.LENGTH_SHORT).show();
			}
		});

		builder.setNegativeButton(R.string.cancel, (di, i) -> {
			di.dismiss();
		});

		builder.create().show();
	}
}
