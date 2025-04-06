package com.authority.otp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送OTP结果类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpResponse {
    /**
     * 会话ID
     */
    private String sessionId;
    /**
     * 目的地
     */
    private String destination;
    /**
     * 允许重发时间
     */
    private Long resendAllowedAt;
    /**
     * 剩余尝试次数
     */
    private Integer remainingAttempts;
}
