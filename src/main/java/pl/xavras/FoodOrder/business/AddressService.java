package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.api.dto.AddressDTO;
import pl.xavras.FoodOrder.business.dao.AddressDAO;
import pl.xavras.FoodOrder.domain.Address;


@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressDAO addressDAO;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Transactional
    public Address saveAddress(Address address) {
        return addressDAO.saveAddress(address);
    }

    public String createMapUrl(AddressDTO restaurant, AddressDTO delivery) {

        String origin = restaurant.getCountry() + " " + restaurant.getCity() + " " + restaurant.getStreet() + " " + restaurant.getBuildingNumber();
        String destination = delivery.getCountry() + " " + delivery.getCity() + " " + delivery.getStreet() + " " + delivery.getBuildingNumber();
        return "https://www.google.com/maps/embed/v1/directions?key=" + googleMapsApiKey + "&origin=" + origin + "&destination=" + destination;
    }

    public String createMapUrlPoint(AddressDTO restaurant) {

        String origin = restaurant.getCountry() + " " + restaurant.getCity() + " " + restaurant.getStreet() + " " + restaurant.getBuildingNumber();

        return "https://www.google.com/maps/embed/v1/place?key=" + googleMapsApiKey  +"&q=" + origin;
    }
}