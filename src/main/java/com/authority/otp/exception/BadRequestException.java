package com.authority.otp.exception;

public class BadRequestException extends Exception {
    /**
     * 构造一个新的BadRequestException对象。
     * 
     * @param message 异常信息
     */
    public BadRequestException(String message) {
        super(message);
    }
}
