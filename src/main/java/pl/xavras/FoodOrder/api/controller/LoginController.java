package pl.xavras.FoodOrder.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.xavras.FoodOrder.api.dto.UserDTO;
import pl.xavras.FoodOrder.api.dto.mapper.UserMapper;
import pl.xavras.FoodOrder.business.UserService;
import pl.xavras.FoodOrder.domain.User;
import pl.xavras.FoodOrder.infrastructure.security.FoodOrderingUserDetailsService;
import pl.xavras.FoodOrder.infrastructure.security.UserRepository;

import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    public static final String HOME = "/home";
    public static final String REGISTER = "/register";
    public static final String LOGOUT = "/logout";
    public static final String LOGIN = "/login";
    public static final String CONGRATULATIONS = """
            Hello %s,
            Congratulations! Your new account has just been created in our application.
            We are thrilled to have you join our community. Now you can login and enjoy all the features of our application and make use of the available options.
            Your account details:
            - Username: %s
            - Email Address: %s
            - Your role: %s
            We wish you a great experience using our application!
            Best regards,
            The Food Order Team
                        """;
    private final FoodOrderingUserDetailsService userDetailsService;
    private final UserService userService;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @GetMapping(LOGIN)
    public String showLoginForm() {
        return "login";
    }

    @PostMapping(LOGIN)
    public void processLogin(
    ) {

    }

    @GetMapping(HOME)
    public String home(Authentication authentication) {

                String authorities = authentication.getAuthorities()
                .stream()
                .findFirst()
                        .orElseThrow(() -> new SecurityException("Something went terribly wrong with security. There is no role assigned to the user"))
                .toString();
                log.info("LOGGED USER ROLE: "+ authorities);

        return switch (authorities) {
            case "CUSTOMER" -> "customer";
            case "OWNER" -> "owner";
            default ->
                    throw new SecurityException("Something went terribly wrong with security. No valid role assigned to the current user");
        };
    }





    @GetMapping(REGISTER)
    public String showLoginPageWithRegistration(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping(REGISTER)
    public String registerUser(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            RedirectAttributes redirectAttributes) {

        User user = userMapper.map(userDTO);
        User createdUser = userService.registerNewUser(user);
        log.info("user email" + userDTO.getEmail());


        if (Objects.nonNull(createdUser)) {
            redirectAttributes.addFlashAttribute("registrationCongratulation", CONGRATULATIONS
                    .formatted(createdUser.getUsername(), createdUser.getUsername(), createdUser.getEmail(), createdUser.getRole()));
            return "redirect:/login";
        }

        return "/login";
    }

    @GetMapping(LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }


}

