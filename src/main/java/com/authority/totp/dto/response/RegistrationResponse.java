package com.authority.totp.dto.response;

import lombok.Data;

/**
 * 注册响应
 */
@Data
public class RegistrationResponse {
    /**
     * URI
     */
    private String uri;
    /**
     * QR码
     */
    private String qr;
}
