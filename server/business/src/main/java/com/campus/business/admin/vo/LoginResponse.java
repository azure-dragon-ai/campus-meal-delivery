package com.campus.business.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 登录响应 VO
 */
@Data
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private Long userId;
    private String username;
    private String name;
    private String avatar;
    private List<String> roles;
    private List<MenuVO> menus;

    @Data
    public static class MenuVO implements Serializable {
        private Long id;
        private Long parentId;
        private String name;
        private String path;
        private String component;
        private String icon;
        private Integer type;
        private String permission;
        private Integer sort;
        private List<MenuVO> children;
    }
}
