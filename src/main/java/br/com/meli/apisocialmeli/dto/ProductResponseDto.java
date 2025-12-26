package br.com.meli.apisocialmeli.dto;

import br.com.meli.apisocialmeli.model.ProductModel;
import br.com.meli.apisocialmeli.model.ProductType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponseDto {
    private Long id;
    private UserDto seller;
    private String name;
    private ProductType type;
    private String brand;
    private String color;
    private String notes;
    private Integer category;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponseDto() {
    }

    public ProductResponseDto(ProductModel product) {
        this.id = product.getId();
        this.seller = new UserDto(product.getSeller().getId(), product.getSeller().getNome());
        this.name = product.getName();
        this.type = product.getType();
        this.brand = product.getBrand();
        this.color = product.getColor();
        this.notes = product.getNotes();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public UserDto getSeller() {
        return seller;
    }

    public String getName() {
        return name;
    }

    public ProductType getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getNotes() {
        return notes;
    }

    public Integer getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSeller(UserDto seller) {
        this.seller = seller;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
