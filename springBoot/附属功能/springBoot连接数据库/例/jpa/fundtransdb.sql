-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2018 年 05 月 13 日 11:00
-- 服务器版本: 5.6.12-log
-- PHP 版本: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `fundtransdb`
--
CREATE DATABASE IF NOT EXISTS `fundtransdb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `fundtransdb`;

-- --------------------------------------------------------

--
-- 表的结构 `client`
--

CREATE TABLE IF NOT EXISTS `client` (
  `idcard` char(18) NOT NULL DEFAULT '',
  `name` varchar(30) NOT NULL DEFAULT '',
  `sex` char(1) DEFAULT NULL COMMENT 'M OR F',
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `hobby` varchar(50) DEFAULT NULL,
  `createTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`idcard`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `client`
--

INSERT INTO `client` (`idcard`, `name`, `sex`, `phone`, `address`, `email`, `hobby`, `createTime`) VALUES
('111111111111111111', '张三', 'M', '12345678', '福州', 'abc@126.com', '兵乓球', '2012-12-12 00:00:00'),
('222222222222222222', '李四', 'M', '22222222', '厦门', '123@126.com', '篮球', '2015-01-01 00:00:00'),
('555555555555555555', '1', 'M', '1', '1', '1', '1', '2015-12-31 00:00:00'),
('6666666666666666', '6', 'M', '6', '6', '6', '6', '2015-12-31 00:00:00');

-- --------------------------------------------------------

--
-- 表的结构 `financial_account`
--

CREATE TABLE IF NOT EXISTS `financial_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` char(6) NOT NULL DEFAULT '',
  `amount` decimal(12,2) DEFAULT NULL,
  `status` char(3) NOT NULL DEFAULT '' COMMENT 'A OR F',
  `idcard` char(18) NOT NULL DEFAULT '',
  `createTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `IDCARD_NO` (`idcard`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- 转存表中的数据 `financial_account`
--

INSERT INTO `financial_account` (`id`, `password`, `amount`, `status`, `idcard`, `createTime`) VALUES
(1, '123456', '1783.39', 'A', '111111111111111111', '2012-12-12 00:00:00'),
(2, '123456', '1996.88', 'A', '222222222222222222', '2012-12-12 00:00:00');

-- --------------------------------------------------------

--
-- 表的结构 `financial_account_transinfo`
--

CREATE TABLE IF NOT EXISTS `financial_account_transinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` char(1) NOT NULL DEFAULT '' COMMENT 'D,W,O,F,A,B OR G',
  `amount` decimal(12,2) NOT NULL DEFAULT '0.00',
  `accId` int(11) NOT NULL DEFAULT '0',
  `createTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `ACC_NO` (`accId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=18 ;

--
-- 转存表中的数据 `financial_account_transinfo`
--

INSERT INTO `financial_account_transinfo` (`id`, `type`, `amount`, `accId`, `createTime`) VALUES
(1, 'B', '30.30', 1, '2016-01-07 09:50:01'),
(2, 'B', '159.98', 1, '2016-01-07 10:47:35'),
(8, 'B', '3.24', 1, '2016-05-02 10:29:46'),
(9, 'B', '3.18', 1, '2016-05-02 10:31:22'),
(12, 'B', '12.73', 1, '2016-05-02 10:39:56'),
(13, 'B', '3.21', 1, '2016-05-02 10:46:04'),
(14, 'B', '6.43', 1, '2016-05-02 10:46:57'),
(15, 'B', '3.30', 1, '2016-05-02 15:17:57'),
(16, 'B', '3.15', 1, '2016-05-02 15:36:45'),
(17, 'B', '2.73', 1, '2016-05-20 20:30:17');

-- --------------------------------------------------------

--
-- 表的结构 `fund`
--

CREATE TABLE IF NOT EXISTS `fund` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `price` decimal(6,2) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `status` char(3) NOT NULL DEFAULT '' COMMENT 'Y-可交易, N-不可交易',
  `createTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Fund_Name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=26 ;

--
-- 转存表中的数据 `fund`
--

INSERT INTO `fund` (`id`, `name`, `price`, `description`, `status`, `createTime`) VALUES
(1, '新华基金', '3.00', '新华中小市值优选混合', 'Y', '2012-12-03 00:00:00'),
(2, '工行基金', '7.00', '中国工行基金', 'Y', '2000-12-30 00:00:00'),
(4, '长盛基金', '3.00', '长盛电子信息主题', 'Y', '1999-12-30 00:00:00'),
(5, '富国基金', '2.00', '富国低碳环保混合', 'Y', '2012-12-30 00:00:00'),
(6, '浦银安盛基金', '3.00', '浦银安盛基金', 'Y', '2013-12-30 00:00:00'),
(7, '华安基金', '2.00', ' 华安逆向策略混合', 'Y', '2012-12-30 00:00:00'),
(8, '益民基金', '4.00', '益民服务领先', 'Y', '2014-12-30 00:00:00'),
(9, '华泰柏瑞基金', '2.00', '华泰柏瑞价值', 'Y', '2013-12-30 00:00:00'),
(10, '中邮基金', '3.00', ' -中邮战略新兴产业', 'Y', '2015-12-30 00:00:00'),
(11, '兴业全球基金', '4.50', ' 兴全合润分级', 'Y', '2012-12-30 00:00:00'),
(12, '华泰柏瑞创新基金', '3.10', '华泰柏瑞创新动力混合', 'N', '1998-11-23 00:00:00'),
(13, '长城基金', '1.00', '长城双动力混合估值图基金吧', 'Y', '1997-12-30 00:00:00'),
(14, '宝盈基金', '2.00', '宝盈泛沿', 'Y', '2012-11-11 00:00:00'),
(15, '大成基金', '5.00', '大成基金', 'Y', '2013-12-12 00:00:00'),
(16, '嘉实基金', '5.00', '嘉实基金', 'Y', '2014-01-01 00:00:00'),
(17, '易方达基金', '5.00', '易方达基金', 'Y', '2013-05-05 00:00:00'),
(18, '易方达安心债券', '3.00', '易方达安心债券', 'Y', '2015-12-29 00:00:00'),
(19, '南方基金', '1.68', '南方盛元红利混合', 'Y', '2015-12-29 00:00:00'),
(20, '万家基金', '2.53', '万家行业优选混合', 'Y', '2015-12-29 00:00:00'),
(21, '汇添富基金', '2.55', '汇添富消费行业混合', 'Y', '2015-12-29 00:00:00'),
(22, '招行基金', '3.00', '招行基金', 'Y', '2015-12-30 00:00:00'),
(25, '国泰中国企业境外高收益债券', '6.00', '国泰中国企业境外高收益债券', 'Y', '2016-01-05 00:00:00');

-- --------------------------------------------------------

--
-- 表的结构 `fund_holding`
--

CREATE TABLE IF NOT EXISTS `fund_holding` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accId` int(11) NOT NULL DEFAULT '0',
  `fundId` int(11) NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `ACC_NO2` (`accId`),
  KEY `FUND_NO1` (`fundId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=16 ;

--
-- 转存表中的数据 `fund_holding`
--

INSERT INTO `fund_holding` (`id`, `accId`, `fundId`, `amount`) VALUES
(2, 1, 4, 31),
(3, 2, 1, 100),
(6, 2, 2, 10),
(7, 1, 7, 7),
(8, 1, 8, 4),
(9, 1, 10, 10),
(10, 1, 12, 1),
(11, 1, 15, 1),
(12, 2, 10, 1),
(13, 1, 2, 5),
(15, 1, 1, 2);

-- --------------------------------------------------------

--
-- 表的结构 `fund_transinfo`
--

CREATE TABLE IF NOT EXISTS `fund_transinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` char(1) NOT NULL DEFAULT '',
  `accId` int(11) NOT NULL DEFAULT '0',
  `fundId` int(11) NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL DEFAULT '0',
  `price` double(6,2) NOT NULL DEFAULT '0.00',
  `createTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `FUND_NO` (`fundId`),
  KEY `ACC_NO1` (`accId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=28 ;

--
-- 转存表中的数据 `fund_transinfo`
--

INSERT INTO `fund_transinfo` (`id`, `type`, `accId`, `fundId`, `amount`, `price`, `createTime`) VALUES
(1, 'B', 1, 1, 1, 5.00, '2016-01-04 14:42:16'),
(2, 'B', 1, 1, 1, 6.00, '2016-01-04 14:43:32'),
(3, 'B', 1, 1, 1, 5.88, '2016-01-04 14:45:33'),
(4, 'B', 1, 1, 1, 5.10, '2016-01-04 14:45:44'),
(5, 'B', 1, 1, 1, 5.54, '2016-01-04 15:14:31'),
(6, 'B', 1, 1, 1, 5.66, '2016-01-04 15:17:12'),
(7, 'B', 1, 1, 1, 5.66, '2016-01-04 19:08:17'),
(8, 'B', 1, 1, 1, 5.94, '2016-01-04 19:11:56'),
(9, 'B', 1, 1, 1, 5.77, '2016-01-04 19:13:06'),
(10, 'B', 1, 1, 1, 5.26, '2016-01-04 19:13:48'),
(11, 'B', 1, 1, 99, 5.15, '2016-01-04 19:14:54'),
(12, 'B', 1, 4, 1, 3.09, '2016-01-05 14:44:47'),
(13, 'B', 1, 7, 7, 2.10, '2016-01-05 14:45:09'),
(14, 'B', 1, 8, 4, 4.32, '2016-01-05 14:45:57'),
(15, 'B', 1, 10, 10, 2.76, '2016-01-05 14:46:27'),
(16, 'B', 1, 12, 1, 3.24, '2016-01-05 14:46:39'),
(17, 'B', 1, 15, 1, 4.95, '2016-01-05 14:46:54'),
(18, 'B', 2, 10, 1, 3.09, '2016-01-05 14:47:52'),
(19, 'B', 1, 2, 2, 6.37, '2016-01-06 15:14:58'),
(20, 'B', 1, 1, 10, 3.00, '2016-01-07 09:50:01'),
(21, 'B', 1, 1, 30, 5.28, '2016-01-07 10:47:35'),
(22, 'B', 1, 2, 2, 6.30, '2016-05-02 10:39:56'),
(23, 'B', 1, 1, 1, 3.18, '2016-05-02 10:46:04'),
(24, 'B', 1, 2, 1, 6.37, '2016-05-02 10:46:57'),
(25, 'B', 1, 1, 1, 3.27, '2016-05-02 15:17:57'),
(26, 'B', 1, 1, 1, 3.12, '2016-05-02 15:36:45'),
(27, 'B', 1, 1, 1, 2.70, '2016-05-20 20:30:20');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  `password` varchar(10) NOT NULL DEFAULT '',
  `createTime` date NOT NULL DEFAULT '0000-00-00',
  `realname` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `name`, `password`, `createTime`, `realname`) VALUES
(1, 'admin', 'admin', '2015-12-01', NULL),
(2, 'haha1', 'haha', '2018-04-23', NULL);

--
-- 限制导出的表
--

--
-- 限制表 `financial_account`
--
ALTER TABLE `financial_account`
  ADD CONSTRAINT `financial_account_ibfk_1` FOREIGN KEY (`idcard`) REFERENCES `client` (`idcard`);

--
-- 限制表 `financial_account_transinfo`
--
ALTER TABLE `financial_account_transinfo`
  ADD CONSTRAINT `financial_account_transinfo_ibfk_1` FOREIGN KEY (`accId`) REFERENCES `financial_account` (`id`);

--
-- 限制表 `fund_holding`
--
ALTER TABLE `fund_holding`
  ADD CONSTRAINT `fund_holding_ibfk_1` FOREIGN KEY (`accId`) REFERENCES `financial_account` (`id`),
  ADD CONSTRAINT `fund_holding_ibfk_2` FOREIGN KEY (`fundId`) REFERENCES `fund` (`id`);

--
-- 限制表 `fund_transinfo`
--
ALTER TABLE `fund_transinfo`
  ADD CONSTRAINT `fund_transinfo_ibfk_1` FOREIGN KEY (`accId`) REFERENCES `financial_account` (`id`),
  ADD CONSTRAINT `fund_transinfo_ibfk_2` FOREIGN KEY (`fundId`) REFERENCES `fund` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
