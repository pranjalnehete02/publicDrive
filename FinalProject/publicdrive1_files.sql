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
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `files` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `folder_id` bigint NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `folder_id` (`folder_id`),
  CONSTRAINT `files_ibfk_1` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES (1,1,'Public Drive Spring Boot.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\4\\Public Drive Spring Boot.pdf','2025-02-02 10:22:37','2025-02-02 10:22:37'),(2,1,'Untitled.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\4\\Untitled.pdf','2025-02-02 10:58:00','2025-02-02 10:58:00'),(3,1,'HTTP vs NFS for File Sharing.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\4\\HTTP vs NFS for File Sharing.pdf','2025-02-02 12:16:58','2025-02-02 12:16:58'),(4,2,'Public Drive Spring Boot.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\5\\Public Drive Spring Boot.pdf','2025-02-02 12:30:52','2025-02-02 12:30:52'),(5,3,'11 PG-DAC 0824 - 4th Nov 2024  to 9th Nov 2024- Ver 1.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\6\\11 PG-DAC 0824 - 4th Nov 2024  to 9th Nov 2024- Ver 1.pdf','2025-02-02 12:58:16','2025-02-02 12:58:16'),(6,2,'book.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\5\\book.pdf','2025-02-02 14:37:30','2025-02-02 14:37:30'),(7,2,'wallpaper3.jpg','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\5\\wallpaper3.jpg','2025-02-02 16:10:56','2025-02-02 16:10:56'),(8,4,'Screenshot (2).png','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\7\\Screenshot (2).png','2025-02-02 17:55:27','2025-02-02 17:55:27'),(9,5,'Public Drive Spring Boot (1).pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\10\\Public Drive Spring Boot (1).pdf','2025-02-02 20:17:21','2025-02-02 20:17:21'),(10,5,'Public Drive Spring Boot.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\10\\Public Drive Spring Boot.pdf','2025-02-02 20:18:50','2025-02-02 20:18:50'),(11,5,'downloaded-file.pdf','C:\\Users\\adity\\OneDrive\\Desktop\\Project CDAC\\FIleDataBase\\10\\downloaded-file.pdf','2025-02-02 20:33:11','2025-02-02 20:33:11');
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
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
