package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.RestaurantDAO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.entity.RestaurantEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.AddressEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RestaurantRepository implements RestaurantDAO {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final AddressEntityMapper addressEntityMapper;

    private final OwnerRepository ownerRepository;
    private final StreetRepository streetRepository;
    private final AddressRepository addressRepository;



    @Override
    public Optional<Restaurant> findByName(String name) {
        return restaurantJpaRepository.findByName(name)
                .map(restaurantEntityMapper::mapFromEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantJpaRepository.findAll().stream()
                .map(restaurantEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Set<Restaurant> findRestaurantsByStreetName(String streetName) {
        var street = streetRepository.findByStreetName(streetName)
                .orElseThrow(() -> new RuntimeException("wrong street name [%s]".formatted(streetName)));
        return street.getRestaurantStreets().stream().map(RestaurantStreet::getRestaurant)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Restaurant> findRestaurantsByOwner(String ownerEmail) {

        return restaurantJpaRepository.findRestaurantsByOwner(ownerEmail)
                .stream().map(restaurantEntityMapper::mapFromEntity).collect(Collectors.toSet());
    }

    @Override
    public Restaurant createNewRestaurant(Restaurant newRestaurant, Address newAddress) {

        Restaurant restaurant = newRestaurant.withOwner(ownerRepository.findLoggedOwner());
        RestaurantEntity restaurantEntity = restaurantEntityMapper.mapToEntity(restaurant);

        //weryfikacja czy przekazany adres jest już w bazie
        Optional<AddressEntity> existingAddress = addressRepository.findExistingAddress(newAddress);
        if (existingAddress.isPresent()) {
            restaurantEntity.setAddress(existingAddress.get());
        } else {
            restaurantEntity.setAddress(addressEntityMapper.mapToEntity(newAddress));
        }
        return restaurantEntityMapper.mapFromEntity(restaurantJpaRepository.save(restaurantEntity));
    }

}

