package com.example.carrental.model;

public class Booking {
    private int id;
    private int car_id;
    private int user_id;
    private String booking_date;
    private String booking_time;

    private String booking_status;

    private String booking_remark;
    private String admin_message;

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getBooking_remark() {
        return booking_remark;
    }

    public void setBooking_remark(String booking_remark) {
        this.booking_remark = booking_remark;
    }
    public String getAdminMessage() { return admin_message;}
    public void setAdmin_message(String admin_message){this.admin_message = admin_message; }




    public Booking(int id, int carId, int userId, String bookingDate, String bookingTime, String bookingRemark, String bookingStatus) {
        this.id = id;
        this.car_id = carId;
        this.user_id = userId;
        this.booking_date = bookingDate;
        this.booking_time = bookingTime;
        this.booking_remark = bookingRemark;
        this.booking_status = bookingStatus;
    }


}
