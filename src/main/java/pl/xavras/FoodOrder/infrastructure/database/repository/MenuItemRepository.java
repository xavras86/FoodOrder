package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.MenuItemDAO;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.MenuItemOrderEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.MenuItemJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.MenuItemEntityMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class MenuItemRepository implements MenuItemDAO {

    private final MenuItemJpaRepository menuItemJpaRepository;
    private final RestaurantJpaRepository restaurantJpaRepository;

    private final MenuItemEntityMapper menuItemMapper;


    @Override
    public List<MenuItem> findAll(){
        return menuItemJpaRepository.findAll().stream()
                .map(menuItemMapper::mapFromEntity).toList();
    }

    @Override
    public MenuItem addMenuItem(MenuItem menuItem, Restaurant restaurant) {
        MenuItemEntity toSave = menuItemMapper.mapToEntity(menuItem);
        RestaurantEntity restaurantToSet = restaurantJpaRepository.findByName(restaurant.getName())
                .orElseThrow(() -> new RuntimeException("Could not find restaurant with2 name: [%s]"
                        .formatted(restaurant.getName())));
        toSave.setRestaurant(restaurantToSet);
        MenuItemEntity saved = menuItemJpaRepository.save(toSave);
        return menuItemMapper.mapFromEntity(saved);
    }


//    @Override
//    public Customer saveCustomer(Customer customer) {
//        CustomerEntity toSave = customerEntityMapper.mapToEntity(customer);
//        CustomerEntity saved = customerJpaRepository.save(toSave);
//        return customerEntityMapper.mapFromEntity(saved);
//    }
}
