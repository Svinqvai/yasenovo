CREATE SCHEMA IF NOT EXISTS`yasenovo` ;

USE yasenovo;

CREATE TABLE IF NOT EXISTS participant (
    id BIGINT NOT NULL AUTO_INCREMENT,
    number INT,
    name VARCHAR(255),
    sector VARCHAR(255),
    PRIMARY KEY (id)
);
