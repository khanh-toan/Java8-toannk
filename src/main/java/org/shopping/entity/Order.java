package org.shopping.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders",
uniqueConstraints = { @UniqueConstraint(columnNames = "ORDER_NUM")})
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Order extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @Column(name = "CUSTOMER_ADDRESS")
    private String address;

    @Column(name = "CUSTOMER_EMAIL")
    private String email;

    @Column(name = "CUSTOMER_NAME")
    private String name;

    @Column(name = "CUSTOMER_PHONE")
    private String phone;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "ORDER_NUM")
    private Integer order_num;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetails> orderDetailsList;
}
