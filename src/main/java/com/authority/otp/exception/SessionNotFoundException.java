package com.authority.otp.exception;

import com.authority.exception.NotFoundException;

/**
 * 会话未找到异常类。
 */
public class SessionNotFoundException extends NotFoundException {

    /**
     * 构造函数，初始化异常信息为"session not found"。
     */
    public SessionNotFoundException() {
        super("session not found");
    }
}
