
 -- MySQL Script generated by MySQL Workbench
-- Sun Sep 18 20:52:09 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema coworking_space
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `coworkingspace` ;

-- -----------------------------------------------------
-- Schema coworking_space
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `coworkingspace` DEFAULT CHARACTER SET utf8 ;
USE `coworkingspace` ;

DROP TABLE IF EXISTS `coworkingspace`.`MEMBER` ;
CREATE TABLE `coworkingspace`.`MEMBER` (
    `id` UUID,
    `name` VARCHAR(50) NOT NULL,
    `lastname` VARCHAR(50) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `coworkingspace`.`BOOKING` CASCADE;
CREATE TABLE `coworkingspace`.`BOOKING` (
    `id` UUID,
    `creator` UUID,
    `day_duration` int NOT NULL,
    `date` date NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (creator) REFERENCES MEMBER (id)
);

INSERT INTO `coworkingspace`.`MEMBER` (`id`, `name`, `lastname`, `email`, `password`, `role`)
VALUES (
        '4be5f5bf-8eb5-44ea-8eb5-a5e807856d09',
        'Peter',
        'Rutschmann',
        'peter.rutschmann@gmail.com',
        'test1234',
        'ADMIN'
    ),
    (
        'e1eec954-6ef4-4926-8183-7575af189f2a',
        'Johannes',
        'Höffliger',
        'johannes.höffliger@gmail.com',
        'test1234',
        'MEMBER'
    );
INSERT INTO BOOKING (id, creator, day_duration, date, status)
VALUES ('413e2297-b84b-42ef-97ed-16a8a9d1d671', '4be5f5bf-8eb5-44ea-8eb5-a5e807856d09', 0.5, '2022-09-23', 'Pending');