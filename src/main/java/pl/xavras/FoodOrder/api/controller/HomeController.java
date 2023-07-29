package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    public static final String HOME = "/";

    @GetMapping(HOME)
    public String home() {

        return "home";
    }

}

