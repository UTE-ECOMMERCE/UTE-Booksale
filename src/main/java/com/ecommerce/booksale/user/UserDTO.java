package com.ecommerce.booksale.user;

import com.ecommerce.booksale.user.address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotBlank(message = "Tên không được để trống")
    private String fullName;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    public UserDTO(User user){
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
    }
}
