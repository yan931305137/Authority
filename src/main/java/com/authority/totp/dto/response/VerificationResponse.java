package com.authority.totp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证响应模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationResponse {
    /**
     * 验证是否有效
     */
    private boolean valid;
}
