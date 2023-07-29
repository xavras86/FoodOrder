package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.xavras.FoodOrder.api.dto.mapper.StreetMapper;
import pl.xavras.FoodOrder.business.StreetService;

@Controller
@AllArgsConstructor
@Slf4j
public class StreetController {

    public static final String STREETS = "/streets";
    private final StreetService streetService;
    private final StreetMapper streetMapper;

    @GetMapping(STREETS)
    public String streets(Model model) {
        var allStreets = streetService.findAll().stream()
                .map(streetMapper::map).toList();

        model.addAttribute("streets", allStreets);

//        log.info("streets: " + allStreets);

        return "streets";
    }



//    @GetMapping(STREETS_BY_RESTAURANTS)
//    public String showStreetsByRestaurant(@PathVariable String restaurantName, Model model) {
//        var streetsSet = streetService.findStreetsByRestaurantName(restaurantName).stream()
//                .map(streetMapper::map).collect(Collectors.toSet());
//        model.addAttribute("streets", streetsSet);
//        return "streetsByRestaurant";
//    }


}

