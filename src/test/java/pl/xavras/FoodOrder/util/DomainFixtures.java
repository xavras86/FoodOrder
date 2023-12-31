package pl.xavras.FoodOrder.util;

import lombok.experimental.UtilityClass;
import pl.xavras.FoodOrder.domain.*;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;

import java.util.HashSet;


@UtilityClass
public class DomainFixtures {

    public static Customer someCustomer() {
        return Customer.builder()
                .customerId(666)
                .name("Customer")
                .surname("Testowy")
                .phone("+48 000 000 000")
                .email("testowy@customer.pl")
                .build();
    }

    public static Restaurant someRestaurant1() {
        return Restaurant.builder()
                .restaurantId(1)
                .name("someName1")
                .phone("+48 123 123 123")
                .email("jakUmamy@test.pl")
                .address(someAddress1())
                .owner(someOwner1())
                .build();
    }

    public static Restaurant someRestaurant2() {
        return Restaurant.builder()
                .restaurantId(2)
                .name("someName2")
                .phone("+48 123 432 123")
                .email("restautant2@test.pl")
                .address(someAddress2())
                .owner(someOwner1())
                .build();
    }

    public static Restaurant someRestaurant3() {
        return Restaurant.builder()
                .restaurantId(3)
                .name("someName3")
                .phone("+48 123 123 321")
                .phone("+48 123 123 321")
                .email("restautant3@test.pl")
                .address(someAddress3())
                .owner(someOwner1())
                .build();
    }

    public static Address someAddress1() {
        return Address.builder()
                .country("France")
                .city("Paris")
                .street("Polna")
                .buildingNumber("20")
                .build();
    }

    public static Address someAddress2() {
        return Address.builder()
                .country("Germany")
                .city("Berlin")
                .street("Pomorska")
                .buildingNumber("15")
                .build();
    }

    public static Address someAddress3() {
        return Address.builder()
                .country("Russia")
                .city("Moscov")
                .street("Wrocławska")
                .buildingNumber("12")
                .build();
    }

    public static Owner someOwner1() {
        return Owner.builder()
                .name("Jan")
                .surname("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48 111 222 333")
                .build();
    }

    public static User someUser1() {
        return User.builder()
                .name("Paweł")
                .surname("Nowak")
                .password("1234")
                .email("pawel@nowak.pl")
                .phone("+48 123 252 333")
                .build();
    }

    public static Street someStreet1() {
        return Street.builder()
                .streetId(1)
                .city("Poznań")
                .streetName("Rolna")
                .build();
    }

    public static Street someStreet2() {
        return Street.builder()
                .streetId(2)
                .city("Poznań")
                .streetName("Dolna")
                .build();
    }

    public static Street someStreet3() {
        return Street.builder()
                .streetId(3)
                .city("Poznań")
                .streetName("Warszawska")
                .build();
    }


    public static RestaurantStreet someRestaurantStreet1() {
        return RestaurantStreet.builder()
                .street(someStreet1())
                .restaurant(someRestaurant1())
                .build();
    }

    public static RestaurantStreet someRestaurantStreet2() {
        return RestaurantStreet.builder()
                .street(someStreet2())
                .restaurant(someRestaurant1())
                .build();

    }

    public static MenuItem someMenuItem() {
        return MenuItem.builder().build();
    }

    public static MenuItemOrder someMenuItemOrder() {
        return MenuItemOrder.builder().build();
    }

    public static Order someOrder() {
        return Order.builder().build();
    }

}









