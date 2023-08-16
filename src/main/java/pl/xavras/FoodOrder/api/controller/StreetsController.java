package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.xavras.FoodOrder.business.StreetService;
import pl.xavras.FoodOrder.business.UtilityService;
import pl.xavras.FoodOrder.domain.Street;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class StreetsController {

    public static final String STREETS = "/customer/streets";
    private final StreetService streetService;

    private final UtilityService utilityService;


    @GetMapping(STREETS)
    public String streets(Model model,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(defaultValue = "1") int pageNumber,
                          @RequestParam(defaultValue = "streetName") String sortBy,
                          @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        Page<Street> streetPage = streetService.findAll(pageable);
        List<Integer> pageNumbers = utilityService.generatePageNumbers(pageNumber, streetPage.getTotalPages());

        model.addAttribute("streets", streetPage.getContent());
        model.addAttribute("totalPages", streetPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("pageNumbers", pageNumbers);

        return "customer-streets";

    }
}



