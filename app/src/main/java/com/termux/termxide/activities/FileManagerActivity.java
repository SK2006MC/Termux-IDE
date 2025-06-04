package com.termux.termxide.activities;

import android.os.Bundle;
import android.view.Menu;

import com.termux.termxide.R;
import com.termux.termxide.databinding.ActivityFilemanagerBinding;

public class FileManagerActivity extends BaseActivity {

	ActivityFilemanagerBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	private void initNavView() {
		binding.fileNav.setNavigationItemSelectedListener(item -> {
			int id = item.getItemId();
			if (id == R.id.nav_settings) {
				startMyActivity(SettingsActivity.class);
			} else if (id == R.id.nav_about) {
				startMyActivity(AboutActivity.class);
			} else if (id == R.id.nav_logs) {
				startMyActivity(LogActivity.class);
			} else {
				return false;
			}
			return true;
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("a");
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
