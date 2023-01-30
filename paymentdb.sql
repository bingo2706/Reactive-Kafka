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
-- Database: `paymentdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `amount` double NOT NULL,
  `status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`id`, `account_id`, `amount`, `status`) VALUES
(93, 2, 50, 'REJECTED'),
(94, 2, 160, 'REJECTED'),
(95, 2, 100, 'SUCCESSFUL'),
(96, 2, 110, 'REJECTED'),
(97, 2, 50, 'SUCCESSFUL'),
(98, 2, 60, 'REJECTED'),
(99, 2, 20, 'REJECTED'),
(100, 2, 10, 'REJECTED'),
(101, 2, 20, 'SUCCESSFUL'),
(102, 2, 10, 'SUCCESSFUL'),
(103, 2, 50, 'REJECTED'),
(104, 2, 160, 'REJECTED'),
(105, 2, 50, 'SUCCESSFUL'),
(106, 2, 20, 'SUCCESSFUL'),
(107, 2, 20, 'SUCCESSFUL'),
(108, 2, 10, 'REJECTED'),
(109, 2, 20, 'SUCCESSFUL'),
(110, 2, 10, 'SUCCESSFUL'),
(111, 2, 100, 'SUCCESSFUL'),
(112, 2, 10, 'REJECTED'),
(113, 1, 30, 'REJECTED'),
(114, 1, 40, 'SUCCESSFUL');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
