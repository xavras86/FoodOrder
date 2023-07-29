package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.MenuItemOrder;
import pl.xavras.FoodOrder.domain.Order;

import java.util.Set;

public interface MenuItemOrderDAO {

    Set<MenuItemOrder> findByOrderNumber(Order order);

}
