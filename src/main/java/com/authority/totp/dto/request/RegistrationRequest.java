package com.authority.totp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 注册请求模型
 */
@Data
public class RegistrationRequest {
    /**
     * 用户名
     */
    @NotEmpty
    private String username;
}
