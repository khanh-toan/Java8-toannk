package org.ttkd6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account extends BaseEntity{
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