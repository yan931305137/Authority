package com.authority.webauthn.dto.request;

import lombok.Data;

/**
 * 挑战请求类
 */
@Data
public class ChallengeRequest {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 验证类型
     */
    private String attestationType;
    /**
     * 认证器类型
     */
    private String authenticatorType;
}
