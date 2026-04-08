package com.example.firebase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.firebase.adapters.ShowtimeAdapter;
import com.example.firebase.databinding.ActivityBookingBinding;
import com.example.firebase.models.Movie;
import com.example.firebase.models.Showtime;
import com.example.firebase.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {
    private ActivityBookingBinding binding;
    private ShowtimeAdapter adapter;
    private List<Showtime> showtimeList;
    private FirebaseFirestore db;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        movie = (Movie) getIntent().getSerializableExtra("movie");

        if (movie != null) {
            binding.tvBookingMovieTitle.setText(movie.getTitle());
            fetchShowtimes(movie.getId());
        }

        showtimeList = new ArrayList<>();
        adapter = new ShowtimeAdapter(showtimeList, showtime -> {
            // Showtime selected
        });
        binding.rvShowtimes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvShowtimes.setAdapter(adapter);

        binding.btnBookNow.setOnClickListener(v -> bookTicket());
    }

    private void fetchShowtimes(String movieId) {
        db.collection("showtimes")
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showtimeList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Showtime showtime = document.toObject(Showtime.class);
                        showtime.setId(document.getId());
                        showtimeList.add(showtime);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }

    private void bookTicket() {
        Showtime selectedShowtime = adapter.getSelectedShowtime();
        String seatNumber = binding.etSeatNumber.getText().toString().trim();

        if (selectedShowtime == null) {
            Toast.makeText(this, "Please select a showtime", Toast.LENGTH_SHORT).show();
            return;
        }
        if (seatNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a seat number", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String ticketId = UUID.randomUUID().toString();

        Ticket ticket = new Ticket(
            ticketId,
            userId,
            selectedShowtime.getId(),
            movie.getTitle(),
            "Theater Name",
            selectedShowtime.getStartTime(),
            seatNumber,
            selectedShowtime.getPrice()
        );

        db.collection("tickets").document(ticketId).set(ticket)
            .addOnSuccessListener(aVoid -> {
                if (selectedShowtime.getStartTime() != null) {
                    scheduleNotification(selectedShowtime.getStartTime().getTime(), movie.getTitle(), seatNumber);
                }
                Toast.makeText(this, "Ticket booked successfully!", Toast.LENGTH_LONG).show();
                finish();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void scheduleNotification(long timeInMillis, String movieTitle, String seatNumber) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("movieTitle", movieTitle);
        intent.putExtra("seatNumber", seatNumber);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) timeInMillis,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Thông báo trước 5 phút. Nếu muốn đúng giờ thì bỏ "- (5 * 60 * 1000)"
            long triggerTime = timeInMillis - (5 * 60 * 1000);
            
            // Đảm bảo không đặt thời gian trong quá khứ
            if (triggerTime < System.currentTimeMillis()) {
                triggerTime = System.currentTimeMillis() + 1000;
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        }
    }
}