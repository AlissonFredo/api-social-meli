package br.com.meli.apisocialmeli.dto;

import java.math.BigDecimal;

public class PostPromoPubRequestDto {
    private Long userId;
    private Integer category;
    private ProductRequestDto product;
    private BigDecimal price;
    private Boolean hasPromo;
    private BigDecimal discount;

    public Boolean getHasPromo() {
        return hasPromo;
    }

    public void setHasPromo(Boolean hasPromo) {
        this.hasPromo = hasPromo;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public ProductRequestDto getProduct() {
        return product;
    }

    public void setProduct(ProductRequestDto product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
