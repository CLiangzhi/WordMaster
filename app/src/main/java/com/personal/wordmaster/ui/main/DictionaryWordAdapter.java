package com.personal.wordmaster.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.wordmaster.R;
import com.personal.wordmaster.data.entity.DictionaryWord;

import java.util.ArrayList;
import java.util.List;

public class DictionaryWordAdapter extends RecyclerView.Adapter<DictionaryWordAdapter.ViewHolder> {

    private List<DictionaryWord> words = new ArrayList<>();

    public void setWords(List<DictionaryWord> words) {
        this.words = words == null ? new ArrayList<>() : words;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_dictionary_word, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DictionaryWord w = words.get(position);
        holder.tvWord.setText(w.getWord());
        holder.tvMeaning.setText(w.getMeaning());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord, tvMeaning;

        ViewHolder(View v) {
            super(v);
            tvWord = v.findViewById(R.id.tv_dict_word);
            tvMeaning = v.findViewById(R.id.tv_dict_meaning);
        }
    }
}
