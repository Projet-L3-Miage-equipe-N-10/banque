/*
SQLyog Community v9.30 
MySQL - 5.6.25-log : Database - banking-system
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`banking-system` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `banking-system`;

/*Table structure for table `Account` */

DROP TABLE IF EXISTS `Account`;

CREATE TABLE `Account` (
  `ID` bigint(20) NOT NULL,
  `Acc_no` bigint(20) DEFAULT NULL,
  `OpenDate` date DEFAULT NULL,
  `Balance` decimal(10,0) DEFAULT NULL,
  `overDraftLimit` decimal(10,0) DEFAULT NULL,
  `AccType` varchar(225) DEFAULT NULL,
  `IntrestRate` decimal(10,0) DEFAULT NULL,
  `created_by` varchar(225) DEFAULT NULL,
  `modified_by` varchar(225) DEFAULT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_datetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `Account` */

insert  into `Account`(`ID`,`Acc_no`,`OpenDate`,`Balance`,`overDraftLimit`,`AccType`,`IntrestRate`,`created_by`,`modified_by`,`created_datetime`,`modified_datetime`) values (1,9091500001,'2021-02-17','2800','200','Saving Account','2','Admin123','Admin123','2021-02-17 10:30:25','2021-02-17 10:30:25');

/*Table structure for table `Beneficiary` */

DROP TABLE IF EXISTS `Beneficiary`;

CREATE TABLE `Beneficiary` (
  `ID` bigint(20) NOT NULL,
  `Acc_no` bigint(20) DEFAULT NULL,
  `Name` varchar(225) DEFAULT NULL,
  `BankName` varchar(225) DEFAULT NULL,
  `IBAN` varchar(225) DEFAULT NULL,
  `UserId` bigint(20) DEFAULT NULL,
  `created_by` varchar(225) DEFAULT NULL,
  `modified_by` varchar(225) DEFAULT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_datetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `Beneficiary` */

insert  into `Beneficiary`(`ID`,`Acc_no`,`Name`,`BankName`,`IBAN`,`UserId`,`created_by`,`modified_by`,`created_datetime`,`modified_datetime`) values (1,123456789,'Hari','Bank Of India','BKID5555',4,'Demo123','Demo123','2021-02-17 10:29:32','2021-02-17 10:29:32');

/*Table structure for table `Customer` */

DROP TABLE IF EXISTS `Customer`;

CREATE TABLE `Customer` (
  `ID` bigint(20) NOT NULL,
  `acc_No` bigint(20) DEFAULT NULL,
  `name` varchar(225) DEFAULT NULL,
  `emailId` varchar(225) DEFAULT NULL,
  `mobileNo` varchar(225) DEFAULT NULL,
  `address` varchar(225) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `Created_by` varchar(225) DEFAULT NULL,
  `modified_by` varchar(225) DEFAULT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_datetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `Customer` */

insert  into `Customer`(`ID`,`acc_No`,`name`,`emailId`,`mobileNo`,`address`,`userId`,`Created_by`,`modified_by`,`created_datetime`,`modified_datetime`) values (1,9091500001,'Demo Spain','Demo@gmail.com','9685456585','sdvsfvsfbvsdfv',4,'Demo123','Demo123','2021-02-17 10:28:51','2021-02-17 10:28:51');

/*Table structure for table `Transaction` */

DROP TABLE IF EXISTS `Transaction`;

CREATE TABLE `Transaction` (
  `ID` bigint(20) NOT NULL,
  `TransactionId` bigint(20) DEFAULT NULL,
  `TransactionDate` date DEFAULT NULL,
  `transactionType` varchar(225) DEFAULT NULL,
  `transactionAmount` decimal(10,0) DEFAULT NULL,
  `description` varchar(225) DEFAULT NULL,
  `toAccountNo` bigint(20) DEFAULT NULL,
  `fromAccountNo` bigint(20) DEFAULT NULL,
  `created_by` varchar(225) DEFAULT NULL,
  `modified_by` varchar(225) DEFAULT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modefied_datetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `Transaction` */

insert  into `Transaction`(`ID`,`TransactionId`,`TransactionDate`,`transactionType`,`transactionAmount`,`description`,`toAccountNo`,`fromAccountNo`,`created_by`,`modified_by`,`created_datetime`,`modefied_datetime`) values (1,80245234,'2021-02-17','Debit','200','adcfadfsf,sdfvsdgsrg',9091500001,9091500001,'Admin123','Admin123','2021-02-17 10:31:02','2021-02-17 10:31:02'),(2,28843630,'2021-02-17','Creadit','400','dfwrfwrfwfwef',123456789,9091500001,'Demo123','Demo123','2021-02-17 10:31:48','2021-02-17 10:31:48');

/*Table structure for table `User` */

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `ID` bigint(20) NOT NULL,
  `Login` varchar(225) DEFAULT NULL,
  `Password` varchar(225) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  `created_By` varchar(225) DEFAULT NULL,
  `modified_by` varchar(225) DEFAULT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_datetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `User` */

insert  into `User`(`ID`,`Login`,`Password`,`roleId`,`created_By`,`modified_by`,`created_datetime`,`modified_datetime`) values (2,'Hari123','Hari@123',2,'root','root','2021-02-15 13:48:05','2021-02-15 13:48:05'),(3,'Admin123','Admin@321',1,'root','root','2021-02-15 15:55:32','2021-02-15 15:55:32'),(4,'Demo123','Demo@123',2,'root','root','2021-02-17 10:27:52','2021-02-17 10:27:52');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
