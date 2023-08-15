package pl.xavras.FoodOrder.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.xavras.FoodOrder.api.dto.UserDTO;
import pl.xavras.FoodOrder.api.dto.mapper.UserMapper;
import pl.xavras.FoodOrder.business.UserService;
import pl.xavras.FoodOrder.domain.User;
import pl.xavras.FoodOrder.infrastructure.security.FoodOrderingUserDetailsService;
import pl.xavras.FoodOrder.infrastructure.security.UserJpaRepository;

import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    public static final String HOME = "/";

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
    private final UserJpaRepository userRepository;

    private final UserService userService;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final FoodOrderingUserDetailsService userDetailsService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public void processLogin(
    ) {}

    @GetMapping(HOME)
    public String home() {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var role = userRepository.findByUserName(username).getRoles().stream().toList().get(0).getRole();
        userDetailsService.loadUserByUsername(username);
        //to jest jakaś lipa - trzeba to przerobić ;)
        return switch (role) {
            case "CUSTOMER" -> "customer";
            case "OWNER" -> "owner";
            default ->
                    throw new RuntimeException(new RuntimeException("something went terribly wrong with security :( no owner related to current user email [%s]"
                            .formatted(role)));
        };
    }


    @GetMapping("/register")
    public String showLoginPageWithRegistration(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("userDTO") UserDTO userDTO,
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }





}

