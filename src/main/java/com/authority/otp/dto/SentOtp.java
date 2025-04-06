package com.authority.otp.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

/**
 * 此类表示已发送的OTP实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "sent_otp")
public class SentOtp {

    /**
     * 会话ID
     */
    @Id
    @Column(name = "session_id")
    private UUID sessionId;

    /**
     * OTP
     */
    private String otp;

    /**
     * 过期时间
     */
    private long expireTime;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 上次发送时间
     */
    private long lastSentAt;

    /**
     * 尝试次数
     */
    private Integer attempts;

}
