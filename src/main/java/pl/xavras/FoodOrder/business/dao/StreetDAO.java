package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.Street;

import java.util.List;
import java.util.Optional;

public interface StreetDAO {
   List<Street> findAll();

   Optional<Street> findByStreet(String street);

   //
//    Set<Street> findStreetsByRestaurantName(String name);

}
