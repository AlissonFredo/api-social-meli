package br.com.meli.apisocialmeli.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PostRequestDto {
    @NotNull(message = "O id não pode estar vazio.")
    @Min(value = 1, message = "id deve ser maior que zero.")
    private Long userId;

    @NotNull(message = "O campo não pode estar vazio.")
    private Integer category;

    @Valid
    @NotNull(message = "O campo não pode estar vazio.")
    private ProductRequestDto product;

    @NotNull(message = "O campo não pode estar vazio.")
    @DecimalMax(value = "10000000", inclusive = true, message = "O preço máximo por produto é de 10.000.000.")
    private BigDecimal price;

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
