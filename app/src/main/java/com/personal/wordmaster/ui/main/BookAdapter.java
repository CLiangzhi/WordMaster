package com.personal.wordmaster.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.wordmaster.R;
import com.personal.wordmaster.data.entity.WordBook;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(WordBook book);
    }

    public interface OnDeleteClickListener {
        void onDelete(WordBook book);
    }

    private List<WordBook> books = new ArrayList<>();
    private final OnItemClickListener itemListener;
    private final OnDeleteClickListener deleteListener;

    public BookAdapter(OnItemClickListener itemListener, OnDeleteClickListener deleteListener) {
        this.itemListener = itemListener;
        this.deleteListener = deleteListener;
    }

    public void setBooks(List<WordBook> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordBook book = books.get(position);
        holder.tvName.setText(book.getName());
        holder.tvTotal.setText("总计: " + book.getTotalCount());
        holder.tvMastered.setText("已掌握: " + book.getMasteredCount());
        holder.tvUnfamiliar.setText("不熟: " + book.getUnfamiliarCount());

        holder.itemView.setOnClickListener(v -> itemListener.onClick(book));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTotal, tvMastered, tvUnfamiliar;
        View btnDelete;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_book_name);
            tvTotal = v.findViewById(R.id.tv_book_total);
            tvMastered = v.findViewById(R.id.tv_book_mastered);
            tvUnfamiliar = v.findViewById(R.id.tv_book_unfamiliar);
            btnDelete = v.findViewById(R.id.btn_delete);
        }
    }
}
