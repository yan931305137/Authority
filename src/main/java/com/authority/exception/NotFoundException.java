package com.authority.exception;

/**
 * NotFoundException类，用于抛出资源未找到异常。
 */
public class NotFoundException extends Exception {
    /**
     * 构造方法，传入异常信息。
     * @param message 异常信息。
     */
    public NotFoundException(String message) {
        super(message);
    }
}
