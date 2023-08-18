package pl.xavras.FoodOrder.infrastructure.database.repository;


import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.AddressEntityMapper;
import pl.xavras.FoodOrder.util.integration.configuration.AbstractIT;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class AddressRepositoryDataJpaTest extends AbstractIT {

    private AddressJpaRepository addressJpaRepository;
    private AddressRepository addressRepository;

    @Test
    void thatAddressCanBeSavedCorrectly() {
        //given
        var addresses = List.of(someAddress1(), someAddress2());
        int sizeBefore = addressJpaRepository.findAll().size();
        addresses.forEach(a -> addressRepository.saveAddress(a));

        //when
        List<AddressEntity> employeesFound = addressJpaRepository.findAll();
        int sizeAfter = addressJpaRepository.findAll().size();

        //then
        assertThat(sizeBefore).isEqualTo(sizeAfter - 2);

    }


    @Test
    void testThatFindExistingAddressWorksCorrectly() {
        //given
        Address address = someAddress3();
        Address addressSaved = addressRepository.saveAddress(address);

        //when
        Optional<AddressEntity> foundAddressEntity = addressJpaRepository.findByCountryAndCityAndStreetAndBuildingNumber(
                addressSaved.getCountry(),
                addressSaved.getCity(),
                addressSaved.getStreet(),
                addressSaved.getBuildingNumber()
        );
        //then

        assertThat(foundAddressEntity).isPresent();
        assertThat(foundAddressEntity.get())
                .usingRecursiveComparison().ignoringFields("addressId", "restaurant", "orders")
                .isEqualTo(address);

    }

}