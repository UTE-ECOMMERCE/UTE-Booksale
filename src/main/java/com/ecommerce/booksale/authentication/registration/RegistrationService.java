package com.ecommerce.booksale.authentication.registration;


import com.ecommerce.booksale.authentication.registration.token.ConfirmationToken;
import com.ecommerce.booksale.authentication.registration.token.ConfirmationTokenService;
import com.ecommerce.booksale.exception.Messages;
import com.ecommerce.booksale.exception.SendMailFailException;
import com.ecommerce.booksale.user.User;
import com.ecommerce.booksale.user.UserRepository;
import com.ecommerce.booksale.user.UserService;
import com.ecommerce.booksale.utils.constants.AuthenError;
import com.ecommerce.booksale.utils.email.EmailSender;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {

    private static final String REGISTRATION_EMAIL = "Registration Confirmation";
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    public void register(RegisterData user, String confirmPassword) {
        if (!checkUserInput(user, confirmPassword)) {
            throw new IllegalArgumentException(AuthenError.NOT_MATCH_PASSWORD_ERROR);
        }

        if (!isValidEmail(user)) {
            throw new IllegalStateException(AuthenError.EMAIL_IN_USE);
        }


        try{
            // create user and token
            String token = userService.registerUser(user);

            String link = "http://localhost:8080/booksale/register/confirm?token=" + token;
            emailSender.send(
                    user.getEmail(),
                    REGISTRATION_EMAIL,
                    buildEmail(link));
            log.info("Sent email to " + user.getEmail());
        }catch (Exception e){
            log.error(e.getMessage());
            throw new SendMailFailException(Messages.SEND_EMAIL_ERROR);
        }

    }

    @Transactional
    public String confirmToken(String token) {
        // get token from database
        ConfirmationToken confirmationToken = confirmationTokenService
                .findToken(token).
                orElseThrow(() -> new IllegalStateException(AuthenError.TOKEN_NOT_FOUND));

        // Check if the token was confirmed
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(AuthenError.TOKEN_CONFIRMED);
        }

        // get expired time of the token
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(AuthenError.EXPIRED_TOKEN);
        }

        confirmationTokenService.setConfirmedAt(token);

        userService.enableUser(confirmationToken.getUser().getEmail());

        return "confirmed";

    }

    // this function check if user confirm correct their password
    public boolean checkUserInput(RegisterData user, String confirmPassword) {
        if (user.getPassword().equals(confirmPassword)) {
            return true;
        }
        return false;
    }

    // check if the email was exists in db
    public boolean isValidEmail(RegisterData user) {
        Optional<User> newUser = userRepository.findByEmail(user.getEmail().trim());
        if (newUser.isPresent()) {
            return false;
        }
        return true;
    }

    private String buildEmail(String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi ,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
