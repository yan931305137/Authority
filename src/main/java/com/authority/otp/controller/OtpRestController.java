package com.authority.otp.controller;

import com.authority.exception.NotFoundException;
import com.authority.otp.dto.request.SendOtpRequest;
import com.authority.otp.dto.response.SendOtpResponse;
import com.authority.otp.dto.request.VerifyOtpRequest;
import com.authority.otp.dto.response.VerifyOtpResponse;
import com.authority.otp.exception.FrequentSendingForbidden;
import com.authority.otp.services.OtpService;
import com.authority.otp.exception.OtpVerifyAttemptsExceeded;
import com.authority.otp.exception.SendOtpException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OTP控制器，处理OTP发送和验证请求。
 */
@RestController
@AllArgsConstructor
@RequestMapping("/otp/v1")
public class OtpRestController {

    private final OtpService otpService;

    /**
     * 发送OTP请求。
     * 
     * @param sendOTPRequest 发送OTP请求对象
     * @return 发送OTP的结果
     * @throws NotFoundException     如果发送者或目的地不存在，则抛出此异常
     * @throws SendOtpException       如果发送OTP失败，则抛出此异常
     * @throws FrequentSendingForbidden 如果发送OTP过于频繁，则抛出此异常
     */
    @PostMapping("/send")
    public SendOtpResponse send(@RequestBody @Valid SendOtpRequest sendOTPRequest)
            throws NotFoundException, SendOtpException, FrequentSendingForbidden {
        return otpService.send(sendOTPRequest.getSender(), sendOTPRequest.getDestination());
    }

    /**
     * 验证OTP请求。
     * 
     * @param verifyOTPRequest 验证OTP请求对象
     * @return 验证OTP的结果
     * @throws NotFoundException         如果会话ID不存在，则抛出此异常
     * @throws OtpVerifyAttemptsExceeded 如果验证尝试次数超过限制，则抛出此异常
     */
    @PostMapping("/verify")
    public VerifyOtpResponse verify(@RequestBody @Valid VerifyOtpRequest verifyOTPRequest)
            throws NotFoundException, OtpVerifyAttemptsExceeded {
        return otpService.verify(verifyOTPRequest.getSessionId(), verifyOTPRequest.getOtp());
    }
}
