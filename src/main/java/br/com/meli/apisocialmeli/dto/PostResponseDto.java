package br.com.meli.apisocialmeli.dto;

import br.com.meli.apisocialmeli.model.PostModel;
import br.com.meli.apisocialmeli.model.ProductType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private UserDto seller;
    private Integer category;
    private BigDecimal price;
    private ProductResponseDto product;
    private LocalDateTime createdAt;
    private Boolean hasPromo;
    private BigDecimal discount;

    public PostResponseDto() {
    }

    public PostResponseDto(PostModel post) {
        this.id = post.getId();
        this.seller = new UserDto(post.getSeller().getId(), post.getSeller().getNome());
        this.category = post.getCategory();
        this.price = post.getPrice();
        this.product = new ProductResponseDto(post.getProduto());
        this.createdAt = post.getCreatedAt();
        this.hasPromo = post.getHasPromo();
        this.discount = post.getDiscount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getSeller() {
        return seller;
    }

    public void setSeller(UserDto seller) {
        this.seller = seller;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
}
