create TABLE restaurant_street
(
    restaurant_street_id    SERIAL       NOT NULL,
    restaurant_id                   INT             NOT NULL,
    street_id               INT             NOT NULL,
    PRIMARY KEY (restaurant_street_id),
    CONSTRAINT fk_restaurant_streete_restaurant
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (restaurant_id),
    CONSTRAINT fk_restaurant_street_street
        FOREIGN KEY (street_id)
            REFERENCES street (street_id)
);