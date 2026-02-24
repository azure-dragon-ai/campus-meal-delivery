-- 校园配餐系统数据库初始化脚本
-- MySQL 8.0+

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `campus_meal` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `campus_meal`;

-- ========================
-- 基础系统表
-- ========================

-- 管理员表
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `name` varchar(50) NOT NULL COMMENT '姓名',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` varchar(500) DEFAULT NULL COMMENT '头像',
    `status` tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录 IP',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `name` varchar(50) NOT NULL COMMENT '角色名称',
    `code` varchar(50) NOT NULL COMMENT '角色编码',
    `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
    `status` tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `parent_id` bigint DEFAULT 0 COMMENT '父菜单 ID',
    `name` varchar(50) NOT NULL COMMENT '菜单名称',
    `path` varchar(255) DEFAULT NULL COMMENT '路由路径',
    `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
    `icon` varchar(50) DEFAULT NULL COMMENT '图标',
    `type` tinyint DEFAULT 1 COMMENT '类型 1-目录 2-菜单 3-按钮',
    `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
    `sort` int DEFAULT 0 COMMENT '排序',
    `visible` tinyint DEFAULT 1 COMMENT '是否可见 0-否 1-是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- 管理员角色关联表
DROP TABLE IF EXISTS `sys_admin_role`;
CREATE TABLE `sys_admin_role` (
    `admin_id` bigint NOT NULL COMMENT '管理员 ID',
    `role_id` bigint NOT NULL COMMENT '角色 ID',
    PRIMARY KEY (`admin_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员角色关联表';

-- 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `role_id` bigint NOT NULL COMMENT '角色 ID',
    `menu_id` bigint NOT NULL COMMENT '菜单 ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- ========================
-- 业务表
-- ========================

-- 学校表
DROP TABLE IF EXISTS `biz_school`;
CREATE TABLE `biz_school` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `name` varchar(100) NOT NULL COMMENT '学校名称',
    `code` varchar(50) NOT NULL COMMENT '学校编码',
    `address` varchar(255) DEFAULT NULL COMMENT '地址',
    `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
    `status` tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学校表';

-- 班级表
DROP TABLE IF EXISTS `biz_class`;
CREATE TABLE `biz_class` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `school_id` bigint NOT NULL COMMENT '学校 ID',
    `name` varchar(50) NOT NULL COMMENT '班级名称',
    `grade` varchar(20) DEFAULT NULL COMMENT '年级',
    `teacher_id` bigint DEFAULT NULL COMMENT '班主任 ID',
    `status` tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_school_id` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级表';

-- 教师表
DROP TABLE IF EXISTS `biz_teacher`;
CREATE TABLE `biz_teacher` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `name` varchar(50) NOT NULL COMMENT '姓名',
    `phone` varchar(20) NOT NULL COMMENT '手机号',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `school_id` bigint NOT NULL COMMENT '学校 ID',
    `class_id` bigint DEFAULT NULL COMMENT '班级 ID',
    `position` varchar(50) DEFAULT NULL COMMENT '职务',
    `status` tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_school_id` (`school_id`),
    KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师表';

-- 家长表
DROP TABLE IF EXISTS `biz_parent`;
CREATE TABLE `biz_parent` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `name` varchar(50) NOT NULL COMMENT '姓名',
    `phone` varchar(20) NOT NULL COMMENT '手机号',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `status` tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家长表';

-- 就餐人表
DROP TABLE IF EXISTS `biz_diner`;
CREATE TABLE `biz_diner` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `parent_id` bigint NOT NULL COMMENT '家长 ID',
    `school_id` bigint NOT NULL COMMENT '学校 ID',
    `class_id` bigint DEFAULT NULL COMMENT '班级 ID',
    `name` varchar(50) NOT NULL COMMENT '姓名',
    `role` tinyint NOT NULL COMMENT '角色 1-学生 2-教师 3-校干',
    `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
    `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
    `bank_account_name` varchar(50) DEFAULT NULL COMMENT '退款收款人',
    `bank_name` varchar(100) DEFAULT NULL COMMENT '开户行',
    `bank_subbranch` varchar(100) DEFAULT NULL COMMENT '支行名称',
    `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
    `status` tinyint DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_school_id` (`school_id`),
    KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='就餐人表';

-- 请假表
DROP TABLE IF EXISTS `biz_leave`;
CREATE TABLE `biz_leave` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `diner_id` bigint NOT NULL COMMENT '就餐人 ID',
    `diner_name` varchar(50) NOT NULL COMMENT '就餐人姓名',
    `phone` varchar(20) NOT NULL COMMENT '联系电话',
    `leave_date` date NOT NULL COMMENT '请假日期',
    `reason` varchar(500) DEFAULT NULL COMMENT '请假原因',
    `status` tinyint DEFAULT 0 COMMENT '状态 0-待审核 1-通过 2-拒绝',
    `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
    `audit_remark` varchar(255) DEFAULT NULL COMMENT '审核备注',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_diner_id` (`diner_id`),
    KEY `idx_leave_date` (`leave_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='请假表';

SET FOREIGN_KEY_CHECKS = 1;

-- 初始化数据
-- 默认管理员 (密码：admin123)
INSERT INTO `sys_admin` (`username`, `password`, `name`, `phone`, `status`)
VALUES ('admin', '$2a$10$XoLvF5C2dz9.7xWqTq3hGu0YqZvNl8z4X5K6J7H8M9P0Q1R2S3T4U', '系统管理员', '13800138000', 1);

-- 默认角色
INSERT INTO `sys_role` (`name`, `code`, `description`) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有所有权限'),
('普通管理员', 'ADMIN', '普通管理员权限'),
('教师', 'TEACHER', '教师权限'),
('家长', 'PARENT', '家长权限');

-- 默认菜单
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `icon`, `type`, `permission`, `sort`) VALUES
(0, '系统管理', '/system', 'Layout', 'setting', 1, NULL, 100),
(1, '管理员管理', '/system/admin', 'system/admin', 'user', 2, 'system:admin:list', 1),
(1, '角色管理', '/system/role', 'system/role', 'peoples', 2, 'system:role:list', 2),
(1, '菜单管理', '/system/menu', 'system/menu', 'tree-table', 2, 'system:menu:list', 3),
(0, '学校管理', '/school', 'Layout', 'school', 1, NULL, 50),
(4, '学校列表', '/school/list', 'school/list', 'list', 2, 'school:list', 1),
(4, '班级管理', '/school/class', 'school/class', 'peoples', 2, 'school:class:list', 2),
(0, '人员管理', '/person', 'Layout', 'team', 1, NULL, 40),
(7, '教师管理', '/person/teacher', 'person/teacher', 'user', 2, 'person:teacher:list', 1),
(7, '家长管理', '/person/parent', 'person/parent', 'user', 2, 'person:parent:list', 2),
(7, '就餐人管理', '/person/diner', 'person/diner', 'user', 2, 'person:diner:list', 3),
(0, '请假管理', '/leave', 'Layout', 'date', 1, NULL, 30),
(10, '请假列表', '/leave/list', 'leave/list', 'list', 2, 'leave:list', 1);
