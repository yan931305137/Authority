package com.authority.otp.services.impl;

import com.authority.otp.services.OtpSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * 一个简单的OTP发送器实现。
 */
@Service("DummyOTPSender")
@Log4j2
public class DummyOtpSender implements OtpSender {

    /**
     * 发送OTP消息。
     * 
     * @param destination 目标地址
     * @param messageBody 消息体
     * @param messageTitle 消息标题
     */
    @Override
    public void sendOTP(String destination, String messageBody, String messageTitle) {
        log.info("message: {}", messageBody);
    }
}
