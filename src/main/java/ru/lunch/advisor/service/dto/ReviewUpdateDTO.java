package ru.lunch.advisor.service.dto;

import java.time.LocalDate;

public class ReviewUpdateDTO {

    private Boolean isVote;
    private Long menuId;
    private LocalDate date;

    public Boolean getVote() {
        return isVote;
    }

    public void setVote(Boolean vote) {
        isVote = vote;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}