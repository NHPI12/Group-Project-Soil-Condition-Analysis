package vn.edu.usth.soicondition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private List<String> languages;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    private int selectedPosition = 0;

    public LanguageAdapter(Context context, String[] languages, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.languages = new ArrayList<>(Arrays.asList(languages)); // Convert array to list for manipulation
        this.listener = listener;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.textView.setText(languages.get(position));
        holder.itemView.setOnClickListener(v -> {
            // Notify the previous item and the clicked item of the change
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);

            // Call the onItemClick method of the OnItemClickListener
            listener.onItemClick(languages.get(position), position);
        });

        // Highlight the selected item in some way
        holder.itemView.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    static class LanguageViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
    public void setLanguages(List<String> newLanguages) {
        languages.clear();
        languages.addAll(newLanguages);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String language, int position);
    }
    public String getSelectedLanguage() {
        return languages.get(selectedPosition);
    }
}