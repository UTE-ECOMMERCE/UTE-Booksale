package com.ecommerce.booksale.user;

import com.ecommerce.booksale.user.address.Address;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String fullName;
    private String phone;
    private List<Address> addressList;

    public UserDTO(User user){
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.addressList = user.getAddresses();
    }
}
