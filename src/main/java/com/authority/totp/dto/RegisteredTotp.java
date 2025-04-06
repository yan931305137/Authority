package com.authority.totp.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * RegisteredTotp实体类，用于存储已注册的TOTP信息。
 */
@Entity(name = "registered_totp")
@Data
public class RegisteredTotp {
    /**
     * 用户名，作为主键。
     */
    @Id
    private String username;

    /**
     * 私钥，用于生成TOTP验证码。
     * TODO: 添加加密和解密功能。
     */
    @Column
    private String secret;
}
