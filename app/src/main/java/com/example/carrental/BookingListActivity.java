package com.example.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrental.Adapter.BookingAdapter;
import com.example.carrental.Adapter.CarAdapter;
import com.example.carrental.model.Booking;
import com.example.carrental.model.Car;
import com.example.carrental.model.User;
import com.example.carrental.remote.ApiClient;
import com.example.carrental.remote.ApiService;
import com.example.carrental.sharedpref.SharedPrefManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private CarAdapter carAdapter;
    private ApiService apiService;
    private SharedPrefManager sharedPrefManager;

    private Button btnViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        User user = sharedPrefManager.getUser();

        apiService = ApiClient.getApiService();

        fetchCarsAndBookings(user.getToken(), user.getId());


    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCarsAndBookings(sharedPrefManager.getUser().getToken(), sharedPrefManager.getUser().getId());
    }

    private void fetchCarsAndBookings(String apiKey, int userId) {
        Call<List<Car>> carCall = apiService.getCars(apiKey);
        carCall.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Car> cars = response.body();
                    carAdapter = new CarAdapter(BookingListActivity.this, cars);
                    fetchBookings(apiKey, userId);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }

    private void fetchBookings(String apiKey, int userId) {
        Call<List<Booking>> bookingCall = apiService.getBookingsByUserId(apiKey, userId);
        bookingCall.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();
                    sortBookingsByIdDesc(bookings); // Sort bookings by ID in descending order
                    bookingAdapter = new BookingAdapter(bookings, carAdapter, BookingListActivity.this);
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

    private void sortBookingsByIdDesc(List<Booking> bookings) {
        Collections.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking b1, Booking b2) {
                return Integer.compare(b2.getId(), b1.getId()); // Descending order
            }
        });
    }


}
