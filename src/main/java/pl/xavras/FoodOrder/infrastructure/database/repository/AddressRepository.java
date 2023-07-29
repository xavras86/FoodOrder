package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.AddressDAO;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.AddressEntityMapper;

import java.util.Optional;


@Repository
@AllArgsConstructor
public class AddressRepository implements AddressDAO {

    private final AddressJpaRepository addressJpaRepository;

    private final AddressEntityMapper addressEntityMapper;



    @Override
    public Optional<Address> findByAddressId(Integer id) {
        return addressJpaRepository.findById(id)
                .map(addressEntityMapper::mapFromEntity);
    }



    @Override
    public Address saveAddress(Address address) {
        AddressEntity toSave = addressEntityMapper.mapToEntity(address);
        System.out.println("z encji do zapisania: "+toSave);
        AddressEntity saved = addressJpaRepository.saveAndFlush(toSave);
        System.out.println("z encji po zapisaniu: "+saved);
        Address address1 = addressEntityMapper.mapFromEntity(saved);
        System.out.println("z z obiektu domenowego po mapowaniu: "+address1);
        return  address1;

    }




}
