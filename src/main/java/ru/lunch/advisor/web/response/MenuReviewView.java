package ru.lunch.advisor.web.response;

import org.springframework.util.CollectionUtils;
import ru.lunch.advisor.service.State;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.ReviewDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuReviewView {

    private Long id;
    private String menu;
    private LocalDate date;
    private String restaurant;
    private List<ReviewView> reviews;
    private Long countVote;

    public MenuReviewView() {
    }

    public MenuReviewView(MenuFullDTO dto) {
        this.id = dto.getId();
        this.menu = dto.getName();
        this.date = dto.getDate();
        this.restaurant = dto.getRestaurant();

        if (!CollectionUtils.isEmpty(dto.getReviews())) {
            List<ReviewDTO> filtered = dto.getReviews().stream().filter(d-> !State.DELETED.equals(d.getState())).collect(Collectors.toList());
            this.reviews = filtered.stream().map(ReviewView::new).collect(Collectors.toList());
            this.countVote = filtered.stream().distinct().count();
        } else {
            this.countVote = 0L;
        }
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

    public List<ReviewView> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewView> reviews) {
        this.reviews = reviews;
    }

    public Long getCountVote() {
        return countVote;
    }

    public void setCountVote(Long countVote) {
        this.countVote = countVote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuReviewView fullView = (MenuReviewView) o;
        return Objects.equals(id, fullView.id) &&
                Objects.equals(menu, fullView.menu) &&
                Objects.equals(date, fullView.date) &&
                Objects.equals(restaurant, fullView.restaurant) &&
                Objects.equals(reviews, fullView.reviews) &&
                Objects.equals(countVote, fullView.countVote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menu, date, restaurant, reviews, countVote);
    }
}
