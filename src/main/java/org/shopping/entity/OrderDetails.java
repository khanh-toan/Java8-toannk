package org.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
@Data
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class OrderDetails extends BaseEntity{
    @Serial
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
