package br.com.meli.apisocialmeli.dto;

import br.com.meli.apisocialmeli.model.ProductModel;
import br.com.meli.apisocialmeli.model.ProductType;

public class ProductResponseDto {
    private Long productId;
    private String productName;
    private ProductType type;
    private String brand;
    private String color;
    private String notes;

    public ProductResponseDto() {
    }

    public ProductResponseDto(ProductModel product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.type = product.getType();
        this.brand = product.getBrand();
        this.color = product.getColor();
        this.notes = product.getNotes();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
