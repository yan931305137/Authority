package com.authority.totp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 验证请求模型
 */
@Data
public class VerificationRequest {
    /**
     * 用户名
     */
    @NotEmpty
    private String username;

    /**
     * TOTP
     */
    @NotNull
    private Integer totp;

}
