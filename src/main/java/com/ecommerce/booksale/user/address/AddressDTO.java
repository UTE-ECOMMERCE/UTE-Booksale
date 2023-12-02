package com.ecommerce.booksale.user.address;

import com.ecommerce.booksale.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @NotBlank(message = "Tỉnh/Thành phố không thể để trống")
    private String province;

    @NotBlank(message = "Quận/Huyện không thể để trống")
    private String district;

    @NotBlank(message = "Phường/Xã không thể để trống")
    private String ward;

    @NotBlank(message = "Đường không thể để trống")
    private String street;

    private String description;


    @Valid
    private UserDTO userInformation;
}
