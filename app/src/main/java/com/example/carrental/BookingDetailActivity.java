package com.example.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carrental.model.Booking;
import com.example.carrental.model.Car;
import com.example.carrental.model.User;
import com.example.carrental.remote.ApiClient;
import com.example.carrental.remote.ApiService;
import com.example.carrental.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView tvAdminMessage, tvAdminMessageValue, tvBookingIdValue, tvUserNameValue, tvCarModelValue, tvBookingDateValue, tvBookingTimeValue, tvBookingStatusValue, tvBookingRemarkValue;
    private ImageView ivCarImage;
    private Button btnCancelBooking;

    private ApiService apiService;
    private int bookingId;
    private String bookingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBookingIdValue = findViewById(R.id.tvBookingIdValue);
        tvCarModelValue = findViewById(R.id.tvCarModelValue);
        tvBookingDateValue = findViewById(R.id.tvBookingDateValue);
        tvBookingTimeValue = findViewById(R.id.tvBookingTimeValue);
        tvBookingStatusValue = findViewById(R.id.tvBookingStatusValue);
        tvBookingRemarkValue = findViewById(R.id.tvBookingRemarkValue);
        tvAdminMessage = findViewById(R.id.tvAdminMessage);
        tvAdminMessageValue = findViewById(R.id.tvAdminMessageValue);
        ivCarImage = findViewById(R.id.ivCarImage);
        btnCancelBooking = findViewById(R.id.btnCancelBooking);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        User user = sharedPrefManager.getUser();

        apiService = ApiClient.getApiService();

        Intent intent = getIntent();
        bookingId = intent.getIntExtra("booking_id", -1);  // Default value of -1 if not found

        if (bookingId != -1) {
            fetchBookingDetails(user.getToken(), bookingId);
        }

        btnCancelBooking.setOnClickListener(view -> cancelBooking(user.getToken(), bookingId));
    }

    private void fetchBookingDetails(String apiKey, int bookingId) {
        Call<List<Booking>> bookingCall = apiService.getBooking(apiKey, bookingId);
        bookingCall.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();
                    Booking booking = bookings.get(0);

                    tvBookingIdValue.setText(String.valueOf(booking.getId()));
                    tvBookingDateValue.setText(booking.getBooking_date());
                    tvBookingTimeValue.setText(booking.getBooking_time());
                    tvBookingRemarkValue.setText(booking.getBooking_remark());
                    tvBookingStatusValue.setText(booking.getBooking_status());
                    tvAdminMessageValue.setText(booking.getAdminMessage());
                    if (booking.getAdminMessage() == null){
                        tvAdminMessage.setVisibility(View.GONE);
                        tvAdminMessageValue.setVisibility(View.GONE);
                    }
                    bookingStatus = booking.getBooking_status();
                    if (!"new".equalsIgnoreCase(bookingStatus)) {
                        btnCancelBooking.setVisibility(View.GONE);
                    }

                    fetchCarDetails(apiKey, booking.getCar_id());
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }

    private void fetchCarDetails(String apiKey, int carId) {
        Call<Car> carCall = apiService.getCarDetails(apiKey, carId);
        carCall.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Car car = response.body();

                    tvCarModelValue.setText(car.getName());

                    String imageName = car.getImageUrl();
                    int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    ivCarImage.setImageResource(resourceId);
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }

    private void cancelBooking(String apiKey, int bookingId) {
            Call<Void> deleteCall = apiService.deleteBooking(apiKey, bookingId);
            deleteCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Booking canceled successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(BookingDetailActivity.this, BookingListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to cancel booking", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                    Log.e("MyApp:", t.toString());
                }
            });
    }
}
