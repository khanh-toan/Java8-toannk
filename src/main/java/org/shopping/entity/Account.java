package org.shopping.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
@AttributeOverride(name = "id", column = @Column(name = "id"
        , nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Account extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "ENCRYTED_PASSWORD")
    private String password;

    @Column(name = "USER_ROLE")
    private String role;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
}