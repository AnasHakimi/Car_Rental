package com.example.carrental.remote;

import com.example.carrental.model.Booking;
import com.example.carrental.model.BookingUpdateRequest;
import com.example.carrental.model.Car;
import com.example.carrental.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    //login using username
    @FormUrlEncoded
    @POST("users/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);


    //login using email
    @FormUrlEncoded
    @POST("users/login")
    Call<User> loginEmail(@Field("email") String username, @Field("password") String password);

    @GET("users/{id}")
    Call<User> getUserDetails(@Header("api-key") String api_key, @Path("id") int id);


    //get car by id
    @GET("cars/{id}")
    Call<Car> getCarDetails(@Header("api-key") String api_key, @Path("id") int id);


    //get all car sort by asc
    @GET("cars/?order=name&orderType=asc")
    Call<List<Car>> getCars(@Header("api-key") String api_key);


    //admin get all booking and sort desc
    @GET("bookings/?order=id&orderType=desc")
    Call<List<Booking>> getAllBooking(@Header("api-key") String api_key);


    //get booking by user id
    @GET("bookings")
    Call<List<Booking>> getBookingsByUserId(
            @Header("api-key") String api_key,
            @Query("user_id") int userId
    );


    //get booking by id
    @GET("bookings")
    Call<List<Booking>> getBooking(
            @Header("api-key") String api_key,
            @Query("id") int bookingId

    );

    //add booking car
    @FormUrlEncoded
    @POST("bookings")
    Call<Booking> bookCar(
            @Header("api-key") String api_key,
            @Field("car_id") int car_id,
            @Field("user_id") int user_id,
            @Field("booking_date") String booking_date,
            @Field("booking_time") String booking_time,
            @Field("booking_remark") String booking_remark
    );

   //delete by status new
    @DELETE("bookings/{id}")
    Call<Void> deleteBooking(@Header("api-key") String api_key, @Path("id") int bookingId);


    //update booking status
    @PUT("bookings/{id}")
    Call<Booking> updateBookingStatus(@Header("api-key") String apiKey, @Path("id") int bookingId, @Body BookingUpdateRequest bookingUpdateRequest);


    // Add new car
    @POST("cars")
    Call<Car> addCar(@Header("api-key") String apiKey, @Body Car car);


    @POST("users/register")
    Call<User> registerUser(@Body User user);

}


