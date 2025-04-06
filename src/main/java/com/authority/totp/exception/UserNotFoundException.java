package com.authority.totp.exception;

import com.authority.exception.NotFoundException;

/**
 * 用户未找到异常类。
 */
public class UserNotFoundException extends NotFoundException {
    /**
     * 构造函数，初始化异常信息为"User not found"。
     */
    public UserNotFoundException() {
        super("User not found");
    }
}
