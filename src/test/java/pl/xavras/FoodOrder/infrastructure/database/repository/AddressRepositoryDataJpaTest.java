package pl.xavras.FoodOrder.infrastructure.database.repository;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.infrastructure.database.entity.AddressEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.xavras.FoodOrder.util.integration.configuration.AbstractIT;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;


@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
class AddressRepositoryDataJpaTest extends AbstractIT {

    private AddressJpaRepository addressJpaRepository;
    private AddressRepository addressRepository;

    @Test
    void thatAddressCanBeSavedCorrectly() {
        //given
        var addresses = List.of(someAddress1(), someAddress2());
        int sizeBefore = addressJpaRepository.findAll().size();
        log.info("sizeBefore :" + sizeBefore);
        addresses.forEach(a -> addressRepository.saveAddress(a));


        //when
        int sizeAfter = addressJpaRepository.findAll().size();
        log.info("sizeAfter :" + sizeAfter);

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
        assertThat(foundAddressEntity.get().getCountry()).isEqualTo(someAddress3().getCountry());
        assertThat(foundAddressEntity.get().getCity()).isEqualTo(someAddress3().getCity());
        assertThat(foundAddressEntity.get().getStreet()).isEqualTo(someAddress3().getStreet());
        assertThat(foundAddressEntity.get().getBuildingNumber()).isEqualTo(someAddress3().getBuildingNumber());
    }

}