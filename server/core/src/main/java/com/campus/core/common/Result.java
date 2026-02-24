package com.campus.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;

    public static Result<Void> success() {
        return result(200, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return result(200, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return result(200, message, data);
    }

    public static Result<Void> error() {
        return result(500, "操作失败", null);
    }

    public static Result<Void> error(String message) {
        return result(500, message, null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return result(code, message, null);
    }

    public static <T> Result<T> result(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
