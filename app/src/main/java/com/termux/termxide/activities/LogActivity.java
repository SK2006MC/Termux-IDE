package com.termux.termxide.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.termux.termxide.R;
import com.termux.termxide.databinding.ActivityLogBinding;
import com.termux.termxide.databinding.ItemLogBinding;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {

	private ActivityLogBinding binding;
	private LogRecyclerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLogBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		setupRecyclerView();
		binding.refreshButton.setOnClickListener(v -> refreshLogs());
	}

	private void setupRecyclerView() {
		RecyclerView recyclerView = binding.logsRecyclerView;
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
		dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
		recyclerView.addItemDecoration(dividerItemDecoration);

		List<ItemLog> logs = new ArrayList<>();
		adapter = new LogRecyclerAdapter(logs);
		recyclerView.setAdapter(adapter);
	}

	private void refreshLogs() {
		List<ItemLog> newLogs = new ArrayList<>();
		adapter.setLogs(newLogs);
		adapter.notifyDataSetChanged();
	}

	private class ItemLog {
		public String timestamp, pid, tid, logLevel, tag, msg;
	}

	private class LogRecyclerAdapter extends RecyclerView.Adapter<LogRecyclerAdapter.ViewHolder> {

		List<ItemLog> logs;

		public LogRecyclerAdapter(List<ItemLog> logs) {
			this.logs = logs;
		}

		@NonNull

		public void setLogs(List<ItemLog> logs) {
			this.logs = logs;
			notifyDataSetChanged();
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return new ViewHolder(ItemLogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			holder.bind(logs.get(position));
		}

		@Override
		public int getItemCount() {
			return logs.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			ItemLogBinding binding;

			public ViewHolder(@NonNull ItemLogBinding binding) {
				super(binding.getRoot());
				this.binding = binding;
			}

			public void bind(ItemLog log) {

			}
		}
	}
}