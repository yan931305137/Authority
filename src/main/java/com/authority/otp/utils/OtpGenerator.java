package com.authority.otp.utils;

import com.authority.otp.config.OtpSettings;
import com.authority.otp.dto.SentOtp;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * OTP生成器类，用于生成并返回已发送的OTP对象。
 */
@Component
public class OtpGenerator {

    /**
     * 根据给定的OTP设置和目标地址生成并返回已发送的OTP对象。
     * 
     * @param otpSettings OTP设置对象，包含OTP长度、是否使用字母和数字等信息。
     * @param destination 目标地址，用于发送OTP的目的地。
     * @return 已发送的OTP对象，包含会话ID、过期时间、OTP码本身、目标地址和最后发送时间。
     */
    public SentOtp generateSentOTP(OtpSettings otpSettings, String destination) {
        // 确保OTP长度不超过数据库字段限制（10位）
        int otpLength = Math.min(otpSettings.getOtpLength(), 10);
        String otp = RandomStringUtils.random(otpLength, otpSettings.isUseLetters(), otpSettings.isUseDigits());
        SentOtp sentOTP = new SentOtp();
        sentOTP.setSessionId(UUID.randomUUID());
        sentOTP.setExpireTime(System.currentTimeMillis() + otpSettings.getTtlMinutes() * 60 * 1000);
        sentOTP.setOtp(otp);
        sentOTP.setDestination(destination);
        sentOTP.setLastSentAt(System.currentTimeMillis());
        return sentOTP;
    }
}
