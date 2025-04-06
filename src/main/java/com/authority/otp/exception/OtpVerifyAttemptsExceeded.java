package com.authority.otp.exception;

/**
 * OTP验证尝试次数超过限制时抛出的异常。
 */
public class OtpVerifyAttemptsExceeded extends BadRequestException {
    /**
     * 构造函数，初始化异常信息。
     */
    public OtpVerifyAttemptsExceeded() {
        super("OTP verify attempts exceeded");
    }
}
