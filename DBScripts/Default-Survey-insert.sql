USE `ss_user`;
-- MySQL dump 10.13  Distrib 5.5.40, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: ss_user
-- ------------------------------------------------------
-- Server version	5.5.40-0ubuntu0.14.04.1

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


LOCK TABLES `SURVEY_QUESTIONS_ANSWER_OPTIONS` WRITE;
/*!40000 ALTER TABLE `SURVEY_QUESTIONS_ANSWER_OPTIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SURVEY_QUESTIONS_ANSWER_OPTIONS` ENABLE KEYS */;
UNLOCK TABLES;


--

LOCK TABLES `SURVEY` WRITE;
/*!40000 ALTER TABLE `SURVEY` DISABLE KEYS */;
INSERT INTO `SURVEY` VALUES (2,'Default Banking Survey',1,1,1,'2015-02-10 08:14:34','1','2015-02-10 08:09:53','1'),(3,'Default Realtor Survey',3,1,1,'2015-02-10 08:15:11','1','2015-02-10 08:12:44','1'),(4,'Default Mortgage Survey',2,1,1,'2015-02-10 08:16:41','1','2015-02-10 08:15:17','1');
/*!40000 ALTER TABLE `SURVEY` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `SURVEY_QUESTIONS` WRITE;
/*!40000 ALTER TABLE `SURVEY_QUESTIONS` DISABLE KEYS */;
INSERT INTO `SURVEY_QUESTIONS` VALUES (1,'sb-range-smiles','Default Banking Question 1',0,'2015-02-10 08:10:17','1','2015-02-10 08:10:17','1'),(2,'sb-range-star','Default Banking Question 1',0,'2015-02-10 08:10:35','1','2015-02-10 08:10:35','1'),(3,'sb-range-scale','Default Banking Question 3',0,'2015-02-10 08:10:48','1','2015-02-10 08:10:48','1'),(4,'sb-range-smiles','Default Realtor Question 1',0,'2015-02-10 08:13:00','1','2015-02-10 08:13:00','1'),(5,'sb-range-star','Default Realtor Question 2',0,'2015-02-10 08:13:15','1','2015-02-10 08:13:15','1'),(6,'sb-range-scale','Default Realtor Question 3',0,'2015-02-10 08:13:28','1','2015-02-10 08:13:28','1'),(7,'sb-range-smiles','Default Mortgage Question 1',0,'2015-02-10 08:15:42','1','2015-02-10 08:15:42','1'),(8,'sb-range-star','Default Mortgage Question 2',0,'2015-02-10 08:15:57','1','2015-02-10 08:15:57','1'),(9,'sb-range-scale','Default Mortgage Question 3',0,'2015-02-10 08:16:05','1','2015-02-10 08:16:05','1');
/*!40000 ALTER TABLE `SURVEY_QUESTIONS` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `SURVEY_QUESTIONS_MAPPING` WRITE;
/*!40000 ALTER TABLE `SURVEY_QUESTIONS_MAPPING` DISABLE KEYS */;
INSERT INTO `SURVEY_QUESTIONS_MAPPING` VALUES (1,2,1,1,1,1,'2015-02-10 08:10:17','1','2015-02-10 08:10:17','1'),(2,2,2,2,1,1,'2015-02-10 08:10:35','1','2015-02-10 08:10:35','1'),(3,2,3,3,1,1,'2015-02-10 08:10:48','1','2015-02-10 08:10:48','1'),(4,3,4,1,1,1,'2015-02-10 08:13:00','1','2015-02-10 08:13:00','1'),(5,3,5,2,1,1,'2015-02-10 08:13:15','1','2015-02-10 08:13:15','1'),(6,3,6,3,1,1,'2015-02-10 08:13:28','1','2015-02-10 08:13:28','1'),(7,4,7,1,1,1,'2015-02-10 08:15:42','1','2015-02-10 08:15:42','1'),(8,4,8,2,1,1,'2015-02-10 08:15:57','1','2015-02-10 08:15:57','1'),(9,4,9,3,1,1,'2015-02-10 08:16:05','1','2015-02-10 08:16:05','1');
/*!40000 ALTER TABLE `SURVEY_QUESTIONS_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-10 13:48:17
