package org.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "reviews")
@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Review extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "RATING")
    private Integer rating;
    @Column(name = "COMMENT")
    private String comment;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product product;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private Account user;
}

