package org.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.shopping.entity.Product;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo extends SearchRequestDTO{
    private int id;
    private String name;
    private String code;
    private String description;
    private double price;
    private boolean isDelete;
    private Integer quantity;

    public ProductInfo(Product product) {
        this.code = product.getCode();
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.isDelete = product.getIsDeleted();
        this.quantity = product.getQuantity();
    }
}
