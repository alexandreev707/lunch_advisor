package ru.lunch.advisor.web.response;

import ru.lunch.advisor.service.State;
import ru.lunch.advisor.service.dto.ReviewDTO;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReviewView {

    private Long id;
    private Long userId;
    private Long menuId;
    private LocalDateTime dateTime;
    private State state;

    public ReviewView() {
    }

    public ReviewView(Long id, Long userId, Long menuId, LocalDateTime dateTime, State state) {
        this.id = id;
        this.userId = userId;
        this.menuId = menuId;
        this.dateTime = dateTime;
        this.state = state;
    }

    public ReviewView(ReviewDTO dto) {
        this.id = dto.getId();
        this.userId = dto.getUserId();
        this.menuId = dto.getMenuId();
        this.dateTime = dto.getDateTime();
        this.state = dto.getState();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewView that = (ReviewView) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(menuId, that.menuId) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, menuId, state);
    }
}
