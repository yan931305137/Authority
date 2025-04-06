package com.authority.totp.config;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * TOTP配置类，用于生成TOTP验证码。
 */
@Configuration
public class TotpBeansConfiguration {
    /**
     * 创建TOTP生成器的Bean。
     * @param totpConfiguration TOTP配置对象。
     * @return TOTP生成器实例。
     */
    @Bean(name = "totpGenerator")
    public TimeBasedOneTimePasswordGenerator totpGenerator(TotpConfiguration totpConfiguration) {
        // 根据配置生成Duration对象，表示每个TOTP码的有效期。
        final Duration duration = Duration.ofSeconds(totpConfiguration.getPeriod());
        // 获取TOTP码的长度。
        final Integer length = totpConfiguration.getDigits();
        // 构造Hmac算法名称，用于生成TOTP码。
        final String algorithm = "Hmac".concat(totpConfiguration.getAlgorithm());
        // 创建并返回TOTP生成器实例。
        return new TimeBasedOneTimePasswordGenerator(duration, length, algorithm);
    }
}
