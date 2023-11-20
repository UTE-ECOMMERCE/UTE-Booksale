package com.ecommerce.booksale.authentication.registration;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterData {

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Nhập email của bạn")
    private String email;

    @Size(min = 6, max = 50, message = "Tên đăng nhập phải có ít nhất 6 ký tự và nhiều nhất 50 ký tự")
    @NotBlank(message = "Nhập tên đăng nhập của bạn")
    private String fullName;

    @NotBlank(message = "Nhập mật khẩu")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường và 1 số")
    private String password;

    @NotBlank(message = "Nhập lại mật khẩu")
    private String confirmPassword;

}
