package com.termux.termxide.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.termux.termxide.databinding.FragmentFileViewBinding;

public class FileViewFragment extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	FragmentFileViewBinding binding;
	private String mParam1;
	private String mParam2;


	public FileViewFragment() {
		// Required empty public constructor
	}

	public static FileViewFragment newInstance(String param1, String param2) {
		FileViewFragment fragment = new FileViewFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentFileViewBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}
}