package com.termux.termxide.adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.termux.termxide.R;
import com.termux.termxide.data.ItemFile;
import com.termux.termxide.databinding.ItemFileBinding;
import com.termux.termxide.services.FileOperationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class ItemFileAdapter extends RecyclerView.Adapter<ItemFileAdapter.FileViewHolder> {
	private final List<ItemFile> files;
	Context context;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.ENGLISH);
	private OnItemClickListener listener;

	public ItemFileAdapter(Context context) {
		this.files = new ArrayList<>();
		this.context = context;
	}

	public void setFiles(List<ItemFile> files) {
		this.files.clear();
		this.files.addAll(files);
		notifyDataSetChanged();
	}

	public void loadFiles(String pathStr) throws IOException {
		if (pathStr == null || pathStr.isEmpty()) {
			throw new IOException("Invalid path: path cannot be null or empty");
		}

		files.clear();
		notifyDataSetChanged();
		
		Path folder = Paths.get(pathStr);
		if (!Files.exists(folder)) {
			throw new IOException("Path does not exist: " + pathStr);
		}
		if (!Files.isDirectory(folder)) {
			throw new IOException("Path is not a directory: " + pathStr);
		}

		try (Stream<Path> walk = Files.walk(folder, 1)) {
			walk.sorted()
				.filter(path -> !path.equals(folder)) // Skip the root folder itself
				.forEach(path -> {
					ItemFile file = FileOperationService.getFileMetadata(path.toFile());
					if (file != null) {
						files.add(file);
						notifyItemInserted(files.size() - 1);
					}
				});
		} catch (Exception e) {
			throw new IOException("Error loading files from: " + pathStr, e);
		}
	}

	public void sort(SortType type, boolean reverse) {
		if (files == null || files.isEmpty()) {
			return;
		}

		Comparator<ItemFile> comparator;
		switch (type) {
			case NAME:
				comparator = Comparator.comparing(ItemFile::getName, String.CASE_INSENSITIVE_ORDER);
				break;
			case SIZE:
				comparator = Comparator.comparing(ItemFile::getSize);
				break;
			case TYPE:
				comparator = Comparator.comparing(ItemFile::isDirectory);
				break;
			case TIME:
				comparator = Comparator.comparing(ItemFile::getLastModified);
				break;
			default:
				comparator = Comparator.comparing(ItemFile::getName, String.CASE_INSENSITIVE_ORDER);
		}

		if (reverse) {
			comparator = comparator.reversed();
		}

		files.sort(comparator);
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
		ItemFile file = files.get(position);
		holder.bind(file);

		if (listener != null) {
			holder.itemView.setOnClickListener(v -> listener.onItemClick(new File(file.getPath()), position));
		}
	}

	@Override
	public int getItemCount() {
		return files.size();
	}

	public enum SortType {
		NAME, SIZE, TYPE, TIME
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
			ItemFileBinding binding = ItemFileBinding.bind(itemView);
			icon = binding.imageViewIcon;
			name = binding.textViewName;
			time = binding.textViewTime;
			size = binding.textViewSize;
		}

		public void bind(ItemFile file) {
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
			} else {
				size.setText("");
			}
		}


		private String formatTime(long timeInMillis) {
			//must returns time in 'dd-mm-yy hh:mm' format
			return sdf.format(timeInMillis);
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

