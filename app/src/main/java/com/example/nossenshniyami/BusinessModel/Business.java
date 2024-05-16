package com.example.nossenshniyami.BusinessModel;

public class Business {
    private String name;
    private String address;
    private String phoneNumber;
    private String Info; // תקציר בקצרה של החנות מה הם מוכרים
    private String imageURL; // Field for storing image URL

    public Business(String name, String address, String phoneNumber, String imageURL,String tkzir) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.imageURL = imageURL;
        this.Info=tkzir;
    }

    public Business(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getInfo() {
        return this.Info;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
