CREATE TABLE state (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL
);

CREATE TABLE city (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      state_id BIGINT,
                      FOREIGN KEY (state_id) REFERENCES state(id)
);

CREATE TABLE measurement (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             city_id BIGINT,
                             temperature DOUBLE,
                             humidity DOUBLE,
                             timestamp DATETIME,
                             FOREIGN KEY (city_id) REFERENCES city(id)
);
