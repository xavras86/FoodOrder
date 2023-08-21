package pl.xavras.FoodOrder.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.RestaurantDAO;
import pl.xavras.FoodOrder.domain.*;
import pl.xavras.FoodOrder.infrastructure.database.entity.*;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RestaurantRepository implements RestaurantDAO {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final AddressEntityMapper addressEntityMapper;
    private final OwnerEntityMapper ownerEntityMapper;
    private final RestaurantStreetEntityMapper restaurantStreetEntityMapper;
    private final OwnerRepository ownerRepository;
    private final StreetRepository streetRepository;
    private final AddressRepository addressRepository;
    private final RestaurantStreetRepository restaurantStreetRepository;


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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Unfortunately, the given street name [%s] is incorrect, please try again"
                                .formatted(streetName)));
        return street.getRestaurantStreets().stream().map(RestaurantStreet::getRestaurant)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Restaurant> findRestaurantsByOwner(String ownerEmail) {

        return restaurantJpaRepository.findRestaurantsByOwner(ownerEmail)
                .stream().map(restaurantEntityMapper::mapFromEntity).collect(Collectors.toSet());
    }

    @Override
    public Restaurant createNewRestaurant(Restaurant newRestaurant) {

        Restaurant restaurant = newRestaurant.withOwner(ownerRepository.findLoggedOwner());
        Address address = restaurant.getAddress();
        RestaurantEntity restaurantEntity = restaurantEntityMapper.mapToEntity(restaurant);

        //weryfikacja czy przekazany adres jest już w bazie
        Optional<AddressEntity> existingAddress = addressRepository.findExistingAddress(address);
        if (existingAddress.isPresent()) {
            restaurantEntity.setAddress(existingAddress.get());
        } else {
            restaurantEntity.setAddress(addressEntityMapper.mapToEntity(address));
        }
        log.info("Created new Restaurant Entity: "+ restaurantEntity.getName());
        return restaurantEntityMapper.mapFromEntity(restaurantJpaRepository.save(restaurantEntity));
    }

    public void alternateCoverageStateForStreet(String restaurantName, Street street) {

        RestaurantEntity restaurantEntity = restaurantJpaRepository.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException("No restaurant with name [%s]".formatted(restaurantName)));
        Set<RestaurantStreetEntity> restaurantStreetEntities = restaurantEntity.getRestaurantStreets();

        RestaurantStreet restaurantStreet = RestaurantStreet.builder()
                .street(street)
                .restaurant(restaurantEntityMapper.mapFromEntity(restaurantEntity))
                .build();

        RestaurantStreetEntity restaurantStreetEntity = restaurantStreetEntityMapper.mapToEntity(restaurantStreet);

        if (restaurantStreetEntities.contains(restaurantStreetEntity)) {
            RestaurantStreetEntity first = restaurantStreetEntities.stream().filter(restaurantStreetEntity::equals).findFirst().get();
            restaurantStreetEntities.remove(restaurantStreetEntity);
            restaurantStreetRepository.delete(first);
        } else {
            restaurantStreetEntities.add(restaurantStreetEntity);
        }
        restaurantEntity.setRestaurantStreets(restaurantStreetEntities);
        RestaurantEntity save = restaurantJpaRepository.save(restaurantEntity);

    }


    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantJpaRepository.findAll(pageable)
                .map(restaurantEntityMapper::mapFromEntity);
    }

    @Override
    public Page<Restaurant> findByOwner(Pageable pageable, Owner activeOwner) {
        OwnerEntity ownerEntity = ownerEntityMapper.mapToEntity(activeOwner);

        Page<RestaurantEntity> byActiveOwner =  restaurantJpaRepository.findByOwner(pageable, ownerEntity);
        return byActiveOwner.map(restaurantEntityMapper::mapFromEntity);
    }



    @Override
    public Page<Restaurant> findRestaurantsByStreetNamePaged(String streetName, Pageable pageable) {
        return restaurantJpaRepository.findByRestaurantStreets_Street_StreetName(streetName,pageable)
                .map(restaurantEntityMapper::mapFromEntity);
    }

    @Override
    public Restaurant editRestaurant(String currentName, String name, String phone, String email) {
        RestaurantEntity restaurantEntity = restaurantJpaRepository.findByName(currentName)
                .orElseThrow(() -> new EntityNotFoundException("Could not find restaurant with name: [%s]"
                        .formatted(currentName)));
        restaurantEntity.setName(name);
        restaurantEntity.setPhone(phone);
        restaurantEntity.setEmail(email);
        RestaurantEntity save = restaurantJpaRepository.save(restaurantEntity);

        return restaurantEntityMapper.mapFromEntity(save);
    }


}

