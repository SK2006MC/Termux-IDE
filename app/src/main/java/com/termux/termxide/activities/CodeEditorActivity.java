package com.termux.termxide.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.termux.termxide.R;
import com.termux.termxide.ui.CodeEditorView;
import com.termux.termxide.databinding.ActivityCodeEditorBinding;

import java.io.File;

public class CodeEditorActivity extends AppCompatActivity {
    private ActivityCodeEditorBinding binding;
    private CodeEditorView codeEditor;
    private File currentFile;
    private String currentFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCodeEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize UI components
        setupUI();

        // Get file path from intent
        currentFilePath = getIntent().getStringExtra("file_path");
        if (currentFilePath != null) {
            currentFile = new File(currentFilePath);
            loadFile();
        }
    }

    private void setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar);

        // Setup code editor
        codeEditor = binding.codeEditor;

        // Setup bottom app bar
        setSupportActionBar(binding.bottomToolbar);

        // Setup FABs
        FloatingActionButton fabSave = binding.fabSave;
        FloatingActionButton fabUndo = binding.fabUndo;
        FloatingActionButton fabRedo = binding.fabRedo;

        fabSave.setOnClickListener(v -> saveFile());
        fabUndo.setOnClickListener(v -> codeEditor.undo());
        fabRedo.setOnClickListener(v -> codeEditor.redo());
    }

    private void loadFile() {
        if (currentFile == null || !currentFile.exists()) {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement file loading
        Toast.makeText(this, "Loading file: " + currentFile.getName(), Toast.LENGTH_SHORT).show();
    }

    private void saveFile() {
        if (currentFile == null) {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement file saving
        Toast.makeText(this, "Saving file: " + currentFile.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_code_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // TODO: Show search dialog
            return true;
        } else if (id == R.id.action_replace) {
            // TODO: Show replace dialog
            return true;
        } else if (id == R.id.action_settings) {
            // TODO: Open settings
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
