package ru.lunch.advisor.service.dto;

import java.time.LocalDate;

public class UserMenuDTO {

    private Long id;
    private String name;
    private LocalDate date;
    private String restaurant;
    private boolean isVote;

    public UserMenuDTO(MenuDTO dto, boolean isVote) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.date = dto.getDate();
        this.restaurant = dto.getRestaurant();
        this.isVote = isVote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isVote() {
        return isVote;
    }

    public void setVote(boolean vote) {
        isVote = vote;
    }
}
