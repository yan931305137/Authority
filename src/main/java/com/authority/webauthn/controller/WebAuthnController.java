package com.authority.webauthn.controller;

import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.data.PublicKeyCredentialCreationOptions;
import com.webauthn4j.data.PublicKeyCredentialRequestOptions;
import com.webauthn4j.data.attestation.authenticator.AuthenticatorData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.authority.webauthn.dto.request.AssertRequest;
import com.authority.webauthn.dto.request.CredentialRequest;
import com.authority.webauthn.repository.UserAuthenticatorRepository;
import com.authority.webauthn.services.WebAuthnLoginService;
import com.authority.webauthn.services.WebAuthnRegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * WebAuthn控制器类，负责处理WebAuthn相关的请求和响应。
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/webauthn/v1")
public class WebAuthnController {

    /**
     * 用户名会话属性键。
     */
    public static final String USERNAME_SESSION_ATTRIBUTE = "username";

    /**
     * WebAuthn注册服务。
     */
    private final WebAuthnRegistrationService webAuthnRegistrationService;

    /**
     * WebAuthn登录服务。
     */
    private final WebAuthnLoginService webAuthnLoginService;

    /**
     * 用户认证器仓库。
     */
    private final UserAuthenticatorRepository userAuthenticatorRepository;

    /**
     * 处理注册挑战请求。
     * @param request HttpServletRequest对象
     * @param username 用户名
     * @return PublicKeyCredentialCreationOptions对象
     */
    @GetMapping("/register/challenge/{username}")
    public PublicKeyCredentialCreationOptions challenge(HttpServletRequest request,
                                                        @PathVariable("username") String username) {
        PublicKeyCredentialCreationOptions credentialCreationOptions
                = webAuthnRegistrationService.requestCredentials(username, request);

        request.getSession().setAttribute(USERNAME_SESSION_ATTRIBUTE, username);  //authencticated user

        return credentialCreationOptions;
    }

    /**
     * 处理注册凭据请求。
     * @param credentialRequest CredentialRequest对象
     * @param request HttpServletRequest对象
     * @return Map对象
     */
    @PostMapping("/register/credential/")
    public Map<String, Object> registerCredential(@RequestBody CredentialRequest credentialRequest, HttpServletRequest request) {
        log.info("credential request:  {}", credentialRequest);

        String username = (String)request.getSession().getAttribute(USERNAME_SESSION_ATTRIBUTE);

        CredentialRecord credentialRecord = webAuthnRegistrationService.processCredentials(credentialRequest, request);

        userAuthenticatorRepository.save(username, credentialRecord);

        return Collections.singletonMap("credentialId", Base64.getUrlEncoder().encodeToString(credentialRecord.getAttestedCredentialData().getCredentialId()));
    }

    /**
     * 处理登录挑战请求。
     * @param request HttpServletRequest对象
     * @param username 用户名
     * @return PublicKeyCredentialRequestOptions对象
     */
    @RequestMapping("/login/challenge/{username}")
    public PublicKeyCredentialRequestOptions credentialRequest(HttpServletRequest request,
                                                               @PathVariable("username") String username) {

        Set<CredentialRecord> authenticators = userAuthenticatorRepository.load(username);
        PublicKeyCredentialRequestOptions credentialRequestOptions
                = webAuthnLoginService.requestCredentials(username, request, authenticators);

        request.getSession().setAttribute(USERNAME_SESSION_ATTRIBUTE, username);  //authencticated user

        return  credentialRequestOptions;
    }

    /**
     * 处理匿名登录挑战请求。
     * @param request HttpServletRequest对象
     * @return PublicKeyCredentialRequestOptions对象
     */
    @RequestMapping("/login/challenge/")
    public PublicKeyCredentialRequestOptions credentialAnonRequest(HttpServletRequest request) {

        PublicKeyCredentialRequestOptions credentialRequestOptions
                = webAuthnLoginService.requestCredentials("", request, Collections.emptySet());

        request.getSession().setAttribute(USERNAME_SESSION_ATTRIBUTE, "");  //authencticated user

        return  credentialRequestOptions;
    }

    /**
     * 处理登录凭据请求。
     * @param assertRequest AssertRequest对象
     * @param request HttpServletRequest对象
     * @return Map对象
     */
    @PostMapping("/login/credential/")
    public Map<String, Object> assertCredential(@RequestBody AssertRequest assertRequest, HttpServletRequest request) {

        log.info("assert request: {}", assertRequest);

        String username = (String)request.getSession().getAttribute(USERNAME_SESSION_ATTRIBUTE);

        Set<CredentialRecord> authenticators = userAuthenticatorRepository.load(username);

        AuthenticatorData<?> authenticatorData = webAuthnLoginService.processCredentials(request, assertRequest, authenticators);

        return Collections.singletonMap("response", authenticatorData);
    }

}
