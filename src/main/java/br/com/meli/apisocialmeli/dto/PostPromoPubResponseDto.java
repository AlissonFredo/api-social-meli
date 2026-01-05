package br.com.meli.apisocialmeli.dto;

import br.com.meli.apisocialmeli.model.PostModel;

import java.math.BigDecimal;

public class PostPromoPubResponseDto {
    private Long userId;
    private Integer category;
    private ProductResponseDto product;
    private BigDecimal price;
    private Boolean hasPromo;
    private BigDecimal discount;

    public PostPromoPubResponseDto() {
    }

    public PostPromoPubResponseDto(PostModel post) {
        this.userId = post.getSeller().getId();
        this.category = post.getCategory();
        this.product = new ProductResponseDto(post.getProduto());
        this.price = post.getPrice();
        this.hasPromo = post.getHasPromo();
        this.discount = post.getDiscount();
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductResponseDto getProduct() {
        return product;
    }

    public void setProduct(ProductResponseDto product) {
        this.product = product;
    }
}
