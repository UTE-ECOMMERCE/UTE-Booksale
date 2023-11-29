package com.ecommerce.booksale.user;

import com.ecommerce.booksale.user.address.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String fullName;
    @NotBlank
    private String phone;

    public UserDTO(User user){
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
    }
}
