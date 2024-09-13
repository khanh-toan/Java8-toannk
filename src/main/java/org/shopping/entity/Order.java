package org.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "orders",
uniqueConstraints = { @UniqueConstraint(columnNames = "ORDER_NUM")})
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Order extends BaseEntity implements Serializable {
    @Serial
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
    private Integer orderNum;

    @Column(name = "Order_Date", nullable = false)
    private Date orderDate;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetails> orderDetailsList;
}
