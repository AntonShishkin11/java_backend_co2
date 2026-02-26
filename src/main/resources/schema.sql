DROP TABLE IF EXISTS emissions;

CREATE TABLE emissions (
    id BIGINT PRIMARY KEY,
    country VARCHAR(255) NOT NULL,
    region VARCHAR(255),
    year_value INT NOT NULL,
    kilotons DOUBLE,
    metric_tons_per_capita DOUBLE
);