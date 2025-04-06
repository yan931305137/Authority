package com.authority.otp.exception;

/**
 * 发送OTP时发生的异常。
 */
public class SendOtpException extends Exception {
    /**
     * 构造一个新的SendOtpException对象。
     */
    public SendOtpException() {
        super("发送OTP时发生异常");
    }
}
