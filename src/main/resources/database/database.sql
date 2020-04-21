# initial sql script for creating schema
DROP DATABASE IF EXISTS website;
CREATE DATABASE IF NOT EXISTS website;

CREATE TABLE IF NOT EXISTS website.user (
    id SMALLINT AUTO_INCREMENT,
    login CHAR(30) NOT NULL UNIQUE,
    password CHAR(30) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS website.roles (
    id TINYINT AUTO_INCREMENT,
    name CHAR(10) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS website.user_role (
    user_id SMALLINT NOT NULL,
    role_id TINYINT NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES user(id)
        ON DELETE CASCADE,
    FOREIGN KEY (role_id)
        REFERENCES role(id)
        ON DELETE CASCADE
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

insert into website.roles(name) values ('ADMIN');
insert into website.roles(name) values ('USER');

