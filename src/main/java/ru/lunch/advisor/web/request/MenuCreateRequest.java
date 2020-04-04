package ru.lunch.advisor.web.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class MenuCreateRequest {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate date;
    @NotBlank
    private String restaurant;
    @Valid
    private List<ItemRequest> items;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, LocalDate date, String restaurant, List<ItemRequest> items) {
        this.name = name;
        this.date = date;
        this.restaurant = restaurant;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }
}
