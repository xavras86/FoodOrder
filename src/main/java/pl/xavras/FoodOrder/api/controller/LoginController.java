package pl.xavras.FoodOrder.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.xavras.FoodOrder.api.dto.UserDTO;
import pl.xavras.FoodOrder.api.dto.mapper.UserMapper;
import pl.xavras.FoodOrder.business.UserService;
import pl.xavras.FoodOrder.domain.User;
import pl.xavras.FoodOrder.infrastructure.security.UserJpaRepository;
@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {

    public static final String HOME = "/";
    private final UserJpaRepository userRepository;

    private final UserService userService;


    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    @GetMapping("/register")
    public String showLoginPageWithRegistration(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password
    ) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String role = userRepository.findByUserName(username).getRoles().stream().toList().get(0).getRole();

        return switch (role) {
            case "CUSTOMER" -> "customer";
            case "OWNER" -> "owner";
            default ->
                    throw new RuntimeException(new RuntimeException("something went terribly wrong with security :( no user related to current user email [%s]"
                            .formatted(role)));
        };
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute ("userDTO") UserDTO userDTO)
    {

        User user = userMapper.map(userDTO);
        userService.registerNewUser(user);
        log.info("useer email" + userDTO.getEmail());

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

