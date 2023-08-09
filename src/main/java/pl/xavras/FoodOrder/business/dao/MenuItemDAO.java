package pl.xavras.FoodOrder.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.List;

public interface MenuItemDAO {

    List<MenuItem> findAll();

    MenuItem addMenuItem(MenuItem menuItem, Restaurant restaurant);

    void changeAvailability(MenuItem menuItem);

    MenuItem findById(Integer menuItemId);



    MenuItem findByName(String name);

    Page<MenuItem> getMenuItemsByRestaurantPaged(Restaurant restaurant, Pageable pageable);
}
