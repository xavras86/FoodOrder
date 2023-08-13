package pl.xavras.FoodOrder.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.xavras.FoodOrder.domain.Street;

import java.util.List;
import java.util.Optional;

public interface StreetDAO {
   List<Street> findAll();

   Optional<Street> findByStreetId(Integer streetId);

   Optional<Street> findByStreetName(String streetName);

   Page<Street> findAll(Pageable pageable);
}
