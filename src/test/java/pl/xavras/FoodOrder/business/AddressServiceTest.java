package pl.xavras.FoodOrder.business;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.business.dao.AddressDAO;
import pl.xavras.FoodOrder.domain.Address;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressDAO addressDAO;


    @Test
    public void testSaveAddress() {
        // Given
        Address address = someAddress1();

        when(addressDAO.saveAddress(address)).thenReturn(address);

        // When
        Address result = addressService.saveAddress(address);

        // Then
        assertNotNull(result);
        assertEquals(address.getCountry(), result.getCountry());
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getStreet(), result.getStreet());
        assertEquals(address.getBuildingNumber(), result.getBuildingNumber());
    }

    @Test
    public void testCreateMapUrl() {
        // Given
        Address restaurant = someAddress1();
        Address delivery = someAddress2();

        // When
        String mapUrl = addressService.createMapUrl(restaurant, delivery);

        // Then
        assertNotNull(mapUrl);
        assertTrue(mapUrl.contains(restaurant.getCountry()));
        assertTrue(mapUrl.contains(restaurant.getCity()));
        assertTrue(mapUrl.contains(restaurant.getStreet()));
        assertTrue(mapUrl.contains(restaurant.getBuildingNumber()));
        assertTrue(mapUrl.contains(delivery.getCountry()));
        assertTrue(mapUrl.contains(delivery.getCity()));
        assertTrue(mapUrl.contains(delivery.getStreet()));
        assertTrue(mapUrl.contains(delivery.getBuildingNumber()));
    }

    @Test
    public void testCreateMapUrlPoint() {
        // Given
        Address restaurant = someAddress3();

        // When
        String mapUrl = addressService.createMapUrlPoint(restaurant);

        // Then
        assertNotNull(mapUrl);
        assertTrue(mapUrl.contains(restaurant.getCountry()));
        assertTrue(mapUrl.contains(restaurant.getCity()));
        assertTrue(mapUrl.contains(restaurant.getStreet()));
        assertTrue(mapUrl.contains(restaurant.getBuildingNumber()));
    }
}