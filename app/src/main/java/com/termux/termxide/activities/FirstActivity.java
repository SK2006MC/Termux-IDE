package com.termux.termxide.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.termux.termxide.databinding.ActivityFirstBinding;
import com.termux.termxide.managers.SettingsManager;

public class FirstActivity extends BaseActivity {

	private static final int REQUEST_CODE_PICK_FOLDER = 101;
	private static final int PERMISSION_REQUEST_STORAGE = 102;
	ActivityFirstBinding binding;
	SettingsManager settingsManager;

	// ActivityResultLauncher for folder picking (Storage Access Framework)
	private ActivityResultLauncher<Intent> folderPickerLauncher;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityFirstBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		settingsManager = new SettingsManager(this);

		// Disable Start button until permission is granted
		binding.btnStart.setEnabled(isStoragePermissionGranted());

		// Register folder picker launcher
		folderPickerLauncher = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(),
				result -> {
					if (result.getResultCode() == RESULT_OK && result.getData() != null) {
						// Handle picked folder URI
						Uri folderUri = result.getData().getData();
						if (folderUri != null) {
							alert("Folder selected: " + folderUri.toString());
							// Persist permission if needed
							final int takeFlags = result.getData().getFlags()
									& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
							getContentResolver().takePersistableUriPermission(folderUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
							// Save folderUri to settings if needed
							settingsManager.setHomePath(folderUri.toString());
						}
					} else {
						alert("No folder selected");
					}
				}
		);

		initUi();
	}

	private void initUi() {
		binding.btnGrantPermission.setOnClickListener(v -> {
			if (!isStoragePermissionGranted()) {
				// For Android 11+ use Storage Access Framework, otherwise request legacy permission
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
					// Optionally, you can request MANAGE_EXTERNAL_STORAGE, but it's discouraged
					// Instead, use folder picker
					openFolderPicker();
				} else {
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
				}
			} else {
				alert("Permission already granted");
				binding.btnStart.setEnabled(true);
			}
		});

		binding.btnStart.setOnClickListener(v -> {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		});
	}

	private boolean isStoragePermissionGranted() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			// For Android 11+, check if we have access to at least one folder (or use MANAGE_EXTERNAL_STORAGE if needed)
			// For simplicity, just return true if you want to allow folder picker only
			return true;
		} else {
			return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
		}
	}

	private void openFolderPicker() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
		folderPickerLauncher.launch(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permission, grantResults);
		if (requestCode == PERMISSION_REQUEST_STORAGE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				alert("Permission Granted");
				binding.btnStart.setEnabled(true);
			} else {
				alert("Permission Denied");
				binding.btnStart.setEnabled(false);
				finish();
			}
		}
	}
}
