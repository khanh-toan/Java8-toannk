package org.shopping.model;

import lombok.*;
import org.shopping.entity.Order;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private int id;
    private Date orderDate;
    private int orderNum;
    private double amount;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String customerPhone;
    private List<OrderDetailInfo> details;

    public Order getOrder() {
        Order order = new Order();
        order.setId(this.id);
        order.setOrderDate(this.orderDate);
        order.setOrderNum(this.orderNum);
        order.setAmount(this.amount);
        order.setAddress(this.customerAddress);
        order.setEmail(this.customerEmail);
        order.setName(this.customerName);
        order.setPhone(this.customerPhone);
        return order;
    }

    public OrderInfo(int id, Date orderDate, int orderNum,
                     double amount, String customerName, String customerAddress,
                     String customerEmail, String customerPhone) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderNum = orderNum;
        this.amount = amount;

        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }
}
