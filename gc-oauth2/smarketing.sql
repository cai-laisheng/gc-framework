/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : smarketing

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 24/12/2022 16:47:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `registered_client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `principal_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authorization_grant_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authorized_scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `state` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authorization_code_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `authorization_code_issued_at` timestamp(0) NULL DEFAULT NULL,
  `authorization_code_expires_at` timestamp(0) NULL DEFAULT NULL,
  `authorization_code_metadata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `access_token_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `access_token_issued_at` timestamp(0) NULL DEFAULT NULL,
  `access_token_expires_at` timestamp(0) NULL DEFAULT NULL,
  `access_token_metadata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `access_token_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `access_token_scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `oidc_id_token_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `oidc_id_token_issued_at` timestamp(0) NULL DEFAULT NULL,
  `oidc_id_token_expires_at` timestamp(0) NULL DEFAULT NULL,
  `oidc_id_token_metadata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `refresh_token_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `refresh_token_issued_at` timestamp(0) NULL DEFAULT NULL,
  `refresh_token_expires_at` timestamp(0) NULL DEFAULT NULL,
  `refresh_token_metadata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth2_authorization_consent
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent`  (
  `registered_client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `principal_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authorities` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`registered_client_id`, `principal_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth2_registered_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `client_id_issued_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `client_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_secret_expires_at` timestamp(0) NULL DEFAULT NULL,
  `client_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `client_authentication_methods` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authorization_grant_types` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `redirect_uris` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `client_settings` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token_settings` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
