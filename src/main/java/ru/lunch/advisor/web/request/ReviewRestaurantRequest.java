package ru.lunch.advisor.web.request;

import javax.validation.constraints.NotNull;

public class ReviewRestaurantRequest {

    @NotNull
    private Boolean isVote;

    public ReviewRestaurantRequest() {
    }

    public ReviewRestaurantRequest(Boolean isVote) {
        this.isVote = isVote;
    }

    public Boolean getVote() {
        return isVote;
    }

    public void setVote(Boolean vote) {
        isVote = vote;
    }
}
