package org.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailInfo {
    private Integer id;
    private String productCode;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double amount;
}
