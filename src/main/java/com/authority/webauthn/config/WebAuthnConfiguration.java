package com.authority.webauthn.config;

import com.webauthn4j.data.AttestationConveyancePreference;
import com.webauthn4j.data.AuthenticatorAttachment;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * WebAuthn配置类
 */
@Configuration
@Getter
public class WebAuthnConfiguration {
    /**
     * RP ID，用于标识WebAuthn的Relying Party
     */
    @Value("${webauthn.settings.rpId:localhost}")
    private String rpId;

    /**
     * 超时时间，单位毫秒
     */
    @Value("${webauthn.settings.timeout:60000}")
    private long timeout;

    /**
     * 来源URL，用于标识WebAuthn的来源
     */
    @Value("${webauthn.settings.origin}")
    private String originUrl;

    /**
     * 证书传递偏好
     */
    private AttestationConveyancePreference attestationConveyancePreference;

    /**
     * 设置证书传递偏好
     * @param strVal 证书传递偏好字符串
     */
    @Value("${webauthn.settings.attestationConveyancePreference:none}")
    private void setAttestationConveyancePreference(String strVal) {
        this.attestationConveyancePreference = AttestationConveyancePreference.create(strVal);
    }

    /**
     * 认证器附件
     */
    @Value("${webauthn.settings.authenticatorAttachment:#{null}}")
    private AuthenticatorAttachment authenticatorAttachment;
}
