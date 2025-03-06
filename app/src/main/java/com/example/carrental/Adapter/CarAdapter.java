package com.example.carrental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carrental.R;
import com.example.carrental.model.Car;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    // ViewHolder class to bind list item view
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvCarName;
        public TextView tvCarBrand;
        public TextView tvCarModel;
        public TextView tvCarPrice;
        public TextView tvCarYear;
        public TextView tvCarColor;
        public TextView tvCarAvailability;
        public ImageView tvCarImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarBrand = itemView.findViewById(R.id.tvCarBrand);
            tvCarModel = itemView.findViewById(R.id.tvCarModel);
            tvCarPrice = itemView.findViewById(R.id.tvCarPrice);
            tvCarYear = itemView.findViewById(R.id.tvCarYear);
            tvCarColor = itemView.findViewById(R.id.tvCarColor);
            tvCarAvailability = itemView.findViewById(R.id.tvCarAvailability);
            tvCarImage = itemView.findViewById(R.id.tvCarImage);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition();
            return false;
        }
    }

    private List<Car> carList;
    private Context mContext;
    private int currentPos;

    public CarAdapter(Context context, List<Car> cars) {
        mContext = context;
        carList = cars;
    }

    private Context getmContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_car, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.tvCarName.setText(car.getName());
        holder.tvCarPrice.setText(String.format("RM%.2f", car.getPricePerDay()) + "/hours");
        holder.tvCarBrand.setText(car.getBrand());
        holder.tvCarModel.setText(car.getModel());
        holder.tvCarYear.setText(String.valueOf(car.getYear()));
        holder.tvCarColor.setText(car.getColor());
        holder.tvCarAvailability.setText(car.getAvailability());

        String imageName = car.getImageUrl();
        int resourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        holder.tvCarImage.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public Car getSelectedItem() {
        if (currentPos >= 0 && carList != null && currentPos < carList.size()) {
            return carList.get(currentPos);
        }
        return null;
    }

    // New method to get Car by ID
    public Car getCarById(int carId) {
        for (Car car : carList) {
            if (car.getId() == carId) {
                return car;
            }
        }
        return null;
    }
}