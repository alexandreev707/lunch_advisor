package ru.lunch.advisor.web.response;

import ru.lunch.advisor.service.dto.MenuItemDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuItemView {

    private Long id;
    private String menu;
    private LocalDate date;
    private String restaurant;
    private List<ItemView> items;

    public MenuItemView() {
    }

    public MenuItemView(MenuItemDTO dto) {
        this.id = dto.getId();
        this.menu = dto.getName();
        this.date = dto.getDate();
        this.restaurant = dto.getRestaurant();
        this.items = dto.getItems().stream().map(ItemView::new).collect(Collectors.toList());
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

    public List<ItemView> getItems() {
        return items;
    }

    public void setItems(List<ItemView> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItemView itemView = (MenuItemView) o;
        return Objects.equals(id, itemView.id) &&
                Objects.equals(menu, itemView.menu) &&
                Objects.equals(date, itemView.date) &&
                Objects.equals(restaurant, itemView.restaurant) &&
                Objects.equals(items, itemView.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menu, date, restaurant, items);
    }
}
