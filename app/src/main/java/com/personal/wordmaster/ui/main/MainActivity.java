package com.personal.wordmaster.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.wordmaster.R;
import com.personal.wordmaster.base.BaseActivity;
import com.personal.wordmaster.data.entity.StudyRecord;
import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.ui.book.BookDetailActivity;
import com.personal.wordmaster.ui.review.ReviewActivity;
import com.personal.wordmaster.ui.upload.UploadActivity;
import com.personal.wordmaster.utils.ApiKeyUtils;
import com.personal.wordmaster.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends BaseActivity {

    private MainViewModel viewModel;
    private RecyclerView rvBooks;
    private TextView tvEmpty;
    private TextView tvTodayLearn;
    private TextView tvTodayReview;
    private BookAdapter adapter;

    private EditText etSearch;
    private RecyclerView rvSearchResults;
    private TextView tvSearchEmpty;
    private DictionaryWordAdapter searchAdapter;
    private View cardStats, btnReview, tvSectionTitle, btnNewBook, btnApiKey;
    private boolean hasBooks = false;
    private boolean searching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        rvBooks = findViewById(R.id.rv_books);
        tvEmpty = findViewById(R.id.tv_empty);
        tvTodayLearn = findViewById(R.id.tv_today_learn);
        tvTodayReview = findViewById(R.id.tv_today_review);

        cardStats = findViewById(R.id.card_stats);
        btnReview = findViewById(R.id.btn_review);
        tvSectionTitle = findViewById(R.id.tv_section_title);
        btnNewBook = findViewById(R.id.btn_new_book);
        btnApiKey = findViewById(R.id.btn_api_key);
        etSearch = findViewById(R.id.et_search);
        rvSearchResults = findViewById(R.id.rv_search_results);
        tvSearchEmpty = findViewById(R.id.tv_search_empty);

        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            intent.putExtra("book_name", book.getName());
            startActivity(intent);
        }, book -> viewModel.deleteBook(book.getId()));
        rvBooks.setAdapter(adapter);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new DictionaryWordAdapter();
        rvSearchResults.setAdapter(searchAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        findViewById(R.id.btn_new_book).setOnClickListener(v ->
            startActivity(new Intent(this, UploadActivity.class))
        );

        findViewById(R.id.btn_review).setOnClickListener(v ->
            startActivity(new Intent(this, ReviewActivity.class))
        );

        findViewById(R.id.btn_api_key).setOnClickListener(v -> showApiKeyDialog());

        observeData();
    }

    private void showApiKeyDialog() {
        String current = ApiKeyUtils.getApiKey();

        EditText input = new EditText(this);
        input.setHint("sk-你的API-Key");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(current);
        input.setSelection(input.getText().length());
        int pad = (int) (20 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, pad, pad, pad);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("设置 API Key")
            .setMessage("在 platform.deepseek.com 申请，按量计费价格很低")
            .setView(input)
            .setPositiveButton("保存", null)
            .setNeutralButton("删除", null)
            .setNegativeButton("取消", null)
            .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String key = input.getText().toString().trim();
                if (key.isEmpty()) {
                    Toast.makeText(this, "Key 不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiKeyUtils.saveApiKey(key);
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                ApiKeyUtils.deleteApiKey();
                Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void observeData() {
        viewModel.getAllBooks().observe(this, books -> {
            hasBooks = books != null && !books.isEmpty();
            if (searching) return;
            if (hasBooks) {
                tvEmpty.setVisibility(View.GONE);
                rvBooks.setVisibility(View.VISIBLE);
                adapter.setBooks(books);
            } else {
                tvEmpty.setVisibility(View.VISIBLE);
                rvBooks.setVisibility(View.GONE);
            }
        });

        viewModel.getTodayRecord().observe(this, record -> {
            if (record != null) {
                tvTodayLearn.setText(String.valueOf(record.getNewWordCount()));
                tvTodayReview.setText(String.valueOf(record.getReviewWordCount()));
            }
        });

        viewModel.searchResults.observe(this, results -> {
            if (results == null) {
                setSearchMode(false);
            } else {
                setSearchMode(true);
                searchAdapter.setWords(results);
                boolean empty = results.isEmpty();
                tvSearchEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
                rvSearchResults.setVisibility(empty ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void setSearchMode(boolean s) {
        searching = s;
        cardStats.setVisibility(s ? View.GONE : View.VISIBLE);
        btnReview.setVisibility(s ? View.GONE : View.VISIBLE);
        tvSectionTitle.setVisibility(s ? View.GONE : View.VISIBLE);
        btnNewBook.setVisibility(s ? View.GONE : View.VISIBLE);
        btnApiKey.setVisibility(s ? View.GONE : View.VISIBLE);
        if (s) {
            tvEmpty.setVisibility(View.GONE);
            rvBooks.setVisibility(View.GONE);
        } else {
            rvSearchResults.setVisibility(View.GONE);
            tvSearchEmpty.setVisibility(View.GONE);
            rvBooks.setVisibility(hasBooks ? View.VISIBLE : View.GONE);
            tvEmpty.setVisibility(hasBooks ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
