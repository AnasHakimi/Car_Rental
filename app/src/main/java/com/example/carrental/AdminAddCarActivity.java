package com.example.carrental;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.model.Car;
import com.example.carrental.model.User;
import com.example.carrental.remote.ApiClient;
import com.example.carrental.remote.ApiService;

import com.example.carrental.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdminAddCarActivity extends AppCompatActivity {

    private EditText etName, etBrand, etModel, etYear, etColor, etPricePerDay, etAvailability, etImageUrl;
    private Button btnAddCar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_car);

        etName = findViewById(R.id.etName);
        etBrand = findViewById(R.id.etBrand);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etColor = findViewById(R.id.etColor);
        etPricePerDay = findViewById(R.id.etPricePerDay);
        etAvailability = findViewById(R.id.etAvailability);
        etImageUrl = findViewById(R.id.etImageUrl);
        btnAddCar = findViewById(R.id.btnAddCar);


        apiService = ApiClient.getApiService();


        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar();
            }
        });
    }

    private void addCar() {
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        int year = Integer.parseInt(etYear.getText().toString().trim());
        String color = etColor.getText().toString().trim();
        double pricePerDay = Double.parseDouble(etPricePerDay.getText().toString().trim());
        String availability = etAvailability.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        Car car = new Car(0, name, brand, model, year, color, pricePerDay, availability, imageUrl);


        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        User user = sharedPrefManager.getUser();

        Call<Car> call = apiService.addCar(user.getToken(), car);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminAddCarActivity.this, "Car added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminAddCarActivity.this, "Failed to add car", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(AdminAddCarActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
