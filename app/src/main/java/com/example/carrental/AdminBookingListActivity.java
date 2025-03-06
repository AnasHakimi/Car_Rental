package com.example.carrental;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrental.Adapter.AdminBookingAdapter;
import com.example.carrental.Adapter.CarAdapter;
import com.example.carrental.model.Booking;
import com.example.carrental.model.Car;
import com.example.carrental.model.User;
import com.example.carrental.remote.ApiClient;
import com.example.carrental.remote.ApiService;
import com.example.carrental.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBookingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminBookingAdapter bookingAdapter;
    private CarAdapter carAdapter;
    private ApiService apiService;
    private SharedPrefManager sharedPrefManager;

    private Button btnViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        User user = sharedPrefManager.getUser();

        apiService = ApiClient.getApiService();

        fetchCarsAndBookings(user.getToken());



    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCarsAndBookings(sharedPrefManager.getUser().getToken());
    }


    private void fetchCarsAndBookings(String apiKey) {
        Call<List<Car>> carCall = apiService.getCars(apiKey);
        carCall.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Car> cars = response.body();
                    carAdapter = new CarAdapter(AdminBookingListActivity.this, cars);
                    fetchBookings(apiKey);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }

    private void fetchBookings(String apiKey) {
        Call<List<Booking>> bookingCall = apiService.getAllBooking(apiKey);
        bookingCall.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();
                    bookingAdapter = new AdminBookingAdapter(bookings, carAdapter, AdminBookingListActivity.this);
                    recyclerView.setAdapter(bookingAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to retrieve bookings", Toast.LENGTH_LONG).show();
                    Log.e("MyApp:", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }


}