package ru.lunch.advisor.web.request;

import javax.validation.constraints.NotBlank;

public class RestaurantRequest {

    @NotBlank
    private String name;
    private String address;

    public RestaurantRequest() {
    }

    public RestaurantRequest(String name, String address) {
        this.name = name;
        this.address = address;
    }

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
}
