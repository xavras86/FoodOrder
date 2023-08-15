package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.User;

import java.util.Optional;

public interface UserDAO {


    User registerNewUser(User user);

}
