package com.personal.wordmaster.ui.learn;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.personal.wordmaster.R;
import com.personal.wordmaster.base.BaseActivity;
import com.personal.wordmaster.data.entity.WordInfo;
import com.personal.wordmaster.viewmodel.LearnViewModel;

public class LearnActivity extends BaseActivity {

    private LearnViewModel viewModel;
    private TextView tvWord;
    private TextView tvMeaning;
    private TextView tvProgress;
    private View divider;
    private View cardWord;
    private View llButtons;
    private View llComplete;
    private View btnKnown;
    private View btnUnknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        long bookId = getIntent().getLongExtra("book_id", -1);
        if (bookId == -1) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(LearnViewModel.class);

        tvWord = findViewById(R.id.tv_word);
        tvMeaning = findViewById(R.id.tv_meaning);
        tvProgress = findViewById(R.id.tv_progress);
        divider = findViewById(R.id.divider);
        cardWord = findViewById(R.id.card_word);
        llButtons = findViewById(R.id.ll_buttons);
        llComplete = findViewById(R.id.ll_complete);

        btnKnown = findViewById(R.id.btn_known);
        btnUnknown = findViewById(R.id.btn_unknown);

        btnKnown.setOnClickListener(v -> onKnown());
        btnUnknown.setOnClickListener(v -> onUnknown());

        observeViewModel();
        viewModel.loadWords(bookId);
    }

    private void onKnown() {
        showMeaningOnly();
        btnKnown.setEnabled(false);
        btnUnknown.setEnabled(false);
        cardWord.postDelayed(() -> {
            viewModel.onKnown();
            btnKnown.setEnabled(true);
            btnUnknown.setEnabled(true);
        }, 800);
    }

    private void onUnknown() {
        showMeaningOnly();
        btnKnown.setEnabled(false);
        btnUnknown.setEnabled(false);
        cardWord.postDelayed(() -> {
            viewModel.onUnknown();
            btnKnown.setEnabled(true);
            btnUnknown.setEnabled(true);
        }, 800);
    }

    private void showMeaningOnly() {
        WordInfo w = viewModel.currentWord.getValue();
        if (w != null) {
            tvMeaning.setText(w.getMeaning());
            tvMeaning.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }
    }

    private void observeViewModel() {
        viewModel.currentWord.observe(this, word -> {
            if (word != null) {
                tvWord.setText(word.getWord());
                tvMeaning.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
        });

        viewModel.progress.observe(this, p -> {
            if (p != null) {
                tvProgress.setText(p + " / " + viewModel.getTotalCount());
            }
        });

        viewModel.complete.observe(this, complete -> {
            if (Boolean.TRUE.equals(complete)) {
                cardWord.setVisibility(View.GONE);
                llButtons.setVisibility(View.GONE);
                llComplete.setVisibility(View.VISIBLE);
            }
        });
    }
}
