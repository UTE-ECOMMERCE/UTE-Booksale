package com.ecommerce.booksale.user.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="roles")
public class Role {
    @Id
    @Column(name="role_id")
    private Integer roleId;

    @Column(name="role_name")
    private String roleName;
}
