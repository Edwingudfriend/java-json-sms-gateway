SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `jjsg_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `jjsg_db` ;

-- -----------------------------------------------------
-- Table `jjsg_db`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jjsg_db`.`users` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `passwd` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jjsg_db`.`sms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jjsg_db`.`sms` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `users_id` INT UNSIGNED NOT NULL,
  `idSMSC` VARCHAR(45) NULL,
  `subid` VARCHAR(20) NULL,
  `msisdn` VARCHAR(11) NOT NULL,
  `sender` VARCHAR(11) NOT NULL,
  `text` VARCHAR(160) NOT NULL,
  `status` SMALLINT NOT NULL,
  `datetime_inbound` DATETIME NOT NULL,
  `datetime_lastmodified` DATETIME NULL,
  `datetime_scheduled` DATETIME NULL,
  `ackurl` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `idSMSC` (`idSMSC` ASC),
  INDEX `fk_sms_users_idx` (`users_id` ASC),
  CONSTRAINT `fk_sms_users`
    FOREIGN KEY (`users_id`)
    REFERENCES `jjsg_db`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
