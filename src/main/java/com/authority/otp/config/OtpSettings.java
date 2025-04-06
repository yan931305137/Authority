package com.authority.otp.config;

import com.authority.otp.services.OtpSender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.ApplicationContext;

/**
 * OTP设置类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtpSettings {

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 名称
     */
    private String name;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 消息模板
     */
    private String messageTemplate;

    /**
     * OTP长度
     */
    private int otpLength;

    /**
     * 是否使用字母
     */
    private boolean useLetters;

    /**
     * 是否使用数字
     */
    private boolean useDigits;

    /**
     * OTP有效期（分钟）
     */
    private long ttlMinutes;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 根据ApplicationContext获取OtpSender实例
     * @param applicationContext 应用上下文
     * @return OtpSender实例
     */
    public OtpSender getOtpSender(ApplicationContext applicationContext) {
        return applicationContext.getBean(sender, OtpSender.class);
    }
}
