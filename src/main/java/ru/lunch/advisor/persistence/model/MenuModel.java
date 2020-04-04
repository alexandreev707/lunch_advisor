package ru.lunch.advisor.persistence.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "MENU")
public class MenuModel {

    @SequenceGenerator(name = "SEQ_MENU", sequenceName = "SEQ_MENU", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MENU")
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DATE", columnDefinition = "DATE")
    private LocalDate date;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "ITEMS_MENU",
            joinColumns = @JoinColumn(name = "MENU_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private Set<ItemModel> items;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "RESTAURANT_ID")
    private RestaurantModel restaurant;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    private Set<ReviewModel> reviews;

    public MenuModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<ItemModel> getItems() {
        return items;
    }

    public void setItems(Set<ItemModel> itemModels) {
        this.items = itemModels;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurantModel) {
        this.restaurant = restaurantModel;
    }

    public Set<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ReviewModel> reviewModels) {
        this.reviews = reviewModels;
    }
}
