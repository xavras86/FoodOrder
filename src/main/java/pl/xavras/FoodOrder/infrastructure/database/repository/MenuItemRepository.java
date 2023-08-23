package pl.xavras.FoodOrder.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.business.dao.MenuItemDAO;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.MenuItemJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.MenuItemEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class MenuItemRepository implements MenuItemDAO {

    private final MenuItemJpaRepository menuItemJpaRepository;
    private final RestaurantJpaRepository restaurantJpaRepository;

    private final MenuItemEntityMapper menuItemMapper;

    private final RestaurantEntityMapper restaurantEntityMapper;


    @Override
    public List<MenuItem> findAll(){
        return menuItemJpaRepository.findAll().stream()
                .map(menuItemMapper::mapFromEntity).toList();
    }

    @Override
    public MenuItem addMenuItem(MenuItem menuItem, Restaurant restaurant) {
        MenuItemEntity toSave = menuItemMapper.mapToEntity(menuItem);
        RestaurantEntity restaurantToSet = getRestaurantEntityByName(restaurant);
        toSave.setRestaurant(restaurantToSet);
        MenuItemEntity saved = menuItemJpaRepository.save(toSave);
        log.info("Created new Menu Item Entity: "+ saved.getName());
        return menuItemMapper.mapFromEntity(saved);
    }

    @Override
    public void changeAvailability(MenuItem menuItem) {
        MenuItemEntity menuItemEntity = getMenuItemEntityById(menuItem.getMenuItemId());
        menuItemEntity.setAvailable(!menuItem.getAvailable());
       menuItemJpaRepository.save(menuItemEntity);
    }

    @Override
    public MenuItem findById(Integer menuItemId) {
        return menuItemMapper.mapFromEntity(getMenuItemEntityById(menuItemId));
    }

    @Override
    public Page<MenuItem> getMenuItemsByRestaurantPaged(Restaurant restaurant, Pageable pageable) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.mapToEntity(restaurant);
        Page<MenuItemEntity> byRestaurant = menuItemJpaRepository.findByRestaurant(restaurantEntity, pageable);
        return byRestaurant.map(menuItemMapper::mapFromEntity);
    }

    @Override
    public Page<MenuItem> getAvailableMenuItemsByRestaurant(Restaurant restaurant, Pageable pageable) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.mapToEntity(restaurant);
        Page<MenuItemEntity> byRestaurantAndAvailable = menuItemJpaRepository.findByRestaurantAndAvailable(restaurantEntity, true, pageable);
        return byRestaurantAndAvailable.map(menuItemMapper::mapFromEntity);
    }


    @Override
    public MenuItem findByName(String name) {
        return menuItemMapper.mapFromEntity(menuItemJpaRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Could not find MenuItem with id: [%s]"
                        .formatted(name))));
    }


    private MenuItemEntity getMenuItemEntityById(Integer menuItemId) {
        return menuItemJpaRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find MenuItem with id: [%s]"
                        .formatted(menuItemId)));
    }

    private RestaurantEntity getRestaurantEntityByName(Restaurant restaurant) {
        return restaurantJpaRepository.findByName(restaurant.getName())
                .orElseThrow(() -> new EntityNotFoundException("Could not find restaurant with name: [%s]"
                        .formatted(restaurant.getName())));
    }
}
