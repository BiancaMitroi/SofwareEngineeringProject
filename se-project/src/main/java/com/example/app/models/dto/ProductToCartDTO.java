package com.example.app.models.dto;

public class ProductToCartDTO {

    private String username;

    private long productId;

    public ProductToCartDTO(String username, long productId){
        this.username = username;
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
