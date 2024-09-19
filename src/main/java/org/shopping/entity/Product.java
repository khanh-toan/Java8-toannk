package org.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "products")
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Product extends BaseEntity{
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "Code")
    private String code;
    @Column(name = "Name")
    private String name;
    @Column(name = "Price")
    private Double price;
    @Column(name = "Description")
    private String description;
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;
    @Column(name = "Is_delete")
    private Boolean isDeleted;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetails> orderDetailsList;
}
