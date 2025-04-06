package com.authority.webauthn.services;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.validator.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import com.authority.webauthn.config.WebAuthnConfiguration;
import com.authority.webauthn.dto.request.AssertRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * WebAuthn登录服务类，提供WebAuthn登录相关的功能。
 */
@Service
public class WebAuthnLoginService {

    /**
     * 用户验证要求，设置为首选。
     */
    private final UserVerificationRequirement userVerificationRequirement = UserVerificationRequirement.PREFERRED;

    /**
     * WebAuthn管理器实例。
     */
    private final WebAuthnManager webAuthnManager;

    /**
     * WebAuthn配置实例。
     */
    private final WebAuthnConfiguration webAuthnConfiguration;

    /**
     * 构造函数，初始化WebAuthn管理器和配置。
     * @param webAuthnConfiguration WebAuthn配置实例。
     */
    public WebAuthnLoginService(WebAuthnConfiguration webAuthnConfiguration) {
        webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
        this.webAuthnConfiguration = webAuthnConfiguration;
    }

    /**
     * 请求凭证，用于WebAuthn登录过程。
     * @param username 用户名。
     * @param request HTTP请求对象。
     * @param authenticators 认证器记录集。
     * @return PublicKeyCredentialRequestOptions对象，包含了请求凭证的选项。
     */
    public PublicKeyCredentialRequestOptions requestCredentials(String username, HttpServletRequest request,
                                                                Set<CredentialRecord> authenticators) {

        Challenge challenge = new DefaultChallenge(request.getSession().getId().getBytes());

        List<PublicKeyCredentialDescriptor> allowCredentials = new ArrayList<>();

        for(CredentialRecord authenticator : authenticators) {
            PublicKeyCredentialDescriptor publicKeyCredentialDescriptor = new PublicKeyCredentialDescriptor(
                    PublicKeyCredentialType.PUBLIC_KEY,
                    authenticator.getAttestedCredentialData().getCredentialId(),
                    authenticator.getTransports()
            );
            allowCredentials.add(publicKeyCredentialDescriptor);
        }

        PublicKeyCredentialRequestOptions publicKeyCredentialRequestOptions = new PublicKeyCredentialRequestOptions(
                challenge, webAuthnConfiguration.getTimeout(),
                webAuthnConfiguration.getRpId(),
                null, userVerificationRequirement, null
        );

        return publicKeyCredentialRequestOptions;
    }

    /**
     * 处理凭证，用于WebAuthn登录过程的凭证验证。
     * @param request HTTP请求对象。
     * @param assertRequest 断言请求对象。
     * @param credentialRecords 认证器记录集。
     * @return AuthenticatorData对象，包含了认证器数据。
     */
    public AuthenticatorData<?> processCredentials(HttpServletRequest request, AssertRequest assertRequest, Set<CredentialRecord> credentialRecords) {

        byte[] id = Base64.getUrlDecoder().decode(assertRequest.getId());

        byte[] userHandle =  Base64.getUrlDecoder().decode(assertRequest.getResponse().getUserHandle());
        byte[] clientDataJSON = Base64.getUrlDecoder().decode(assertRequest.getResponse().getClientDataJSON());
        byte[] authenticatorData =  Base64.getUrlDecoder().decode(assertRequest.getResponse().getAuthenticatorData());
        byte[] signature =  Base64.getUrlDecoder().decode(assertRequest.getResponse().getSignature());

        Origin origin = new Origin(webAuthnConfiguration.getOriginUrl());

        Challenge challenge = new DefaultChallenge(request.getSession().getId().getBytes());

        byte[] tokenBindingId = null;
        ServerProperty serverProperty = new ServerProperty(origin, webAuthnConfiguration.getRpId(), challenge, tokenBindingId);
        List<byte[]> allowCredentials = null;
        boolean userVerificationRequired = false;
        boolean userPresenceRequired = true;

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                id, userHandle, authenticatorData, clientDataJSON, null, signature
        );

        CredentialRecord credentialRecord = credentialRecords.stream().filter(cr ->
                Objects.deepEquals(cr.getAttestedCredentialData().getCredentialId(), id))
                .findFirst().orElse(null);

        AuthenticationParameters authenticationParameters =
                new AuthenticationParameters(
                        serverProperty,
                        credentialRecord,
                        allowCredentials,
                        userVerificationRequired,
                        userPresenceRequired
                );

        AuthenticationData authenticationData;
        try {
            authenticationData = webAuthnManager.parse(authenticationRequest);
        } catch (DataConversionException e) {
            // 如果您想处理WebAuthn数据结构解析错误，请捕获DataConversionException
            throw e;
        }
        try {
            webAuthnManager.validate(authenticationData, authenticationParameters);
        } catch (ValidationException e) {
            // 如果您想处理WebAuthn数据验证错误，请捕获ValidationException
            throw e;
        }
// 请更新认证器记录的计数 TODO
//        updateCounter(
//                authenticationData.getCredentialId(),
//                authenticationData.getAuthenticatorData().getSignCount()
//        );

        return authenticationData.getAuthenticatorData();

    }
}
