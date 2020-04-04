package ru.lunch.advisor.web.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class ReviewRequest {

    @NotNull
    private Boolean isVote;
    @NotNull
    @PositiveOrZero
    private Long menuId;

    public ReviewRequest() {
    }

    public ReviewRequest(Boolean isVote, Long menuId) {
        this.isVote = isVote;
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Boolean getVote() {
        return isVote;
    }

    public void setVote(Boolean vote) {
        isVote = vote;
    }
}
