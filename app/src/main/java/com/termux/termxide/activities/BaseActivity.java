package com.termux.termxide.activities;

import android.content.Intent;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.termxide.TermXIDE;

public class BaseActivity extends AppCompatActivity {

	protected String TAG = this.getClass().getSimpleName();

	protected void initOnBackPressB(OnBackPressedCallback callback) {
		getOnBackPressedDispatcher().addCallback(callback);
	}

	public TermXIDE getTermXIDE() {
		return (TermXIDE) getApplication();
	}

//	SettingsManager getSettingsManager(){
//		return
//	}

	protected void startMyActivity(Class<?> activityClass) {
		startActivity(new Intent(this, activityClass));
	}

	protected void alert(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}