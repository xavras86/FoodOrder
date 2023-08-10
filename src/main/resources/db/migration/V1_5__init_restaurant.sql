create TABLE restaurant
(
    restaurant_id       SERIAL          NOT NULL,
    name                VARCHAR(32)     NOT NULL,
    phone               VARCHAR(32)     NOT NULL,
    email               VARCHAR(32)     NOT NULL,
    address_id          INT             NOT NULL,
    owner_id            INT             NOT NULL,
    PRIMARY KEY (restaurant_id),
    UNIQUE (name),
    CONSTRAINT fk_restaurant_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id),
    CONSTRAINT fk_restaurant_owner
        FOREIGN KEY (owner_id)
            REFERENCES owner (owner_id)

);