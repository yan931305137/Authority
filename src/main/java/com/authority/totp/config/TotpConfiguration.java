package com.authority.totp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TOTP配置类
 */
@Configuration
@ConfigurationProperties(prefix = "totp")
@Data
public class TotpConfiguration {
    /**
     * 发行者
     */
    private String issuer;
    /**
     * 发行者标签
     */
    private String issuerLabel;

    /**
     * 数字
     */
    private Integer digits = 6;

    /**
     * 算法
     */
    private final String algorithm = "SHA1";

    /**
     * 周期
     */
    private final Integer period = 30;
}
