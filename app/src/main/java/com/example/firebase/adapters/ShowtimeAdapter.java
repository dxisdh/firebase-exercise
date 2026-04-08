package com.example.firebase.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firebase.databinding.ItemShowtimeBinding;
import com.example.firebase.models.Showtime;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private List<Showtime> showtimes;
    private int selectedPosition = -1;
    private OnShowtimeClickListener listener;

    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime);
    }

    public ShowtimeAdapter(List<Showtime> showtimes, OnShowtimeClickListener listener) {
        this.showtimes = showtimes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShowtimeBinding binding = ItemShowtimeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShowtimeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimes.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        if (showtime.getStartTime() != null) {
            holder.binding.tvShowtimeTime.setText(sdf.format(showtime.getStartTime()));
        } else {
            holder.binding.tvShowtimeTime.setText("");
        }

        if (selectedPosition == position) {
            holder.binding.cardShowtime.setCardBackgroundColor(Color.LTGRAY);
        } else {
            holder.binding.cardShowtime.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onShowtimeClick(showtime);
        });
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }

    public Showtime getSelectedShowtime() {
        if (selectedPosition != -1) {
            return showtimes.get(selectedPosition);
        }
        return null;
    }

    static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        ItemShowtimeBinding binding;
        ShowtimeViewHolder(ItemShowtimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}