-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 30, 2023 at 09:39 AM
-- Server version: 10.4.8-MariaDB
-- PHP Version: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `accountdb`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `reservedAccount` (IN `accountId` BIGINT, IN `amount` DOUBLE, OUT `result` BOOLEAN)  NO SQL
BEGIN
	START TRANSACTION;
  	SELECT * FROM account WHERE id = accountId FOR UPDATE;
  	SELECT @reservedVal := reserved, @balanceVal := balance FROM account WHERE id = accountId;
	IF(@reservedVal + amount <= @balanceVal) THEN
        UPDATE account
        SET reserved = amount + reserved
        WHERE id = accountId;
        IF (ROW_COUNT() > 0) THEN
           COMMIT;
           SELECT true INTO result;
        ELSE
           ROLLBACK;
           SELECT false INTO result;
        END IF;
    ELSE
        ROLLBACK;	
        SELECT false INTO result;
    END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `rollbackReserved` (IN `accountId` BIGINT, IN `amount` DOUBLE, OUT `result` BOOLEAN)  NO SQL
BEGIN
	START TRANSACTION;
  	SELECT * FROM account WHERE id = accountId FOR UPDATE;
        UPDATE account
        SET reserved = amount
        WHERE id = accountId;
        IF (ROW_COUNT() > 0) THEN
           COMMIT;
           SELECT true INTO result;
        ELSE
           ROLLBACK;
           SELECT false INTO result;
        END IF;
   
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `subtractAccount` (IN `accountId` BIGINT, IN `amountReversed` DOUBLE, IN `amountBalance` DOUBLE, OUT `result` BOOLEAN)  NO SQL
BEGIN
	START TRANSACTION;
  	SELECT * FROM account WHERE id = accountId FOR UPDATE;
    UPDATE account
    SET reserved = amountReversed, balance = amountBalance
    WHERE id = accountId;
    IF (ROW_COUNT() > 0) THEN
       COMMIT;
       SELECT true INTO result;
    ELSE
       ROLLBACK;
       SELECT false INTO result;
    END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `balance` double NOT NULL,
  `reserved` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `email`, `currency`, `balance`, `reserved`) VALUES
(1, 'dotanthanhvlog@gmail.com', 'USD', 60, 0),
(2, 'test@gmail.com', 'USD', 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
