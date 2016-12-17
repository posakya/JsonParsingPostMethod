package com.example.roshan.cinqsnipe;

/**
 * Created by roshan on 12/2/16.
 */

public class Profile {
    private String id;
    private String name;
    private String email;
    private String user_image;
    private String phone;
    private String total_sale;
    private String total_rent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal_sale() {
        return total_sale;
    }

    public void setTotal_sale(String total_sale) {
        this.total_sale = total_sale;
    }

    public String getTotal_rent() {
        return total_rent;
    }

    public void setTotal_rent(String total_rent) {
        this.total_rent = total_rent;
    }
}