package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.xavras.FoodOrder.business.dao.StreetDAO;
import pl.xavras.FoodOrder.domain.Street;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
                .orElseThrow(() -> new RuntimeException("Street with name [%s] doest not exists"
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

}