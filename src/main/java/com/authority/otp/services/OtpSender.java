package com.authority.otp.services;

import org.springframework.stereotype.Service;

import com.authority.otp.exception.SendOtpException;

/**
 * OTP发送器接口，定义了发送OTP的方法。
 */
@Service
public interface OtpSender {
    /**
     * 发送OTP到指定的目的地。
     * 
     * @param destination OTP的目的地，例如手机号码或邮箱。
     * @param messageBody OTP的消息体。
     * @param messageTitle OTP的标题。
     * @throws SendOtpException 发送OTP时可能抛出的异常。
     */
    void sendOTP(String destination, String messageBody, String messageTitle) throws SendOtpException;
}
