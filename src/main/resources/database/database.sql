# initial sql script for creating schema
DROP DATABASE IF EXISTS website;
CREATE DATABASE IF NOT EXISTS website;

CREATE TABLE IF NOT EXISTS website.user (
    login CHAR(30) NOT NULL UNIQUE,
    password CHAR(30) NOT NULL,
    full_name CHAR(100) NOT NULL,
    PRIMARY KEY(login)
);

CREATE TABLE IF NOT EXISTS website.category (
    id SMALLINT AUTO_INCREMENT,
    title CHAR(100) NOT NULL,
    url CHAR(20) NOT NULL,
    description CHAR(200),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS website.subgroup (
    id SMALLINT AUTO_INCREMENT,
    category_id SMALLINT NOT NULL,
    title CHAR(100) NOT NULL,
    url CHAR(20) NOT NULL,
    description CHAR(200),
    PRIMARY KEY(id),
    FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS website.project (
    id SMALLINT AUTO_INCREMENT,
    subgroup_id SMALLINT NOT NULL,
    title CHAR(100) NOT NULL,
    url CHAR(20) NOT NULL,
    description CHAR(200),
    PRIMARY KEY(id),
    FOREIGN KEY (subgroup_id)
        REFERENCES subgroup(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS website.image (
    id SMALLINT AUTO_INCREMENT,
    project_id SMALLINT NOT NULL,
    title CHAR(100) NOT NULL,
    data blob,
    PRIMARY KEY(id),
    FOREIGN KEY (project_id)
        REFERENCES project(id)
        ON DELETE CASCADE
);

