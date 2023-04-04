DROP TABLE IF EXISTS people;

--create an h2 table

CREATE TABLE people
(

    person_id  INT         NOT NULL AUTO_INCREMENT,

    first_name VARCHAR(20),
    last_name  VARCHAR(20) NOT NULL,

    PRIMARY KEY (person_id)
);
