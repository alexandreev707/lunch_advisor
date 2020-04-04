package ru.lunch.advisor.web.response;

import ru.lunch.advisor.service.dto.ItemDTO;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemView {

    private Long id;

    private String name;

    private BigDecimal price;

    public ItemView() {
    }

    public ItemView(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ItemView(ItemDTO dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.id = dto.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemView itemView = (ItemView) o;
        return Objects.equals(id, itemView.id) &&
                Objects.equals(name, itemView.name) &&
                Objects.equals(price, itemView.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
