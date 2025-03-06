package com.example.carrental.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrental.BookingDetailActivity;
import com.example.carrental.R;
import com.example.carrental.model.Booking;
import com.example.carrental.model.Car;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookingList;
    private CarAdapter carAdapter;

    private static Context context;


    public BookingAdapter(List<Booking> bookingList, CarAdapter carAdapter, Context context) {
        this.bookingList = bookingList;
        this.carAdapter = carAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.bookingDateTextView.setText(booking.getBooking_date());
        holder.bookingTimeTextView.setText(booking.getBooking_time());
        holder.bookingStatusTextView.setText(booking.getBooking_status());

        Car car = carAdapter.getCarById(booking.getCar_id());
        if (car != null) {
            holder.carNameTextView.setText(car.getName());
            holder.carPriceTextView.setText(String.format("RM%.2f", car.getPricePerDay()) + "/hours");

            String imageName = car.getImageUrl();
            int resourceId = holder.itemView.getContext().getResources().getIdentifier(imageName, "drawable", holder.itemView.getContext().getPackageName());
            holder.carImageView.setImageResource(resourceId);
        }

        holder.viewBookingButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);
            intent.putExtra("booking_id", booking.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingDateTextView;
        TextView bookingTimeTextView;
        TextView bookingStatusTextView;
        TextView carNameTextView;
        TextView carPriceTextView;
        ImageView carImageView;

        Button viewBookingButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingDateTextView = itemView.findViewById(R.id.bookingDateTextView);
            bookingTimeTextView = itemView.findViewById(R.id.bookingTimeTextView);
            bookingStatusTextView = itemView.findViewById(R.id.bookingStatusTextView);
            carNameTextView = itemView.findViewById(R.id.carNameTextView);
            carPriceTextView = itemView.findViewById(R.id.carPriceTextView);
            carImageView = itemView.findViewById(R.id.carImageView);
            viewBookingButton = itemView.findViewById(R.id.viewBookingButton);


        }



    }
}