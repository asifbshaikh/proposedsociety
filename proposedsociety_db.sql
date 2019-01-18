-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: proposedsociety
-- ------------------------------------------------------
-- Server version	5.7.24-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `line1` varchar(100) NOT NULL,
  `line2` varchar(100) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `area` varchar(100) NOT NULL,
  `taluka` varchar(100) DEFAULT NULL,
  `city` varchar(100) NOT NULL,
  `district` varchar(100) DEFAULT NULL,
  `state` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `pin` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_address__state` (`state`),
  KEY `ix_address__city` (`city`),
  KEY `ix_address__area` (`area`),
  KEY `ix_address__pin` (`pin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `form_id` bigint(20) DEFAULT NULL,
  `total_amount_paid` bigint(20) DEFAULT NULL,
  `form_credits` bigint(20) DEFAULT NULL,
  `total_form_filled_count` bigint(20) DEFAULT NULL,
  `ib_id` bigint(20) DEFAULT NULL,
  `agent_code` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_agent_code` (`agent_code`),
  KEY `fk_agent__user_id` (`user_id`),
  KEY `fk_agent_form_id` (`form_id`),
  KEY `fk_agent_update_ib_id` (`ib_id`),
  CONSTRAINT `fk_agent__user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_agent_form_id` FOREIGN KEY (`form_id`) REFERENCES `agent_form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_agent_update_ib_id` FOREIGN KEY (`ib_id`) REFERENCES `buero` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent`
--

LOCK TABLES `agent` WRITE;
/*!40000 ALTER TABLE `agent` DISABLE KEYS */;
INSERT INTO `agent` VALUES (1,14,1,0,0,0,1,'AG1712061');
/*!40000 ALTER TABLE `agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent_address`
--

DROP TABLE IF EXISTS `agent_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `line1` varchar(100) NOT NULL,
  `line2` varchar(100) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `area` varchar(100) NOT NULL,
  `taluka` varchar(100) DEFAULT NULL,
  `city` varchar(100) NOT NULL,
  `district` varchar(100) DEFAULT NULL,
  `state` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `pin` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_address`
--

LOCK TABLES `agent_address` WRITE;
/*!40000 ALTER TABLE `agent_address` DISABLE KEYS */;
INSERT INTO `agent_address` VALUES (1,'Dehugaon','','','Dehugaon','','Pune','','Maharashtra','India','412109'),(2,'Dehugaon','','','Dehugaon','','Pune','','Maharashtra','India','412109'),(3,'Dehugaon','','','Dehugaon','','Pune','','Maharashtra','India','412109'),(4,'Dehugaon','','','Dehugaon','','Pune','','Maharashtra','India','412109'),(5,'Dehugaon','','','Dehugaon','','Pune','','Maharashtra','India','412109');
/*!40000 ALTER TABLE `agent_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent_commission`
--

DROP TABLE IF EXISTS `agent_commission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent_commission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commission_amount` decimal(15,2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `commission_date` date DEFAULT NULL,
  `commission_against_id` bigint(20) DEFAULT NULL,
  `agent_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_agent_commission_agent_id` (`agent_id`),
  CONSTRAINT `fk_agent_commission_agent_id` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_commission`
--

LOCK TABLES `agent_commission` WRITE;
/*!40000 ALTER TABLE `agent_commission` DISABLE KEYS */;
/*!40000 ALTER TABLE `agent_commission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent_feedback_form_file`
--

DROP TABLE IF EXISTS `agent_feedback_form_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent_feedback_form_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `upload_time` datetime NOT NULL,
  `feedback_form_file_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_agent_feedback_form_file__feedback_form_file_id` (`feedback_form_file_id`),
  CONSTRAINT `fk_agent_feedback_form_file__feedback_form_file_id` FOREIGN KEY (`feedback_form_file_id`) REFERENCES `attachment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_feedback_form_file`
--

LOCK TABLES `agent_feedback_form_file` WRITE;
/*!40000 ALTER TABLE `agent_feedback_form_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `agent_feedback_form_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent_form`
--

DROP TABLE IF EXISTS `agent_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent_form` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(4) NOT NULL,
  `fname` varchar(50) NOT NULL,
  `mname` varchar(50) NOT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `dob` datetime NOT NULL,
  `email` varchar(100) NOT NULL,
  `sex` char(1) NOT NULL,
  `fh_title` varchar(4) NOT NULL,
  `fh_fname` varchar(50) NOT NULL,
  `fh_mname` varchar(50) NOT NULL,
  `fh_lname` varchar(50) DEFAULT NULL,
  `fh_relation` varchar(50) NOT NULL,
  `identity_number1` varchar(50) DEFAULT NULL,
  `identity_number1_type` varchar(20) DEFAULT NULL,
  `identity_number2` varchar(50) DEFAULT NULL,
  `identity_number2_type` varchar(20) DEFAULT NULL,
  `identity_number3` varchar(50) DEFAULT NULL,
  `identity_number3_type` varchar(20) DEFAULT NULL,
  `phone1_type` varchar(20) NOT NULL,
  `phone1` varchar(20) NOT NULL,
  `phone2_type` varchar(20) DEFAULT NULL,
  `phone2` varchar(20) DEFAULT NULL,
  `phone3_type` varchar(20) DEFAULT NULL,
  `phone3` varchar(20) DEFAULT NULL,
  `marital_status` char(1) NOT NULL,
  `nationality` varchar(50) NOT NULL,
  `permanent_address_same_as` varchar(20) NOT NULL,
  `residential_address_same_as` varchar(20) NOT NULL,
  `communication_address_same_as` varchar(20) NOT NULL,
  `birth_place_id` bigint(20) NOT NULL,
  `permanent_address_id` bigint(20) DEFAULT NULL,
  `residential_address_id` bigint(20) DEFAULT NULL,
  `communication_address_id` bigint(20) DEFAULT NULL,
  `resi_status` varchar(50) NOT NULL,
  `residence_yrs_residing` int(11) NOT NULL,
  `city_yrs_residing` int(11) NOT NULL,
  `office_address_id` bigint(20) DEFAULT NULL,
  `off_mail` varchar(100) DEFAULT NULL,
  `website_addres` varchar(100) DEFAULT NULL,
  `off_fax` varchar(100) DEFAULT NULL,
  `off_phone1` varchar(20) DEFAULT NULL,
  `off_phone1_type` varchar(20) DEFAULT NULL,
  `off_phone2` varchar(20) DEFAULT NULL,
  `off_phone2_type` varchar(20) DEFAULT NULL,
  `off_phone3` varchar(20) DEFAULT NULL,
  `off_phone3_type` varchar(20) DEFAULT NULL,
  `hindi_id` bigint(20) NOT NULL,
  `marathi_id` bigint(20) NOT NULL,
  `english_id` bigint(20) NOT NULL,
  `bank_name` varchar(300) NOT NULL,
  `branch_name` varchar(300) NOT NULL,
  `bank_account` bigint(20) NOT NULL,
  `agent_type` varchar(100) NOT NULL,
  `company_name` varchar(200) NOT NULL,
  `occupation` varchar(200) NOT NULL,
  `qualification` varchar(200) NOT NULL,
  `other_qualification` varchar(100) DEFAULT NULL,
  `is_income_tax_payee` varchar(6) NOT NULL,
  `pan_card_number` varchar(20) NOT NULL,
  `online_monatory_transaction_facility` varchar(6) NOT NULL,
  `form_filled_place` varchar(200) DEFAULT NULL,
  `form_filled_day` varchar(200) DEFAULT NULL,
  `photograph_id` bigint(20) NOT NULL,
  `identity2_id` bigint(20) DEFAULT NULL,
  `identity1_id` bigint(20) NOT NULL,
  `identity3_id` bigint(20) DEFAULT NULL,
  `addressproof_id` bigint(20) NOT NULL,
  `birth_certificate_id` bigint(20) NOT NULL,
  `office_addressproof_id` bigint(20) DEFAULT NULL,
  `verification_code` varchar(100) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `nri_address_id` bigint(20) DEFAULT NULL,
  `office_address_same_as` varchar(20) NOT NULL,
  `resi_light_bill_id` bigint(20) DEFAULT NULL,
  `resi_tax_receipt_id` bigint(20) DEFAULT NULL,
  `office_light_bill_id` bigint(20) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `form_filled_year` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_agent_hindi_language_proficiency_id` (`hindi_id`),
  KEY `fk_agent_marathi_language_proficiency_id` (`marathi_id`),
  KEY `fk_agent_english_language_proficiency_id` (`english_id`),
  KEY `fk_agent_photograph_id` (`photograph_id`),
  KEY `fk_agent_identity1_id` (`identity1_id`),
  KEY `fk_agent_identity2_id` (`identity2_id`),
  KEY `fk_agent_identity3_id` (`identity3_id`),
  KEY `fk_agent_addressproof_id` (`addressproof_id`),
  KEY `fk_agent_birth_certificate_id` (`birth_certificate_id`),
  KEY `fk_agent_office_addressproof_id` (`office_addressproof_id`),
  KEY `fk_agent_user_id` (`user_id`),
  KEY `fk_agent_nri_address_id` (`nri_address_id`),
  KEY `fk_agent_resi_light_bill_id` (`resi_light_bill_id`),
  KEY `fk_agent_resi_tax_receipt_id` (`resi_tax_receipt_id`),
  KEY `fk_agent_office_light_bill_id` (`office_light_bill_id`),
  KEY `fk_agent__birth_place_id` (`birth_place_id`),
  KEY `fk_agent__residential_address_id` (`residential_address_id`),
  KEY `fk_agent__permanent_address_id` (`permanent_address_id`),
  KEY `fk_agent__communication_address_id` (`communication_address_id`),
  KEY `fk_agent__office_address_id` (`office_address_id`),
  CONSTRAINT `fk_agent__birth_place_id` FOREIGN KEY (`birth_place_id`) REFERENCES `agent_address` (`id`),
  CONSTRAINT `fk_agent__communication_address_id` FOREIGN KEY (`communication_address_id`) REFERENCES `agent_address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_agent__office_address_id` FOREIGN KEY (`office_address_id`) REFERENCES `agent_address` (`id`),
  CONSTRAINT `fk_agent__permanent_address_id` FOREIGN KEY (`permanent_address_id`) REFERENCES `agent_address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_agent__residential_address_id` FOREIGN KEY (`residential_address_id`) REFERENCES `agent_address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_agent_addressproof_id` FOREIGN KEY (`addressproof_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_birth_certificate_id` FOREIGN KEY (`birth_certificate_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_english_language_proficiency_id` FOREIGN KEY (`english_id`) REFERENCES `language_proficiency` (`id`),
  CONSTRAINT `fk_agent_hindi_language_proficiency_id` FOREIGN KEY (`hindi_id`) REFERENCES `language_proficiency` (`id`),
  CONSTRAINT `fk_agent_identity1_id` FOREIGN KEY (`identity1_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_identity2_id` FOREIGN KEY (`identity2_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_identity3_id` FOREIGN KEY (`identity3_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_marathi_language_proficiency_id` FOREIGN KEY (`marathi_id`) REFERENCES `language_proficiency` (`id`),
  CONSTRAINT `fk_agent_nri_address_id` FOREIGN KEY (`nri_address_id`) REFERENCES `address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_agent_office_addressproof_id` FOREIGN KEY (`office_addressproof_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_office_light_bill_id` FOREIGN KEY (`office_light_bill_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_photograph_id` FOREIGN KEY (`photograph_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_resi_light_bill_id` FOREIGN KEY (`resi_light_bill_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_resi_tax_receipt_id` FOREIGN KEY (`resi_tax_receipt_id`) REFERENCES `attachment` (`id`),
  CONSTRAINT `fk_agent_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_form`
--

LOCK TABLES `agent_form` WRITE;
/*!40000 ALTER TABLE `agent_form` DISABLE KEYS */;
INSERT INTO `agent_form` VALUES (1,'Mr','NIKHIL','ANANDA','RAIKAR','1991-12-05 00:00:00','nitin.pandhare@techbulls.com','M','Mr','ANANDA','SHIVAJI','RAIKAR','F','ASDF1257','PanCard','','Passport','','Passport','M','9021556333','M','','M','','U','I','birth_place','birth_place','birth_place',1,2,3,4,'ownership',15,25,5,'','','','','M','','M','','M',2,1,3,'ICICI','Baner',4587896587,'Individual','Techbulls','','post graduate','','true','ASDF1257','true','Baner','6 dec',2,NULL,1,NULL,4,3,7,NULL,14,NULL,'residential_address',5,6,8,'Approved',2017);
/*!40000 ALTER TABLE `agent_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicant`
--

DROP TABLE IF EXISTS `applicant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `applicant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(4) NOT NULL,
  `fname` varchar(50) NOT NULL,
  `mname` varchar(50) NOT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `dob` datetime NOT NULL,
  `email` varchar(100) NOT NULL,
  `sex` char(1) DEFAULT NULL,
  `identity_number1` varchar(50) DEFAULT NULL,
  `identity_number1_type` varchar(20) DEFAULT NULL,
  `identity_number2` varchar(50) DEFAULT NULL,
  `identity_number2_type` varchar(20) DEFAULT NULL,
  `identity_number3` varchar(50) DEFAULT NULL,
  `identity_number3_type` varchar(20) DEFAULT NULL,
  `fh_title` varchar(255) DEFAULT NULL,
  `fh_fname` varchar(255) DEFAULT NULL,
  `fh_mname` varchar(255) DEFAULT NULL,
  `fh_lname` varchar(50) DEFAULT NULL,
  `fh_relation` varchar(255) DEFAULT NULL,
  `marital_status` char(1) DEFAULT NULL,
  `spouse_details_id` bigint(20) DEFAULT NULL,
  `permanent_address_same_as` varchar(20) DEFAULT NULL,
  `residential_address_same_as` varchar(20) DEFAULT NULL,
  `communication_address_same_as` varchar(20) DEFAULT NULL,
  `birth_place_id` bigint(20) DEFAULT NULL,
  `permanent_address_id` bigint(20) DEFAULT NULL,
  `residential_address_id` bigint(20) DEFAULT NULL,
  `communication_address_id` bigint(20) DEFAULT NULL,
  `resi_status` varchar(50) DEFAULT NULL,
  `residence_yrs_residing` int(11) DEFAULT NULL,
  `city_yrs_residing` int(11) DEFAULT NULL,
  `phone1_type` varchar(255) DEFAULT NULL,
  `phone1` varchar(30) NOT NULL,
  `phone2_type` varchar(20) DEFAULT NULL,
  `phone2` varchar(30) DEFAULT NULL,
  `phone3_type` varchar(20) DEFAULT NULL,
  `phone3` varchar(30) DEFAULT NULL,
  `office_address_id` bigint(20) DEFAULT NULL,
  `off_mail` varchar(100) DEFAULT NULL,
  `employer_web` varchar(100) DEFAULT NULL,
  `off_phone1` varchar(30) DEFAULT NULL,
  `off_phone1_type` varchar(30) DEFAULT NULL,
  `off_phone2` varchar(30) DEFAULT NULL,
  `off_phone2_type` varchar(20) DEFAULT NULL,
  `off_phone3` varchar(30) DEFAULT NULL,
  `off_phone3_type` varchar(20) DEFAULT NULL,
  `approx_eligibility` bigint(20) DEFAULT NULL,
  `occupation` varchar(50) DEFAULT NULL,
  `employed_income_id` bigint(20) DEFAULT NULL,
  `self_employed_income_id` bigint(20) DEFAULT NULL,
  `loan` tinyint(1) DEFAULT '0',
  `relationship_with_applicant` varchar(100) DEFAULT NULL,
  `photograph_id` bigint(20) DEFAULT NULL,
  `identity1_id` bigint(20) DEFAULT NULL,
  `identity2_id` bigint(20) DEFAULT NULL,
  `identity3_id` bigint(20) DEFAULT NULL,
  `addressproof_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_applicant__spouse_details_id` (`spouse_details_id`),
  KEY `fk_applicant__residential_address_id` (`residential_address_id`),
  KEY `fk_applicant__permanent_address_id` (`permanent_address_id`),
  KEY `fk_applicant__communication_address_id` (`communication_address_id`),
  KEY `fk_applicant__employed_income_id` (`employed_income_id`),
  KEY `fk_applicant__self_employed_income_id` (`self_employed_income_id`),
  KEY `fk_applicant__office_address_id` (`office_address_id`),
  KEY `fk_applicant__birth_place_id` (`birth_place_id`),
  KEY `fk_applicant__photograph_id` (`photograph_id`),
  KEY `fk_applicant__identity1_id` (`identity1_id`),
  KEY `fk_applicant__identity2_id` (`identity2_id`),
  KEY `fk_applicant__identity3_id` (`identity3_id`),
  KEY `fk_applicant__addressproof_id` (`addressproof_id`),
  CONSTRAINT `fk_applicant__addressproof_id` FOREIGN KEY (`addressproof_id`) REFERENCES `attachment` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_applicant__birth_place_id` FOREIGN KEY (`birth_place_id`) REFERENCES `address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_applicant__communication_address_id` FOREIGN KEY (`communication_address_id`) REFERENCES `address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_applicant__employed_income_id` FOREIGN KEY (`employed_income_id`) REFERENCES `employed_income` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_applicant__identity1_id` FOREIGN KEY (`identity1_id`) REFERENCES `attachment` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_applicant__identity2_id` FOREIGN KEY (`identity2_id`) REFERENCES `attachment` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_applicant__identity3_id` FOREIGN KEY (`identity3_id`) REFERENCES `attachment` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_applicant__office_address_id` FOREIGN KEY (`office_address_id`) REFERENCES `address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_applicant__permanent_address_id` FOREIGN KEY (`permanent_address_id`) REFERENCES `address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_applicant__photograph_id` FOREIGN KEY (`photograph_id`) REFERENCES `attachment` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_applicant__residential_address_id` FOREIGN KEY (`residential_address_id`) REFERENCES `address` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_applicant__self_employed_income_id` FOREIGN KEY (`self_employed_income_id`) REFERENCES `self_employed_income` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_applicant__spouse_details_id` FOREIGN KEY (`spouse_details_id`) REFERENCES `spouse_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicant`
--

LOCK TABLES `applicant` WRITE;
/*!40000 ALTER TABLE `applicant` DISABLE KEYS */;
INSERT INTO `applicant` VALUES (1,'Mr','AMOL','AMOL','NIRMAL','1991-12-05 00:00:00','amol.nirmal@techbulls.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9021556333',NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(2,'Mr','Nitin','Balwant','Pandhare','1991-12-05 05:30:00','nitin.pandhare@techbulls.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9021556333',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(3,'Mr','Nitin','Balwant','Pandhare','1991-12-05 05:30:00','nitin.pandhare@techbulls.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9021556333',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(4,'Mr','Nitin','Balwant','Pandhare','1991-12-05 05:30:00','nitin.pandhare@techbulls.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9021556333',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(5,'Mr','Nitin','Balwant','Pandhare','1991-12-05 05:30:00','nitin.pandhare@techbulls.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9021556333',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(6,'Mr','Nitin','Balwant','Pandhare','1991-12-05 05:30:00','nitin.pandhare@techbulls.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9021556333',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(7,'Mr','test','test','test','2018-01-31 05:30:00','test@example.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9898989898',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(8,'Mr','test','test','test','2018-01-31 05:30:00','test23@example.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9898989898',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(9,'Mr','test','test','test','2018-01-31 05:30:00','test23@example.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9898989898',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(10,'Mr','test','test','test','2018-10-12 05:30:00','asifbshaikhit@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9979797994',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(11,'Mr','test','test','test','2018-10-12 05:30:00','asifbshaikhit@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9979797994',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(12,'Mr','test3','stringify','stringify','2018-10-05 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8528528553',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(13,'Mr','TEST','TEST','TEST','2018-07-12 00:00:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8668217115',NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(14,'Mr','TEST','TEST','TEST','2018-10-23 00:00:00','test@exapmle.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9230032939',NULL,'9232030239',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(15,'Mr','sdadfasd','asdfasd','asdfdsa','2018-11-17 05:30:00','asdfdsa@daadsdf.asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4353454352',NULL,'4354354352',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(16,'Mr','sdadfasd','asdfasd','asdfdsa','2018-11-17 05:30:00','asdfdsa@daadsdf.asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4353454352',NULL,'4354354352',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(17,'Mr','sdadfasd','asdfasd','asdfdsa','2018-11-17 05:30:00','asdfdsa@daadsdf.asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4353454352',NULL,'4354354352',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(18,'Mr','sdadfasd','asdfasd','asdfdsa','2018-11-17 05:30:00','asdfdsa@daadsdf.asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4353454352',NULL,'4354354352',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(19,'Mr','sdadfasd','asdfasd','asdfdsa','2018-11-17 05:30:00','asdfdsa@daadsdf.asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4353454352',NULL,'4354354352',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(20,'Mr','test','test','ewrw','2018-11-14 05:30:00','asd@asdf.as',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342343242',NULL,'2342343240',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(21,'Mr','test','test','ewrw','2018-11-14 05:30:00','asd@asdf.as',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342343242',NULL,'2342343240',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(22,'Mr','test','test','test','2018-11-07 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8529636665',NULL,'8888888886',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(23,'Mr','SAD','ASD','ASD','2010-02-02 00:00:00','asd@asd.d',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'324324234234',NULL,'234234234324',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(24,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','asd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312312',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(25,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','asd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312312',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(26,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','asd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312312',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(27,'Mr','ASD','ASD','ASD','2011-03-01 00:00:00','asd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312312',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(28,'Mr','ASD','ASD','ASD','2010-02-02 00:00:00','asd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312312',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(29,'Mr','test','test','test','2018-11-09 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342341',NULL,'2342343233',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(30,'Mr','ASD','ASD','ASD','2010-02-02 00:00:00','aasd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312317',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(31,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','aasd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2212312312',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(32,'Mr','test','test','test','2018-11-12 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'3232323233',NULL,'3333333332',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(33,'Mr','test','test','test','2018-11-14 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'3242342342',NULL,'2342343244',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(34,'Mr','test','test','test','2018-11-07 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9786421348',NULL,'8956238956',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(35,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','assd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312316',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(36,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','assasd@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312315',NULL,'1231231531',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(37,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','sadg@asd.s',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231234323',NULL,'1231234323',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(38,'Mr','ASD','ASD','ASD','2014-06-03 00:00:00','sadg@asd.s',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231234323',NULL,'1231234323',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(39,'Mr','test','test','test','2018-11-15 05:30:00','adasd@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'3224234234',NULL,'2342342343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(40,'Mr','test','test','test','2018-11-08 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231231233',NULL,'1231231233',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(41,'Mr','test','test','test','2018-11-15 05:30:00','asif.shaikh@techbulls.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231231232',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(42,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2343242334',NULL,'2342342342',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(43,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2343242334',NULL,'2342342342',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(44,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2343242334',NULL,'2342342342',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(45,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231232130',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(46,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231232130',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(47,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231232130',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(48,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231232130',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(49,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231232130',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(50,'Mr','test','test','test','2018-11-15 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1231232130',NULL,'1231231231',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(51,'Mr','test','test','test','2018-11-07 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342343',NULL,'2342342343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(52,'Mr','test','test','test','2018-11-05 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342342',NULL,'2342342341',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(53,'Mr','test','test','test','2018-11-16 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342332444',NULL,'2342342344',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(54,'Mr','test','test','test','2018-11-16 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342332444',NULL,'2342342344',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(55,'Mr','test','test','test','2018-11-16 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342332444',NULL,'2342342344',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(56,'Mr','test','test','test','2018-11-16 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342332444',NULL,'2342342344',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(57,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(58,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(59,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(60,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(61,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(62,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(63,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(64,'Mr','ASD','ASD','ASD','2018-06-06 00:00:00','asdik@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212312963',NULL,'1231231852',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(65,'Mr','test','test','test','2018-11-09 05:30:00','dan@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342344',NULL,'2343242344',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(66,'Mr','test','test','test','2018-11-14 05:30:00','dan@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'6987654321',NULL,'9876543203',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(67,'Mr','test','test','test','2018-11-17 05:30:00','asif9234@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'7654343433',NULL,'4343443434',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(68,'Mr','ASD','ASD','ASD','2010-02-01 00:00:00','asdf@asd.asd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'9823982312',NULL,'2983982392',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(69,'Mr','test','jsd','sd','9929-02-09 05:30:00','ksdf@sjdk.sdj',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8923329889',NULL,'9328923899',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(70,'Mr','test','jsd','sd','9929-02-09 05:30:00','ksdf@sjdk.sdj',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8923329889',NULL,'9328923899',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(71,'Mr','test','test','test3','2018-12-05 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342344',NULL,'2343242343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(72,'Mr','test','test','test3','2018-12-05 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342344',NULL,'2343242343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(73,'Mr','test','test','test3','2018-12-05 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342344',NULL,'2343242343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(74,'Mr','test','test','test3','2018-12-05 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342344',NULL,'2343242343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(75,'Mr','test','test','test3','2018-12-05 05:30:00','ad@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342344',NULL,'2343242343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL),(76,'Mr','test','test','test3','2018-12-05 05:30:00','asd@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2342342344',NULL,'2343242343',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `applicant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `content_type` varchar(100) DEFAULT NULL,
  `upload_time` datetime DEFAULT NULL,
  `file_path` varchar(512) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_attachment_file_path` (`file_path`),
  KEY `fk_attachment__user_id` (`user_id`),
  CONSTRAINT `fk_attachment__user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
INSERT INTO `attachment` VALUES (1,'benefits_view.png','image/png','2017-12-06 19:17:47','Ih4Hj_1512568067174',14),(2,'techbulls_logo.jpg','image/jpeg','2017-12-06 19:17:53','gN0hV_1512568072618',14),(3,'cust_detail_ship_to.png','image/png','2017-12-06 19:18:02','jqSet_1512568081633',14),(4,'currency.png','image/png','2017-12-06 19:18:14','tUzvt_1512568093779',14),(5,'currency.png','image/png','2017-12-06 19:18:15','FK97M_1512568094783',14),(6,'Screenshot from 2017-10-18 18-43-47.png','image/png','2017-12-06 19:18:16','ABq1v_1512568096001',14),(7,'example.png','image/png','2017-12-06 19:18:20','B8wq1_1512568099603',14),(8,'currency.png','image/png','2017-12-06 19:18:22','me8s0_1512568102338',14);
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_account`
--

DROP TABLE IF EXISTS `bank_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bank_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `own_contribution_id` bigint(20) DEFAULT NULL,
  `bank_name` varchar(100) NOT NULL,
  `account_number` varchar(50) DEFAULT NULL,
  `balance` bigint(20) NOT NULL,
  `any_ded_from_bank_account` tinyint(1) DEFAULT '0',
  `loan` tinyint(1) DEFAULT '0',
  `loan_emi` bigint(20) DEFAULT NULL,
  `balance_installments` int(11) DEFAULT NULL,
  `balance_loan_amount` bigint(20) DEFAULT NULL,
  `other_monthly_deduction` bigint(20) DEFAULT NULL,
  `total_monthly_deduction` bigint(20) DEFAULT NULL,
  `allocate_to_buy` bigint(20) DEFAULT NULL,
  `form_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bank_account__form_id` (`form_id`),
  CONSTRAINT `fk_bank_account__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_account`
--

LOCK TABLES `bank_account` WRITE;
/*!40000 ALTER TABLE `bank_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `bank_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buero`
--

DROP TABLE IF EXISTS `buero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buero` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_investigation_buero_user_id` (`user_id`),
  CONSTRAINT `fk_investigation_buero_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buero`
--

LOCK TABLES `buero` WRITE;
/*!40000 ALTER TABLE `buero` DISABLE KEYS */;
INSERT INTO `buero` VALUES (1,6);
/*!40000 ALTER TABLE `buero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cash_with_you`
--

DROP TABLE IF EXISTS `cash_with_you`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cash_with_you` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cash_with_you` bigint(20) NOT NULL,
  `form_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cash_with_you__form_id` (`form_id`),
  CONSTRAINT `fk_cash_with_you__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cash_with_you`
--

LOCK TABLES `cash_with_you` WRITE;
/*!40000 ALTER TABLE `cash_with_you` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_with_you` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dependent`
--

DROP TABLE IF EXISTS `dependent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dependent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dep_title` varchar(4) NOT NULL,
  `dep_relation` varchar(50) NOT NULL,
  `dep_fname` varchar(50) NOT NULL,
  `dep_mname` varchar(50) NOT NULL,
  `dep_lname` varchar(50) NOT NULL,
  `form_id` bigint(20) DEFAULT NULL,
  `is_applicant` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_dependent__form_id` (`form_id`),
  CONSTRAINT `fk_dependent__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dependent`
--

LOCK TABLES `dependent` WRITE;
/*!40000 ALTER TABLE `dependent` DISABLE KEYS */;
/*!40000 ALTER TABLE `dependent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employed_income`
--

DROP TABLE IF EXISTS `employed_income`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employed_income` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employer` varchar(100) DEFAULT NULL,
  `designation` varchar(50) DEFAULT NULL,
  `gross_sal` bigint(20) DEFAULT NULL,
  `deductions` bigint(20) DEFAULT NULL,
  `net_sal` bigint(20) DEFAULT NULL,
  `yrly_bonus` bigint(20) DEFAULT NULL,
  `yrs_service_remn` int(11) DEFAULT NULL,
  `experience_year` int(11) DEFAULT NULL,
  `experience_month` int(11) DEFAULT NULL,
  `yrs_with_employer_d` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employed_income`
--

LOCK TABLES `employed_income` WRITE;
/*!40000 ALTER TABLE `employed_income` DISABLE KEYS */;
/*!40000 ALTER TABLE `employed_income` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `external_amenity`
--

DROP TABLE IF EXISTS `external_amenity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `external_amenity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `external_amenity_name` varchar(255) DEFAULT NULL,
  `if_other_text` varchar(255) DEFAULT NULL,
  `requirement_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_external_amenity` (`requirement_id`,`external_amenity_name`),
  CONSTRAINT `fk_external_amenity__requirement_id` FOREIGN KEY (`requirement_id`) REFERENCES `requirement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `external_amenity`
--

LOCK TABLES `external_amenity` WRITE;
/*!40000 ALTER TABLE `external_amenity` DISABLE KEYS */;
INSERT INTO `external_amenity` VALUES (1,'Gymnasium','',1),(2,'Society Office','',1),(3,'Meditation Pavilion','',13),(4,'Jogging/Walking Tracks','',14),(5,'Natural & Eco-friendly Environment','',24),(6,'Natural & Eco-friendly Environment','',25),(7,'Natural & Eco-friendly Environment','',26);
/*!40000 ALTER TABLE `external_amenity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fixed_deposit`
--

DROP TABLE IF EXISTS `fixed_deposit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fixed_deposit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(100) NOT NULL,
  `name_of_holder` varchar(100) NOT NULL,
  `start_date` datetime NOT NULL,
  `maturity_date` datetime NOT NULL,
  `pricipal` bigint(20) NOT NULL,
  `interest_rate` decimal(5,2) NOT NULL,
  `maturity_amount` bigint(20) NOT NULL,
  `have_taken_loan_on_fd` tinyint(1) DEFAULT '0',
  `monthly_amnt_dedcted_frm_sal` tinyint(1) DEFAULT '0',
  `financer` varchar(100) DEFAULT NULL,
  `loan_amount` bigint(20) DEFAULT NULL,
  `montly_emi` bigint(20) DEFAULT NULL,
  `installments_paid` int(11) DEFAULT NULL,
  `balance_installments` int(11) DEFAULT NULL,
  `balance_loan_amount` bigint(20) DEFAULT NULL,
  `will_surrender` tinyint(1) DEFAULT '0',
  `allocation` bigint(20) DEFAULT NULL,
  `amount_can_avail` bigint(20) DEFAULT NULL,
  `form_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_fixed_deposit__form_id` (`form_id`),
  CONSTRAINT `fk_fixed_deposit__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fixed_deposit`
--

LOCK TABLES `fixed_deposit` WRITE;
/*!40000 ALTER TABLE `fixed_deposit` DISABLE KEYS */;
/*!40000 ALTER TABLE `fixed_deposit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form`
--

DROP TABLE IF EXISTS `form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `applicant_id` bigint(20) DEFAULT NULL,
  `co_applicant_id` bigint(20) DEFAULT NULL,
  `form_number` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `co_applicant_exists` tinyint(1) DEFAULT '0',
  `summary_id` bigint(20) DEFAULT NULL,
  `filled_date` datetime DEFAULT NULL,
  `completed_date` datetime DEFAULT NULL,
  `invoice_id` bigint(20) DEFAULT NULL,
  `suggested_budget` int(11) DEFAULT NULL,
  `total_cash` int(11) DEFAULT NULL,
  `personal_contribution` int(11) DEFAULT NULL,
  `cash_with_you` int(11) DEFAULT NULL,
  `calculated_budget` int(11) DEFAULT NULL,
  `applicant_income_short_form` bigint(20) DEFAULT NULL,
  `co_applicant_income_short_form` bigint(20) DEFAULT NULL,
  `required_loan` bigint(20) DEFAULT NULL,
  `total_budget` bigint(20) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `short_form_applicant_income_per` varchar(255) DEFAULT NULL,
  `short_form_co_applicant_income_per` varchar(255) DEFAULT NULL,
  `registration_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_form__form_number` (`form_number`),
  KEY `fk_form__applicant_id` (`applicant_id`),
  KEY `fk_form__co_applicant_id` (`co_applicant_id`),
  KEY `fk_form__summary_id` (`summary_id`),
  KEY `fk_form_invoice_id` (`invoice_id`),
  KEY `fk_form__agent_id` (`agent_id`),
  CONSTRAINT `fk_form__agent_id` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_form__applicant_id` FOREIGN KEY (`applicant_id`) REFERENCES `applicant` (`id`),
  CONSTRAINT `fk_form__co_applicant_id` FOREIGN KEY (`co_applicant_id`) REFERENCES `applicant` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_form__summary_id` FOREIGN KEY (`summary_id`) REFERENCES `summary` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_form_invoice_id` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form`
--

LOCK TABLES `form` WRITE;
/*!40000 ALTER TABLE `form` DISABLE KEYS */;
INSERT INTO `form` VALUES (1,NULL,NULL,'6666','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,NULL,NULL,'XYZ','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,NULL,NULL,'AXYZ','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,NULL,NULL,'MXYZ','Pending',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,1,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,2000000,NULL,1000000,5000000,NULL,'Yearly','Monthly','Self'),(6,2,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,3,NULL,NULL,NULL,NULL,NULL,300000,300000,100000,1000000,NULL,'yearly','yearly','agent'),(7,3,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,4,NULL,NULL,NULL,NULL,NULL,300000,300000,100000,1000000,NULL,'yearly','yearly','agent'),(8,4,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,300000,300000,100000,1000000,NULL,'yearly','yearly','agent'),(9,5,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,300000,300000,100000,1000000,NULL,'yearly','yearly','agent'),(10,6,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,8,NULL,NULL,NULL,NULL,NULL,3000000,2000000,90000,1500000,NULL,'yearly','yearly','self'),(11,7,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,9,NULL,NULL,NULL,NULL,NULL,50000,29998,2499998,1500000,NULL,'yearly','monthly','self'),(12,8,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,50000,29998,2499998,1500000,NULL,'yearly','monthly','self'),(13,9,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,11,NULL,NULL,NULL,NULL,NULL,50000,29998,2499998,1500000,NULL,'yearly','monthly','self'),(14,10,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,12,NULL,NULL,NULL,NULL,NULL,99998,852852,8528,1000000,NULL,'yearly','monthly','self'),(15,11,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,13,NULL,NULL,NULL,NULL,NULL,99998,852852,8528,1000000,NULL,'yearly','monthly','self'),(16,12,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,14,NULL,NULL,NULL,NULL,NULL,8528528222,123456,23456,1000000,NULL,'monthly','monthly','self'),(17,13,NULL,'DA1811015001','Incompleted',NULL,NULL,'2018-11-01 13:23:28',NULL,15,NULL,NULL,NULL,NULL,NULL,40000,40000,500000,1000000,NULL,'Monthly','Monthly','Self'),(18,14,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,16,NULL,NULL,NULL,NULL,NULL,50000,50000,500000,1000000,NULL,'Monthly','Monthly','Self'),(19,15,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,17,NULL,NULL,NULL,NULL,NULL,34534,345344,3454344,1000000,NULL,'monthly','monthly','self'),(20,16,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,18,NULL,NULL,NULL,NULL,NULL,34534,345344,3454344,1000000,NULL,'monthly','monthly','self'),(21,17,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,19,NULL,NULL,NULL,NULL,NULL,34534,345344,3454344,1000000,NULL,'monthly','monthly','self'),(22,18,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,20,NULL,NULL,NULL,NULL,NULL,34534,345344,3454344,1000000,NULL,'monthly','monthly','self'),(23,19,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,21,NULL,NULL,NULL,NULL,NULL,34534,345344,3454344,1000000,NULL,'monthly','monthly','self'),(24,20,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,22,NULL,NULL,NULL,NULL,NULL,234233,234233,234233,1000000,NULL,'monthly','monthly','self'),(25,21,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,23,NULL,NULL,NULL,NULL,NULL,234233,234233,234233,1000000,NULL,'monthly','monthly','self'),(26,22,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,24,NULL,NULL,NULL,NULL,NULL,8000,7999,7999,1000000,NULL,'monthly','monthly','self'),(27,23,NULL,'DA1811125002','Incompleted',NULL,NULL,'2018-11-12 11:12:20',NULL,25,NULL,NULL,NULL,NULL,NULL,23432,234324,234324,3000000,NULL,'Monthly','Monthly','Self'),(28,24,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,26,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,3000000,NULL,'Monthly','Monthly','Self'),(29,25,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,27,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,3000000,NULL,'Monthly','Monthly','Self'),(30,26,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,28,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,3000000,NULL,'Monthly','Monthly','Self'),(31,27,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,29,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,8000000,NULL,'Monthly','Monthly','Self'),(32,28,NULL,'DA1811125003','Incompleted',NULL,NULL,'2018-11-12 18:20:40',NULL,30,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,4000000,NULL,'Monthly','Monthly','Self'),(33,29,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,31,NULL,NULL,NULL,NULL,NULL,23422,23431,322,2500000,NULL,'monthly','monthly','self'),(34,30,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,32,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,9000000,NULL,'Monthly','Monthly','Self'),(35,31,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,33,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(36,32,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,34,NULL,NULL,NULL,NULL,NULL,3332,33332,3332,1000000,NULL,'monthly','monthly','self'),(37,33,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,35,NULL,NULL,NULL,NULL,NULL,234233,234232,234322,1000000,NULL,'monthly','monthly','self'),(38,34,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,36,NULL,NULL,NULL,NULL,NULL,8518,851,8519,1000000,NULL,'monthly','monthly','self'),(39,35,NULL,'DA1811145004','Incompleted',NULL,NULL,'2018-11-14 17:48:57',NULL,37,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(40,36,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,38,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(41,37,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,39,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(42,38,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,40,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(43,39,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,41,NULL,NULL,NULL,NULL,NULL,234233,234323,234322,1000000,NULL,'monthly','monthly','self'),(44,40,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,42,NULL,NULL,NULL,NULL,NULL,23431,23421233,234233,1000000,NULL,'monthly','monthly','self'),(45,41,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,43,NULL,NULL,NULL,NULL,NULL,1231231232,1231231233,122,1000000,NULL,'monthly','monthly','self'),(46,42,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,44,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(47,43,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,45,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(48,44,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,46,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(49,45,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,47,NULL,NULL,NULL,NULL,NULL,122,123,1222,1000000,NULL,'monthly','monthly','self'),(50,46,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,48,NULL,NULL,NULL,NULL,NULL,122,123,1222,1000000,NULL,'monthly','monthly','self'),(51,47,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,49,NULL,NULL,-998778,NULL,NULL,122,123,1222,1000000,NULL,'monthly','monthly','self'),(52,48,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,50,NULL,NULL,NULL,NULL,NULL,122,123,1222,1000000,NULL,'monthly','monthly','self'),(53,49,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,51,NULL,NULL,NULL,NULL,NULL,122,123,1222,1000000,NULL,'monthly','monthly','self'),(54,50,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,52,NULL,NULL,NULL,NULL,NULL,122,123,1222,1000000,NULL,'monthly','monthly','self'),(55,51,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,53,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(56,52,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,54,NULL,NULL,NULL,NULL,NULL,232,233,234,1000000,NULL,'monthly','monthly','self'),(57,53,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,55,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(58,54,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,56,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(59,55,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,57,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(60,56,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,58,NULL,NULL,NULL,NULL,NULL,232,232,233,1000000,NULL,'monthly','monthly','self'),(61,57,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,59,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(62,58,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,60,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(63,59,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,61,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(64,60,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,62,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(65,61,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,63,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(66,62,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,64,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(67,63,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,65,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(68,64,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,66,NULL,NULL,NULL,NULL,NULL,1231,1233,123123,1000000,NULL,'Monthly','Monthly','Self'),(69,65,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,67,NULL,NULL,NULL,NULL,NULL,232,233,234,1000000,NULL,'monthly','monthly','self'),(70,66,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,68,NULL,NULL,NULL,NULL,NULL,542,542,542,1000000,NULL,'monthly','monthly','self'),(71,67,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,69,NULL,NULL,NULL,NULL,NULL,342,3434,33,1000000,NULL,'monthly','monthly','self'),(72,68,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL,NULL,9238,3298,3298,1500000,NULL,'Monthly','Monthly','Self'),(73,69,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,71,NULL,NULL,NULL,NULL,NULL,4983,9823,3298,1000000,NULL,'monthly','monthly','self'),(74,70,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,72,NULL,NULL,NULL,NULL,NULL,4983,9823,3298,1000000,NULL,'monthly','monthly','self'),(75,74,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,73,NULL,NULL,NULL,NULL,NULL,234,23423,21345,1000000,NULL,'monthly','monthly','self'),(76,71,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,74,NULL,NULL,NULL,NULL,NULL,234,23423,21345,1000000,NULL,'monthly','monthly','self'),(77,73,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,75,NULL,NULL,NULL,NULL,NULL,234,23423,21345,1000000,NULL,'monthly','monthly','self'),(78,72,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,76,NULL,NULL,NULL,NULL,NULL,234,23423,21345,1000000,NULL,'monthly','monthly','self'),(79,75,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,77,NULL,NULL,NULL,NULL,NULL,234,23423,21345,1000000,NULL,'monthly','monthly','self'),(80,76,NULL,NULL,'Incompleted',NULL,NULL,NULL,NULL,78,NULL,NULL,NULL,NULL,NULL,234,23423,21345,1000000,NULL,'monthly','monthly','self');
/*!40000 ALTER TABLE `form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ib_feedback`
--

DROP TABLE IF EXISTS `ib_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ib_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ib_id` bigint(20) NOT NULL,
  `agent_id` bigint(20) NOT NULL,
  `attachment_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ib_feedback_attachment_id` (`attachment_id`),
  KEY `fk_ib_feedback_ib_id` (`ib_id`),
  KEY `fk_ib_feedback_agent_id` (`agent_id`),
  CONSTRAINT `fk_ib_feedback_agent_id` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ib_feedback_attachment_id` FOREIGN KEY (`attachment_id`) REFERENCES `attachment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ib_feedback_ib_id` FOREIGN KEY (`ib_id`) REFERENCES `buero` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ib_feedback`
--

LOCK TABLES `ib_feedback` WRITE;
/*!40000 ALTER TABLE `ib_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `ib_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insurance`
--

DROP TABLE IF EXISTS `insurance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insurance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name_of_holder` varchar(100) NOT NULL,
  `insurer_name` varchar(100) NOT NULL,
  `start_date` datetime NOT NULL,
  `maturity_date` datetime NOT NULL,
  `sum_assured` bigint(20) NOT NULL,
  `premium_freqency` varchar(20) DEFAULT NULL,
  `premium` bigint(20) NOT NULL,
  `premiums_paid` int(11) DEFAULT NULL,
  `premiums_balance` int(11) DEFAULT NULL,
  `last_payment_date` datetime DEFAULT NULL,
  `maturity_amount` bigint(20) NOT NULL,
  `monthly_amnt_dedcted_frm_sal` tinyint(1) DEFAULT '0',
  `receiving_any_matured_amount` tinyint(1) DEFAULT '0',
  `allocation` bigint(20) DEFAULT NULL,
  `form_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_insurance__form_id` (`form_id`),
  CONSTRAINT `fk_insurance__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insurance`
--

LOCK TABLES `insurance` WRITE;
/*!40000 ALTER TABLE `insurance` DISABLE KEYS */;
/*!40000 ALTER TABLE `insurance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `internal_amenity`
--

DROP TABLE IF EXISTS `internal_amenity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `internal_amenity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `internal_amenity_name` varchar(255) DEFAULT NULL,
  `if_other_text` varchar(255) DEFAULT NULL,
  `requirement_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_internal_amenity` (`requirement_id`,`internal_amenity_name`),
  CONSTRAINT `fk_internal_amenity__requirement_id` FOREIGN KEY (`requirement_id`) REFERENCES `requirement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `internal_amenity`
--

LOCK TABLES `internal_amenity` WRITE;
/*!40000 ALTER TABLE `internal_amenity` DISABLE KEYS */;
INSERT INTO `internal_amenity` VALUES (1,'Conceal Copper Wiring With Quality Switches','',1),(2,'CC Tv Camera','',13),(3,'Decorative Main Door','',14),(4,'Marbo Granite Flooring','',24),(5,'Marbo Granite Flooring','',25),(6,'Marbo Granite Flooring','',26);
/*!40000 ALTER TABLE `internal_amenity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_number` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` decimal(15,2) NOT NULL,
  `paid` tinyint(1) DEFAULT '0',
  `user_id` bigint(20) DEFAULT NULL,
  `receipt_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_invoice__invoice_number` (`invoice_number`),
  KEY `fk_invoice__user_id` (`user_id`),
  CONSTRAINT `fk_invoice__user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,'PSINV-1511774001607','Proposed Society Form Fee',500.00,0,NULL,NULL),(2,'PSINV-1512568108291','Proposed Society Form Fee',500.00,1,14,NULL),(3,'PSINV-1512568595644','Proposed Society Form Fee',500.00,0,NULL,NULL),(4,'PSINV-1512568604325','Proposed Society Form Fee',500.00,0,NULL,NULL),(5,'PSINV-1512621957460','Proposed Society Form Fee',500.00,0,NULL,NULL),(6,'PSINV-1512621964551','Proposed Society Form Fee',500.00,0,NULL,NULL),(7,'PSINV-1512626609794','Proposed Society AVC recharge ',7500.00,0,14,NULL),(8,'PSINV-1512971357610','Proposed Society Form Fee',500.00,0,NULL,NULL),(9,'PSINV-1539087935419','Proposed Society Form Fee',500.00,0,NULL,NULL),(10,'PSINV-1539087968978','Proposed Society Form Fee',500.00,0,NULL,NULL),(11,'PSINV-1539087979091','Proposed Society Form Fee',500.00,0,NULL,NULL),(12,'PSINV-1540798283858','Proposed Society Form Fee',500.00,0,NULL,NULL),(13,'PSINV-1540805177402','Proposed Society Form Fee',500.00,0,NULL,NULL),(14,'PSINV-1540807557732','Proposed Society Form Fee',500.00,0,NULL,NULL),(15,'PSINV-1541058787616','Proposed Society Form Fee',500.00,1,22,501),(16,'PSINV-1541062256738','Proposed Society Form Fee',500.00,0,NULL,NULL),(17,'PSINV-1541067655389','Proposed Society Form Fee',500.00,0,NULL,NULL),(18,'PSINV-1541067681662','Proposed Society Form Fee',500.00,0,NULL,NULL),(19,'PSINV-1541067869543','Proposed Society Form Fee',500.00,0,NULL,NULL),(20,'PSINV-1541068423387','Proposed Society Form Fee',500.00,0,NULL,NULL),(21,'PSINV-1541068698771','Proposed Society Form Fee',500.00,0,NULL,NULL),(22,'PSINV-1541068824786','Proposed Society Form Fee',500.00,0,NULL,NULL),(23,'PSINV-1541068855854','Proposed Society Form Fee',500.00,0,NULL,NULL),(24,'PSINV-1541154237725','Proposed Society Form Fee',500.00,0,NULL,NULL),(25,'PSINV-1542001026433','Proposed Society Form Fee',500.00,1,NULL,502),(26,'PSINV-1542006140965','Proposed Society Form Fee',500.00,0,NULL,NULL),(27,'PSINV-1542006289280','Proposed Society Form Fee',500.00,0,NULL,NULL),(28,'PSINV-1542006333081','Proposed Society Form Fee',500.00,0,NULL,NULL),(29,'PSINV-1542020935006','Proposed Society Form Fee',500.00,0,NULL,NULL),(30,'PSINV-1542027007728','Proposed Society Form Fee',500.00,1,NULL,503),(31,'PSINV-1542029629500','Proposed Society Form Fee',500.00,0,NULL,NULL),(32,'PSINV-1542094846232','Proposed Society Form Fee',500.00,0,NULL,NULL),(33,'PSINV-1542186290132','Proposed Society Form Fee',500.00,0,NULL,NULL),(34,'PSINV-1542189622513','Proposed Society Form Fee',500.00,0,NULL,NULL),(35,'PSINV-1542194718489','Proposed Society Form Fee',500.00,0,NULL,NULL),(36,'PSINV-1542195150484','Proposed Society Form Fee',500.00,0,NULL,NULL),(37,'PSINV-1542196502920','Proposed Society Form Fee',500.00,1,NULL,504),(38,'PSINV-1542199175931','Proposed Society Form Fee',500.00,0,NULL,NULL),(39,'PSINV-1542263952339','Proposed Society Form Fee',500.00,0,NULL,NULL),(40,'PSINV-1542263983177','Proposed Society Form Fee',500.00,0,NULL,NULL),(41,'PSINV-1542266982398','Proposed Society Form Fee',500.00,0,NULL,NULL),(42,'PSINV-1542270362238','Proposed Society Form Fee',500.00,0,NULL,NULL),(43,'PSINV-1542270820534','Proposed Society Form Fee',500.00,0,NULL,NULL),(44,'PSINV-1542271517553','Proposed Society Form Fee',500.00,0,NULL,NULL),(45,'PSINV-1542271914115','Proposed Society Form Fee',500.00,0,NULL,NULL),(46,'PSINV-1542273464727','Proposed Society Form Fee',500.00,0,NULL,NULL),(47,'PSINV-1542277716404','Proposed Society Form Fee',500.00,0,NULL,NULL),(48,'PSINV-1542277809595','Proposed Society Form Fee',500.00,0,NULL,NULL),(49,'PSINV-1542278276210','Proposed Society Form Fee',500.00,0,NULL,NULL),(50,'PSINV-1542278463185','Proposed Society Form Fee',500.00,0,NULL,NULL),(51,'PSINV-1542278588571','Proposed Society Form Fee',500.00,0,NULL,NULL),(52,'PSINV-1542278963384','Proposed Society Form Fee',500.00,0,NULL,NULL),(53,'PSINV-1542279213253','Proposed Society Form Fee',500.00,0,NULL,NULL),(54,'PSINV-1542279532093','Proposed Society Form Fee',500.00,0,NULL,NULL),(55,'PSINV-1542279675318','Proposed Society Form Fee',500.00,0,NULL,NULL),(56,'PSINV-1542286794886','Proposed Society Form Fee',500.00,0,NULL,NULL),(57,'PSINV-1542348327163','Proposed Society Form Fee',500.00,0,NULL,NULL),(58,'PSINV-1542348343594','Proposed Society Form Fee',500.00,0,NULL,NULL),(59,'PSINV-1542349870687','Proposed Society Form Fee',500.00,0,NULL,NULL),(60,'PSINV-1542349948523','Proposed Society Form Fee',500.00,0,NULL,NULL),(61,'PSINV-1542351170785','Proposed Society Form Fee',500.00,0,NULL,NULL),(62,'PSINV-1542351213473','Proposed Society Form Fee',500.00,0,NULL,NULL),(63,'PSINV-1542351219853','Proposed Society Form Fee',500.00,0,NULL,NULL),(64,'PSINV-1542351288099','Proposed Society Form Fee',500.00,0,NULL,NULL),(65,'PSINV-1542351301082','Proposed Society Form Fee',500.00,0,NULL,NULL),(66,'PSINV-1542351344255','Proposed Society Form Fee',500.00,0,NULL,NULL),(67,'PSINV-1542607056120','Proposed Society Form Fee',500.00,0,NULL,NULL),(68,'PSINV-1542608581608','Proposed Society Form Fee',500.00,0,NULL,NULL),(69,'PSINV-1542610329378','Proposed Society Form Fee',500.00,0,NULL,NULL),(70,'PSINV-1542794015785','Proposed Society Form Fee',500.00,0,NULL,NULL),(71,'PSINV-1542965766354','Proposed Society Form Fee',500.00,0,NULL,NULL),(72,'PSINV-1542965879192','Proposed Society Form Fee',500.00,0,NULL,NULL),(73,'PSINV-1543932730007','Proposed Society Form Fee',500.00,0,NULL,NULL),(74,'PSINV-1543932729946','Proposed Society Form Fee',500.00,0,NULL,NULL),(75,'PSINV-1543932729973','Proposed Society Form Fee',500.00,0,NULL,NULL),(76,'PSINV-1543932729875','Proposed Society Form Fee',500.00,0,NULL,NULL),(77,'PSINV-1543932730559','Proposed Society Form Fee',500.00,0,NULL,NULL),(78,'PSINV-1543999884429','Proposed Society Form Fee',500.00,0,NULL,NULL);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `language_proficiency`
--

DROP TABLE IF EXISTS `language_proficiency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `language_proficiency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `can_read` varchar(10) DEFAULT NULL,
  `can_write` varchar(10) DEFAULT NULL,
  `can_speak` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `language_proficiency`
--

LOCK TABLES `language_proficiency` WRITE;
/*!40000 ALTER TABLE `language_proficiency` DISABLE KEYS */;
INSERT INTO `language_proficiency` VALUES (1,'Marathi','False','False','False'),(2,'Hindi','False','False','False'),(3,'English','False','False','False');
/*!40000 ALTER TABLE `language_proficiency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan_details`
--

DROP TABLE IF EXISTS `loan_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loan_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `form_id` bigint(20) DEFAULT NULL,
  `financer` varchar(100) DEFAULT NULL,
  `loan_amount` bigint(20) DEFAULT NULL,
  `monthly_emi` bigint(20) DEFAULT NULL,
  `installments_paid` int(11) DEFAULT NULL,
  `installments_balance` int(11) DEFAULT NULL,
  `balance_loan_amount` bigint(20) DEFAULT NULL,
  `amount_per_month_from_sal` tinyint(1) DEFAULT '0',
  `loan_shown_on_bs` tinyint(1) DEFAULT '0',
  `is_applicant` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_loan_details__form_id` (`form_id`),
  CONSTRAINT `fk_loan_details__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan_details`
--

LOCK TABLES `loan_details` WRITE;
/*!40000 ALTER TABLE `loan_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `loan_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `misc_borrowing`
--

DROP TABLE IF EXISTS `misc_borrowing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `misc_borrowing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` bigint(20) NOT NULL,
  `number_of_years` int(11) DEFAULT NULL,
  `type` varchar(11) NOT NULL,
  `interest_rate` decimal(5,2) DEFAULT NULL,
  `emi` bigint(20) DEFAULT NULL,
  `total_deduction` bigint(20) DEFAULT NULL,
  `form_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_misc_borrowing__form_id` (`form_id`),
  CONSTRAINT `fk_misc_borrowing__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `misc_borrowing`
--

LOCK TABLES `misc_borrowing` WRITE;
/*!40000 ALTER TABLE `misc_borrowing` DISABLE KEYS */;
/*!40000 ALTER TABLE `misc_borrowing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nearby`
--

DROP TABLE IF EXISTS `nearby`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nearby` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `location_near` varchar(255) DEFAULT NULL,
  `if_other_text` varchar(255) DEFAULT NULL,
  `requirement_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_nearby` (`requirement_id`,`location_near`),
  CONSTRAINT `fk_nearby__requirement_id` FOREIGN KEY (`requirement_id`) REFERENCES `requirement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nearby`
--

LOCK TABLES `nearby` WRITE;
/*!40000 ALTER TABLE `nearby` DISABLE KEYS */;
INSERT INTO `nearby` VALUES (1,'Market','',1),(2,'College','',1),(3,'Market','',13),(4,'Market','',14),(5,'Market','',24),(6,'Market','',25),(7,'Market','',26);
/*!40000 ALTER TABLE `nearby` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page`
--

DROP TABLE IF EXISTS `page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `formatted_title` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_page__fomatted_title` (`formatted_title`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page`
--

LOCK TABLES `page` WRITE;
/*!40000 ALTER TABLE `page` DISABLE KEYS */;
INSERT INTO `page` VALUES (1,'concept','Our Concept','<p>For a better tomorrow, by providing homes at affordable cost and by contributing to the development of the community &amp; society at large, as a part of corporate social responsibility. Because it is a proven fact that, investment in properties offers the maximum returns and this sector is in second position in providing employment in India. With our mission to add value to human life, our innovative ideas and spirit of service have presented the common man a huge hope to have a home of his own.</p>\r\n'),(2,'benefits','Benefits of Concept','<p>For a better tomorrow, by providing homes at affordable cost and by contributing to the development of the community &amp; society at large, as a part of corporate social responsibility. Because it is a proven fact that, investment in properties offers the maximum returns and this sector is in second position in providing employment in India. With our mission to add value to human life, our innovative ideas and spirit of service have presented the common man a huge hope to have a home of his own.</p>\r\n'),(3,'rules','Rules & Regulations','<p>For a better tomorrow, by providing homes at affordable cost and by contributing to the development of the community &amp; society at large, as a part of corporate social responsibility. Because it is a proven fact that, investment in properties offers the maximum returns and this sector is in second position in providing employment in India. With our mission to add value to human life, our innovative ideas and spirit of service have presented the common man a huge hope to have a home of his own.</p>\r\n'),(4,'terms_and_conditions','Applicants Terms & Conditions','<p>For a better tomorrow, by providing homes at affordable cost and by contributing to the development of the community &amp; society at large, as a part of corporate social responsibility. Because it is a proven fact that, investment in properties offers the maximum returns and this sector is in second position in providing employment in India. With our mission to add value to human life, our innovative ideas and spirit of service have presented the common man a huge hope to have a home of his own.</p>\r\n'),(5,'success_story','Success Story of Concept','<p>For a better tomorrow, by providing homes at affordable cost and by contributing to the development of the community &amp; society at large, as a part of corporate social responsibility. Because it is a proven fact that, investment in properties offers the maximum returns and this sector is in second position in providing employment in India. With our mission to add value to human life, our innovative ideas and spirit of service have presented the common man a huge hope to have a home of his own.</p>\r\n'),(6,'history','History','<p>For a better tomorrow, by providing homes at affordable cost and by contributing to the development of the community &amp; society at large, as a part of corporate social responsibility. Because it is a proven fact that, investment in properties offers the maximum returns and this sector is in second position in providing employment in India. With our mission to add value to human life, our innovative ideas and spirit of service have presented the common man a huge hope to have a home of his own.</p>\r\n');
/*!40000 ALTER TABLE `page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint(20) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `completion_time` datetime DEFAULT NULL,
  `redirect_url` varchar(1024) DEFAULT NULL,
  `client_processed` tinyint(1) DEFAULT '0',
  `gateway_processed` tinyint(1) DEFAULT '0',
  `success` tinyint(1) DEFAULT '0',
  `auth_desc` varchar(10) DEFAULT NULL,
  `transaction_id` varchar(50) DEFAULT NULL,
  `payment_mode` varchar(255) DEFAULT NULL,
  `bank_name` varchar(50) DEFAULT NULL,
  `nb_bid` varchar(50) DEFAULT NULL,
  `nb_order_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_payment__invoice_id` (`invoice_id`),
  CONSTRAINT `fk_payment__invoice_id` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,'2017-11-27 14:43:33','2017-11-27 14:44:04','/short_form/payment/complete',1,1,0,'N','2017112714433200',NULL,NULL,NULL,NULL),(2,2,'2017-12-06 19:18:45','2017-12-06 19:18:49','/agent/fill/payment/complete',1,1,1,'Y','2017120619184500','Test card','Test Bank','00000000-0000-0000-1411-071507993000','CCABK1CNM257'),(3,15,'2018-11-01 13:23:11','2018-11-01 13:23:27','/short_form/payment/complete',1,1,1,'Y','2018110113231100','Test card','Test Bank','00000000-0000-0000-1411-071507993000','CCABK1CNM257'),(4,16,'2018-11-01 14:21:00',NULL,'/short_form/payment/complete',0,NULL,NULL,NULL,'2018110114205900',NULL,NULL,NULL,NULL),(5,25,'2018-11-12 11:12:12','2018-11-12 11:12:20','/short_form/payment/complete',1,1,1,'Y','2018111211121100','Test card','Test Bank','00000000-0000-0000-1411-071507993000','CCABK1CNM257'),(6,30,'2018-11-12 18:20:09','2018-11-12 18:20:40','/short_form/payment/complete',1,1,1,'Y','2018111218200900','Test card','Test Bank','00000000-0000-0000-1411-071507993000','CCABK1CNM257'),(7,37,'2018-11-14 17:48:18','2018-11-14 17:48:57','/short_form/payment/complete',1,1,1,'Y','2018111417481800','Test card','Test Bank','00000000-0000-0000-1411-071507993000','CCABK1CNM257'),(8,38,'2018-11-14 18:09:40',NULL,'/short_form/payment/complete',0,NULL,NULL,NULL,'2018111418094000',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_code`
--

DROP TABLE IF EXISTS `payment_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile` varchar(50) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `generation_time` datetime NOT NULL,
  `invalidate_time` datetime DEFAULT NULL,
  `used` tinyint(1) DEFAULT '0',
  `form_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_code__code` (`code`),
  KEY `fk_payment_code__form_id` (`form_id`),
  CONSTRAINT `fk_payment_code__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_code`
--

LOCK TABLES `payment_code` WRITE;
/*!40000 ALTER TABLE `payment_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_info`
--

DROP TABLE IF EXISTS `payment_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `payment_mode` varchar(100) NOT NULL,
  `bank_name` varchar(100) NOT NULL,
  `branch_name` varchar(100) NOT NULL,
  `payment_date` datetime NOT NULL,
  `cheque_number` bigint(20) NOT NULL,
  `transaction_id` varchar(100) NOT NULL,
  `account_number` varchar(100) NOT NULL,
  `receipt_number` varchar(100) NOT NULL,
  `form_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_form_id` (`form_id`),
  CONSTRAINT `fk_form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_info`
--

LOCK TABLES `payment_info` WRITE;
/*!40000 ALTER TABLE `payment_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persistent_id`
--

DROP TABLE IF EXISTS `persistent_id`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persistent_id` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `key_value` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_persistent_id_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persistent_id`
--

LOCK TABLES `persistent_id` WRITE;
/*!40000 ALTER TABLE `persistent_id` DISABLE KEYS */;
INSERT INTO `persistent_id` VALUES (1,'receiptId',505),(2,'formNumber',5005);
/*!40000 ALTER TABLE `persistent_id` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `play_evolutions`
--

DROP TABLE IF EXISTS `play_evolutions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `play_evolutions` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `applied_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `apply_script` text,
  `revert_script` text,
  `state` varchar(255) DEFAULT NULL,
  `last_problem` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `play_evolutions`
--

LOCK TABLES `play_evolutions` WRITE;
/*!40000 ALTER TABLE `play_evolutions` DISABLE KEYS */;
INSERT INTO `play_evolutions` VALUES (1,'73f3ab59ce9c6936f99d351f0f96228f8ce61ec7','2017-11-27 06:07:14','create table page (\nid                        bigint auto_increment not null,\nformatted_title           varchar(255) not null,\ntitle                     varchar(255) not null,\ncontent                   text,\nconstraint pk_page primary key (id)\n);\n\ncreate table spouse_details (\nid                        bigint auto_increment not null,\nsp_title                  varchar(4) not null,\nsp_fname                  varchar(50) not null,\nsp_mname                  varchar(50) not null,\nsp_lname                  varchar(50),\nsp_profession             varchar(50) not null,\nsp_dob                    datetime not null,\nanniversary               datetime not null,\nchildren                  integer,\nconstraint pk_spouse_details primary key (id)\n);\n\ncreate table address (\nid                        bigint auto_increment not null,\nline1                     varchar(100) not null,\nline2                     varchar(100),\nstreet                    varchar(100),\narea                      varchar(100) not null,\ntaluka                    varchar(100),\ncity                      varchar(100) not null,\ndistrict                  varchar(100),\nstate                     varchar(100) not null,\ncountry                   varchar(100) not null,\npin                       varchar(50) not null,\nconstraint pk_address primary key (id)\n);\n\ncreate table employed_income (\nid                    bigint auto_increment not null,\nemployer              varchar(100) not null,\ndesignation           varchar(50) not null,\ngross_sal             bigint not null,\ndeductions            bigint,\nnet_sal               bigint not null,\nyrly_bonus            bigint,\nyrs_with_employer     int not null,\nexperience            bigint not null,\nyrs_service_remn      int not null,\nconstraint pk_employed_income primary key (id)\n);\n\ncreate table self_employed_income (\nid                   		bigint auto_increment not null,\nbusiness_type        		varchar(50) not null,\nyr1_pat              		bigint not null,\nyr1_depr             		bigint not null,\nyr2_pat              		bigint,\nyr2_depr             		bigint,\nyr3_pat              		bigint,\nyr3_depr             		bigint,\nconstraint pk_self_employed_income primary key (id)\n);\n\ncreate table applicant (\n# Applicant name and details.\nid                        bigint auto_increment not null,\ntitle                     varchar(4) not null,\nfname                     varchar(50) not null,\nmname                     varchar(50) not null,\nlname                     varchar(50),\ndob                       datetime not null,\nemail                     varchar(100) not null,\nsex                       char not null,\nidentity_number1          varchar(50) not null,\nidentity_number1_type     varchar(20) not null,\nidentity_number2          varchar(50),\nidentity_number2_type     varchar(20),\nidentity_number3          varchar(50),\nidentity_number3_type     varchar(20),\nfh_title                  varchar(4) not null,\nfh_fname                  varchar(50) not null,\nfh_mname                  varchar(50) not null,\nfh_lname                  varchar(50),\nfh_relation               varchar(50) not null,\n\n# Details of Spouse if married.\nmarital_status            char not null,\nspouse_details_id         bigint,\nnationality               varchar(50) not null,\n\npermanent_address_same_as varchar(20) not null,\nresidential_address_same_as varchar(20) not null,\ncommunication_address_same_as varchar(20) not null,\n\nbirth_place_id            bigint not null,\npermanent_address_id      bigint,\nresidential_address_id    bigint,\ncommunication_address_id  bigint,\n\nresi_status               varchar(50) not null,\nresidence_yrs_residing    integer not null,\ncity_yrs_residing         integer not null,\nphone1_type               varchar(20) not null,\nphone1                    varchar(20) not null,\nphone2_type               varchar(20),\nphone2                    varchar(20),\nphone3_type               varchar(20),\nphone3                    varchar(20),\n\noffice_address_id         bigint not null,\noff_mail                  varchar(100),\nemployer_web              varchar(100),\noff_phone1                varchar(20) not null,\noff_phone1_type           varchar(20) not null,\noff_phone2                varchar(20),\noff_phone2_type           varchar(20),\noff_phone3                varchar(20),\noff_phone3_type           varchar(20),\n\napprox_eligibility        bigint,\n\n# Income Details.\noccupation                varchar(50) not null,\nemployed_income_id        bigint,\nself_employed_income_id   bigint,\n\n# Details of Loan if any. Note: Here loan_shown_on_bs or amount_per_month_from_sal should not be asked.\nloan                      tinyint(1) default 0,\nconstraint pk_applicant primary key (id)\n);\n\n\ncreate table form (\nid                        bigint auto_increment not null,\napplicant_id              bigint,\nco_applicant_id           bigint,\nform_number               varchar(50) not null,\nstatus                    varchar(50) not null,\nconstraint pk_application_form primary key (id)\n);\n\ncreate table user (\nid                        bigint auto_increment not null,\nemail                     varchar(100) not null,\nname                      varchar(100),\nmobile                    varchar(20) not null,\npassword                  varchar(255),\nauthcode                  varchar(10),\nrole                      varchar(20) not null,\njoining_date              datetime not null,\nlast_login                datetime,\nemail_verified            tinyint(1) default 0,\nphone_verified            tinyint(1) default 0,\nform_id                   bigint,\nconstraint pk_user primary key (id)\n);\n\ncreate table loan_details (\nid                        bigint auto_increment not null,\napplicant_id              bigint,\nfinancer                  varchar(100),\nloan_amount               bigint,\nmonthly_emi               bigint,\ninstallments_paid         integer,\ninstallments_balance      integer,\nbalance_loan_amount       bigint,\nconstraint pk_loan_details primary key (id)\n);\n\ncreate table dependent (\nid                        bigint auto_increment not null,\ndep_title                 varchar(4) not null,\ndep_relation              varchar(50) not null,\ndep_fname                 varchar(50) not null,\ndep_mname                 varchar(50) not null,\ndep_lname                 varchar(50) not null,\napplicant_id              bigint,\nconstraint pk_dependent primary key (id)\n);\n\ncreate table bank_account (\nid                        bigint auto_increment not null,\nown_contribution_id       bigint not null,\nbank_name                 varchar(100) not null,\naccount_number            varchar(50),\nbalance                   bigint not null,\nany_ded_from_bank_account tinyint(1) default 0,\n\nloan                      tinyint(1) default 0,\nloan_emi                  bigint,\nbalance_installments      integer,\nbalance_loan_amount       bigint,\nother_monthly_deduction   bigint,\ntotal_monthly_deduction   bigint,\nallocate_to_buy           bigint,\nform_id                   bigint not null,\nconstraint pk_bank_account primary key (id)\n);\n\ncreate table fixed_deposit (\nid                        bigint auto_increment not null,\nbank_name                 varchar(100) not null,\nname_of_holder            varchar(100) not null,\nstart_date                datetime not null,\nmaturity_date             datetime not null,\npricipal                  bigint not null,\ninterest_rate             decimal(5, 2) not null,\nmaturity_amount           bigint not null,\nhave_taken_loan_on_fd     tinyint(1) default 0,\nmonthly_amnt_dedcted_frm_sal tinyint(1) default 0,\n\n# Loan details\nfinancer                  varchar(100),\nloan_amount               bigint,\nmontly_emi                bigint,\ninstallments_paid         integer,\nbalance_installments      integer,\nbalance_loan_amount       bigint,\n\nwill_surrender            tinyint(1) default 0,\nallocation                bigint,\namount_can_avail          bigint,\nform_id                   bigint not null,\nconstraint pk_fixed_deposit primary key (id)\n);\n\ncreate table insurance (\nid                        bigint auto_increment not null,\nname_of_holder            varchar(100) not null,\ninsurer_name              varchar(100) not null,\nstart_date                datetime not null,\nmaturity_date             datetime not null,\nsum_assured               bigint not null,\npremium_freqency          varchar(20),\npremium                   bigint not null,\npremiums_paid             integer,\npremiums_balance          integer,\nlast_payment_date         datetime,\nmaturity_amount           bigint not null,\nmonthly_amnt_dedcted_frm_sal tinyint(1) default 0,\nreceiving_any_matured_amount tinyint(1) default 0,\nallocation                bigint,\nform_id                   bigint,\nconstraint pk_insurance primary key (id)\n);\n\ncreate table recurring_deposit (\nid                            bigint auto_increment not null,\nbank_name                     varchar(100) not null,\nname_of_holder                varchar(100) not null,\nstart_date                    datetime,\nmaturity_date                 datetime,\ninstallment                   bigint not null,\ninstallments_paid             integer,\ninstallments_balance          integer,\nprincipal                     bigint,\ninterest_rate                 decimal(5, 2),\nmaturity_amount               bigint not null,\nmonthly_installments          bigint,\nbalance_installments          integer,\nbalance_ded_amount            bigint,\nmonthly_amnt_dedcted_frm_sal  tinyint(1) default 0,\nwill_surrender                tinyint(1) default 0,\namount_can_avail              bigint,\nallocation                    bigint,\nform_id                       bigint not null,\nconstraint pk_recurring_deposit primary key (id)\n);\n\ncreate table misc_borrowing (\nid                        bigint auto_increment not null,\namount                    bigint not null,\nnumber_of_years           integer,\ntype                      varchar(11) not null,\ninterest_rate             decimal(5,2),\nemi                       bigint,\ntotal_deduction           bigint,\nform_id                   bigint not null,\nconstraint pk_misc_borrowing primary key (id)\n);\n\ncreate table property (\nid                        bigint auto_increment not null,\ntype                      varchar(50),\ntype_detail               varchar(50),\nlocation_id               bigint,\narea                      decimal(38),\nloan                      tinyint(1) default 0,\nmonthly_amnt_dedcted_frm_sal tinyint(1) default 0,\nloan_details_id           bigint,\nwilling_to_sell           tinyint(1) default 0,\nexpected_price            bigint,\nallocation                bigint,\n\n# Loan details\nfinancer                  varchar(100),\nloan_amount               bigint,\nmontly_emi                bigint,\ninstallments_paid         integer,\ninstallments_balance      integer,\nprincipal                 integer,\ninterest_rate             decimal(38),\nmaturity_amount           integer not null,\nmonthly_installments      integer,\nbalance_installments      integer,\nbalance_loan_amount       bigint,\n\nform_id                   bigint not null,\nconstraint pk_property primary key (id)\n);\n\n\n# TODO:\ncreate table external_amenity (\nid                        bigint auto_increment not null,\nexternal_amenity_name       varchar(255),\nif_other_text               varchar(255),\nrequirement_id                   bigint not null,\nconstraint pk_external_amenity primary key (id))\n;\n\ncreate table internal_amenity (\nid                        bigint auto_increment not null,\ninternal_amenity_name       varchar(255),\nif_other_text               varchar(255),\nrequirement_id                   bigint not null,\nconstraint pk_internal_amenity primary key (id))\n;\n\ncreate table nearby (\nid                        bigint auto_increment not null,\nlocation_near               varchar(255),\nif_other_text               varchar(255),\nrequirement_id                   bigint not null,\nconstraint pk_nearby primary key (id))\n;\n\ncreate table requirement (\nid                        			bigint auto_increment not null,\nbuiltup_area						integer,\ncarpet_area							integer,\nchoice_of_floor						integer,\nsub_type                      		varchar(255),\ngarden_area							integer,\ngarden_area_required                varchar(255),\nno_of_bedrooms						integer,\nno_of_common_toilets				integer,\nno_of_rooms_required				integer,\nno_of_separate_bathroom				integer,\nno_of_separate_wc					integer,\nopen_space							integer,\nplot_area							integer,\nplot_area_measure                   varchar(255),\nterrace_area						integer,\nterrace_required          			tinyint(1) default 0,\nterrace_choice						varchar(255),\ntoilet_wc_bath_requirements         varchar(255),\nuse_of_land                      	varchar(255),\nother_details                      	varchar(255),\ntype                      			varchar(255),\nresidential_type_detail             varchar(255),\ncommercial_type_detail              varchar(255),\nagricultural_type_detail            varchar(255),\ntype_detail							varchar(255),\nspecify_height_required          	tinyint(1) default 0,\nheight								integer,\npower_connection_type               varchar(255),\nif_connection_type_other            varchar(255),\nis_separate_water_connection_required          			tinyint(1) default 0,\nis_separate_power_connection_required          			tinyint(1) default 0,\nloft_required          				tinyint(1) default 0,\nloft_area							integer,\notla_required          				tinyint(1) default 0,\notla_area							integer,\nno_of_attached_toilets				integer,\nno_of_attached_urinals				integer,\nopen_space_required          		tinyint(1) default 0,\ntoilet_type                      	varchar(255),\nwater_pipe_size						integer,\nif_chawl_type_other                 varchar(255),\narea_for_each_room					integer,\ncity                      			varchar(255),\ntaluka                      		varchar(255),\ndistrict                      		varchar(255),\nstate                      			varchar(255),\ncountry                      		varchar(255),\nvillage_or_detail_location          varchar(255),\nconstraint pk_requirement primary key (id))\n;\n\n###### Custom constraint #######\n\n# Page table constraints\nalter table page add constraint uk_page__fomatted_title unique index(formatted_title);\n\n# Address table constraints\ncreate index ix_address__state on address(state);\ncreate index ix_address__city on address(city);\ncreate index ix_address__area on address(area);\ncreate index ix_address__pin on address(pin);\n\n# LoanDetails table constraints\nalter table loan_details add constraint fk_loan_details__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;\n\n# Applicant table constraints\n\nalter table applicant add constraint fk_applicant__spouse_details_id foreign key (spouse_details_id) references spouse_details(id) on delete restrict on update restrict;\nalter table applicant add constraint fk_applicant__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;\nalter table applicant add constraint fk_applicant__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;\nalter table applicant add constraint fk_applicant__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;\nalter table applicant add constraint fk_applicant__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;\nalter table applicant add constraint fk_applicant__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;\nalter table applicant add constraint fk_applicant__employed_income_id foreign key (employed_income_id) references employed_income(id) on delete set null;\nalter table applicant add constraint fk_applicant__self_employed_income_id foreign key (self_employed_income_id) references self_employed_income(id) on delete set null;\n\n# Form table constraints\nalter table form add constraint uk_form__form_number unique index(form_number);\nalter table form add constraint fk_form__applicant_id foreign key (applicant_id) references applicant(id) on delete restrict on update restrict;\nalter table form add constraint fk_form__co_applicant_id foreign key (co_applicant_id) references applicant(id) on delete set null;\n\n# User table constraints\nalter table user add constraint uk_user_email unique index (email);\nalter table user add constraint uk_user_mobile unique index (mobile);\nalter table user add constraint fk_user__form_id foreign key(form_id) references form(id) on delete set null;\n\n# Dependent table constraints\nalter table dependent add constraint fk_dependent__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;\n\n# bank_account table constraints\nalter table bank_account add constraint fk_bank_account__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;\n\n# fixed_deposit table constraints\nalter table fixed_deposit add constraint fk_fixed_deposit__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;\n\n# insurance table constraints\nalter table insurance add constraint fk_insurance__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;\n\n# recurring_deposit table constraints\nalter table recurring_deposit add constraint fk_recurring_deposit__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;\n\n# misc_borrowing table constraints\nalter table misc_borrowing add constraint fk_misc_borrowing__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;\n\n# property table constraints\nalter table property add constraint fk_property__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;\n\n# nearby table constraints\nalter table nearby add constraint uk_nearby unique (requirement_id,location_near);\nalter table nearby add constraint fk_nearby__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;\n\n# external_amenity table constraints\nalter table external_amenity add constraint uk_external_amenity unique (requirement_id,external_amenity_name);\nalter table external_amenity add constraint fk_external_amenity__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;\n\n# internal_amenity table constraints\nalter table internal_amenity add constraint uk_internal_amenity unique (requirement_id,internal_amenity_name);\nalter table internal_amenity add constraint fk_internal_amenity__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;','SET FOREIGN_KEY_CHECKS=0;\ndrop table page;\ndrop table spouse_details;\ndrop table address;\ndrop table loan_details;\ndrop table employed_income;\ndrop table self_employed_income;\ndrop table applicant;\ndrop table form;\ndrop table user;\ndrop table dependent;\ndrop table bank_account;\ndrop table fixed_deposit;\ndrop table insurance;\ndrop table recurring_deposit;\ndrop table misc_borrowing;\ndrop table property;\ndrop table requirement;\ndrop table external_amenity;\ndrop table internal_amenity;\ndrop table nearby;\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(2,'08f1c39e6a8491745388754f733d4afa4f5d0c67','2017-11-27 06:07:17','alter table form modify form_number varchar(50);\nalter table form modify status varchar(50);\n\nalter table applicant modify phone1 varchar(30) not null;\nalter table applicant modify phone2 varchar(30);\nalter table applicant modify phone3 varchar(30);\nalter table applicant modify off_phone1 varchar(30) not null;\nalter table applicant modify off_phone2 varchar(30);\nalter table applicant modify off_phone3 varchar(30);\n\nalter table user add column last_login_ip varchar(20);','alter table form modify form_number varchar(50) not null;\nalter table form modify status varchar(50) not null;\n\nalter table applicant modify phone1 varchar(20) not null;\nalter table applicant modify phone2 varchar(20);\nalter table applicant modify phone3 varchar(20);\nalter table applicant modify off_phone1 varchar(20) not null;\nalter table applicant modify off_phone2 varchar(20);\nalter table applicant modify off_phone3 varchar(20);\n\nalter table user drop column last_login_ip;','applied',''),(3,'229642761956aae36aef7149c0efa4bd0fbc6be6','2017-11-27 06:07:21','alter table applicant modify column birth_place_id bigint;\nalter table applicant modify column office_address_id bigint;\n\nalter table applicant drop foreign key fk_applicant__office_address_id;\nalter table applicant add constraint fk_applicant__office_address_id foreign key (office_address_id) references address(id) on delete set null;\n\nalter table applicant drop foreign key fk_applicant__birth_place_id;\nalter table applicant add constraint fk_applicant__birth_place_id foreign key (birth_place_id) references address(id) on delete set null;','alter table applicant modify column birth_place_id bigint not null;\nalter table applicant modify column office_address_id bigint not null;\n\nalter table applicant drop foreign key fk_applicant__office_address_id;\nalter table applicant add constraint fk_applicant__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;\n\nalter table applicant drop foreign key fk_applicant__birth_place_id;\nalter table applicant add constraint fk_applicant__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;','applied',''),(4,'6073af76df8f9222959100e6ad4df49e44a7b718','2017-11-27 06:07:28','alter table bank_account modify column own_contribution_id bigint;\n\n#removing non-used columns from_property_table\nalter table property drop column principal;\nalter table property drop column interest_rate;\nalter table property drop column maturity_amount;\nalter table property drop column monthly_installments;\nalter table property drop column installments_balance;\nalter table property drop column loan_details_id;','alter table bank_account modify column own_contribution_id bigint not null;\n\n#readding non-used columns to_property_table\nalter table property add column principal integer;\nalter table property add column interest_rate decimal(38);\nalter table property add column maturity_amount integer not null;\nalter table property add column monthly_installments integer;\nalter table property add column installments_balance integer;\nalter table property add column loan_details_id bigint;','applied',''),(5,'1d2f90d663fb08a6fe41d7217b061aac4463f52b','2017-11-27 06:07:32','CREATE TABLE property_address (\nid            bigint(20) NOT NULL AUTO_INCREMENT,\nline1         varchar(100) NOT NULL,\ncity 				  varchar(100) NOT NULL,\ntaluka 			  varchar(100) DEFAULT NULL,\ndistrict   	  varchar(100) DEFAULT NULL,\nstate 			  varchar(100) NOT NULL,\ncountry 		  varchar(100) NOT NULL,\npin 				  varchar(50) NOT NULL,\nconstraint pk_property_address primary key (id)\n);\n\ncreate table invoice (\nid                   bigint(20) NOT NULL AUTO_INCREMENT,\ninvoice_number       varchar(50) NOT NULL,\ndescription          varchar(255),\namount               decimal(15, 2) NOT NULL,\npaid                 tinyint(1) default 0,\nuser_id              bigint(20),\nconstraint pk_invoice primary key(id)\n);\n\ncreate table payment (\nid                   bigint(20) NOT NULL AUTO_INCREMENT,\ninvoice_id			       bigint(20),\nstart_time		       datetime NOT NULL,\ncompletion_time      datetime,\nresponse_code		     char,\nresponse_message	   varchar(255),\nmerchant_txn_id      varchar(50),\nepg_txn_id		       varchar(15),\nauth_code			       varchar(6),\nprn                  varchar(12),\ncvresp_code		       varchar(1),\nprocessed            tinyint(1) default 0,\namount               decimal(15, 2) NOT NULL,\nredirect_url         varchar(1024),\nconstraint pk_payment primary key(id)\n);\n\nalter table property add constraint fk_property__location_id foreign key (location_id) references property_address(id) on delete set null;\n\nalter table invoice add constraint fk_invoice__user_id foreign key(user_id) references user(id) on delete set null on update set null;\nalter table invoice add constraint uk_invoice__invoice_number unique index(invoice_number);\n\nalter table payment add constraint fk_payment__invoice_id foreign key(invoice_id) references invoice(id) on delete set null on update set null;\nalter table payment add constraint uk_payment__merchant_txn_id unique index(merchant_txn_id);\nalter table payment add constraint uk_payment__epg_txn_id unique index(epg_txn_id);\ncreate index ix_payment__response_code on payment(response_code);','alter table property drop foreign key fk_property__location_id;\n\ndrop table property_address;\ndrop table invoice;\ndrop table payment;','applied',''),(6,'7d1841caf8f9e525a464bcf6efa9a9d16a1cbb6c','2017-11-27 06:07:33','ALTER TABLE requirement CHANGE type requirement_type varchar(30);','ALTER TABLE requirement CHANGE requirement_type type varchar(255);','applied',''),(7,'f73dfe02d9c5632d40f06037851391c099e1cec8','2017-11-27 06:07:34','alter table requirement add column form_id bigint not null;\n\n# requirement table constraints\nalter table requirement add constraint fk_requirement__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;','alter table requirement drop foreign key fk_requirement__form_id;\nalter table requirement drop column form_id;','applied',''),(8,'8b73ab7b2b019c1e64ab5a5d24f5bb2fc69deadb','2017-11-27 06:07:36','alter table loan_details add column amount_per_month_from_sal tinyint(1) default 0;\nalter table loan_details add column loan_shown_on_bs tinyint(1) default 0;','alter table loan_details drop column amount_per_month_from_sal;\nalter table loan_details drop column loan_shown_on_bs;','applied',''),(9,'c1399bc48a861f75d49a7ed510afafd551d4f161','2017-11-27 06:07:37','alter table form add column co_applicant_exists tinyint(1) default 0;','alter table form drop column co_applicant_exists;','applied',''),(10,'d7868d352f5b788953ca29c955f87494e21c6cd8','2017-11-27 06:07:40','create table summary (\nid                        bigint auto_increment not null,\ni_accpt_abv_cal                  tinyint(1) default 0,\napprox_loan_eli                 tinyint(1) default 0,\ncash_balance                   tinyint(1) default 0,\nall_fd_summ_amnt                 tinyint(1) default 0,\nall_rd_summ_amnt                 tinyint(1) default 0,\nall_insurance_summ_amnt          tinyint(1) default 0,\nselling_summ_amnt               tinyint(1) default 0,\nborrowed_summary               tinyint(1) default 0,\ndonation_summary               tinyint(1) default 0,\nreturned_amount                tinyint(1) default 0,\nconstraint pk_summary primary key (id)\n);\n\nalter table form add column summary_id bigint;\nalter table form add constraint fk_form__summary_id foreign key (summary_id) references summary(id) on delete set null;','alter table form drop foreign key fk_form__summary_id;\nalter table form drop column summary_id bigint;\n\ndrop table summary;','applied',''),(11,'9dedf62cdd38a83a036b395618250debd3228fa7','2017-11-27 06:07:41','ALTER TABLE summary MODIFY i_accpt_abv_cal char not null;','ALTER TABLE summary MODIFY i_accpt_abv_cal tinyint(1) default 0;','applied',''),(12,'4c13d41059113921abbfd8d510336210bc82ac76','2017-11-27 06:07:41','alter table summary add column all_bank_bal_summ_amnt tinyint(1) default 0;','alter table summary drop column all_bank_bal_summ_amnt;','applied',''),(13,'b91c1309effddfd240ad0f530cf26b1879f19994','2017-11-27 06:07:44','alter table payment drop column amount;\n\ncreate table cash_with_you (\nid                        bigint auto_increment not null,\ncash_with_you                   bigint not null,\nform_id                   bigint not null,\nconstraint pk_cash_with_you primary key (id)\n);\n\n# cash_with_you table constraints\nalter table cash_with_you add constraint fk_cash_with_you__form_id foreign key (form_id) references form(id) on delete cascade on update cascade;','alter table payment add column amount decimal(15, 2) NOT NULL;\nalter table cash_with_you drop foreign key fk_cash_with_you__form_id;\ndrop table cash_with_you;','applied',''),(14,'d3b30a4fb7dec8118904f8359275c87b6b6d1694','2017-11-27 06:07:45','alter table applicant drop column nationality;','alter table applicant add column nationality varchar(50) not null;','applied',''),(15,'d01f1f4a9dea41118fefe7f94f3cc878656eb4f7','2017-11-27 06:07:50','alter table payment drop column processed;\nalter table payment add column client_processed tinyint(1) default 0;\nalter table payment add column gateway_processed tinyint(1) default 0;\nalter table payment add column success tinyint(1) default 0;','alter table applicant add column nationality varchar(50) not null;\nalter table payment add column processed tinyint(1) default 0;\nalter table payment drop column client_processed;\nalter table payment drop column gateway_processed;\nalter table payment drop column success;','applied',''),(16,'dfcce7b00ffc2fa985b8ccc8aee93a09c11d8920','2017-11-27 06:08:01','alter table loan_details drop foreign key fk_loan_details__applicant_id;\nalter table dependent drop foreign key fk_dependent__applicant_id;\n\nALTER TABLE loan_details CHANGE applicant_id form_id bigint;\nALTER TABLE dependent CHANGE applicant_id form_id bigint;\n\nalter table loan_details add constraint fk_loan_details__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;\nalter table dependent add constraint fk_dependent__form_id foreign key(form_id) references form(id) on delete cascade on update cascade;\n\nalter table dependent add column is_applicant tinyint(1) default 0;\nalter table loan_details add column is_applicant tinyint(1) default 0;\n\n# Adding column for storing authentication code hash.\nalter table user add column authcode_hash varchar(255);\n\ncreate table agent(\n# Agent name and details.\nid                        bigint auto_increment not null,\ntitle                     varchar(4) not null,\nfname                     varchar(50) not null,\nmname                     varchar(50) not null,\nlname                     varchar(50),\ndob                       datetime not null,\nemail                     varchar(100) not null,\nsex                       char not null,\nidentity_number1          varchar(50) not null,\nidentity_number1_type     varchar(20) not null,\nidentity_number2          varchar(50),\nidentity_number2_type     varchar(20),\nidentity_number3          varchar(50),\nidentity_number3_type     varchar(20),\n\nmarital_status            char not null,\nnationality               varchar(50) not null,\n\npermanent_address_same_as varchar(20) not null,\nresidential_address_same_as varchar(20) not null,\ncommunication_address_same_as varchar(20) not null,\n\nbirth_place_id            bigint not null,\npermanent_address_id      bigint,\nresidential_address_id    bigint,\ncommunication_address_id  bigint,\nresi_status               varchar(50) not null,\nresidence_yrs_residing    integer not null,\ncity_yrs_residing         integer not null,\nphone1_type               varchar(20) not null,\nphone1                    varchar(20) not null,\nphone2_type               varchar(20),\nphone2                    varchar(20),\nphone3_type               varchar(20),\nphone3                    varchar(20),\n\noffice_address_id         bigint not null,\noff_mail                  varchar(100),\nemployer_web              varchar(100),\noff_phone1                varchar(20) not null,\noff_phone1_type           varchar(20) not null,\noff_phone2                varchar(20),\noff_phone2_type           varchar(20),\noff_phone3                varchar(20),\noff_phone3_type           varchar(20),\n\n\n#Agent specific\noccupation                varchar(200) not null,\nqualification             varchar(200) not null,\nagent_type                varchar(1) not null,\ncompany_name              varchar(200) not null,\nis_income_tax_payee       char not null,\npan_card_number           varchar(10) not null,\nonline_monatory_transaction_facility char not null,\n\n#Agent Bank Details\nbank_name				varchar(300) not null,\nbranch_name				varchar(300) not null,\ncard_type				char not null,\ncard_number				varchar(20),\ncard_expiry_date		datetime not null,\namount_authorised_currency varchar(4) not null,\namount_authorised		bigint not null,\n\nany_criminal_record		char not null,\n\nconstraint pk_agent primary key (id)\n);\n\ncreate table language_proficiency(\n\nid                      bigint auto_increment not null,\nname                    varchar(100) not null,\ncan_read                    char not null,\ncan_write                   char not null,\ncan_speak                   char not null,\nagent_id                bigint not null,\nconstraint pk_language_proficiency primary key(id)\n);\n\n# Agent table constraints\n\nalter table agent add constraint fk_agent__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;\n\n# language_proficiency table constraints\nalter table language_proficiency add constraint fk_agent_id foreign key (agent_id)  references  agent(id) on delete restrict on update restrict;\n\n# Adding column for experience_month,experience_year,yrs_with_employer\nalter table employed_income add column experience_year int ;\nalter table employed_income add column experience_month int;\nalter table employed_income add column yrs_with_employer_d datetime;\nalter table employed_income drop column experience;\nalter table employed_income drop column yrs_with_employer;','SET FOREIGN_KEY_CHECKS=0;\n\nalter table loan_details drop column is_applicant;\nalter table dependent drop column is_applicant;\n\nalter table loan_details drop foreign key fk_loan_details__form_id;\nalter table dependent drop foreign key fk_dependent__form_id;\n\nALTER TABLE loan_details CHANGE form_id applicant_id bigint;\nALTER TABLE dependent CHANGE form_id applicant_id bigint;\n\nalter table loan_details add constraint fk_loan_details__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;\nalter table dependent add constraint fk_dependent__applicant_id foreign key(applicant_id) references applicant(id) on delete cascade on update cascade;\n\nalter table user drop column authcode_hash;\n\ndrop table if exists agent;\ndrop table if exists language_proficiency;\n\n\nalter table employed_income drop column experience_year;\nalter table employed_income drop column experience_month;\nalter table employed_income drop column yrs_with_employer_d;\nalter table employed_income add column yrs_with_employer int not null;\nalter table employed_income add column experience bigint not null;\n\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(17,'ce3eb90e0c3d29329bc07ae1dd0fd5bd66d4f6a1','2017-11-27 06:08:02','#Adding Relationship With Applicant\nalter table applicant add column relationship_with_applicant varchar(100);','alter table applicant drop column relationship_with_applicant;','applied',''),(18,'7c99e606eb743dd163dbbd27c5d23f8874717f7c','2017-11-27 06:08:18','create table attachment (\nid				bigint auto_increment not null,\nfile_name		varchar(255),\ncontent_type 	varchar(100),\nupload_time		datetime,\nfile_path		varchar(512) not null,\nuser_id			bigint not null,\nconstraint pk_attachment primary key(id)\n);\n\nalter table attachment add constraint fk_attachment__user_id foreign key(user_id) references user(id) on delete cascade on update cascade;\n\nalter table applicant add column photograph_id bigint;\n\nalter table applicant add constraint fk_applicant__photograph_id foreign key(photograph_id) references attachment(id) on delete set null on update set null;\n\n\n\nalter table applicant add column identity1_id bigint;\n\nalter table applicant add constraint fk_applicant__identity1_id foreign key(identity1_id) references attachment(id) on delete set null on update set null;\n\nalter table applicant add column identity2_id bigint;\n\nalter table applicant add constraint fk_applicant__identity2_id foreign key(identity2_id) references attachment(id) on delete set null on update set null;\n\nalter table applicant add column identity3_id bigint;\n\nalter table applicant add constraint fk_applicant__identity3_id foreign key(identity3_id) references attachment(id) on delete set null on update set null;\n\nalter table applicant add column addressproof_id bigint;\n\nalter table applicant add constraint fk_applicant__addressproof_id foreign key(addressproof_id) references attachment(id) on delete set null on update set null;','SET FOREIGN_KEY_CHECKS=0;\n\ndrop table if exists attachment;\nalter table applicant drop foreign key fk_applicant__photograph_id;\nalter table applicant drop column photograph_id;\nalter table applicant drop foreign key fk_applicant__identity1_id;\nalter table applicant drop column identity1_id;\nalter table applicant drop foreign key fk_applicant__identity2_id;\nalter table applicant drop column identity2_id;\nalter table applicant drop foreign key fk_applicant__identity3_id;\nalter table applicant drop column identity3_id;\nalter table applicant drop foreign key fk_applicant__addressproof_id;\nalter table applicant drop column addressproof_id;\n\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(19,'e667e09be1e14d708303a3ea831c8b47627f6ed8','2017-11-27 06:08:20','create table payment_info(\nid 				bigint auto_increment not null,\npayment_mode	varchar(100) not null,\nbank_name		varchar(100) not null,\nbranch_name		varchar(100) not null,\npayment_date 	datetime 	not null,\ncheque_number	bigint not null,\ntransaction_id	varchar(100) not null,\naccount_number	varchar(100) not null,\nreceipt_number	varchar(100) not null,\nform_id    bigint ,\nconstraint pk_payment_info primary key (id)\n);\n\ncreate table payment_code(\nid 				    bigint auto_increment not null,\ncode                varchar(50) not null,\nname                varchar(100) not null,\nemail               varchar(100) not null,\nmobile              varchar(50) not null,\namount              bigint not null,\ngeneration_time 	datetime not null,\ninvalidate_time 	datetime,\nused			    tinyint(1) default 0,\nform_id             bigint,\nconstraint pk_payment_code primary key (id)\n);\n\ncreate table staff_user(\nid 				    bigint auto_increment not null,\nname                varchar(100) not null,\nemail                varchar(100) not null,\nrole                varchar(50) not null,\npassword            varchar(255),\nuser_id				varchar(50) not null,\nlast_login			datetime,\nlast_login_ip		varchar(20),\nconstraint pk_staff_user primary key (id)\n);\n\nalter table payment_info add constraint fk_form_id  foreign key (form_id) references form(id) on delete cascade on update cascade;\nalter table payment_code add constraint fk_payment_code__form_id foreign key (form_id) references form(id) on delete set null on update set null;\nalter table payment_code add constraint uk_payment_code__code unique index(code);\n\nalter table staff_user add constraint uk_staff_user__user_id unique index(user_id);','SET FOREIGN_KEY_CHECKS=0;\ndrop table payment_info;\ndrop table payment_code;\ndrop table staff_user;\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(20,'c30eb0a9158062944d94b07567b2430eb98fe03d','2017-11-27 06:08:34','SET FOREIGN_KEY_CHECKS=0;\ndrop table if exists agent;\ndrop table if exists language_proficiency;\nSET FOREIGN_KEY_CHECKS=1;\n\ncreate table language_proficiency(\n\nid                      bigint auto_increment not null,\nname                    varchar(100) not null,\ncan_read                    tinyint(1)  ,\ncan_write                  tinyint(1)  ,\ncan_speak                  tinyint(1)  ,\nconstraint pk_language_proficiency primary key(id)\n);\n\ncreate table agent(\n# Agent name and details.\nid                        bigint auto_increment not null,\ntitle                     varchar(4) not null,\nfname                     varchar(50) not null,\nmname                     varchar(50) not null,\nlname                     varchar(50),\ndob                       datetime not null,\nemail                     varchar(100) not null,\nsex                       char not null,\nfh_title                  varchar(4) not null,\nfh_fname                  varchar(50) not null,\nfh_mname                  varchar(50) not null,\nfh_lname                  varchar(50),\nfh_relation               varchar(50) not null,\n\nidentity_number1          varchar(50) ,\nidentity_number1_type     varchar(20) ,\nidentity_number2          varchar(50),\nidentity_number2_type     varchar(20),\nidentity_number3          varchar(50),\nidentity_number3_type     varchar(20),\n\nphone1_type               varchar(20) not null,\nphone1                    varchar(20) not null,\nphone2_type               varchar(20),\nphone2                    varchar(20),\nphone3_type               varchar(20),\nphone3                    varchar(20),\n\nmarital_status            char not null,\nnationality               varchar(50) not null,\n\n\n#Agent Address Details\npermanent_address_same_as varchar(20) not null,\nresidential_address_same_as varchar(20) not null,\ncommunication_address_same_as varchar(20) not null,\n\nbirth_place_id            bigint not null,\npermanent_address_id      bigint,\nresidential_address_id    bigint,\ncommunication_address_id  bigint,\n\n#Agent - Residential Status\nresi_status               varchar(50) not null,\nresidence_yrs_residing    integer not null,\ncity_yrs_residing         integer not null,\nresi_phone                varchar(20) not null,\ntype_resi_phone           varchar(20) not null,\n\n\n#Agent Office Address Details\noffice_address_id         bigint not null,\noff_mail                  varchar(100) not null,\nwebsite_addres            varchar(100) not null,\noff_fax                   varchar(100) not null,\noff_phone1                varchar(20) not null,\noff_phone1_type           varchar(20) not null,\noff_phone2                varchar(20) not null,\noff_phone2_type           varchar(20) not null,\noff_phone3                varchar(20) not null,\noff_phone3_type           varchar(20) not null,\n\n#Agent Language Proficiency(fk)\nhindi_id  bigint not null,\nmarathi_id  bigint not null,\nenglish_id   bigint not null,\n\n#Agent Bank Details\nbank_name				           varchar(300) not null,\nbranch_name			           varchar(300) not null,\nbank_account               bigint not null,\n\n#Status of Agent\nagent_type                varchar(100) not null,\ncompany_name              varchar(200) not null,\noccupation                varchar(200) not null,\nqualification             varchar(200) not null,\nother_qualification       varchar(100) ,\nis_income_tax_payee       tinyint(1) not null,\npan_card_number           varchar(10) not null,\nonline_monatory_transaction_facility  tinyint(1) not null,\n\n#Form Place And Day\nform_filled_place         varchar(200) ,\nform_filled_day           varchar(200) ,\n\nconstraint pk_agent primary key (id)\n);\n\n# Agent table constraints\n\nalter table agent add constraint fk_agent__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;\nalter table agent add constraint fk_hindi_language_proficiency_id foreign key (hindi_id) references language_proficiency(id) on delete restrict on update restrict;\nalter table agent add constraint fk_marathi_language_proficiency_id foreign key (marathi_id) references language_proficiency(id) on delete restrict on update restrict;\nalter table agent add constraint fk_english_language_proficiency_id foreign key (english_id) references language_proficiency(id) on delete restrict on update restrict;\n\n# Increasing size of last_login_ip. We are potentially retrieving IPv6 address which is longer.\nalter table user modify last_login_ip varchar(100);','SET FOREIGN_KEY_CHECKS=0;\ndrop table if exists agent;\ndrop table if exists language_proficiency;\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(21,'072b8a74694231c8f07891d4aa99ec551d70777e','2017-11-27 06:08:55','SET FOREIGN_KEY_CHECKS=0;\ndrop table if exists agent;\ndrop table if exists language_proficiency;\nSET FOREIGN_KEY_CHECKS=1;\n\ncreate table language_proficiency(\n\nid                      bigint auto_increment not null,\nname                    varchar(100) not null,\ncan_read                   varchar(10) ,\ncan_write                  varchar(10),\ncan_speak                 varchar(10) ,\nconstraint pk_language_proficiency primary key(id)\n);\n\ncreate table agent(\n# Agent name and details.\nid                        bigint auto_increment not null,\ntitle                     varchar(4) not null,\nfname                     varchar(50) not null,\nmname                     varchar(50) not null,\nlname                     varchar(50),\ndob                       datetime not null,\nemail                     varchar(100) not null,\nsex                       char not null,\nfh_title                  varchar(4) not null,\nfh_fname                  varchar(50) not null,\nfh_mname                  varchar(50) not null,\nfh_lname                  varchar(50),\nfh_relation               varchar(50) not null,\n\nidentity_number1          varchar(50) ,\nidentity_number1_type     varchar(20) ,\nidentity_number2          varchar(50),\nidentity_number2_type     varchar(20),\nidentity_number3          varchar(50),\nidentity_number3_type     varchar(20),\n\nphone1_type               varchar(20) not null,\nphone1                    varchar(20) not null,\nphone2_type               varchar(20),\nphone2                    varchar(20),\nphone3_type               varchar(20),\nphone3                    varchar(20),\n\nmarital_status            char not null,\nnationality               varchar(50) not null,\n\n\n#Agent Address Details\npermanent_address_same_as varchar(20) not null,\nresidential_address_same_as varchar(20) not null,\ncommunication_address_same_as varchar(20) not null,\n\nbirth_place_id            bigint not null,\npermanent_address_id      bigint,\nresidential_address_id    bigint,\ncommunication_address_id  bigint,\n\n#Agent - Residential Status\nresi_status               varchar(50) not null,\nresidence_yrs_residing    integer not null,\ncity_yrs_residing         integer not null,\nresi_phone                varchar(20) not null,\ntype_resi_phone           varchar(20) not null,\n\n\n#Agent Office Address Details\noffice_address_id         bigint not null,\noff_mail                  varchar(100) not null,\nwebsite_addres            varchar(100) not null,\noff_fax                   varchar(100) not null,\noff_phone1                varchar(20) not null,\noff_phone1_type           varchar(20) not null,\noff_phone2                varchar(20) not null,\noff_phone2_type           varchar(20) not null,\noff_phone3                varchar(20) not null,\noff_phone3_type           varchar(20) not null,\n\n#Agent Language Proficiency(fk)\nhindi_id  bigint not null,\nmarathi_id  bigint not null,\nenglish_id   bigint not null,\n\n#Agent Bank Details\nbank_name				           varchar(300) not null,\nbranch_name			           varchar(300) not null,\nbank_account               bigint not null,\n\n#Status of Agent\nagent_type                varchar(100) not null,\ncompany_name              varchar(200) not null,\noccupation                varchar(200) not null,\nqualification             varchar(200) not null,\nother_qualification       varchar(100) ,\nis_income_tax_payee       tinyint(1) not null,\npan_card_number           varchar(20) not null,\nonline_monatory_transaction_facility  tinyint(1) not null,\n\n#Form Place And Day\nform_filled_place         varchar(200) ,\nform_filled_day           varchar(200) ,\n\n#attachment File\nphotograph_id				bigint not null ,\nidentity2_id 				bigint not null,\nidentity1_id 				bigint not null,\nidentity3_id 				bigint not null,\naddressproof_id			bigint not null,\nbirth_certificate_id		bigint not null,\noffice_addressproof_id	bigint not null,\n\n#Code Verification\nverification_code			varchar(100) ,\nuser_id					bigint not null,\n\nconstraint pk_agent primary key (id)\n);\n\n# Agent table constraints\n\nalter table agent add constraint fk_agent__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;\nalter table agent add constraint fk_agent__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_hindi_language_proficiency_id foreign key (hindi_id) references language_proficiency(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_marathi_language_proficiency_id foreign key (marathi_id) references language_proficiency(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_english_language_proficiency_id foreign key (english_id) references language_proficiency(id) on delete restrict on update restrict;\n\n# File Attachment\nalter table agent add constraint fk_agent_photograph_id	foreign key (photograph_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_identity1_id    foreign key (identity1_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_identity2_id    foreign key (identity2_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_identity3_id    foreign key (identity3_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_addressproof_id    foreign key (addressproof_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_birth_certificate_id    foreign key (birth_certificate_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_office_addressproof_id    foreign key   (office_addressproof_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_user_id   foreign key   (user_id ) references user(id) on delete restrict on update restrict;','SET FOREIGN_KEY_CHECKS=0;\ndrop table if exists agent;\ndrop table if exists language_proficiency;\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(22,'67459c6440cc6b27cedd82d59583baa711e8e559','2017-11-27 06:09:57','#Agent Form\nalter table agent drop column resi_phone;\nalter table agent drop column type_resi_phone;\nalter table agent add column nri_address_id bigint;\nalter table agent add column office_address_same_as varchar(20) not null;\nalter table agent add constraint fk_agent_nri_address_id foreign key (nri_address_id) references address(id)  on delete set null;\nalter table agent modify column  office_address_id bigint;\nalter table agent modify column  off_mail  			varchar(100);\nalter table agent modify column website_addres 	 	varchar(100) ;\nalter table agent modify column  off_fax       		varchar(100) ;\nalter table agent modify column  off_phone1      	varchar(20) ;\nalter table agent modify column off_phone1_type  	varchar(20);\nalter table agent modify column off_phone2        	varchar(20);\nalter table agent modify column  off_phone2_type    varchar(20) ;\nalter table agent modify column off_phone3         varchar(20) ;\nalter table agent modify column off_phone3_type  	varchar(20) ;\nalter table agent modify column identity2_id  	bigint ;\nalter table agent modify column identity3_id  	bigint ;\n\n#Attachment Ids\nalter table agent add column resi_light_bill_id  bigint;\nalter table agent add column resi_tax_receipt_id  bigint;\nalter table agent add column office_light_bill_id  bigint;\nalter table agent modify column office_addressproof_id bigint;\n\nalter table agent add constraint fk_agent_resi_light_bill_id	foreign key (resi_light_bill_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_resi_tax_receipt_id	foreign key (resi_tax_receipt_id) references attachment(id) on delete restrict on update restrict;\nalter table agent add constraint fk_agent_office_light_bill_id	foreign key (office_light_bill_id) references attachment(id) on delete restrict on update restrict;\n\n# Issue #101 Adding multiple roles for the user.\ncreate table role (\nid                      bigint auto_increment not null,\nuser_id                 bigint not null,\nrole                    varchar(50) not null,\nconstraint pk_role primary key(id)\n);\n\nalter table role add constraint fk_role__user_id foreign key (user_id) references user(id) on delete cascade on update cascade;\nalter table role add constraint uk_role__user_id__role unique (user_id, role);\ninsert into role(user_id, role) select id, role from user;\n\n\n# Issue #101 removing not null constraint on the role column. It maybe safe to delete, but lets keep it there for now.\nalter table user modify column role varchar(20);\n\n# Issue 99 Payment schema changes for CCAvenue payment gateway.\n\nalter table payment add column auth_desc varchar(10);\nalter table payment add column transaction_id varchar(50);\n\nalter table payment drop column response_code;\nalter table payment drop column response_message;\nalter table payment drop column merchant_txn_id;\nalter table payment drop column epg_txn_id;\nalter table payment drop column auth_code;\nalter table payment drop column prn;\nalter table payment drop column cvresp_code;\n\nselect id from user;','#agent Form\n\nSET FOREIGN_KEY_CHECKS=0;\n\nalter table agent add column resi_phone    varchar(20) not null;\nalter table agent add column type_resi_phone  varchar(20) not null;\nalter table agent drop foreign key fk_agent_nri_address_id;\nalter table agent drop column nri_address_id;\nalter table agent drop column office_address_same_as;\nalter table agent modify column office_address_id bigint;\nalter table agent modify column off_mail varchar(100) not null;\nalter table agent modify column website_addres 	 	varchar(100) not null;\nalter table agent modify column off_fax varchar(100) not null;\nalter table agent modify column off_phone1      	varchar(20) not null;\nalter table agent modify column off_phone1_type  	varchar(20) not null;\nalter table agent modify column off_phone2        	varchar(20) not null;\nalter table agent modify column off_phone2_type    varchar(20) not null;\nalter table agent modify column off_phone3         varchar(20) not null;\nalter table agent modify column off_phone3_type  	varchar(20) not null;\nalter table agent modify column identity2_id  	bigint not null ;\nalter table agent modify column identity3_id  	bigint not null;\n\nalter table agent drop foreign key fk_agent_resi_light_bill_id ;\nalter table agent drop foreign key fk_agent_resi_tax_receipt_id;\nalter table agent drop foreign key fk_agent_office_light_bill_id;\nalter table agent drop column resi_light_bill_id;\nalter table agent drop column resi_tax_receipt_id;\nalter table agent drop column office_light_bill_id;\nalter table agent modify column office_addressproof_id bigint not null;\n\ndrop table if exists role;\nalter table user modify column role varchar(20) not null;\n\nSET FOREIGN_KEY_CHECKS=1;\n\n# Issue 99\nalter table payment add column response_code char;\nalter table payment add column response_message varchar(255);\nalter table payment add column merchant_txn_id varchar(50);\nalter table payment add column epg_txn_id varchar(15);\nalter table payment add column auth_code varchar(6);\nalter table payment add column prn varchar(12);\nalter table payment add column cvresp_code varchar(1);\n\nalter table payment drop column auth_desc;\nalter table payment drop column transaction_id;','applied',''),(23,'c4f7b05ecfe032cfd8dd78de718f0d35aa00e390','2017-11-27 06:10:05','alter table agent  add column status varchar(50);\nrename table agent to agent_form;\n\n# Issue #110  refactoring agent\ncreate table agent(\nid              bigint auto_increment not null,\nuser_id         bigint not null ,\nform_id         bigint ,\ntotal_amount_paid bigint ,\nform_credits  bigint ,\ntotal_form_filled_count  bigint ,\nprimary key(id)\n);\nalter table agent add constraint fk_agent__user_id foreign key (user_id) references user(id) on delete cascade on update cascade;\nalter table agent add constraint fk_agent_form_id foreign key (form_id)	 references agent_form(id) on delete cascade on update cascade;\n\nalter table requirement add column  requirement_sub_type varchar(255);\n\ncreate table requirement_address (\nid                        			bigint auto_increment not null,\nrequirement_id                      bigint not null,\ncity                      			varchar(255),\ntaluka                      		varchar(255),\ndistrict                      		varchar(255),\nstate                      			varchar(255),\ncountry                      		varchar(255),\nvillage_or_detail_location          varchar(255),\nconstraint pk_requirement_address primary key (id)\n);\n\n# requirement_address table constraints\nalter table requirement_address add constraint fk_requirement_address__requirement_id foreign key (requirement_id) references requirement(id) on delete cascade on update cascade;','SET FOREIGN_KEY_CHECKS=0;\ndrop table agent;\nrename table agent_form to agent;\nalter table agent drop column status;\n\nalter table requirement drop column  requirement_sub_type;\ndrop table requirement_address;\nSET FOREIGN_KEY_CHECKS=0;\n\n\n# Note ---- Have to drop column user_id from agent_form','applied',''),(24,'f1f783d635aa28beba114639a931ad8e68257949','2017-11-27 06:10:48','SET FOREIGN_KEY_CHECKS=0;\n\ndelete from address where id in (\nselect office_address_id from agent_form  where office_address_id is not null\nunion\nselect communication_address_id from agent_form  where communication_address_id is not null\nunion\nselect birth_place_id from agent_form  where birth_place_id is not null\nunion\nselect residential_address_id from agent_form  where residential_address_id is not null\nunion\nselect  permanent_address_id from agent_form  where  permanent_address_id is not null\n\n);\ntruncate table agent ;\ntruncate table agent_form ;\n\nSET FOREIGN_KEY_CHECKS=1;\n\n#Issue116\nalter table applicant modify column  identity_number1 varchar(50);\nalter table applicant modify column identity_number1_type varchar(20) ;\nalter table applicant modify column permanent_address_same_as varchar(20);\nalter table applicant modify column  residential_address_same_as varchar(20);\nalter table applicant modify column  communication_address_same_as varchar(20);\nalter table applicant modify column  resi_status varchar(50);\nalter table applicant modify column  residence_yrs_residing integer;\nalter table applicant modify column city_yrs_residing integer;\nalter table applicant modify column  off_phone1 varchar(30);\nalter table applicant modify column  off_phone1_type varchar(30);\nalter table applicant modify column  occupation varchar(50);\n\nalter table self_employed_income modify column business_type varchar(50);\nalter table self_employed_income modify column yr1_pat varchar(20);\nalter table self_employed_income modify column yr1_depr varchar(20);\n\nalter table employed_income modify column  employer varchar(100) ;\nalter table employed_income modify column designation varchar(50);\nalter table employed_income modify column gross_sal bigint(20);\nalter table employed_income modify column  net_sal bigint(20);\nalter table employed_income modify column  yrs_service_remn int;\n\nalter table spouse_details modify column sp_title varchar(4);\nalter table spouse_details modify column sp_fname varchar(50);\nalter table spouse_details modify column sp_mname varchar(50);\nalter table spouse_details modify column sp_profession varchar(50);\nalter table spouse_details modify column  sp_dob datetime ;\nalter table spouse_details modify column anniversary datetime ;\n\n#Adress to AgentAddress\n\n\n\ncreate table agent_address (\nid                        bigint auto_increment not null,\nline1                     varchar(100) not null,\nline2                     varchar(100),\nstreet                    varchar(100),\narea                      varchar(100) not null,\ntaluka                    varchar(100),\ncity                      varchar(100) not null,\ndistrict                  varchar(100),\nstate                     varchar(100) not null,\ncountry                   varchar(100) not null,\npin                       varchar(50) not null,\nconstraint pk_address primary key (id)\n);\n\nalter table agent_form drop foreign key   fk_agent__birth_place_id ;\nalter table agent_form drop foreign key   fk_agent__residential_address_id ;\nalter table agent_form drop foreign key   fk_agent__permanent_address_id ;\nalter table agent_form drop foreign key   fk_agent__communication_address_id ;\nalter table agent_form drop foreign key   fk_agent__office_address_id ;\n\nalter table agent_form add constraint fk_agent__birth_place_id foreign key (birth_place_id) references agent_address(id) on delete restrict on update restrict;\nalter table agent_form add constraint fk_agent__residential_address_id foreign key (residential_address_id) references agent_address(id) on delete set null;\nalter table agent_form add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references agent_address(id) on delete set null;\nalter table agent_form add constraint fk_agent__communication_address_id foreign key (communication_address_id) references agent_address(id) on delete set null;\nalter table agent_form add constraint fk_agent__office_address_id foreign key (office_address_id) references agent_address(id) on delete restrict on update restrict;\n\nupdate loan_details set amount_per_month_from_sal = 1 where loan_shown_on_bs = 1;\n\n# Settings form\'s status as \"Paid\" for forms of users who have already paid the form fee.\nupdate form set status = \'Paid\' where id in (select form_id from user where id in (select distinct user_id from invoice where paid = 1));','SET FOREIGN_KEY_CHECKS=0;\n\nalter table applicant modify column  identity_number1 varchar(50) not null  ;\nalter table applicant modify column identity_number1_type varchar(20)  not null ;\nalter table applicant modify column permanent_address_same_as varchar(20) not null ;\nalter table applicant modify column  residential_address_same_as varchar(20) not null ;\nalter table applicant modify column  communication_address_same_as varchar(20) not null ;\nalter table applicant modify column  resi_status varchar(50) not null ;\nalter table applicant modify column  residence_yrs_residing integer not null ;\nalter table applicant modify column city_yrs_residing integer not null ;\nalter table applicant modify column  off_phone1 varchar(30) not null ;\nalter table applicant modify column  off_phone1_type varchar(30) not null ;\nalter table applicant modify column  occupation varchar(50) not null ;\n\nalter table self_employed_income modify column business_type varchar(50) not null ;\nalter table self_employed_income modify column yr1_pat varchar(20) not null ;\nalter table self_employed_income modify column yr1_depr varchar(20) not null ;\n\nalter table employed_income modify column  employer varchar(100)  not null ;\nalter table employed_income modify column designation varchar(50) not null ;\nalter table employed_income modify column gross_sal bigint(20) not null ;\nalter table employed_income modify column  net_sal bigint(20) not null ;\nalter table employed_income modify column  yrs_service_remn int not null ;\n\n\nalter table spouse_details modify column sp_title varchar(4) not null ;\nalter table spouse_details modify column sp_fname varchar(50) not null ;\nalter table spouse_details modify column sp_mname varchar(50) not null ;\nalter table spouse_details modify column sp_profession varchar(50) not null ;\nalter table spouse_details modify column  sp_dob datetime  not null ;\nalter table spouse_details modify column anniversary datetime  not null ;\n\n# Agent table constraints\n\nalter table agent_form drop foreign key   fk_agent__birth_place_id ;\nalter table agent_form drop foreign key   fk_agent__residential_address_id ;\nalter table agent_form drop foreign key   fk_agent__permanent_address_id ;\nalter table agent_form drop foreign key   fk_agent__communication_address_id ;\nalter table agent_form drop foreign key   fk_agent__office_address_id ;\n\nalter table agent_form add constraint fk_agent__birth_place_id foreign key (birth_place_id) references address(id) on delete restrict on update restrict;\nalter table agent_form add constraint fk_agent__residential_address_id foreign key (residential_address_id) references address(id) on delete set null;\nalter table agent_form add constraint fk_agent__permanent_address_id foreign key (permanent_address_id) references address(id) on delete set null;\nalter table agent_form add constraint fk_agent__communication_address_id foreign key (communication_address_id) references address(id) on delete set null;\nalter table agent_form add constraint fk_agent__office_address_id foreign key (office_address_id) references address(id) on delete restrict on update restrict;\n\ndrop table agent_address;\nupdate loan_details set amount_per_month_from_sal = 0 where  loan_shown_on_bs = 1;\n\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(25,'8a50fe3f7789cc5bdbcc35ade5dbe7d57c0cbba3','2017-11-27 06:10:59','# Scheduler\nalter table form add column filled_date datetime ;\nalter table form add column completed_date datetime ;\n\n#form - invoice relation\nalter table form add column invoice_id bigint ;\nalter table form add constraint fk_form_invoice_id foreign key (invoice_id) references invoice(id) on delete set null ;\n\nupdate form join user, invoice set form.invoice_id = invoice.id where form.id = user.form_id and invoice.user_id = user.id;\n\nalter table form add column suggested_budget integer;\nalter table form add column total_cash integer;\nalter table form add column personal_contribution integer;\nalter table form add column cash_with_you integer;\nalter table form add column calculated_budget integer;\nalter table property add column property_desc varchar(80);','SET FOREIGN_KEY_CHECKS = 0;\n\nalter table property drop column property_desc;\nalter table form drop column calculated_budget;\nalter table form drop column cash_with_you;\nalter table form drop column personal_contribution ;\nalter table form drop column total_cash;\nalter table form drop column suggested_budget;\n\nalter table form DROP FOREIGN KEY fk_form_invoice_id ;\nalter table form drop column invoice_id ;\n\nalter table form drop column filled_date ;\nalter table form drop column completed_date;\n\nSET FOREIGN_KEY_CHECKS = 1;','applied',''),(26,'0e50b97b8d999a4bef15dd6053829f876766d304','2017-11-27 06:11:12','alter table agent_form modify column is_income_tax_payee varchar(6) not null ;\nalter table agent_form modify column online_monatory_transaction_facility varchar(6) not null;\nupdate agent_form set is_income_tax_payee = \'true \' where is_income_tax_payee = \'1\' ;\nupdate agent_form set is_income_tax_payee = \'false \' where is_income_tax_payee = \'0\' ;\nupdate agent_form set online_monatory_transaction_facility = \'true\' where online_monatory_transaction_facility = \'1\' ;\nupdate agent_form set online_monatory_transaction_facility = \'false\' where online_monatory_transaction_facility = \'0\' ;\n\n#Desired location to buy property address format change\nalter table requirement_address change city pincode varchar(255);\nalter table requirement_address drop column country;\nalter table requirement_address  change village_or_detail_location locality varchar(100);\n\ncreate table agent_commission (\n\nid 					bigint auto_increment not null ,\ncommission_amount 	decimal(15,2),\ndescription      	varchar(255),\ncommission_date	 	date,\ncommission_against_id  		bigint,\nagent_id					bigint not null,\nconstraint pk_agent_commission primary key (id)\n\n);\n\nalter table payment add column payment_mode varchar(255) ;\n\n#Handling agent filling application form behalf of applicant\n\nalter table user add column agent_id bigint ;\nalter table user add constraint fk_user_agent_id foreign key (agent_id) references agent(id)  on delete set null;\nalter table agent_commission add constraint fk_agent_commission_agent_id foreign key (agent_id) references agent(id)  on delete cascade on update cascade;\n\n# handling statff user\nalter table staff_user add constraint staff_user_email_unique_key unique(email);\nalter table staff_user add column agent_id bigint ;\nalter table staff_user add constraint fk_staff_user_agent_id  foreign key (agent_id) references agent(id)  on delete cascade on update cascade;\nalter table staff_user modify role varchar(300);','SET FOREIGN_KEY_CHECKS = 0;\nalter table staff_user modify role varchar(50);\nalter table staff_user drop foreign key fk_staff_user_agent_id ;\nalter table staff_user drop column agent_id ;\nalter table staff_user  drop index staff_user_email_unique_key ;\nalter table payment drop column payment_mode ;\nalter table requirement_address  change locality village_or_detail_location varchar(100);\nalter table requirement_address add column country varchar(100);\nalter table requirement_address change pincode city varchar(255);\nalter table user drop foreign key fk_user_agent_id ;\nalter table user drop column agent_id ;\ndrop table agent_commission ;\n\nSET FOREIGN_KEY_CHECKS = 1;','applied',''),(27,'37aa8c5c229a5e423c48046d283d0909aa125e99','2017-11-27 06:11:28','alter table agent  add column ib_id bigint;\n\ncreate table investigation_buero(\nid 				bigint auto_increment not null,\nib_name 		varchar(255),\nconstraint pk_investigation_buero primary key(id)\n);\nalter table agent add constraint fk_agent_ib_id foreign key (ib_id) references investigation_buero(id)  on delete cascade on update cascade;\n\n\nalter table investigation_buero drop column ib_name;\nalter table investigation_buero add column user_id bigint;\nalter table investigation_buero add constraint fk_investigation_buero_user_id foreign key (user_id) references user(id)  on delete cascade on update cascade;\ncreate table ib_feedback(\nid 				bigint auto_increment not null,\nib_id			bigint not null,\nagent_id		bigint not null,\nattachment_id   bigint not null,\nconstraint pk_ib_feedback primary key(id)\n);\n\nalter table ib_feedback add constraint fk_ib_feedback_attachment_id foreign key (attachment_id) references attachment(id) on delete cascade on update cascade;\nalter table ib_feedback add constraint fk_ib_feedback_ib_id foreign key (ib_id) references investigation_buero(id) on delete cascade on update cascade;\nalter table ib_feedback add constraint fk_ib_feedback_agent_id foreign key (agent_id) references agent(id) on delete cascade on update cascade;\nalter table attachment add constraint uk_attachment_file_path unique (file_path);\nalter table requirement_address add street varchar(100);\nrename TABLE  `investigation_buero` TO  `buero`;\nalter table agent add column agent_code varchar(250);\nalter table agent add constraint uk_agent_agent_code unique key (agent_code) ;\ndrop table if exists staff_user;\n\n#Adding Applicant Password to user\nalter table user add column applicant_password varchar(255);\n\ncreate table short_requirement_address (\nid                        			bigint auto_increment not null,\nshort_form_id                bigint not null,\ncity                      			varchar(255),\ntaluka                      		varchar(255),\ndistrict                      		varchar(255),\nstate                      			varchar(255),\ncountry                      		varchar(255),\npincode								varchar(6),\nvillage_or_detail_location          varchar(255),\nconstraint pk_short_requirement_address primary key (id)\n);\n\ncreate table short_form(\nid                        bigint auto_increment not null,\ntitle                     varchar(4) not null,\nfname                     varchar(50) not null,\nmname                     varchar(50) not null,\nlname                     varchar(50),\ndob                       datetime not null,\nemail                     varchar(100) not null,\nmobile_number				varchar(20) not null,\nlandline_number			varchar(20) not null,\napplicant_income_per		varchar(50) not null,\napplicant_income			decimal(15, 2) not null,\nco_applicant_income_per	varchar(50) not null,\nco_applicant_income		decimal(15, 2),\nloan_amount_required		decimal(15, 2) not null,\npesonal_contribution_amount decimal(15, 2) not null,\ntotal_budget				decimal(15, 2) not null,\nregistred_by				varchar(50) not null,\nagent_identity 			varchar(100),\nrequirement_type			varchar(200),\ncarpet_area				int ,\nbuilt_up_area				int,\ninvoice_id				bigint,\nconstraint pk_short_form primary key (id)\n);\n\n# short_requirement_address table constraints\nalter table short_requirement_address add constraint fk_short_requirement_address__short_form_id foreign key (short_form_id) references short_form(id) on delete cascade on update cascade;\n\n# short_form table constraint\nalter table short_form add constraint fk_short_form_invoice_id foreign key (invoice_id) references invoice(id) on delete cascade on update cascade;\n\n# Handling applicants , applicant_password for those have password\nupdate user set applicant_password = password where password is not null;\n\n# an additional text box for capturing the \"sub-locality\"\nalter table short_requirement_address add column street varchar(100);','SET FOREIGN_KEY_CHECKS = 0;\n\n\n\nalter table short_requirement_address drop column street ;\nalter table user drop column applicant_password ;\ndrop table short_form;\ndrop table short_requirement_address ;\nrename TABLE  `buero` TO  `investigation_buero`;\ndrop table if exists ib_feedback;\nalter table investigation_buero drop foreign key fk_investigation_buero_user_id;\nalter table investigation_buero drop column user_id;\nalter table investigation_buero add column ib_name 	varchar(255);\n\nalter table agent drop index uk_agent_agent_code ;\nalter table agent drop column agent_code ;\ndrop index uk_attachment_file_path  on attachment;\nalter table requirement_address drop column street;\n\nalter table agent drop foreign key fk_agent_ib_id ;\ndrop table if exists investigation_buero;\nalter table agent  drop column ib_id ;\n\nSET FOREIGN_KEY_CHECKS = 1;','applied',''),(28,'28272d37b3506a6192f1a33d1dfa5efc0b0df267','2017-11-27 06:11:34','alter table user drop column role;\nalter table user add column reset_password varchar(100) ;\nalter table user add constraint uk_user_reset_password unique key (reset_password);\nalter table user modify column mobile varchar(255);\nalter table user modify column email varchar(255);\nalter table user modify column joining_date datetime;\nalter table agent drop foreign key fk_agent_ib_id, add constraint fk_agent_update_ib_id foreign key  (ib_id) references buero(id) on delete set null;','SET FOREIGN_KEY_CHECKS = 0;\n\nalter table agent add constraint fk_agent_ib_id foreign key (ib_id) references buero(id)  on delete cascade on update cascade;\nalter table user modify column joining_date datetime not null;\nalter table user modify column mobile varchar(255) not null;\nalter table user modify column email varchar(255) not null;\nalter table user drop column reset_password;\nalter table user add column role varchar(255);\n\nSET FOREIGN_KEY_CHECKS = 1;','applied',''),(29,'2b3ee5cfc4e51184e29ab73dda5df782ad3c42a6','2017-11-27 06:11:57','alter table user\nadd column interested_in_agent tinyint(1);\nalter table form add column applicant_income_short_form bigint;\nalter table form add column co_applicant_income_short_form bigint;\nalter table form add column required_loan bigint;\nalter table form add column total_budget bigint;\nalter table form add column agent_id bigint;\nalter table form add column short_form_applicant_income_per varchar(255);\nalter table form add column short_form_co_applicant_income_per varchar(255);\nalter table form add column registration_by varchar(255);\n\nalter table applicant modify column sex char(1) null;\nalter table applicant modify column fh_title varchar(255) null;\nalter table applicant modify column fh_fname varchar(255) null;\nalter table applicant modify column fh_mname varchar(255) null;\nalter table applicant modify column fh_relation varchar(255) null;\nalter table applicant modify column marital_status char(1) null;\nalter table applicant modify column phone1_type varchar(255) null;\ndrop table if exists short_requirement_address;\ndrop table if exists short_form;\nalter table form add constraint fk_form__agent_id foreign key (agent_id) references agent(id) on delete set null on update set null;','SET FOREIGN_KEY_CHECKS = 0;\n\nalter table user\n\ndrop column interested_in_agent;\nalter table form drop foreign key fk_form__agent_id;\nalter table applicant modify column phone1_type varchar(255) not null;\nalter table applicant modify column marital_status char(1) not null;\nalter table applicant modify column fh_relation varchar(255) not null;\nalter table applicant modify column fh_mname varchar(255) not null;\nalter table applicant modify column fh_fname varchar(255) not null;\nalter table applicant modify column fh_title varchar(255) not null;\nalter table applicant modify column sex char(1) not null;\n\nalter table form drop column registration_by;\nalter table form drop column short_form_co_applicant_income_per;\nalter table form drop column short_form_applicant_income_per;\nalter table form drop column agent_id ;\nalter table form drop column total_budget;\nalter table form drop column required_loan;\nalter table form drop column co_applicant_income_short_form;\nalter table form drop column applicant_income_short_form;\n\nSET FOREIGN_KEY_CHECKS = 1;','applied',''),(30,'a25300f07d05a8ae73672cfd404c18388385de33','2017-11-27 06:12:01','create table query (\nid                        bigint auto_increment not null,\nuser_id              	    bigint not null,\nquery_text                text,\ntime_stamp                datetime not null,\nconstraint pk_user_query primary key (id)\n);\n\nalter table payment add column bank_name varchar(50);\nalter table payment add column nb_bid varchar(50);\nalter table payment add column nb_order_no varchar(50);\n\n\nalter table query add constraint fk_query_user foreign key(user_id) references user(id);','SET FOREIGN_KEY_CHECKS = 0;\n\nalter table payment drop column bank_name;\nalter table payment drop column nb_bid;\nalter table payment drop column nb_order_no;\ndrop table if exists query;\n\nSET FOREIGN_KEY_CHECKS = 1;','applied',''),(31,'e5adccaa4e71f663811ca2d967faa99dd2e1d186','2017-11-27 06:12:02','create table persistent_id (\nid                      bigint auto_increment not null,\nname                    varchar(100) not null,\nkey_value               bigint not null,\nconstraint pk_persistent_id primary key (id)\n);\n\nalter table persistent_id add constraint uk_persistent_id_name unique key (name);\ninsert into persistent_id (name, key_value) values (\'receiptId\',\'501\');\ninsert into persistent_id (name, key_value) values (\'formNumber\',\'5001\');\nalter table invoice add column receipt_id bigint;','SET FOREIGN_KEY_CHECKS = 0;\n\nalter table invoice drop column receipt_id;\ndrop table if exists persistent_id;\n\nSET FOREIGN_KEY_CHECKS = 1;','applied',''),(32,'16ec3a0336050edfef2055ff0b9f3039ced5e51c','2017-11-27 06:12:06','alter table agent_form add column form_filled_year bigint;\n\ncreate table agent_feedback_form_file (\nid bigint auto_increment not null,\nupload_time datetime not null,\nfeedback_form_file_id bigint not null,\nconstraint pk_agent_feedback_form_file primary key(id)\n);\n\nalter table agent_feedback_form_file add constraint fk_agent_feedback_form_file__feedback_form_file_id foreign key(feedback_form_file_id) references attachment(id);','SET FOREIGN_KEY_CHECKS=0;\nalter table agent_form drop column form_filled_year;\ndrop table agent_feedback_form_file;\n\nSET FOREIGN_KEY_CHECKS=1;','applied',''),(33,'c10c8b73c2a66ec1fb0a7f6c0b27570aab80be49','2017-11-27 06:12:06','insert into page(formatted_title, title, content) values (\'concept\', \'Our Concept\', \'\');\ninsert into page(formatted_title, title, content) values (\'benefits\', \'Benefits of Concept\', \'\');\ninsert into page(formatted_title, title, content) values (\'rules\', \'Rules & Regulations\', \'\');\ninsert into page(formatted_title, title, content) values (\'terms_and_conditions\', \'Applicants Terms & Conditions\', \'\');\ninsert into page(formatted_title, title, content) values (\'success_story\', \'Success Story of Concept\', \'\');\ninsert into page(formatted_title, title, content) values (\'history\', \'History\', \'\');','delete from page where formatted_title = \'concept\';\ndelete from page where formatted_title = \'benefits\';\ndelete from page where formatted_title = \'rules\';\ndelete from page where formatted_title = \'terms_and_conditions\';\ndelete from page where formatted_title = \'success_story\';\ndelete from page where formatted_title = \'history\';','applied',''),(34,'b4fab11d5962edc684a286f2feff3c628c0f814b','2018-11-15 05:41:46','alter table user add column api_key varchar(255);','alter table user drop column api_key;','applied',''),(35,'ed72645d1c3f6b7b2897eb42f322767d0e290866','2018-11-15 05:41:46','alter table user add constraint uk_user_api_key unique key(api_key);','alter table user drop index uk_user_api_key;','applied','');
/*!40000 ALTER TABLE `play_evolutions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS `property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  `type_detail` varchar(50) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `area` decimal(38,0) DEFAULT NULL,
  `loan` tinyint(1) DEFAULT '0',
  `monthly_amnt_dedcted_frm_sal` tinyint(1) DEFAULT '0',
  `willing_to_sell` tinyint(1) DEFAULT '0',
  `expected_price` bigint(20) DEFAULT NULL,
  `allocation` bigint(20) DEFAULT NULL,
  `financer` varchar(100) DEFAULT NULL,
  `loan_amount` bigint(20) DEFAULT NULL,
  `montly_emi` bigint(20) DEFAULT NULL,
  `installments_paid` int(11) DEFAULT NULL,
  `balance_installments` int(11) DEFAULT NULL,
  `balance_loan_amount` bigint(20) DEFAULT NULL,
  `form_id` bigint(20) NOT NULL,
  `property_desc` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_property__form_id` (`form_id`),
  KEY `fk_property__location_id` (`location_id`),
  CONSTRAINT `fk_property__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_property__location_id` FOREIGN KEY (`location_id`) REFERENCES `property_address` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property`
--

LOCK TABLES `property` WRITE;
/*!40000 ALTER TABLE `property` DISABLE KEYS */;
/*!40000 ALTER TABLE `property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_address`
--

DROP TABLE IF EXISTS `property_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `line1` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `taluka` varchar(100) DEFAULT NULL,
  `district` varchar(100) DEFAULT NULL,
  `state` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `pin` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_address`
--

LOCK TABLES `property_address` WRITE;
/*!40000 ALTER TABLE `property_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `property_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query`
--

DROP TABLE IF EXISTS `query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `query_text` text,
  `time_stamp` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_query_user` (`user_id`),
  CONSTRAINT `fk_query_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query`
--

LOCK TABLES `query` WRITE;
/*!40000 ALTER TABLE `query` DISABLE KEYS */;
/*!40000 ALTER TABLE `query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurring_deposit`
--

DROP TABLE IF EXISTS `recurring_deposit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurring_deposit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(100) NOT NULL,
  `name_of_holder` varchar(100) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `maturity_date` datetime DEFAULT NULL,
  `installment` bigint(20) NOT NULL,
  `installments_paid` int(11) DEFAULT NULL,
  `installments_balance` int(11) DEFAULT NULL,
  `principal` bigint(20) DEFAULT NULL,
  `interest_rate` decimal(5,2) DEFAULT NULL,
  `maturity_amount` bigint(20) NOT NULL,
  `monthly_installments` bigint(20) DEFAULT NULL,
  `balance_installments` int(11) DEFAULT NULL,
  `balance_ded_amount` bigint(20) DEFAULT NULL,
  `monthly_amnt_dedcted_frm_sal` tinyint(1) DEFAULT '0',
  `will_surrender` tinyint(1) DEFAULT '0',
  `amount_can_avail` bigint(20) DEFAULT NULL,
  `allocation` bigint(20) DEFAULT NULL,
  `form_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_recurring_deposit__form_id` (`form_id`),
  CONSTRAINT `fk_recurring_deposit__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurring_deposit`
--

LOCK TABLES `recurring_deposit` WRITE;
/*!40000 ALTER TABLE `recurring_deposit` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurring_deposit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requirement`
--

DROP TABLE IF EXISTS `requirement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requirement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `builtup_area` int(11) DEFAULT NULL,
  `carpet_area` int(11) DEFAULT NULL,
  `choice_of_floor` int(11) DEFAULT NULL,
  `sub_type` varchar(255) DEFAULT NULL,
  `garden_area` int(11) DEFAULT NULL,
  `garden_area_required` varchar(255) DEFAULT NULL,
  `no_of_bedrooms` int(11) DEFAULT NULL,
  `no_of_common_toilets` int(11) DEFAULT NULL,
  `no_of_rooms_required` int(11) DEFAULT NULL,
  `no_of_separate_bathroom` int(11) DEFAULT NULL,
  `no_of_separate_wc` int(11) DEFAULT NULL,
  `open_space` int(11) DEFAULT NULL,
  `plot_area` int(11) DEFAULT NULL,
  `plot_area_measure` varchar(255) DEFAULT NULL,
  `terrace_area` int(11) DEFAULT NULL,
  `terrace_required` tinyint(1) DEFAULT '0',
  `terrace_choice` varchar(255) DEFAULT NULL,
  `toilet_wc_bath_requirements` varchar(255) DEFAULT NULL,
  `use_of_land` varchar(255) DEFAULT NULL,
  `other_details` varchar(255) DEFAULT NULL,
  `requirement_type` varchar(30) DEFAULT NULL,
  `residential_type_detail` varchar(255) DEFAULT NULL,
  `commercial_type_detail` varchar(255) DEFAULT NULL,
  `agricultural_type_detail` varchar(255) DEFAULT NULL,
  `type_detail` varchar(255) DEFAULT NULL,
  `specify_height_required` tinyint(1) DEFAULT '0',
  `height` int(11) DEFAULT NULL,
  `power_connection_type` varchar(255) DEFAULT NULL,
  `if_connection_type_other` varchar(255) DEFAULT NULL,
  `is_separate_water_connection_required` tinyint(1) DEFAULT '0',
  `is_separate_power_connection_required` tinyint(1) DEFAULT '0',
  `loft_required` tinyint(1) DEFAULT '0',
  `loft_area` int(11) DEFAULT NULL,
  `otla_required` tinyint(1) DEFAULT '0',
  `otla_area` int(11) DEFAULT NULL,
  `no_of_attached_toilets` int(11) DEFAULT NULL,
  `no_of_attached_urinals` int(11) DEFAULT NULL,
  `open_space_required` tinyint(1) DEFAULT '0',
  `toilet_type` varchar(255) DEFAULT NULL,
  `water_pipe_size` int(11) DEFAULT NULL,
  `if_chawl_type_other` varchar(255) DEFAULT NULL,
  `area_for_each_room` int(11) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `taluka` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `village_or_detail_location` varchar(255) DEFAULT NULL,
  `form_id` bigint(20) NOT NULL,
  `requirement_sub_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_requirement__form_id` (`form_id`),
  CONSTRAINT `fk_requirement__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requirement`
--

LOCK TABLES `requirement` WRITE;
/*!40000 ALTER TABLE `requirement` DISABLE KEYS */;
INSERT INTO `requirement` VALUES (1,2200,1800,0,NULL,150,NULL,2,0,NULL,0,0,200,NULL,NULL,1500,NULL,NULL,'AsPerPlan',NULL,'',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,'HolidayHome'),(2,450,500,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,350,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,'FlatOrTerraceFlat'),(3,450,500,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,350,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,7,'FlatOrTerraceFlat'),(4,450,500,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,350,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,'FlatOrTerraceFlat'),(5,450,500,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,350,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,9,'FlatOrTerraceFlat'),(6,2500,3000,0,'1RK',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2200,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,10,'DuplexFlat'),(7,1050,1050,0,'1RK',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,500,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,11,'DuplexFlat'),(8,1050,1050,0,'1RK',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,500,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,12,'DuplexFlat'),(9,1050,1050,0,'1RK',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,500,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,13,'DuplexFlat'),(10,852,852,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,852,NULL,NULL,'AsPerPlan',NULL,'askdjsakhd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,14,'FlatOrTerraceFlat'),(11,852,852,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,852,NULL,NULL,'AsPerPlan',NULL,'askdjsakhd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,15,'FlatOrTerraceFlat'),(12,2345421,23433,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4325422,NULL,NULL,'AsPerPlan',NULL,'dfgdssfd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,16,'FlatOrTerraceFlat'),(13,650,650,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,650,NULL,NULL,'AsPerPlan',NULL,'test',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,17,'FlatOrTerraceFlat'),(14,550,550,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,550,NULL,NULL,'AsPerPlan',NULL,'test',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,18,'FlatOrTerraceFlat'),(15,345435,43543,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,34543543,NULL,NULL,'AsPerPlan',NULL,'dfgdfg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,19,'FlatOrTerraceFlat'),(16,345435,43543,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,34543543,NULL,NULL,'AsPerPlan',NULL,'dfgdfg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,20,'FlatOrTerraceFlat'),(17,345435,43543,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,34543543,NULL,NULL,'AsPerPlan',NULL,'dfgdfg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,21,'FlatOrTerraceFlat'),(18,345435,43543,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,34543543,NULL,NULL,'AsPerPlan',NULL,'dfgdfg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,22,'FlatOrTerraceFlat'),(19,345435,43543,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,34543543,NULL,NULL,'AsPerPlan',NULL,'dfgdfg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23,'FlatOrTerraceFlat'),(20,234232,23423,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,234233,NULL,NULL,'AsPerPlan',NULL,'scdsfsdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,24,'FlatOrTerraceFlat'),(21,234232,23423,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,234233,NULL,NULL,'AsPerPlan',NULL,'scdsfsdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,25,'FlatOrTerraceFlat'),(22,548,550,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,550,NULL,NULL,'AsPerPlan',NULL,'sadfasdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,26,'FlatOrTerraceFlat'),(23,234,234,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,234,NULL,NULL,'AsPerPlan',NULL,'234',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,27,'FlatOrTerraceFlat'),(24,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,28,'FlatOrTerraceFlat'),(25,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,29,'FlatOrTerraceFlat'),(26,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,30,'FlatOrTerraceFlat'),(27,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,31,'FlatOrTerraceFlat'),(28,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,32,'FlatOrTerraceFlat'),(29,123,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'dsfsd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,33,'FlatOrTerraceFlat'),(30,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,34,'FlatOrTerraceFlat'),(31,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,35,'FlatOrTerraceFlat'),(32,332,332,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,332,NULL,NULL,'AsPerPlan',NULL,'sdfdsf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36,'FlatOrTerraceFlat'),(33,324,234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'dsfs',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,37,'FlatOrTerraceFlat'),(34,851,851,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,850,NULL,NULL,'AsPerPlan',NULL,'sdfghj',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,38,'FlatOrTerraceFlat'),(35,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,39,'FlatOrTerraceFlat'),(36,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,40,'FlatOrTerraceFlat'),(37,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,41,'FlatOrTerraceFlat'),(38,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,42,'FlatOrTerraceFlat'),(39,23430,234234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23421,NULL,NULL,'AsPerPlan',NULL,'asdfasd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,43,'FlatOrTerraceFlat'),(40,233,234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,232,NULL,NULL,'AsPerPlan',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,44,'FlatOrTerraceFlat'),(41,123,121,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'asdfdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,45,'FlatOrTerraceFlat'),(42,233,233,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,232,NULL,NULL,'AsPerPlan',NULL,'sdfsdds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,46,'FlatOrTerraceFlat'),(43,233,233,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,232,NULL,NULL,'AsPerPlan',NULL,'sdfsdds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,47,'FlatOrTerraceFlat'),(44,233,233,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,232,NULL,NULL,'AsPerPlan',NULL,'sdfsdds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,48,'FlatOrTerraceFlat'),(45,123,121,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,49,'FlatOrTerraceFlat'),(46,123,121,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,50,'FlatOrTerraceFlat'),(47,123,121,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,51,'FlatOrTerraceFlat'),(48,123,121,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,52,'FlatOrTerraceFlat'),(49,123,121,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,53,'FlatOrTerraceFlat'),(50,123,121,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'asdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,54,'FlatOrTerraceFlat'),(51,234,234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,232,NULL,NULL,'AsPerPlan',NULL,'sdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,55,'FlatOrTerraceFlat'),(52,123,232,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'sdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,56,'FlatOrTerraceFlat'),(53,232,234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,233,NULL,NULL,'AsPerPlan',NULL,'sdfsdfds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,57,'FlatOrTerraceFlat'),(54,232,234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,233,NULL,NULL,'AsPerPlan',NULL,'sdfsdfds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,58,'FlatOrTerraceFlat'),(55,232,234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,233,NULL,NULL,'AsPerPlan',NULL,'sdfsdfds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,59,'FlatOrTerraceFlat'),(56,232,234,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,233,NULL,NULL,'AsPerPlan',NULL,'sdfsdfds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,60,'FlatOrTerraceFlat'),(57,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,61,'FlatOrTerraceFlat'),(58,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,62,'FlatOrTerraceFlat'),(59,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,63,'FlatOrTerraceFlat'),(60,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,64,'FlatOrTerraceFlat'),(61,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,65,'FlatOrTerraceFlat'),(62,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,66,'FlatOrTerraceFlat'),(63,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,67,'FlatOrTerraceFlat'),(64,123,123,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'123',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,68,'FlatOrTerraceFlat'),(65,233,232,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,234,NULL,NULL,'AsPerPlan',NULL,'sdfsdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,69,'FlatOrTerraceFlat'),(66,53,541,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,6542,NULL,NULL,'AsPerPlan',NULL,'hgfd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,70,'FlatOrTerraceFlat'),(67,3443,3443,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,34432,NULL,NULL,'AsPerPlan',NULL,'fsfdfdfd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,71,'FlatOrTerraceFlat'),(68,89,89,0,'1RK',NULL,NULL,NULL,0,NULL,0,0,NULL,NULL,NULL,89,NULL,NULL,'AsPerPlan',NULL,'89',NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,72,'FlatOrTerraceFlat'),(69,123,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'sdafasdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,73,'FlatOrTerraceFlat'),(70,123,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,NULL,'AsPerPlan',NULL,'sdafasdf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,74,'FlatOrTerraceFlat'),(71,234,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23423,NULL,NULL,'AsPerPlan',NULL,'sdfdfgsd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,75,'FlatOrTerraceFlat'),(72,234,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23423,NULL,NULL,'AsPerPlan',NULL,'sdfdfgsd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,76,'FlatOrTerraceFlat'),(73,234,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23423,NULL,NULL,'AsPerPlan',NULL,'sdfdfgsd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,77,'FlatOrTerraceFlat'),(74,234,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23423,NULL,NULL,'AsPerPlan',NULL,'sdfdfgsd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,78,'FlatOrTerraceFlat'),(75,234,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23423,NULL,NULL,'AsPerPlan',NULL,'sdfdfgsd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,79,'FlatOrTerraceFlat'),(76,234,123,0,'1RK',NULL,'Required',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,23423,NULL,NULL,'AsPerPlan',NULL,'sdfdfgsd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,80,'FlatOrTerraceFlat');
/*!40000 ALTER TABLE `requirement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requirement_address`
--

DROP TABLE IF EXISTS `requirement_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requirement_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `requirement_id` bigint(20) NOT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `taluka` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `locality` varchar(100) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_requirement_address__requirement_id` (`requirement_id`),
  CONSTRAINT `fk_requirement_address__requirement_id` FOREIGN KEY (`requirement_id`) REFERENCES `requirement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requirement_address`
--

LOCK TABLES `requirement_address` WRITE;
/*!40000 ALTER TABLE `requirement_address` DISABLE KEYS */;
INSERT INTO `requirement_address` VALUES (1,1,'412109','Haveli','Pune','MAHARASHTRA',' Sadumbre B.O',''),(2,2,'411021','Haveli','Pune','MAHARASHTRA',' Bavdhan B.O',NULL),(3,3,'411021','Haveli','Pune','MAHARASHTRA',' Bavdhan B.O',NULL),(4,4,'411021','Haveli','Pune','MAHARASHTRA',' Bavdhan B.O',NULL),(5,5,'411021','Haveli','Pune','MAHARASHTRA',' Bavdhan B.O',NULL),(6,6,'411021','Haveli','Pune','MAHARASHTRA',' Sus B.O',NULL),(7,7,'414301','Parner','Ahmed Nagar','MAHARASHTRA',' Narayan Gavan B.O',NULL),(8,8,'414301','Parner','Ahmed Nagar','MAHARASHTRA',' Narayan Gavan B.O',NULL),(9,9,'414301','Parner','Ahmed Nagar','MAHARASHTRA',' Narayan Gavan B.O',NULL),(10,10,'839282','test','test','test','',NULL),(11,11,'839282','test','test','test','',NULL),(12,12,'23423','asd','asfg','sdfg','sdfdsaf',NULL),(13,13,'412208','Shirur','Pune','MAHARASHTRA',' Shikrapur B.O',''),(14,14,'412209','Shirur','Pune','MAHARASHTRA',' Ganegaon Khalasa B.O',''),(15,15,'345435','dfgdfg','dfgdfg','dfdfg','',NULL),(16,16,'345435','dfgdfg','dfgdfg','dfdfg','',NULL),(17,17,'345435','dfgdfg','dfgdfg','dfdfg','',NULL),(18,18,'345435','dfgdfg','dfgdfg','dfdfg','',NULL),(19,19,'345435','dfgdfg','dfgdfg','dfdfg','',NULL),(20,20,'324234','sdfdsf','sdfsdfs','sdfds','2',NULL),(21,21,'324234','sdfdsf','sdfsdfs','sdfds','2',NULL),(22,22,'234234','sfdg','test','test','asdfasdf',NULL),(23,23,'403107','Sanquelim','South Goa','GOA',' Amona B.O','234'),(24,24,'788112','Cleaver House','Cachar','ASSAM',' Derby S.O','asd'),(25,25,'788112','Cleaver House','Cachar','ASSAM',' Derby S.O','asd'),(26,26,'788112','Cleaver House','Cachar','ASSAM',' Derby S.O','asd'),(27,27,'744202','Diglipur','North And Middle Andaman','ANDAMAN & NICOBAR ISLANDS',' Diglipur S.O','zxcvbn'),(28,28,'177001','Hamirpur','Hamirpur(HP)','HIMACHAL PRADESH',' Jhaniari B.O','sadf'),(29,29,'324234','sdfsdf','test','wer','2',NULL),(30,30,'393050','Sagbara','Narmada','GUJARAT',' Motidevrupam B.O','sadf'),(31,31,'515556','Amadagur','Ananthapur','ANDHRA PRADESH',' Amadagur S.O',''),(32,32,'234234','sdfsdf','sdfsdf','dsfdsf','',NULL),(33,33,'sdfd','sdf','sdf','sdfdsf','sdf',NULL),(34,34,'234234','sdfsdf','test','sdfdsf','sdfghj',NULL),(35,35,'517129','Arugonda','Chittoor','ANDHRA PRADESH',' Kattakindapalle B.O','sadf'),(36,36,'815352','Narayanpur','Jamtara','JHARKHAND',' Kurta B.O',''),(37,37,'782440','Nagon','Nagaon','ASSAM',' Hindu Block B.O',''),(38,38,'782440','Nagon','Nagaon','ASSAM',' Hindu Block B.O',''),(39,39,'234324','sadf','asdf','asdfdsa','asdfasd',NULL),(40,40,'234','asdf','asdf','asdf','asdf',NULL),(41,41,'123312','asdf','asdf','asdf','asdf',NULL),(42,42,'234324','sdf','sdf','sdf','sdf',NULL),(43,43,'234324','sdf','sdf','sdf','sdf',NULL),(44,44,'234324','sdf','sdf','sdf','sdf',NULL),(45,45,'23423','sadf','sdf','sdf','sdf',NULL),(46,46,'23423','sadf','sdf','sdf','sdf',NULL),(47,47,'23423','sadf','sdf','sdf','sdf',NULL),(48,48,'23423','sadf','sdf','sdf','sdf',NULL),(49,49,'23423','sadf','sdf','sdf','sdf',NULL),(50,50,'23423','sadf','sdf','sdf','sdf',NULL),(51,51,'234','sdf','sdf','sdf','sdf',NULL),(52,52,'234234','sdf','sdf','sdf','df',NULL),(53,53,'234324','sdf','sdf','sdf','ddsfdsf',NULL),(54,54,'234324','sdf','sdf','sdf','ddsfdsf',NULL),(55,55,'234324','sdf','sdf','sdf','ddsfdsf',NULL),(56,56,'234324','sdf','sdf','sdf','ddsfdsf',NULL),(57,57,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(58,58,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(59,59,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(60,60,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(61,61,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(62,62,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(63,63,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(64,64,'515311','Agali','Ananthapur','ANDHRA PRADESH',' Hallikera B.O','sadf'),(65,65,'23423','dsfs','sdf','sdf','sdfsdf',NULL),(66,66,'654','hgf','hgg','hgf','hgfd',NULL),(67,67,'3434','dfdfd','dfdff','fdfdff','dffdffd',NULL),(68,68,'481661','Mandla','Mandla','MADHYA PRADESH',' Mandla H.O',''),(69,69,'234345','asdf','asdf','asdf','',NULL),(70,70,'234345','asdf','asdf','asdf','',NULL),(71,72,'234324','sdf','hhhh','sdfsdf','dsfsdfsdf',NULL),(72,71,'234324','sdf','hhhh','sdfsdf','dsfsdfsdf',NULL),(73,73,'234324','sdf','hhhh','sdfsdf','dsfsdfsdf',NULL),(74,74,'234324','sdf','hhhh','sdfsdf','dsfsdfsdf',NULL),(75,75,'234324','sdf','hhhh','sdfsdf','dsfsdfsdf',NULL),(76,76,'234324','sdf','hhhh','sdfsdf','dsfsdfsdf',NULL);
/*!40000 ALTER TABLE `requirement_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role__user_id__role` (`user_id`,`role`),
  CONSTRAINT `fk_role__user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (2,1,'accountant'),(1,1,'visitor'),(4,2,'admin'),(3,2,'visitor'),(5,3,'visitor'),(6,4,'manager'),(7,5,'admin'),(8,5,'visitor'),(9,6,'buero'),(10,6,'visitor'),(20,8,'member'),(21,9,'agent'),(27,14,'agent'),(26,14,'visitor'),(28,15,'agent'),(29,16,'agent'),(30,17,'agent'),(31,18,'visitor'),(32,19,'visitor'),(33,20,'agent'),(34,21,'agent'),(39,22,'applicant'),(35,22,'visitor'),(36,23,'visitor'),(37,24,'visitor'),(38,25,'visitor'),(40,26,'visitor'),(41,27,'visitor'),(42,28,'visitor'),(43,29,'visitor'),(44,30,'applicant'),(45,30,'visitor'),(46,31,'applicant'),(47,31,'visitor'),(48,32,'applicant'),(49,32,'visitor'),(50,33,'visitor');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `self_employed_income`
--

DROP TABLE IF EXISTS `self_employed_income`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `self_employed_income` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_type` varchar(50) DEFAULT NULL,
  `yr1_pat` varchar(20) DEFAULT NULL,
  `yr1_depr` varchar(20) DEFAULT NULL,
  `yr2_pat` bigint(20) DEFAULT NULL,
  `yr2_depr` bigint(20) DEFAULT NULL,
  `yr3_pat` bigint(20) DEFAULT NULL,
  `yr3_depr` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `self_employed_income`
--

LOCK TABLES `self_employed_income` WRITE;
/*!40000 ALTER TABLE `self_employed_income` DISABLE KEYS */;
/*!40000 ALTER TABLE `self_employed_income` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spouse_details`
--

DROP TABLE IF EXISTS `spouse_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spouse_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sp_title` varchar(4) DEFAULT NULL,
  `sp_fname` varchar(50) DEFAULT NULL,
  `sp_mname` varchar(50) DEFAULT NULL,
  `sp_lname` varchar(50) DEFAULT NULL,
  `sp_profession` varchar(50) DEFAULT NULL,
  `sp_dob` datetime DEFAULT NULL,
  `anniversary` datetime DEFAULT NULL,
  `children` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spouse_details`
--

LOCK TABLES `spouse_details` WRITE;
/*!40000 ALTER TABLE `spouse_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `spouse_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `summary`
--

DROP TABLE IF EXISTS `summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `i_accpt_abv_cal` char(1) NOT NULL,
  `approx_loan_eli` tinyint(1) DEFAULT '0',
  `cash_balance` tinyint(1) DEFAULT '0',
  `all_fd_summ_amnt` tinyint(1) DEFAULT '0',
  `all_rd_summ_amnt` tinyint(1) DEFAULT '0',
  `all_insurance_summ_amnt` tinyint(1) DEFAULT '0',
  `selling_summ_amnt` tinyint(1) DEFAULT '0',
  `borrowed_summary` tinyint(1) DEFAULT '0',
  `donation_summary` tinyint(1) DEFAULT '0',
  `returned_amount` tinyint(1) DEFAULT '0',
  `all_bank_bal_summ_amnt` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `summary`
--

LOCK TABLES `summary` WRITE;
/*!40000 ALTER TABLE `summary` DISABLE KEYS */;
/*!40000 ALTER TABLE `summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `authcode` varchar(10) DEFAULT NULL,
  `joining_date` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `email_verified` tinyint(1) DEFAULT '0',
  `phone_verified` tinyint(1) DEFAULT '0',
  `form_id` bigint(20) DEFAULT NULL,
  `last_login_ip` varchar(100) DEFAULT NULL,
  `authcode_hash` varchar(255) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `applicant_password` varchar(255) DEFAULT NULL,
  `reset_password` varchar(100) DEFAULT NULL,
  `interested_in_agent` tinyint(1) DEFAULT NULL,
  `api_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_email` (`email`),
  UNIQUE KEY `uk_user_mobile` (`mobile`),
  UNIQUE KEY `uk_user_reset_password` (`reset_password`),
  UNIQUE KEY `uk_user_api_key` (`api_key`),
  KEY `fk_user__form_id` (`form_id`),
  KEY `fk_user_agent_id` (`agent_id`),
  CONSTRAINT `fk_user__form_id` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_user_agent_id` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'accountant@ps.com','accountant','00000007011','$2a$10$0l7vTPZqnCwWAlhwmyMDReplO1AyC2IpBSwAudEiEsC5LswshFZ0C',NULL,'2017-11-27 11:42:21','2017-11-27 11:42:21',NULL,NULL,1,NULL,'$2a$10$K7VMq70OTw0uwhVtZbqWsuZDblBbsiTv//oJmQ2uHClY9TRK8b3gu',NULL,NULL,NULL,NULL,NULL),(2,'admin@ps.com','Administrator','00000000001','$2a$10$WvK7fDoLhtFjuVven6M6gOoqME2/O8L3HePlp2aOD.dyCw7tmVQc2','asdf123','2017-11-27 11:42:21','2017-12-11 12:21:59',NULL,NULL,2,'0:0:0:0:0:0:0:1','$2a$10$F6/7xQpl7jdaG2.h0Xug4uYnfdozwr50W8s.j1XWRkRuwpdky6YJq',NULL,NULL,NULL,1,NULL),(3,'visitor@ps.com','Visitor','12345','$2a$10$xuQ9u5PkWs/ATrZyA4dMVOfZlTG34OUJjWGK7/KYjHPkIWs0O338a',NULL,'2017-11-27 11:42:22','2017-11-27 11:42:22',NULL,NULL,3,NULL,'$2a$10$vpGl7CAKT9ZLfrXEKEkyt.xi59M6huyrL..DZW5jloKjzF34PWNVW',NULL,NULL,NULL,NULL,NULL),(4,'manager@ps.com','Manager','12356','$2a$10$8MLAjJM9c4eGXkelLhKPz.DEHmhaCobRR0RjspRxBJSlG4XNqZSLa',NULL,'2017-11-27 11:42:22','2017-11-27 18:00:43',NULL,NULL,4,'0:0:0:0:0:0:0:1','$2a$10$F6Khq9r5Uloau3t2GMoJ6eaYvw95fZsreQ9lHf5Lij75XCSYtAama',NULL,NULL,NULL,NULL,NULL),(5,'hybrid@ps.com','hybrid','12231333','$2a$10$KohuwWnS7l6KhqgQJ2tEs.jS39D96epWt3XWt38PLf47MSwEkENn6',NULL,'2017-11-27 11:42:23','2017-11-27 11:42:23',NULL,NULL,NULL,NULL,'$2a$10$JWg6FuV.bHjXJ5LHMQtmseJLd4BgI.MjSb0Ia0eiVvQ94K27tJjVm',NULL,NULL,NULL,NULL,NULL),(6,'buero@ps.com','buero','221221122','$2a$10$tcmbR1RE9JsE/GSVCsJIKuAkZSGzPU5JkqRT9JjubM7Xg0CeuwhGW',NULL,'2017-11-27 11:42:23','2017-11-27 18:04:18',NULL,NULL,NULL,'0:0:0:0:0:0:0:1','$2a$10$iSGEYZGjwV42NotHn/AG..A4WTGZ3OLsQEStFTJZIs7yk0tf7gxwi',NULL,NULL,NULL,NULL,NULL),(8,'member@ps.com','member','9021556333','$2a$10$MI4YSKlPKn03ujutAlSQuuqWtRWqZSCahHEkuzNsCPcauV9S2PYM6',NULL,'2017-11-27 15:55:03',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'agent@ps.com','Agent','9865326589','$2a$10$AOM/YwBTD9AuOQwH9LgdgugHj2/joa4rP.PniBJIkdv/oGmatGZMK',NULL,'2017-11-27 15:59:49',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(14,'ad@gmail.com','ad','9762566783','$2a$10$ff07bKrha15DStgDBIzOs.PpJtWt92xYA3dtyEuilu8OYayUXKhHi','g9ualocn','2017-12-06 18:50:20','2018-12-24 17:14:44',NULL,NULL,NULL,'0:0:0:0:0:0:0:1','$2a$10$BTRyJ0r62uqWDUD0JTB8P.V0JI.VDBnwqqjC/VzwMK2cmpzhwdy6.',NULL,NULL,NULL,1,'byfyrgnf637mc8ijv3xi'),(15,'amol.nirmal@techbulls.com','Amol','9865326598','$2a$10$jrr5pDbf/cEWv3ztliQyzOczUq98lH8yiQI5OJBiaVe0ia2WG2gbK',NULL,'2017-12-07 12:03:32','2017-12-07 12:58:18',NULL,NULL,NULL,'0:0:0:0:0:0:0:1',NULL,NULL,NULL,NULL,NULL,NULL),(16,'sodak@gmail.com','sodak','3265988754','$2a$10$Bp3YhUs5BxWS2lNjBcozyuFJ/CUjhhUrGmgf9Hd1IoQZC2czGzF3.',NULL,'2017-12-07 14:06:46',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(17,'prashant.patil@techbulls.com','prashant','1245823478','$2a$10$66Y94DJVZM8vSk/z5Dileu.sEocVQTP1L7rsaOBHjT6sD2Ny9RzXe',NULL,'2017-12-07 14:08:48','2017-12-07 14:19:44',NULL,NULL,NULL,'0:0:0:0:0:0:0:1',NULL,NULL,NULL,NULL,NULL,NULL),(18,'dan@gmail.com','dan','9854785632',NULL,'76cv17zg','2017-12-07 15:28:55','2018-11-01 14:34:25',NULL,NULL,NULL,'0:0:0:0:0:0:0:1','$2a$10$s1AAYd5iAwal3/JYygUCc.cV2ynl540OATIDMEiAN.zjIVZikIWg.',NULL,NULL,NULL,NULL,NULL),(19,'sagar.shirge@techbulls.com','Sagar','7887439488',NULL,'asdf','2017-12-11 11:12:56','2018-09-26 17:53:06',NULL,NULL,NULL,'0:0:0:0:0:0:0:1','$2a$10$5m/njfomoLQ9haIraxilzu3oiMN.zJlJZ8trzTD31FVfC.FLqanGu',NULL,NULL,NULL,NULL,NULL),(20,'shirkemukesh@gmail.com','John','2547896325','$2a$10$kHCKuE0qNx9nSfyJYIvPWeb3QM6R4y5ldVH0i0M/yMkSpanKrh9vK',NULL,'2017-12-11 11:22:10',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(21,'nitin.pandhare@techbulls.com','Nitin','3254785124','$2a$10$f/L0RnZ4pNOllgAsB8A91uvL9gTunkPwAHxscKVmxpLwLTP2uEj6S',NULL,'2017-12-11 12:18:49','2017-12-11 12:39:54',NULL,NULL,NULL,'0:0:0:0:0:0:0:1',NULL,NULL,NULL,NULL,NULL,NULL),(22,'asifbshaikhit@gmail.com','Asif','8668217115',NULL,'5qp97r02','2018-09-18 15:56:54','2018-10-09 11:07:06',NULL,NULL,17,'192.168.0.164','$2a$10$yvDaDl6t/ApAEkUtgIUIxubHyDCNuHma2UtHCRqymoqMmio199.3W',NULL,'$2a$10$o7NzGj6x8rKv014JwBF2M./MHR/5Li5O47tk5SJ3.1ch6eQpOKOFm',NULL,NULL,NULL),(23,'test@example.com','test','3773737737',NULL,'tsb0l39y','2018-10-03 12:57:13','2018-10-03 12:58:06',NULL,NULL,NULL,'192.168.0.164','$2a$10$ONqjgemyAjRJtz8Oiucvmeg0ewlF0FBY1ZXCK1cC6aV0J9aIlWe9y',NULL,NULL,NULL,NULL,NULL),(24,'asif@gmail.com','asif','9890056086',NULL,'q9erv6y9','2018-10-03 17:01:34','2018-10-03 17:51:17',NULL,NULL,NULL,'192.168.0.164','$2a$10$D3yADYDHr7cyCpJEX1a4nu0RsFhccSJr/u5wmKbp0y0upFYwnz/nS',NULL,NULL,NULL,NULL,NULL),(25,'as@gmail.com','asif','9328479283',NULL,'vcsu094j','2018-10-03 17:09:45',NULL,NULL,NULL,NULL,NULL,'$2a$10$5OgJzMEfEhbut6ltMmJ1oOuFSX.TG3lTsCIOlxIk1vxfeaPZN3O8W',NULL,NULL,NULL,NULL,NULL),(26,'abc@example.com','Asif','6666666666',NULL,'rb4rpn0q','2018-11-02 07:00:37',NULL,NULL,NULL,NULL,NULL,'$2a$10$rJxZZldL/utsnCiJI2FffOcQYM7HXWb1sLKF2FKNOJZkG2Com4.de',NULL,NULL,NULL,NULL,NULL),(27,'test5@example.com','Asif','9834659043',NULL,'x234pria','2018-11-02 07:03:01',NULL,NULL,NULL,NULL,NULL,'$2a$10$FifeUh1JOl04.exAvrBPo.tp2L8Gaobgx91rRIIlS4Y7Ks5qphxUW',NULL,NULL,NULL,NULL,NULL),(28,'aaaa@gmail.com','aasa','1111111111',NULL,'h7o8otj3','2018-11-02 07:27:41',NULL,NULL,NULL,NULL,NULL,'$2a$10$tagubxiG5w6u7cgoVrssSuVYiGHg26hFNYclU.Mqe9SrrX/odsyGO',NULL,NULL,NULL,NULL,NULL),(29,'garjesoftware@gmail.com','Pandurang','8805309470',NULL,'x883bzga','2018-11-02 07:28:45',NULL,NULL,NULL,NULL,NULL,'$2a$10$9/lqEEIa5MY6dF/uUknG..9/gIAj7JkuVYpJrVjiVyVzhIWUOCzym',NULL,NULL,NULL,NULL,NULL),(30,'asd@asd.d','SAD ASD ASD','324324234234',NULL,'q5br58m0','2018-11-12 11:12:20','2018-11-12 11:12:20',NULL,NULL,27,NULL,'$2a$10$05eDD8C3pp2RvR4u1sLlruu0./958z/l0u9qHZCqZM0fgoRIIo26u',NULL,'$2a$10$05eDD8C3pp2RvR4u1sLlruu0./958z/l0u9qHZCqZM0fgoRIIo26u',NULL,NULL,NULL),(31,'asd@asd.asd','ASD ASD ASD','1212312312',NULL,'xlrch3vw','2018-11-12 18:20:40','2018-11-12 18:20:40',NULL,NULL,32,NULL,'$2a$10$6Q/LSR.eJcUi943O5Wtz1OSGhYHdoJnZGLSRYPAWL1klBCnuoyM7C',NULL,'$2a$10$6Q/LSR.eJcUi943O5Wtz1OSGhYHdoJnZGLSRYPAWL1klBCnuoyM7C',NULL,NULL,NULL),(32,'assd@asd.asd','ASD ASD ASD','1212312316',NULL,'osf33d76','2018-11-14 17:48:57','2018-11-14 17:48:57',NULL,NULL,39,NULL,'$2a$10$wevotFxPIpiUVqdTkCtw7OYDW48XtfuyuDctoN8BXARu85sJK8HQe',NULL,'$2a$10$wevotFxPIpiUVqdTkCtw7OYDW48XtfuyuDctoN8BXARu85sJK8HQe',NULL,NULL,NULL),(33,'asif.shaikh@techbulls.com','Asif','9730950131',NULL,'kms1ie65','2018-12-24 12:37:14','2018-12-24 12:49:14',NULL,NULL,NULL,'0:0:0:0:0:0:0:1','$2a$10$MJzTtwS1rMOFVfad7iYnWOXGokgnAmhoJqAF0FAPzygk4AwvtFbZa',NULL,NULL,NULL,NULL,'ubkmnxfwjqa2p3t7hygt');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-27 11:12:35
