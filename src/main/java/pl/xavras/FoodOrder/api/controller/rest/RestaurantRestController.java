package pl.xavras.FoodOrder.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantsDTO;
import pl.xavras.FoodOrder.api.dto.StreetsDTO;
import pl.xavras.FoodOrder.api.dto.mapper.CustomerMapper;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.api.dto.mapper.StreetMapper;
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.StreetService;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.Street;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
@AllArgsConstructor
public class RestaurantRestController {
    public static final String RESTAURANT = "/api/restaurant";
    public static final String RESTAURANT_NAME = "/api/restaurant/{restaurantName}";
    public static final String RESTAURANT_ADD = "/api/restaurant/add";
    public static final String RESTAURANT_EDIT = "/api/restaurant/edit/{restaurantName}";
    public static final String RESTAURANT_STREETS = "/api/restaurant/streets/{restaurantName}";
    private final RestaurantService restaurantService;
    private final StreetService streetService;
    private final StreetMapper streetMapper;
    private final RestaurantMapper restaurantMapper;

    @Operation(summary = "Retrieving a list of all restaurants from the system.")
    @GetMapping(RESTAURANT)
    public RestaurantsDTO restaurantsList() {
        return RestaurantsDTO.of(restaurantService.findAll().stream()
                .map(restaurantMapper::map)
                .toList());
    }
    @Operation(summary = "Fetching information about a restaurant based on its name.")
    @GetMapping(RESTAURANT_NAME)
    public RestaurantDTO restaurantDetails(
            @Parameter(description = "Name of the restaurant.")
            @PathVariable String restaurantName) {
        return restaurantMapper.map(restaurantService.findByName(restaurantName));
    }
    @Operation(summary = "Fetching a list of streets where delivery services are provided by a restaurant based on its name.")
    @GetMapping(RESTAURANT_STREETS)
    public StreetsDTO deliveryStreets(
            @Parameter(description = "Name of the restaurant.")
            @PathVariable String restaurantName) {

        List<Street> streetsByRestaurantName = streetService.findStreetsByRestaurantName(restaurantName);
        return StreetsDTO.of(streetsByRestaurantName.stream()
                .map(streetMapper::map)
                .toList());
    }
    @Operation(summary = "Editing the name, phone number, and email address for a restaurant based on its current name.")
    @PostMapping(RESTAURANT_ADD)
    public ResponseEntity<RestaurantDTO> addRestaurant(
            @Parameter(description = "Details of the new restaurant according to the names of the fields.")
            @Valid @RequestBody RestaurantDTO restaurantDTO) {

        Restaurant restaurant = restaurantService.saveNewRestaurant(restaurantMapper.map(restaurantDTO));
        return ResponseEntity
                .created(URI.create(RESTAURANT_ADD + "/" + restaurant.getRestaurantId()))
                .build();
    }
    @Operation(summary = "Creating a new restaurant based on the name, contact information, and address. The owner is assigned based on the logged-in user.")
    @PatchMapping(RESTAURANT_EDIT)
    public ResponseEntity<?> editRestaurantData(
            @Parameter(description = "Current name of the restaurant.")
            @PathVariable String restaurantName,
            @Parameter(description = "New name of the restaurant.")
            @RequestParam(required = true) String newName,
            @Parameter(description = "New phone number of the restaurant.")
            @Valid @RequestParam(required = true) String newPhone,
            @Parameter(description = "New email address of the restaurant.")
            @Valid @RequestParam(required = true) String newEmail
    ) {
        restaurantService.editRestaurant(restaurantName, newName, newPhone, newEmail);
        return ResponseEntity.ok().build();
    }
}
