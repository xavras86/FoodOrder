create TABLE menu_item_orders
(
    menu_item_orders_id          SERIAL          NOT NULL,
    quantity                    INT             NOT NULL,
    menu_item_id                 INT             NOT NULL,
    orders_id                    INT            NOT NULL,
    PRIMARY KEY (menu_item_orders_id),
    CONSTRAINT fk_menu_item_orders_menu_item
        FOREIGN KEY (menu_item_id)
            REFERENCES menu_item (menu_item_id),
    CONSTRAINT fk_menu_item_orders_orders
        FOREIGN KEY (orders_id)
            REFERENCES orders (orders_id)
);