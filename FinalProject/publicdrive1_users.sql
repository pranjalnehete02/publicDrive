-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: publicdrive1
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`,`role`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'aditya','aditya@test.com','aditya','2025-01-31 20:56:04','2025-01-31 20:56:04',''),(2,'hitesh','hitesh@test.com','hitesh','2025-01-31 21:00:34','2025-01-31 21:00:34',''),(3,'abhi','abhi@test.com','abhi','2025-02-02 09:34:48','2025-02-02 09:34:48',''),(4,'krutik','krutik@test.com','krutik','2025-02-02 09:57:08','2025-02-02 09:57:08',''),(5,'aman','aman@test.com','aman','2025-02-02 12:30:34','2025-02-02 12:30:34',''),(6,'ayush','ayush@test.com','ayush','2025-02-02 12:57:31','2025-02-02 12:57:31',''),(7,'admin','admin@gmail.com','admin','2025-02-02 17:55:02','2025-02-02 17:55:02',''),(8,'fsdafasdf','adityanikhate2asd@gmail.com','$2a$10$FgffOH6/fcyGwPcCDjZe4uiWw6XdxWVfxZ9fsNdBN469PGA4oxaZi','2025-02-02 19:21:09','2025-02-02 19:21:09','ROLE_USER'),(9,'ayush1','ayushadmin@gmail.com','$2a$10$yvDH7EcI1bNf0x7fcXxnnO6euoHBWD/35/NqH4pKR4ZF63EpISAtK','2025-02-02 19:21:53','2025-02-02 19:21:53','ROLE_USER'),(10,'adi','admin@gmail.comss','$2a$10$fP/73.e8qHtjOU5duGwJd.5FmhYlH7QUhGeIPfUgGMtg9ijD1myuK','2025-02-02 20:04:37','2025-02-02 20:04:37','ROLE_USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-03  2:22:39
