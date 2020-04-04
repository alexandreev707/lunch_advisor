package ru.lunch.advisor.web.response;

import ru.lunch.advisor.service.dto.UserMenuDTO;

import java.time.LocalDate;

public class UserMenuView {

    private Long id;
    private String menu;
    private LocalDate date;
    private String restaurant;
    private Boolean isVote;

    public UserMenuView() {
    }

    public UserMenuView(UserMenuDTO dto) {
        this.id = dto.getId();
        this.menu = dto.getName();
        this.date = dto.getDate();
        this.restaurant = dto.getRestaurant();
        this.isVote = dto.isVote();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
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

    public Boolean getVote() {
        return isVote;
    }

    public void setVote(Boolean vote) {
        isVote = vote;
    }
}
