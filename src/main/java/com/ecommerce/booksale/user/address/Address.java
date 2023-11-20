package com.ecommerce.booksale.user.address;

import com.ecommerce.booksale.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long addressId;

    @Column(name="province")
    private String province;

    @Column(name="district")
    private String district;

    @Column(name="ward") //
    private String ward;

    @Column(name="street")
    private String street;

    @Column(name="type")
    private String type = "HOME"; // HOME or COMPANY

    @Column(name="description")
    private String description;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    @Override
    public String toString() {

            return
                     province +
                     ", " + district +
                     ", " + ward +
                     ", " + street +
                     ", " + type +
                    ", " + description
                    ;

    }
}
