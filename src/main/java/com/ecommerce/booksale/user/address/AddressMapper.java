package com.ecommerce.booksale.user.address;


import com.ecommerce.booksale.user.User;
import com.ecommerce.booksale.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "user", source = "userInformation")
    Address addressDTOToAddress(AddressDTO addressDTO);

    @Mapping(target = "userInformation", source = "user")
    AddressDTO addressToAddressDTO(Address address);

    User userDTOToUser(UserDTO user);
}
