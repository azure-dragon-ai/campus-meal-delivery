package com.campus.core.constant;

/**
 * 系统常量
 */
public interface SystemConstants {

    /**
     * JWT 相关
     */
    String JWT_HEADER = "Authorization";
    String JWT_PREFIX = "Bearer ";
    String JWT_SECRET = "campus-meal-delivery-secret-key-2024";
    long JWT_EXPIRATION = 24 * 60 * 60 * 1000L; // 24 小时

    /**
     * 用户类型
     */
    String USER_TYPE_ADMIN = "ADMIN";
    String USER_TYPE_TEACHER = "TEACHER";
    String USER_TYPE_PARENT = "PARENT";

    /**
     * 状态常量
     */
    Integer STATUS_DISABLED = 0;
    Integer STATUS_NORMAL = 1;

    /**
     * 菜单类型
     */
    Integer MENU_TYPE_DIR = 1;
    Integer MENU_TYPE_MENU = 2;
    Integer MENU_TYPE_BUTTON = 3;

    /**
     * 就餐人角色
     */
    Integer DINER_ROLE_STUDENT = 1;
    Integer DINER_ROLE_TEACHER = 2;
    Integer DINER_ROLE_STAFF = 3;

    /**
     * 请假状态
     */
    Integer LEAVE_STATUS_PENDING = 0;
    Integer LEAVE_STATUS_APPROVED = 1;
    Integer LEAVE_STATUS_REJECTED = 2;

    /**
     * 密码加密强度
     */
    int BCRYPT_strength = 10;
}
