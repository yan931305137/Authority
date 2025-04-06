package com.authority.otp.services.impl;

import com.authority.otp.services.OtpSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * TwilioOTP发送器实现类
 */
@Service("TwilioOTPSender")
@Log4j2
@ConditionalOnProperty(value = "TWILIO_ACCOUNT_SID")
public class TwilioOtpSender implements OtpSender {

    /**
     * Twilio消息服务SID
     */
    @Value("#{environment.TWILIO_MESSAGING_SERVICE_SID}")
    private String MESSAGING_SERVICE_SID;

    /**
     * Twilio账户SID
     */
    @Value("#{environment.TWILIO_ACCOUNT_SID}")
    private String ACCOUNT_SID;

    /**
     * Twilio认证令牌
     */
    @Value("#{environment.TWILIO_AUTH_TOKEN}")
    private String AUTH_TOKEN;

    /**
     * 初始化Twilio
     */
    @PostConstruct
    private void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * 发送OTP
     * @param destination 目标手机号
     * @param messageBody 消息体
     * @param messageTitle 消息标题
     */
    @Override
    public void sendOTP(String destination, String messageBody, String messageTitle) {
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(destination),
                MESSAGING_SERVICE_SID,
                messageBody)
                .create();
        log.info("sent message {}",message.getSid());
    }
}
