package com.example.carrental;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarDetailsActivity extends AppCompatActivity {

    private ApiService apiService;
    private TextView dateBooking, timeBooking, timeLabel, dateLabel;
    private EditText remarkEditText;
    private Button btnBooking, btnTime, btnDate;
    private int carId;
    private int userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dateBooking = findViewById(R.id.dateBooking);
        timeBooking = findViewById(R.id.timeBooking);
        remarkEditText = findViewById(R.id.remarkEditText);
        btnBooking = findViewById(R.id.btnBooking);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        timeLabel = findViewById(R.id.timeLabel);
        dateLabel = findViewById(R.id.dateLabel);


        Intent intent = getIntent();
        carId = intent.getIntExtra("car_id", -1);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        userId = user.getId();
        token = user.getToken();

        apiService = ApiClient.getApiService();

        // Retrieve car details
        getCarDetails(token, carId);

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookCar();
            }
        });
    }

    private void getCarDetails(String token, int carId) {
        apiService.getCarDetails(token, carId).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    Car car = response.body();

                    ImageView carImageView = findViewById(R.id.carImageView);
                    TextView carNameTextView = findViewById(R.id.carNameTextView);
                    TextView carBrandTextView = findViewById(R.id.carBrandTextView);
                    TextView carModelTextView = findViewById(R.id.carModelTextView);
                    TextView carYearTextView = findViewById(R.id.carYearTextView);
                    TextView carColorTextView = findViewById(R.id.carColorTextView);
                    TextView carPriceTextView = findViewById(R.id.carPriceTextView);
                    TextView carAvailabilityTextView = findViewById(R.id.carAvailabilityTextView);

                    String imageName = car.getImageUrl();
                    int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    carImageView.setImageResource(resourceId);

                    carNameTextView.setText(car.getName());
                    carBrandTextView.setText(car.getBrand());
                    carModelTextView.setText(car.getModel());
                    carYearTextView.setText(String.valueOf(car.getYear()));
                    carColorTextView.setText(car.getColor());
                    carPriceTextView.setText(String.format("RM%.2f", car.getPricePerDay()) + "/hour");
                    carAvailabilityTextView.setText(car.getAvailability());

                    if ("Booked".equalsIgnoreCase(car.getAvailability())) {
                        btnBooking.setVisibility(View.GONE);
                        dateBooking.setVisibility(View.GONE);
                        timeBooking.setVisibility(View.GONE);
                        remarkEditText.setVisibility(View.GONE);
                        timeLabel.setVisibility(View.GONE);
                        dateLabel.setVisibility(View.GONE);
                        btnDate.setVisibility(View.GONE);
                        btnTime.setVisibility(View.GONE);  // Hide the booking button

                    } else {
                        btnBooking.setVisibility(View.VISIBLE); // Show the booking button
                        dateBooking.setVisibility(View.VISIBLE);
                        timeBooking.setVisibility(View.VISIBLE);
                        remarkEditText.setVisibility(View.VISIBLE);
                        timeLabel.setVisibility(View.VISIBLE);
                        dateLabel.setVisibility(View.VISIBLE);
                        btnDate.setVisibility(View.VISIBLE);
                        btnTime.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bookCar() {
        String bookingDate = dateBooking.getText().toString();
        String bookingTime = timeBooking.getText().toString();
        String bookingRemark = remarkEditText.getText().toString();

        Log.d("BookingData", "Date: " + bookingDate + ", Time: " + bookingTime + ", Remark: " + bookingRemark + ", Id: " + userId);

        apiService.bookCar(token, carId, userId, bookingDate, bookingTime, bookingRemark).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CarDetailsActivity.this, "Booking successful", Toast.LENGTH_SHORT).show();

                    // Navigate to activity_main upon successful booking
                    Intent intent = new Intent(CarDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        Log.e("BookingError", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CarDetailsActivity.this, "Booking failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Log.e("BookingFailure", t.getMessage(), t);
                Toast.makeText(CarDetailsActivity.this, "Booking failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateBooking.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeBooking.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
