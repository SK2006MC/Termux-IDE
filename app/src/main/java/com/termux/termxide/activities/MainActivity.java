package com.termux.termxide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.termxide.databinding.ActivityMainBinding;
import com.termux.termxide.managers.SettingsManager;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private SettingsManager settingsManager;
	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsManager = new SettingsManager(this);

		// if (settingsManager.getIsFirst()) {
		// 	startMyActivity(FirstActivity.class);
		// 	finish();
		// }

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI();
//		initNavView();
		initOnBackPressed();
	}

	private void initializeUI() {
		
	}

	public void initOnBackPressed() {
		getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}