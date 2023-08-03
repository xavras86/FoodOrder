package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.xavras.FoodOrder.infrastructure.security.RoleEntity;
import pl.xavras.FoodOrder.infrastructure.security.UserRepository;

import java.util.Set;

@Controller
@AllArgsConstructor
public class HomeController {

    public static final String HOME = "/";
    private final UserRepository userRepository;

    @GetMapping(HOME)
    public String home() {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var role = userRepository.findByUserName(username).getRoles().stream().toList().get(0).getRole();
        //to jest jakaś lipa - trzeba to przerobić ;)

        return switch(role){
            case "CUSTOMER" -> "customer";
            case "OWNER" -> "owner";
            default -> throw new RuntimeException(new RuntimeException("something went terribly wrong with security :( no owner related to current user email [%s]"
                    .formatted(role)));
        };
    }
}

