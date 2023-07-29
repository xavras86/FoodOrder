create TABLE orders
(
    orders_id                   SERIAL                      NOT NULL,
    orders_number               VARCHAR(64)                 NOT NULL,
    received_date_time          TIMESTAMP WITH TIME ZONE    NOT NULL,
    completed_date_time         TIMESTAMP WITH TIME ZONE,
    is_cancelled                BOOL DEFAULT FALSE,
    is_completed                BOOL DEFAULT FALSE,
    total_value                 NUMERIC(8, 2)   NOT NULL,
    customer_id                 INT             NOT NULL,
    address_id                  INT             NOT NULL,
    PRIMARY KEY (orders_id),
    UNIQUE (orders_number),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id)
            REFERENCES customer (customer_id),
    CONSTRAINT fk_orders_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)

);