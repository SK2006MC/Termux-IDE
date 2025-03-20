package com.termux.termxide.activities;

import android.os.Bundle;

import com.termux.termxide.databinding.ActivityProjectsBinding;

public class ProjectsActivity extends BaseActivity {
	ActivityProjectsBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityProjectsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
	}

}
