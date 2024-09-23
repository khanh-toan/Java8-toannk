package org.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "account")
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Account extends BaseEntity{
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "ENCRYTED_PASSWORD")
    private String password;
    @Column(name = "USER_ROLE")
    private String role;
    @Column(name = "address")
    private String address;
    @Column(name = "description")
    private String description;
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
    // Quan hệ với bảng Review
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews;
}