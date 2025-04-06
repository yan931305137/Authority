package com.authority.otp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OTP配置类，用于配置OTP相关属性。
 */
@Configuration
@ConfigurationProperties(prefix = "otp")
@Data
public class OtpConfiguration {

    private Integer attempts;
    private Integer resendAllowedAfterMinutes;
    private List<OtpSettings> settings;

    /**
     * 获取指定ID的OTP设置。
     *
     * @param settingId 设置ID。
     * @return 对应的OTP设置。
     */
    public OtpSettings getSetting(String settingId) {
        return settings.stream()
                .filter(s -> s.getId().equals(settingId))
                .findFirst()
                .orElseThrow();
    }
}
