package com.example.carrental;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrental.Adapter.CarAdapter;
import com.example.carrental.model.Car;
import com.example.carrental.model.User;
import com.example.carrental.remote.ApiClient;
import com.example.carrental.remote.ApiService;

import com.example.carrental.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarListActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView rvCarList;
    private CarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        // Enable edge-to-edge display if needed
        // EdgeToEdge.enable(this);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        rvCarList = findViewById(R.id.rvCarList);

        // Register for context menu
        registerForContextMenu(rvCarList);

        // Fetch and update car list
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        // Get user info from SharedPreferences to get token value
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = ((User) user).getToken();

        // Get ApiService instance
        apiService = ApiClient.getApiService();

        // Execute the call to fetch cars
        apiService.getCars(token).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful()) {
                    List<Car> cars = response.body();

                    // Initialize adapter with fetched data
                    adapter = new CarAdapter(getApplicationContext(), cars);

                    // Set adapter to RecyclerView
                    rvCarList.setAdapter(adapter);

                    // Set layout manager to RecyclerView
                    rvCarList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // Add item decoration (optional) - divider between items
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCarList.getContext(),
                            LinearLayoutManager.VERTICAL);
                    rvCarList.addItemDecoration(dividerItemDecoration);
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(getApplicationContext(), "Failed to fetch cars: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("CarListActivity", "Failed to fetch cars: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                // Handle failure to connect to server
                Toast.makeText(getApplicationContext(), "Error connecting to server: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("CarListActivity", "Error connecting to server: " + t.getMessage());
            }
        });
    }

    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_car_list, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Car selectedCar = adapter.getSelectedItem();
        Log.d("MyApp", "selected "+selectedCar.toString());    // debug purpose

        if (item.getItemId() == R.id.menu_view) {

            viewCarDetails(selectedCar);
        }

        return super.onContextItemSelected(item);
    }


    private void viewCarDetails(Car car) {
        // Forward user to CarDetailsActivity, passing the car ID or necessary details
        Intent intent = new Intent(this, CarDetailsActivity.class);
        intent.putExtra("car_id", car.getId());
        startActivity(intent);
    }



}
