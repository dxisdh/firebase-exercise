package com.example.firebase;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.firebase.adapters.TicketAdapter;
import com.example.firebase.databinding.ActivityMyTicketsBinding;
import com.example.firebase.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MyTicketsActivity extends AppCompatActivity {
    private ActivityMyTicketsBinding binding;
    private TicketAdapter adapter;
    private List<Ticket> ticketList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyTicketsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Tickets");
        }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ticketList = new ArrayList<>();
        adapter = new TicketAdapter(ticketList);
        
        binding.rvMyTickets.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMyTickets.setAdapter(adapter);

        fetchTickets();
    }

    private void fetchTickets() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("tickets")
            .whereEqualTo("userId", userId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ticketList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ticket ticket = document.toObject(Ticket.class);
                        ticketList.add(ticket);
                    }
                    adapter.notifyDataSetChanged();
                    
                    if (ticketList.isEmpty()) {
                        Toast.makeText(this, "You haven't booked any tickets yet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}