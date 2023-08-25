# FoodOrder!

This is a Spring Boot + JPA + Spring MVC + Thymeleaf application that allows users to order food with delivery. The basic functionalities implemented in the application include:

## Owner functionalities:
- The owner of a restaurant can create an account on the food ordering portal to offer their services.
- An owner can have multiple different restaurants.
- The owner of a restaurant can define their menu, meaning they can specify food categories (appetizers, soups, main courses, desserts) and input descriptions along with prices.
- Each menu item can be withdrawn or reinstated based on product availability.
- For each of the listed items, the owner of a restaurant can upload photos through the application to make it easier for the customer to choose.
- The owner of a restaurant can provide a list of streets to which they provide food delivery. Streets can be added or removed from the range of services offered by each restaurant.
- The owner of a restaurant can view orders placed at their establishment, categorized as pending, fulfilled, and cancelled.
- The establishment owner can change the status of orders from active to fulfilled.

## Customer Functionalities:
Customers can create an account.
Customers can provide their address, so that the application can display a list of establishments offering food delivery based on a set of streets defined by the establishment owners.
Customers can select an establishment and view its details, including the menu and a Google Maps map displaying the establishment's address.
Customers can place an order, specifying the quantity and type of items they wish to order.
Customers can view a confirmation of their placed order, complete with a generated order number, as well as a Google Maps map showing the route between the establishment and the delivery address.
Customers can cancel an order if it's within 20 minutes of placing it.
Customers can check their orders, categorized as fulfilled, pending, and cancelled, along with a link to a page displaying order details.
REST-API Functionalities:
An integral part of the application is the REST-API, designed for business use by owners (accounts with the owner role have access to the API).
The API consists of two controllers that allow calling GET, POST, PUT, and DELETE endpoints related to establishments and orders.
The detailed functionality of the API is described in the Swagger UI documentation.

## Installation
1. Clone this repository to your local computer.
2. The application is set up to run in a container using Docker Compose. After starting the application and Docker, run the container:

```
./gradlew clean build -x test
docker compose up -d
```

3. The application is accessible from your web browser at the following address:
```
http://localhost:8190/foodorder/login
```

4. The API along with SwaggerUI documentation is accessible from your web browser at the following address (login with owner credentials is required):
```
http://localhost:8190/foodorder/swagger-ui/index.html
```
5. Upon startup, the application is populated with startup data. Sample accounts for both owners and customers are also created.
```
Owner - login: owner password: test
```

```
Customer - login: customer password: test
```


## Utilized Technologies
1. The application has been developed using Spring Boot and PostgreSQL database. The Entity-Relationship Diagram (ERD) showcases the database tables along with the configuration of relationships.

![](https://github.com/xavras86/FoodOrder/assets/99759304/f12ac45d-05ce-48f2-ba95-0a57d2fa485a)

2. The structure of the application on the Java side follows a layered architecture, with distinct layers for repositories, services, and controllers, each with corresponding object layers such as entities, domain objects, and DTOs. The mapping between these layers has been implemented using Mapstruct.

![](https://github.com/xavras86/FoodOrder/assets/99759304/2b93f9fb-186c-4301-8ec0-62341c4deaaa)

3. Startup data is loaded into the application upon launch using Flyway migration scripts.

4. The WEB layer has been prepared using the Thymeleaf template engine.


## Author
Marcin Sikora
