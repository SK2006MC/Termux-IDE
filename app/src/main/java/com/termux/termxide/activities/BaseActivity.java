package com.termux.termxide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initOnCreate();
		initUi();
	}

	public void initOnCreate(){

	}

	public void initUi(){

	}

	public void initOnDestroy(){

	}

	public void initOnBackPress(OnBackPressedCallback callback){
		getOnBackPressedDispatcher().addCallback(callback);
	}

	void startMyActivity(Class<?> activityClass) {
		startActivity(new Intent(this, activityClass));
	}

	void showAlert(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		initOnDestroy();
	}
}