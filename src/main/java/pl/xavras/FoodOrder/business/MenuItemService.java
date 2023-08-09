package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.MenuItemDAO;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;

@Service
@AllArgsConstructor
@Slf4j
public class MenuItemService {

    private final MenuItemDAO menuItemDAO;

    @Transactional
    public MenuItem saveMenuItem(MenuItem menuItemToSave, Restaurant restaurant) {
        return menuItemDAO.addMenuItem(menuItemToSave, restaurant);
    }

    public Page<MenuItem> getMenuItemsByRestaurantPaged(Restaurant restaurant, Pageable pageable) {
        return menuItemDAO.getMenuItemsByRestaurantPaged(restaurant, pageable);
    }

    @Transactional
    public void changeAvailability(MenuItem menuItem) {
        menuItemDAO.changeAvailability(menuItem);
    }

    public MenuItem findById(Integer menuItemId) {
        return menuItemDAO.findById(menuItemId);
    }

    public MenuItem findByName(String name) {
        return menuItemDAO.findByName(name);
    }


}