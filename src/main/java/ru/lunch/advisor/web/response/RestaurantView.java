package ru.lunch.advisor.web.response;

import ru.lunch.advisor.service.dto.RestaurantDTO;

import java.util.Objects;

public class RestaurantView {

    private Long id;
    private String name;
    private String address;

    public RestaurantView() {
    }

    public RestaurantView(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public RestaurantView(RestaurantDTO dto) {
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.id = dto.getId();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantView that = (RestaurantView) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address);
    }
}
