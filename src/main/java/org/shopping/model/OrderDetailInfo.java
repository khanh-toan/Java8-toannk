package org.shopping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailInfo {
    private int id;
    private int productCode;
    private String productName;
    private int quanity;
    private double price;
    private double amount;
}
