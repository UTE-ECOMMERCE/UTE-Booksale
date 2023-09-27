package com.ecommerce.booksale.user;


import com.ecommerce.booksale.order.Order;
import com.ecommerce.booksale.user.role.Role;
import com.ecommerce.booksale.registration.token.ConfirmationToken;
import com.ecommerce.booksale.user.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name="full_name", nullable = false)
    private String fullName;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="phone")
    private String phone;

    @Column(name="enable")
    private Boolean enabled;

    @Column(name="is_lock")
    private Boolean isLock;

    //One user has many roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="user_role",
                joinColumns = @JoinColumn(name="user_id"),
                inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

    // one user has many token
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ConfirmationToken> confirmationTokens;

    // one user has many
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    // one user has many orders
    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH} )
    private List<Order> orders;

    public User(String fullName, String email, String password, String phone, List<Role> roles) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roles = roles;
    }


    public void addAddress(Address newAddress){
        if (addresses != null){
            addresses = new ArrayList<>();
        }
        newAddress.setUser(this);
        addresses.add(newAddress);
    }

    public void addOrders(Order newOrder){
        if (orders != null){
            orders = new ArrayList<>();
        }
        newOrder.setUser(this);
        orders.add(newOrder);
    }

    public void addToken(ConfirmationToken token){
        if (confirmationTokens != null){
            confirmationTokens = new ArrayList<>();
        }
        token.setUser(this);
        confirmationTokens.add(token);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName().toString())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
