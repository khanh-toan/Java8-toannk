package org.ttkd6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private Order order;
}
