package com.example.carrental.model;

public class BookingUpdateRequest {
    private String booking_status;
    private String admin_message;

    public BookingUpdateRequest(String booking_status, String admin_message) {
        this.booking_status = booking_status;
        this.admin_message = admin_message;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getAdmin_message() {
        return admin_message;
    }

    public void setAdmin_message(String admin_message) {
        this.admin_message = admin_message;
    }
}

