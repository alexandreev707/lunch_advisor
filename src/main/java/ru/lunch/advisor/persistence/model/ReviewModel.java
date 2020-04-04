package ru.lunch.advisor.persistence.model;

import ru.lunch.advisor.service.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "REVIEW")
public class ReviewModel {

    @SequenceGenerator(name = "SEQ_REVIEW", sequenceName = "SEQ_REVIEW", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REVIEW")
    @Column(name = "ID")
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    private MenuModel menu;

    @Column(name = "DATE_TIME", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private State state;

    public ReviewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public MenuModel getMenu() {
        return menu;
    }

    public void setMenu(MenuModel menuModel) {
        this.menu = menuModel;
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
}
