package pl.xavras.FoodOrder.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.xavras.FoodOrder.business.dao.StreetDAO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.domain.Street;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class StreetService {

    private final StreetDAO streetDAO;

    private final RestaurantService restaurantService;


    public List<Street> findAll() {
        return streetDAO.findAll();
    }


    public Street findById(Integer streetId) {
        return streetDAO.findByStreetId(streetId)
                .orElseThrow(() -> new EntityNotFoundException("Street with name [%s] doest not exists"
                        .formatted(streetId)));
    }

    public Page<Street> findAll(Pageable pageable) {
        return streetDAO.findAll(pageable);

    }

    public Map<Street, Boolean> createStreetStatusMap(String restaurantName, Page<Street> streetPage) {
        return streetPage.stream()
                .collect(Collectors.toMap(
                        street -> street,
                        street -> restaurantService.checkStreetCoverageForRestaurant(restaurantName, street),
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new
                ));
    }

    public Set<Street> findStreetsByRestaurantName(String restaurantName) {
        Restaurant byName = restaurantService.findByName(restaurantName);
        return byName.getRestaurantStreets().stream()
                .map(RestaurantStreet::getStreet).collect(Collectors.toSet());
    }

    public Boolean isDeliveryStreetInRange(String restaurantName, Address deliveryAddress){
        Set<Street> deliveryRange = new HashSet<>(findStreetsByRestaurantName(restaurantName));
        Street deliveryStreet = Street.builder()
                .city(deliveryAddress.getCity())
                .streetName(deliveryAddress.getStreet())
                .build();
        return deliveryRange.contains(deliveryStreet);
    }

}