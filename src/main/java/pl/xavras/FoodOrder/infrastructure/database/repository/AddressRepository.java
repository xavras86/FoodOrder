package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.AddressDAO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.AddressEntityMapper;

import java.util.Optional;



@Slf4j
@Repository
@AllArgsConstructor
public class AddressRepository implements AddressDAO {

    private final AddressJpaRepository addressJpaRepository;
    private final AddressEntityMapper addressEntityMapper;


    public Optional<AddressEntity> findExistingAddress(Address address) {
        return addressJpaRepository.findByCountryAndCityAndStreetAndBuildingNumber(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getBuildingNumber()
        );
    }


    @Override
    public Address saveAddress(Address address) {
        AddressEntity toSave = addressEntityMapper.mapToEntity(address);
        AddressEntity saved = addressJpaRepository.saveAndFlush(toSave);
        log.info("Created new AddressEntity: "+ saved.getCity() + " "+saved.getStreet() +" "+ saved.getBuildingNumber());
        return addressEntityMapper.mapFromEntity(saved);
    }
}
