package com.authority.otp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证OTP请求模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequest {
    /**
     * 会话ID
     */
    @NotEmpty
    private String sessionId;

    /**
     * OTP验证码
     */
    @NotEmpty
    private String otp;
}
