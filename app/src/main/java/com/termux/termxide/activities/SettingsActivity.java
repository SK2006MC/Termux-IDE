package com.termux.termxide.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.termux.termxide.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}
}