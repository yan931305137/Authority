package com.authority.otp.services.impl;

import com.authority.otp.config.MailConfig;
import com.authority.otp.services.OtpSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * EmailOtpSender类，用于发送OTP验证码的电子邮件。
 */
@Log4j2
@Service("EmailOTPSender")
@RequiredArgsConstructor
public class EmailOtpSender implements OtpSender {

    /**
     * Spring JavaMailSender对象，用于发送电子邮件。
     */
    private final JavaMailSender mailSender;

    private final MailConfig mailConfig;

    /**
     * 发送OTP验证码的电子邮件。
     *
     * @param destination  接收者的电子邮件地址。
     * @param messageBody  电子邮件的内容。
     * @param messageTitle 电子邮件的标题。
     */
    @Override
    public void sendOTP(String destination, String messageBody, String messageTitle) {
        // 检查邮件内容是否为空
        if (messageBody == null || messageBody.isEmpty() || messageTitle == null || messageTitle.isEmpty()) {
            log.error("邮件内容或标题不能为空");
            return; // 退出方法
        }
        // 使用MimeMessage发送HTML邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(mailConfig.getUsername());
            helper.setTo(destination);
            helper.setSubject(messageTitle);
            helper.setText(messageBody, true); // 设置为HTML内容
            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException ex) {
            log.error("发送电子邮件失败: {}", ex.getMessage(), ex); // 记录详细错误信息
        }
    }
}
