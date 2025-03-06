package com.example.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carrental.model.Booking;
import com.example.carrental.model.BookingUpdateRequest;
import com.example.carrental.model.Car;
import com.example.carrental.model.User;
import com.example.carrental.remote.ApiClient;
import com.example.carrental.remote.ApiService;
import com.example.carrental.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBookingDetailActivity extends AppCompatActivity {

    private TextView tvBookingIdValue, tvUserNameValue, tvCarModelValue, tvBookingDateValue, tvBookingTimeValue, tvBookingStatusValue, tvBookingRemarkValue;
    private EditText edtMessage;
    private ImageView ivCarImage;
    private Button btnApproveBooking;
    private Button btnRejectBooking;
    private ApiService apiService;
    private int bookingId;
    private int carId;
    private String bookingTime;
    private String bookingDate;
    private String bookingRemark;
    private String bookingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_booking_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBookingIdValue = findViewById(R.id.tvBookingIdValue);
        tvUserNameValue = findViewById(R.id.tvUserNameValue);
        tvCarModelValue = findViewById(R.id.tvCarModelValue);
        tvBookingDateValue = findViewById(R.id.tvBookingDateValue);
        tvBookingTimeValue = findViewById(R.id.tvBookingTimeValue);
        tvBookingStatusValue = findViewById(R.id.tvBookingStatusValue);
        tvBookingRemarkValue = findViewById(R.id.tvBookingRemarkValue);
        edtMessage = findViewById(R.id.edtMessage);
        ivCarImage = findViewById(R.id.ivCarImage);
        btnApproveBooking = findViewById(R.id.btnApproveBooking);
        btnRejectBooking = findViewById(R.id.btnRejectBooking);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        User user = sharedPrefManager.getUser();

        apiService = ApiClient.getApiService();

        Intent intent = getIntent();
        bookingId = intent.getIntExtra("booking_id", -1);  // Default value of -1 if not found
        carId = intent.getIntExtra("car_id", -1);
        bookingTime = intent.getStringExtra("booking_time");
        bookingDate = intent.getStringExtra("booking_date");

        if (bookingId != -1) {
            fetchBookingDetails(user.getToken(), bookingId);
        }
        if (carId != -1) {
            fetchCarDetails(user.getToken(), carId);
        }

        btnApproveBooking.setOnClickListener(view -> updateBooking(user.getToken(), bookingId, "approved", edtMessage.getText().toString()));
        btnRejectBooking.setOnClickListener(view -> updateBooking(user.getToken(), bookingId, "rejected", edtMessage.getText().toString()));
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
                    bookingStatus = booking.getBooking_status();
                    bookingRemark = booking.getBooking_remark();
                    bookingDate = booking.getBooking_date();
                    bookingTime = booking.getBooking_time();
                    carId = booking.getCar_id();

                    fetchUsername(apiKey, booking.getUser_id());
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

    private void fetchUsername(String apiKey, int userId) {
        Call<User> userCall = apiService.getUserDetails(apiKey, userId); // Adjust this based on your API service method
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    // Assuming tvUsernameValue is the TextView where you want to display the username
                    tvUserNameValue.setText(user.getUsername());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }


    private void updateBooking(String apiKey, int bookingId, String bookingStatus, String adminMessage) {
        // Log values to verify they are correct
        Log.d("UpdateBooking", "Booking ID: " + bookingId);
        Log.d("UpdateBooking", "Booking Status: " + bookingStatus);
        Log.d("UpdateBooking", "Admin Message: " + adminMessage);

        // Create a request body with only the fields to be updated
        BookingUpdateRequest bookingUpdateRequest = new BookingUpdateRequest(bookingStatus, adminMessage);

        Call<Booking> updateCall = apiService.updateBookingStatus(apiKey, bookingId, bookingUpdateRequest);
        updateCall.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Booking updated successfully", Toast.LENGTH_LONG).show();

                    // Refresh the list by restarting the activity
                    Intent intent = new Intent(AdminBookingDetailActivity.this, AdminBookingListActivity.class);
                    startActivity(intent);
                    finish();  // Close the current activity
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update booking", Toast.LENGTH_LONG).show();
                    Log.e("UpdateBooking", "Response Code: " + response.code());
                    Log.e("UpdateBooking", "Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("UpdateBooking", t.toString());
            }
        });
    }
}

