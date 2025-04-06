package com.authority.totp.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.authority.totp.dto.request.RegistrationRequest;
import com.authority.totp.dto.response.RegistrationResponse;
import com.authority.totp.dto.request.VerificationRequest;
import com.authority.totp.dto.response.VerificationResponse;
import com.authority.totp.services.QrService;
import com.authority.totp.services.TotpService;
import com.authority.totp.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * TotpRestController类，处理TOTP注册和验证请求。
 */
@RestController
@AllArgsConstructor
@RequestMapping("/totp/v1")
public class TotpRestController {

    private TotpService totpService; // TOTP服务实例
    private QrService qrService; // QR码服务实例

    /**
     * 处理注册请求，生成并返回URI和QR码。
     * @param registrationRequest 注册请求对象
     * @return 注册响应对象，包含URI和QR码
     */
    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        final URI uri = totpService.register(registrationRequest.getUsername());
        final String qr = qrService.generateQr(uri.toString());
        final RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setUri(uri.toString());
        registrationResponse.setQr(qr);
        return registrationResponse;
    }

    /**
     * 处理验证请求，验证TOTP是否有效。
     * @param verificationRequest 验证请求对象
     * @return 验证响应对象，包含验证结果
     * @throws UserNotFoundException 用户不存在时抛出此异常
     */
    @PostMapping("/verify")
    public VerificationResponse verify(@RequestBody @Valid VerificationRequest verificationRequest) throws UserNotFoundException {
        boolean valid = totpService.verify(verificationRequest.getUsername(), verificationRequest.getTotp());
        return new VerificationResponse(valid);
    }
}
