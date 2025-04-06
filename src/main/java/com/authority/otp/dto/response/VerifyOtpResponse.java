package com.authority.otp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证OTP结果类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyOtpResponse {
    /**
     * 验证是否有效
     */
    private boolean valid;
    /**
     * 剩余尝试次数
     */
    private Integer remainingAttempts;
}
