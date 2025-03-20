package com.termux.termxide.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class CodeEditorView extends AppCompatEditText {
	private static final int LINE_NUMBER_WIDTH = 40;
	private static final int GUTTER_PADDING = 8;
	private Paint lineNumberPaint;
	private Paint gutterPaint;
	private Rect lineNumberBounds;

	public CodeEditorView(@NonNull Context context) {
		this(context, null);
	}

	public CodeEditorView(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public CodeEditorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		// Set up line number paint
		lineNumberPaint = new Paint();
		lineNumberPaint.setColor(Color.LTGRAY);
		lineNumberPaint.setTextSize(getTextSize());
		lineNumberPaint.setAntiAlias(true);

		// Set up gutter paint
		gutterPaint = new Paint();
		gutterPaint.setColor(Color.DKGRAY);

		// Set up bounds for line numbers
		lineNumberBounds = new Rect();

		// Set up text properties
		setSingleLine(false);
		setHorizontallyScrolling(false);
		setVerticalScrollBarEnabled(true);
		setHorizontalScrollBarEnabled(true);
		setGravity(android.view.Gravity.TOP | android.view.Gravity.START);
		setPadding(LINE_NUMBER_WIDTH + GUTTER_PADDING, 0, 0, 0);

		// Add text change listener for syntax highlighting
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO: Implement syntax highlighting
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		// Set up keyboard action
		setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE ||
					(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
				// Handle enter key
				return true;
			}
			return false;
		});

		// Set up touch listener for better text selection
		setOnTouchListener((v, event) -> {
			v.performClick();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				int line = getLayout().getLineForVertical(y);
				int offset = getLayout().getOffsetForHorizontal(line, x);
				setSelection(offset);
				return true;
			}
			return false;
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Draw line numbers
		int firstVisibleLine = getLayout().getLineForVertical(getScrollY());
		int lastVisibleLine = getLayout().getLineForVertical(getScrollY() + getHeight());

		for (int i = firstVisibleLine; i <= lastVisibleLine; i++) {
			if (i >= getLayout().getLineCount()) break;

			String lineNumber = String.valueOf(i + 1);
			int baseline = getLayout().getLineBaseline(i) - getScrollY();

			// Draw line number background
			canvas.drawRect(0, baseline - getLineHeight(), LINE_NUMBER_WIDTH + GUTTER_PADDING,
					baseline, gutterPaint);

			// Draw line number text
			lineNumberPaint.getTextBounds(lineNumber, 0, lineNumber.length(), lineNumberBounds);
			canvas.drawText(lineNumber, LINE_NUMBER_WIDTH - lineNumberBounds.width() - GUTTER_PADDING,
					baseline, lineNumberPaint);
		}
	}

	// TODO: Implement syntax highlighting methods
	private void applySyntaxHighlighting() {
		// TODO: Implement syntax highlighting
	}

	// TODO: Implement search functionality
	public void search(String query) {
		// TODO: Implement search
	}

	// TODO: Implement replace functionality
	public void replace(String find, String replace) {
		// TODO: Implement replace
	}

	// Undo/Redo functionality
	public void undo() {
		if (getEditableText() != null) {
			getEditableText().clear();
		}
	}

	public void redo() {
		// TODO: Implement redo
	}
}
