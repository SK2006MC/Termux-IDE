package com.termux.termxide.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.termux.termxide.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

	ActivityAboutBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityAboutBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
	}

}
