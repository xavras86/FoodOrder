package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerDAO {


    Optional<Owner> findByEmail(String email);

    Owner findLoggedOwner();
}
