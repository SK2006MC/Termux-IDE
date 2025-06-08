package com.termux.termxide.activities;

import android.content.Intent;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.termxide.TermXIDE;

public class BaseActivity extends AppCompatActivity {

	String TAG = this.getClass().getSimpleName();

	public void initOnBackPressB(OnBackPressedCallback callback) {
		getOnBackPressedDispatcher().addCallback(callback);
	}

	public TermXIDE getTermXIDE() {
		return (TermXIDE) getApplication();
	}

//	SettingsManager getSettingsManager(){
//		return
//	}

	void startMyActivity(Class<?> activityClass) {
		startActivity(new Intent(this, activityClass));
	}

	void alert(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}