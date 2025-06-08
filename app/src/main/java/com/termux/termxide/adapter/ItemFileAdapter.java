package com.termux.termxide.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.termux.termxide.data.ItemFile;
import com.termux.termxide.databinding.ItemFileBinding;

import java.util.List;

public class ItemFileAdapter extends RecyclerView.Adapter<ItemFileAdapter.FileViewHolder> {

	List<ItemFile> fileList;

	void setFileList(List<ItemFile> fileList) {
		this.fileList = fileList;
	}

	@NonNull
	@Override
	public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		ItemFileBinding binding = ItemFileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		return new FileViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
		holder.bind(fileList.get(position));
	}

	@Override
	public int getItemCount() {
		return fileList.size();
	}

	public static class FileViewHolder extends RecyclerView.ViewHolder {
		ItemFileBinding binding;

		public FileViewHolder(@NonNull ItemFileBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}

		public void bind(ItemFile file) {

		}
	}
}
