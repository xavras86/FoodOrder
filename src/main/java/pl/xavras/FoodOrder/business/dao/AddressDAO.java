package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.Address;

import java.util.Optional;

public interface AddressDAO {

    Address saveAddress(Address address);

}
