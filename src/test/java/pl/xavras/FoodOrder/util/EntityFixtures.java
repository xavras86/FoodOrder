package pl.xavras.FoodOrder.util;

import lombok.experimental.UtilityClass;
import pl.xavras.FoodOrder.infrastructure.database.entity.*;
import pl.xavras.FoodOrder.infrastructure.security.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;


@UtilityClass
public class EntityFixtures {

    public static RestaurantEntity someRestaurantEntity1() {
        return RestaurantEntity.builder()
                .restaurantId(1)
                .name("someName1")
                .phone("+48 123 123 123")
                .email("restautant1@test.pl")
                .address(someAddressEntity1())
                .owner(someOwnerEntity1())
                .restaurantStreets(new HashSet<>())
                .build();
    }

    public static CustomerEntity someCustomerEntity() {
        return CustomerEntity.builder()
                .customerId(1)
                .name("Customer")
                .surname("Testowy")
                .phone("+48 000 000 000")
                .email("testowy@customer.pl")
                .build();
    }

    public static OwnerEntity someOwnerEntity() {
        return OwnerEntity.builder()
                .ownerId(1)
                .name("Owner")
                .surname("Testowy")
                .phone("+48 111 111 111")
                .email("testowy@owner.pl")
                .build();
    }


    public static RestaurantEntity someRestaurantEntity2() {
        return RestaurantEntity.builder()
                .restaurantId(2)
                .name("someName2")
                .phone("+48 123 432 123")
                .email("restautant2@test.pl")
                .address(someAddressEntity2())
                .owner(someOwnerEntity1())
                .restaurantStreets(new HashSet<>())
                .build();
    }

    public static RestaurantEntity someRestaurantEntity3() {
        return RestaurantEntity.builder()
                .restaurantId(3)
                .name("someName3")
                .phone("+48 123 123 321")
                .email("restautant3@test.pl")
                .address(someAddressEntity3())
                .owner(someOwnerEntity1())
                .restaurantStreets(new HashSet<>())
                .build();
    }

    public static AddressEntity someAddressEntity1() {
        return AddressEntity.builder()
                .country("France")
                .city("Paris")
                .street("Polna")
                .buildingNumber("20")
                .build();
    }

    public static AddressEntity someAddressEntity2() {
        return AddressEntity.builder()
                .country("Germany")
                .city("Berlin")
                .street("Pomorska")
                .buildingNumber("15")
                .build();
    }

    public static AddressEntity someAddressEntity3() {
        return AddressEntity.builder()
                .country("Russia")
                .city("Moscov")
                .street("Wrocławska")
                .buildingNumber("12")
                .build();
    }

    public static OwnerEntity someOwnerEntity1() {
        return OwnerEntity.builder()
                .ownerId(1)
                .name("Jan")
                .surname("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48 111 222 333")
                .build();
    }


    public static StreetEntity someStreetEntity1() {
        return StreetEntity.builder()
                .streetId(1)
                .city("Poznań")
                .streetName("Rolna")
                .build();
    }

    public static StreetEntity someStreetEntity2() {
        return StreetEntity.builder()
                .streetId(2)
                .city("Poznań")
                .streetName("Dolna")
                .build();
    }

    public static StreetEntity someStreetEntity3() {
        return StreetEntity.builder()
                .streetId(3)
                .city("Poznań")
                .streetName("Warszawska")
                .build();
    }


    public static RestaurantStreetEntity someRestaurantStreetEntity1() {
        return RestaurantStreetEntity.builder()
                .street(someStreetEntity1())
                .restaurant(someRestaurantEntity1())
                .build();
    }

    public static RestaurantStreetEntity someRestaurantStreetEntity2() {
        return RestaurantStreetEntity.builder()
                .street(someStreetEntity2())
                .restaurant(someRestaurantEntity1())
                .build();

    }

    public static MenuItemEntity someMenuItemEntity() {
        return MenuItemEntity.builder().build();
    }

    public static OrderEntity someOrderEntity() {
        return OrderEntity.builder()
                .orderId(13)
                .orderNumber("123")
                .address(EntityFixtures.someAddressEntity1())
                .restaurant(EntityFixtures.someRestaurantEntity1())
                .completed(false)
                .cancelled(false)
                .customer(EntityFixtures.someCustomerEntity())
                .receivedDateTime(OffsetDateTime.of(
                        2022, 12, 12, 1, 1, 1, 1, ZoneOffset.UTC))
                .totalValue(null)
                .totalValue(new BigDecimal("123.12"))
                .restaurant(EntityFixtures.someRestaurantEntity1())
                .build();

    }




}