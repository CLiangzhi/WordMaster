package com.personal.wordmaster.ui.review;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.personal.wordmaster.R;
import com.personal.wordmaster.base.BaseActivity;
import com.personal.wordmaster.data.entity.WordInfo;
import com.personal.wordmaster.viewmodel.ReviewViewModel;

public class ReviewActivity extends BaseActivity {

    private ReviewViewModel viewModel;
    private TextView tvWord;
    private TextView tvMeaning;
    private View divider;
    private View cardWord;
    private View llAnswerArea;
    private View llComplete;
    private View tvEmpty;
    private View btnKnown;
    private View btnUnknown;
    private View btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        tvWord = findViewById(R.id.tv_word);
        tvMeaning = findViewById(R.id.tv_meaning);
        divider = findViewById(R.id.divider);
        cardWord = findViewById(R.id.card_word);
        llAnswerArea = findViewById(R.id.ll_answer_area);
        llComplete = findViewById(R.id.ll_complete);
        tvEmpty = findViewById(R.id.tv_empty);
        btnKnown = findViewById(R.id.btn_known);
        btnUnknown = findViewById(R.id.btn_unknown);
        btnNext = findViewById(R.id.btn_next);

        btnKnown.setOnClickListener(v -> onKnown());
        btnUnknown.setOnClickListener(v -> onUnknown());
        btnNext.setOnClickListener(v -> onNext());

        observeViewModel();
        viewModel.loadReviewWords();
    }

    private void onKnown() {
        viewModel.recordKnown();
        showMeaningOnly();
        btnKnown.setEnabled(false);
        btnUnknown.setEnabled(false);
        btnNext.setVisibility(View.VISIBLE);
    }

    private void onUnknown() {
        viewModel.recordUnknown();
        showMeaningOnly();
        btnKnown.setEnabled(false);
        btnUnknown.setEnabled(false);
        btnNext.setVisibility(View.VISIBLE);
    }

    private void onNext() {
        btnKnown.setEnabled(true);
        btnUnknown.setEnabled(true);
        btnNext.setVisibility(View.GONE);
        viewModel.nextWord();
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

        viewModel.complete.observe(this, complete -> {
            if (Boolean.TRUE.equals(complete)) {
                cardWord.setVisibility(View.GONE);
                llAnswerArea.setVisibility(View.GONE);
                llComplete.setVisibility(View.VISIBLE);
            }
        });

        viewModel.empty.observe(this, empty -> {
            if (Boolean.TRUE.equals(empty)) {
                cardWord.setVisibility(View.GONE);
                llAnswerArea.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}
