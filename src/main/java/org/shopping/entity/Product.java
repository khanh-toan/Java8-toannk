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
@Table(name = "products")
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Product extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "IMAGE")
    @Lob
    private byte[] image;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetails> orderDetailsList;
}
