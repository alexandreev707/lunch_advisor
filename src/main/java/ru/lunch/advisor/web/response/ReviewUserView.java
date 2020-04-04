package ru.lunch.advisor.web.response;

import ru.lunch.advisor.service.dto.ReviewUserDTO;

import java.time.LocalDateTime;

public class ReviewUserView {

    private Long id;
    private String menu;
    private LocalDateTime dateTime;
    private String state;

    public ReviewUserView() {
    }

    public ReviewUserView(ReviewUserDTO dto) {
        this.id = dto.getId();
        this.menu = dto.getMenu();
        this.dateTime = dto.getDateTime();
        this.state = dto.getState().name();
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
