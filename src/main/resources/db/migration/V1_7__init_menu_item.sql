create TABLE menu_item
(
    menu_item_id        SERIAL          NOT NULL,
    name                VARCHAR(32)     NOT NULL,
    price               NUMERIC(8, 2)   NOT NULL,
    category            VARCHAR(32)     NOT NULL,
    description         TEXT            NOT NULL,
    available           BOOL            DEFAULT TRUE,
    restaurant_id       INT             NOT NULL,
    image               BYTEA,
    PRIMARY KEY (menu_item_id),
    CONSTRAINT fk_menuItem_restaurant
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (restaurant_id)
);