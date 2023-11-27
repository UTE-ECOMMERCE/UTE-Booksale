package com.ecommerce.booksale.authentication.registration;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/register")
@SessionAttributes("cart")
@Slf4j
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String handleGetRegister(Model model){
        // set object name
        model.addAttribute("registerData", RegisterData.builder().build());
        return "register";
    }

    @GetMapping("/confirm")
    public String confirmRegisterToken(@RequestParam("token") String token){
        registrationService.confirmToken(token);
        return "redirect:/login";
    }

    @PostMapping
    public String handlePostRegister(
            @ModelAttribute("registerData") @Valid  RegisterData registerData,
            Model model){
        try{
            registrationService.register(registerData, registerData.getConfirmPassword());
        }catch (Exception e){
            // set object name
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "register-success";
    }

}
