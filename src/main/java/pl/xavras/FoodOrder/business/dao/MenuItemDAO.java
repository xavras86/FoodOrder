package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.util.List;

public interface MenuItemDAO {

    List<MenuItem> findAll();

    public MenuItem addMenuItem(MenuItem menuItem, Restaurant restaurant);

}
