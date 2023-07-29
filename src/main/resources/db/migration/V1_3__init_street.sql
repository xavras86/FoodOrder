create TABLE street
(
    street_id   SERIAL      NOT NULL,
    city                VARCHAR(32) NOT NULL,
    street              VARCHAR(64) NOT NULL,
    PRIMARY KEY (street_id)
);
