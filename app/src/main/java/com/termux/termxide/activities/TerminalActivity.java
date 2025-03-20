package com.termux.termxide.activities;

import android.os.Bundle;

import com.termux.shared.termux.terminal.TermuxTerminalSessionClientBase;
import com.termux.shared.termux.terminal.TermuxTerminalViewClientBase;
import com.termux.termxide.databinding.ActivityTerminalBinding;

public class TerminalActivity extends BaseActivity {
	ActivityTerminalBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityTerminalBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		TermuxTerminalSessionClientBase terminalSessionClientBase = new TermuxTerminalSessionClientBase();
		TermuxTerminalViewClientBase terminalViewClientBase = new TermuxTerminalViewClientBase();
		binding.terminalView.setTerminalViewClient(terminalViewClientBase);
	}
}
