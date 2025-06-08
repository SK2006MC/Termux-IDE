package com.termux.termxide.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.termux.termxide.R;
import com.termux.termxide.services.FileOperationService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
	private final List<FileOperationService.FileMetadata> files;
	private final FileOperationService fileOperationService;
	private OnItemClickListener listener;
	Context context;

	public FileAdapter(Context context,FileOperationService fileOperationService) {
		this.fileOperationService = fileOperationService;
		this.files = new ArrayList<>();
		this.context = context;
	}

	public void setFiles(List<FileOperationService.FileMetadata> files) {
		this.files.clear();
		this.files.addAll(files);
		notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	@NonNull
	@Override
	public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_file, parent, false);
		return new FileViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
		FileOperationService.FileMetadata file = files.get(position);
		holder.bind(file);

		if (listener != null) {
			holder.itemView.setOnClickListener(v -> listener.onItemClick(new File(file.getPath()), position));
		}
	}

	@Override
	public int getItemCount() {
		return files.size();
	}

	public interface OnItemClickListener {
		void onItemClick(File file, int position);
	}

	class FileViewHolder extends RecyclerView.ViewHolder {
		private final ImageView icon;
		private final TextView name;
		private final TextView time;
		private final TextView size;

		public FileViewHolder(@NonNull View itemView) {
			super(itemView);
			icon = itemView.findViewById(R.id.imageView_icon);
			name = itemView.findViewById(R.id.textView_name);
			time = itemView.findViewById(R.id.textView_time);
			size = itemView.findViewById(R.id.textView_size);
		}

		public void bind(FileOperationService.FileMetadata file) {
			// Set icon based on file type
			if (file.isDirectory()) {
				icon.setImageResource(R.drawable.baseline_folder_24);
			} else {
				icon.setImageResource(R.drawable.baseline_insert_drive_file_24);
			}

			// Set file name
			name.setText(file.getName());

			// Format and set time
			long timeInMillis = file.getLastModified();
			String formattedTime = formatTime(timeInMillis);
			time.setText(formattedTime);

			// Set size only for files
			if (!file.isDirectory()) {
				String formattedSize = formatSize(file.getSize());
				size.setText(formattedSize);
			}
		}

		private String formatTime(long timeInMillis) {
			// TODO: Implement proper time formatting
			return "" + timeInMillis;
		}

		private String formatSize(long bytes) {
			if (bytes == 0) return "0 B";
			int unit = 1024;
			if (bytes < unit) return bytes + " B";
			int exp = (int) (Math.log(bytes) / Math.log(unit));
			String pre = "KMGTPE".charAt(exp - 1) + "";
			return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
		}
	}
}
