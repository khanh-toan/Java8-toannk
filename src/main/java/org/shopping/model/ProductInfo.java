package org.shopping.model;

import lombok.Data;
import org.shopping.entity.Product;

@Data
public class ProductInfo {
    private int id;
    private String name;
    private String description;
    private double price;
    public boolean isDelete;

    public ProductInfo(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.isDelete = product.getIsDeleted();
    }


}
