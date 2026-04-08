package com.example.firebase.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firebase.databinding.ItemTicketBinding;
import com.example.firebase.models.Ticket;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> tickets;

    public TicketAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTicketBinding binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.binding.tvTicketMovieTitle.setText(ticket.getMovieTitle());
        holder.binding.tvTicketTheater.setText(ticket.getTheaterName());
        holder.binding.tvTicketSeat.setText(ticket.getSeatNumber());
        holder.binding.tvTicketPrice.setText(String.format(Locale.getDefault(), "%,.0fđ", ticket.getPrice()));

        if (ticket.getShowtimeTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            holder.binding.tvTicketTime.setText(sdf.format(ticket.getShowtimeTime()));
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        ItemTicketBinding binding;
        TicketViewHolder(ItemTicketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}