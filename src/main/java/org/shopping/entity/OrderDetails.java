package org.shopping.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class OrderDetails extends BaseEntity{
    private static final long serialVersionUID = 1L;

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
