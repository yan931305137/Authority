package com.authority.otp.exception;

/**
 * FrequentSendingForbidden类继承自BadRequestException，用于表示频繁发送OTP的操作被禁止。
 */
public class FrequentSendingForbidden extends BadRequestException {
    /**
     * 构造函数，初始化异常信息为“Frequent OTP send forbidden”。
     */
    public FrequentSendingForbidden() {
        super("Frequent OTP send forbidden");
    }
}
