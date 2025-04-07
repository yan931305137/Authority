package com.authority.otp.services;

import com.authority.otp.exception.*;
import com.authority.otp.utils.EmailGenerator;
import com.authority.otp.utils.OtpGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringSubstitutor;
import com.authority.exception.NotFoundException;
import com.authority.otp.config.OtpConfiguration;
import com.authority.otp.config.OtpSettings;
import com.authority.otp.dto.response.SendOtpResponse;
import com.authority.otp.dto.SentOtp;
import com.authority.otp.dto.response.VerifyOtpResponse;
import com.authority.otp.repository.SentOtpRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * OtpService类
 */
@Service
@AllArgsConstructor
@Log4j2
public class OtpService {

    private final OtpConfiguration otpConfiguration;
    private final SentOtpRepository sentOtpRepository;
    private final OtpGenerator otpGenerator;
    private final EmailGenerator emailGenerator;
    private final ApplicationContext applicationContext;

    /**
     * 发送OTP
     *
     * @param type        发送类型
     * @param destination 目的地
     * @return 发送结果
     * @throws NotFoundException
     * @throws SendOtpException
     * @throws FrequentSendingForbidden
     */
    public SendOtpResponse send(String type, String destination) throws NotFoundException, SendOtpException, FrequentSendingForbidden {
        final OtpSettings otpSettings;
        try {
            otpSettings = otpConfiguration.getSetting(type);
            if (otpSettings == null) {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException e) {
            log.warn("发送器 {} 未找到", type);
            throw new SenderNotFoundException();
        }

        final SentOtp sentOTP = otpGenerator.generateSentOTP(otpSettings, destination);
        final OtpSender otpSender;
        try {
            otpSender = otpSettings.getOtpSender(applicationContext);
        } catch (BeansException e) {
            log.warn("OTP发送器 {} 未找到", otpSettings.getSender());
            throw new SenderNotFoundException();
        }

        String fileContent = otpSettings.getMessageTemplate();
        final String messageTitle = otpSettings.getMessageTitle();
        validateFrequentSending(destination);

        if(type.equals("email")){
            fileContent = emailGenerator.EmailFilter(otpSettings);
        }

        final String messageBody = createMessage(fileContent, sentOTP.getOtp());

        otpSender.sendOTP(destination, messageBody, messageTitle);
        sentOTP.setAttempts(otpConfiguration.getAttempts());
        sentOtpRepository.save(sentOTP);

        return new SendOtpResponse(sentOTP.getSessionId().toString(), sentOTP.getDestination(), sentOTP.getExpireTime(), sentOTP.getAttempts());
    }

    /**
     * 验证OTP
     *
     * @param sessionId 会话ID
     * @param otp       OTP
     * @return 验证结果
     * @throws NotFoundException
     * @throws OtpVerifyAttemptsExceeded
     */
    public VerifyOtpResponse verify(String sessionId, String otp) throws NotFoundException, OtpVerifyAttemptsExceeded {
        final UUID sessionUUID;
        try {
            sessionUUID = UUID.fromString(sessionId);
        } catch (IllegalArgumentException e) {
            log.warn("session {} not found", sessionId);
            throw new SessionNotFoundException();
        }

        Optional<SentOtp> sentOtpOptional = sentOtpRepository.findById(sessionUUID);
        if (sentOtpOptional.isEmpty()) {
            log.warn("session {} not found", sessionId);
            throw new SessionNotFoundException();
        }

        SentOtp sentOtp = sentOtpOptional.get();

        if (sentOtp.getExpireTime() < System.currentTimeMillis()) {
            log.warn("session {} expired", sessionId);
            throw new SessionNotFoundException();
        }

        if (sentOtp.getAttempts() == 0) {
            throw new OtpVerifyAttemptsExceeded();
        }

        boolean result = sentOtp.getExpireTime() > System.currentTimeMillis()
                && sentOtp.getOtp().equals(otp);

        Integer remainingAttempts = null;
        if (!result) {
            remainingAttempts = sentOtp.getAttempts() - 1;
            remainingAttempts = remainingAttempts < 0 ? 0 : remainingAttempts;
            sentOtp.setAttempts(remainingAttempts);
            sentOtpRepository.save(sentOtp);
        }

        return new VerifyOtpResponse(result, remainingAttempts);
    }

    /**
     * 创建消息
     *
     * @param messageTemplate 消息模板
     * @param otp             OTP
     * @return 消息
     */
    private String createMessage(String messageTemplate, String otp) {
        final Map<String, String> values = new HashMap<>();
        values.put("otp", otp);
        StringSubstitutor sub = new StringSubstitutor(values);
        return sub.replace(messageTemplate);
    }

    /**
     * 验证频繁发送
     *
     * @param destination 目的地
     * @throws FrequentSendingForbidden
     */
    private void validateFrequentSending(String destination) throws FrequentSendingForbidden {
        if (otpConfiguration.getResendAllowedAfterMinutes() == null
                || otpConfiguration.getResendAllowedAfterMinutes() == 0) {
            return;
        }
        Optional<SentOtp> sentOtp = sentOtpRepository.findFirstByDestinationOrderByLastSentAtDesc(destination);
        if (sentOtp.isEmpty()) {
            return;
        }
        int resendAfterMilliseconds = otpConfiguration.getResendAllowedAfterMinutes() * 1000 * 60;
        long resendAllowedAt = sentOtp.get().getLastSentAt() + resendAfterMilliseconds;
        if (resendAllowedAt > System.currentTimeMillis()) {
            log.warn("frequent sending to {} forbidden", destination);
            throw new FrequentSendingForbidden();
        }
    }
}
