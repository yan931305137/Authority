package com.authority.otp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 该类用于封装发送OTP请求的信息，包括目的地和发送者信息。
 */
@Data
public class SendOtpRequest {
    /**
     * 目的地号码，不能为空
     */
    @NotEmpty
    private String destination;
    /**
     * 发送者号码，不能为空
     */
    @NotEmpty
    private String sender;
}
