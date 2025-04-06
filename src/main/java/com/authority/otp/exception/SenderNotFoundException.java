package com.authority.otp.exception;

import com.authority.exception.NotFoundException;

/**
 * SenderNotFoundException类继承自NotFoundException，用于处理发送者未找到异常。
 */
public class SenderNotFoundException extends NotFoundException {

    /**
     * 构造方法，初始化异常信息为"sender not found"。
     */
    public SenderNotFoundException() {
        super("sender not found");
    }
}
