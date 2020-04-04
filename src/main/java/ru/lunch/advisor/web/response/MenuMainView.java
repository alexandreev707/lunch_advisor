package ru.lunch.advisor.web.response;

import org.springframework.util.CollectionUtils;
import ru.lunch.advisor.service.State;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.ReviewDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MenuMainView {

    private String menu;
    private LocalDate date;
    private String restaurant;
    private Long countVote;
    private List<ItemView> items;

    public MenuMainView(MenuFullDTO dto) {
        this.menu = dto.getName();
        this.date = dto.getDate();
        this.restaurant = dto.getRestaurant();

        if (!CollectionUtils.isEmpty(dto.getReviews())) {
            List<ReviewDTO> filtered = dto.getReviews().stream()
                    .filter(d-> !State.DELETED.equals(d.getState())).collect(Collectors.toList());
            this.countVote = filtered.stream().distinct().count();
        } else {
            this.countVote = 0L;
        }

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

    public Long getCountVote() {
        return countVote;
    }

    public void setCountVote(Long countVote) {
        this.countVote = countVote;
    }

    public List<ItemView> getItems() {
        return items;
    }

    public void setItems(List<ItemView> items) {
        this.items = items;
    }
}
