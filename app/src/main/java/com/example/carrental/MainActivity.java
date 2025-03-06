package com.example.carrental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carrental.model.User;
import com.example.carrental.sharedpref.SharedPrefManager;



public class MainActivity extends AppCompatActivity {


    private Button btnViewCar;

    private Button btnViewBooking;
    private TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        if (!spm.isLoggedIn()) {
            // Stop this MainActivity
            finish();
            // Forward to Login Page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        String userRole = spm.getUser().getRole();
        if ("admin".equals(userRole)) {
            // Stop this MainActivity
            finish();
            // Forward to AdminMainActivity
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
            return;
        }

        // Set the content view for the customer
        setContentView(R.layout.activity_main);

        // Get references
        tvHello = findViewById(R.id.tvHello);

        // Greet user
        User user = spm.getUser();
        tvHello.setText("Welcome " + user.getUsername());

        // View car list
        btnViewCar = findViewById(R.id.btnViewCar);
        btnViewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CarListActivity.class);
                startActivity(intent);
            }
        });

        // View booking
        btnViewBooking = findViewById(R.id.btnViewBooking);
        btnViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookingListActivity.class);
                startActivity(intent);
            }
        });
    }


   //Logout
    public void logoutClicked(View view) {
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
