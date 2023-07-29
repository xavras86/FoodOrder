package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.MenuItem;

import java.util.List;

public interface MenuItemDAO {

    List<MenuItem> findAll();

}
