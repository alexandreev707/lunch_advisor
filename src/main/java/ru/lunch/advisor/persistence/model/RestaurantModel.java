package ru.lunch.advisor.persistence.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "RESTAURANT")
public class RestaurantModel {

    @SequenceGenerator(name = "SEQ_RESTAURANT", sequenceName = "SEQ_RESTAURANT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESTAURANT")
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @OneToMany(mappedBy = "restaurant", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MenuModel> menus;

    public RestaurantModel() {
    }

    public RestaurantModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MenuModel> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuModel> menuModels) {
        this.menus = menuModels;
    }
}
