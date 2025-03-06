package com.example.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carrental.model.User;
import com.example.carrental.sharedpref.SharedPrefManager;

public class AdminMainActivity extends AppCompatActivity {

    private TextView tvHello;

    private Button btnViewBooking;


    private Button btnAddCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // get references
        tvHello = findViewById(R.id.tvHello);

        // if the user is already logged in we will directly start
        // the admin main activity
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        if (!spm.isLoggedIn()) {    // no session record
            // stop this AdminMainActivity
            finish();
            // forward to Login Page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            // Greet user
            User user = spm.getUser();
            tvHello.setText("Welcome " + user.getUsername());
        }



        //view AddCar
        btnAddCar = findViewById(R.id.btnAddCar);
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAddCarActivity.class);
                startActivity(intent);
            }
        });


        //view booking
        btnViewBooking = findViewById(R.id.btnViewBookings);
        btnViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminBookingListActivity.class);
                startActivity(intent);
            }
        });

    }

    public void logout(View view) {

        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // display message
        Toast.makeText(getApplicationContext(),
                "You have successfully logged out.",
                Toast.LENGTH_LONG).show();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}