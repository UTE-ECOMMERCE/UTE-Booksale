package com.ecommerce.booksale.user;


import com.ecommerce.booksale.utils.constants.AuthenError;
import com.ecommerce.booksale.exception.RoleNotFoundException;
import com.ecommerce.booksale.authentication.registration.RegisterData;
import com.ecommerce.booksale.authentication.registration.token.ConfirmationTokenRepository;
import com.ecommerce.booksale.authentication.registration.token.ConfirmationToken;
import com.ecommerce.booksale.user.role.Role;
import com.ecommerce.booksale.user.role.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(AuthenError.USER_NOT_FOUND, email)));
    }

    public String registerUser(RegisterData user){
        //encode user's password
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role role = roleRepository.findByRoleName("ROLE_CUSTOMER").orElseThrow(() ->  new  RoleNotFoundException("Role not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        User saveUser = User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(encodedPassword)
                .roles(roles)
                .enabled(false)
                .isLock(false)
                .build();

        // create token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new
                ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                saveUser);

        // save confirm token to db
        confirmationTokenRepository.save(confirmationToken);

        return token;
    }

    public int enableUser(String email){
        return userRepository.enableUser(email);
    }


    public UserDTO getUserInformation(String email){
        User user = (User) loadUserByUsername(email);
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }



}
