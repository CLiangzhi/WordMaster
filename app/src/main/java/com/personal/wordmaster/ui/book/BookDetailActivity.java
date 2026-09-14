package com.personal.wordmaster.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.personal.wordmaster.R;
import com.personal.wordmaster.base.BaseActivity;
import com.personal.wordmaster.ui.learn.LearnActivity;
import com.personal.wordmaster.ui.review.ReviewActivity;
import com.personal.wordmaster.viewmodel.BookDetailViewModel;

public class BookDetailActivity extends BaseActivity {

    private BookDetailViewModel viewModel;
    private long bookId;
    private String bookName;
    private TextView tvBookName;
    private TextView tvTotal;
    private TextView tvMastered;
    private TextView tvUnfamiliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookId = getIntent().getLongExtra("book_id", -1);
        bookName = getIntent().getStringExtra("book_name");
        if (bookId == -1) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);

        tvBookName = findViewById(R.id.tv_book_name);
        tvBookName.setText(bookName);

        tvTotal = findViewById(R.id.tv_total);
        tvMastered = findViewById(R.id.tv_mastered);
        tvUnfamiliar = findViewById(R.id.tv_unfamiliar);

        findViewById(R.id.btn_start_learn).setOnClickListener(v -> {
            Intent intent = new Intent(this, LearnActivity.class);
            intent.putExtra("book_id", bookId);
            startActivity(intent);
        });

        findViewById(R.id.btn_start_review).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("book_id", bookId);
            startActivity(intent);
        });

        observeData();
    }

    private void observeData() {
        viewModel.getBookById(bookId).observe(this, book -> {
            if (book != null) {
                tvTotal.setText(String.valueOf(book.getTotalCount()));
                tvMastered.setText(String.valueOf(book.getMasteredCount()));
                tvUnfamiliar.setText(String.valueOf(book.getUnfamiliarCount()));
            }
        });
    }
}
