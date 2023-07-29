create TABLE address
(
    address_id          SERIAL      NOT NULL,
    country             VARCHAR(32) NOT NULL,
    city                VARCHAR(32) NOT NULL,
    street              VARCHAR(64) NOT NULL,
    building_number     VARCHAR(32) NOT NULL,
    PRIMARY KEY (address_id)
);

