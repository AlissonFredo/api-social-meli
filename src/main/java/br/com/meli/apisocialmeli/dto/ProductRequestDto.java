package br.com.meli.apisocialmeli.dto;

import br.com.meli.apisocialmeli.model.ProductType;
import jakarta.validation.constraints.*;

public class ProductRequestDto {
    @NotBlank(message = "O campo não pode estar vazio.")
    @Size(max = 40, message = "O comprimento não pode exceder 40 caracteres.")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\.\\, ]+$", message = "O campo não pode conter caracteres especiais.")
    private String productName;

    @NotNull
    private ProductType type;

    @NotBlank(message = "O campo não pode estar vazio.")
    @Size(max = 25, message = "O comprimento não pode exceder 25 caracteres.")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\.\\, ]+$", message = "O campo não pode conter caracteres especiais.")
    private String brand;

    @NotBlank(message = "O campo não pode estar vazio.")
    @Size(max = 15, message = "O comprimento não pode exceder 15 caracteres.")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\.\\, ]+$", message = "O campo não pode conter caracteres especiais.")
    private String color;

    @Size(max = 80, message = "O comprimento não pode exceder 80 caracteres.")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\.\\, ]+$", message = "O campo não pode conter caracteres especiais.")
    private String notes;

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
