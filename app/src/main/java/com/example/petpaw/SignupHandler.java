package com.example.petpaw;

import com.google.firebase.database.DatabaseReference;

public class SignupHandler {
    private String fullname;
    private String phone;
    private String address;
    private String password;
    public SignupHandler() {
    }

    public SignupHandler(String fullname, String phone, String address, String password) {
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
