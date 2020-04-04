package ru.lunch.advisor.web.response;

import ru.lunch.advisor.service.dto.MenuDTO;

import java.time.LocalDate;
import java.util.Objects;

public class MenuView {

    private Long id;
    private String menu;
    private LocalDate date;
    private String restaurant;

    public MenuView() {
    }

    public MenuView(Long id, String menu, LocalDate date, String restaurant) {
        this.id = id;
        this.menu = menu;
        this.date = date;
        this.restaurant = restaurant;
    }

    public MenuView(MenuDTO dto) {
        this.id = dto.getId();
        this.menu = dto.getName();
        this.date = dto.getDate();
        this.restaurant = dto.getRestaurant();
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
        MenuView menuView = (MenuView) o;
        return Objects.equals(id, menuView.id) &&
                Objects.equals(menu, menuView.menu) &&
                Objects.equals(date, menuView.date) &&
                Objects.equals(restaurant, menuView.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menu, date, restaurant);
    }
}
