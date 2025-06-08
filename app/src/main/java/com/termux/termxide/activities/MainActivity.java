package com.termux.termxide.activities;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import com.termux.termxide.databinding.ActivityMainBinding;
import com.termux.termxide.managers.SettingsManager;

public class MainActivity extends BaseActivity {

	private SettingsManager settingsManager;
	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsManager = new SettingsManager(this);

		 if (settingsManager.getIsFirst()) {
		 	startMyActivity(FirstActivity.class);
		 	finish();
		 }

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.fileManager.setOnClickListener(v->startMyActivity(FileManagerActivity.class));
		binding.codeEditor.setOnClickListener(v->startMyActivity(CodeEditorActivity.class));
		binding.ide.setOnClickListener(v->startMyActivity(IdeActivity.class));
		binding.projects.setOnClickListener(v->startMyActivity(ProjectsActivity.class));
		binding.terminal.setOnClickListener(v->startMyActivity(TerminalActivity.class));
		binding.exit.setOnClickListener(v->finish());

		initOnBackPressB(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				//
			}
		});
	}


}