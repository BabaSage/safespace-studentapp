package com.example.safespace;

public class DataSet {
    private String phoneNumber;
    private String email;
    private String placeOfResidence;
    private String department;

    public DataSet(String phoneNumber, String email, String placeOfResidence, String department) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.placeOfResidence = placeOfResidence;
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public String getDepartment() {
        return department;
    }
}