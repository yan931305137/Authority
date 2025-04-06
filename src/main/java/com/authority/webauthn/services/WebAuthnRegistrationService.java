package com.authority.webauthn.services;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.validator.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import com.authority.webauthn.config.WebAuthnConfiguration;
import com.authority.webauthn.dto.request.CredentialRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * WebAuthn注册服务类，提供WebAuthn注册相关的功能。
 * 
 * @author [Your Name]
 * @version 1.0
 */
@Service
public class WebAuthnRegistrationService {

    /**
     * 公钥凭据参数列表。
     */
    final List<PublicKeyCredentialParameters> pubKeyCredParams;
    
    /**
     * WebAuthn管理器实例。
     */
    final WebAuthnManager webAuthnManager;

    /**
     * WebAuthn配置实例。
     */
    private final WebAuthnConfiguration webAuthnConfiguration;

    /**
     * 构造函数，初始化WebAuthn管理器和配置。
     * 
     * @param webAuthnConfiguration WebAuthn配置实例。
     */
    public WebAuthnRegistrationService(WebAuthnConfiguration webAuthnConfiguration) {

        webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();

        pubKeyCredParams = new ArrayList<>();
        pubKeyCredParams.add(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256));
        pubKeyCredParams.add(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES384));
        pubKeyCredParams.add(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES512));
        pubKeyCredParams.add(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256));
        pubKeyCredParams.add(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS384));
        pubKeyCredParams.add(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS512));

        this.webAuthnConfiguration = webAuthnConfiguration;
    }

    /**
     * 请求凭据。
     * 
     * @param username 用户名。
     * @param request   HttpServletRequest实例。
     * @return PublicKeyCredentialCreationOptions实例。
     */
    public PublicKeyCredentialCreationOptions requestCredentials(String username, HttpServletRequest request) {

        Challenge challenge = new DefaultChallenge(request.getSession().getId().getBytes());
        PublicKeyCredentialRpEntity rp =
                new PublicKeyCredentialRpEntity(webAuthnConfiguration.getRpId(), webAuthnConfiguration.getRpId());

        PublicKeyCredentialUserEntity user = new PublicKeyCredentialUserEntity(username.getBytes(),
                username,
                username);

        UserVerificationRequirement userVerificationRequirement = UserVerificationRequirement.PREFERRED;

        List<PublicKeyCredentialDescriptor> excludeCredentials = Collections.emptyList();

        AuthenticatorSelectionCriteria authenticatorSelectionCriteria =
                new AuthenticatorSelectionCriteria(
                        webAuthnConfiguration.getAuthenticatorAttachment(),
                        true,
                        userVerificationRequirement);

        PublicKeyCredentialCreationOptions credentialCreationOptions = new PublicKeyCredentialCreationOptions(
                rp,
                user,
                challenge,
                pubKeyCredParams,
                webAuthnConfiguration.getTimeout(),
                excludeCredentials,
                authenticatorSelectionCriteria,
                webAuthnConfiguration.getAttestationConveyancePreference(),
                null
        );

        return credentialCreationOptions;
    }

    /**
     * 处理凭据。
     * 
     * @param credentialRequest 凭据请求实例。
     * @param request           HttpServletRequest实例。
     * @return CredentialRecord实例。
     */
    public CredentialRecord processCredentials(CredentialRequest credentialRequest, HttpServletRequest request) {

        Challenge challenge = new DefaultChallenge(request.getSession().getId().getBytes());
        Origin origin = new Origin(webAuthnConfiguration.getOriginUrl());

        String clientDataJSONStr = credentialRequest.getResponse().getClientDataJSON();
        String attestationObjectStr = credentialRequest.getResponse().getAttestationObject();

        byte[] clientDataJSON = Base64.getUrlDecoder().decode(clientDataJSONStr);
        byte[] attestationObject = Base64.getUrlDecoder().decode(attestationObjectStr);
        byte[] tokenBindingId = null;

        ServerProperty serverProperty =
                new ServerProperty(origin, webAuthnConfiguration.getRpId(), challenge, tokenBindingId);

        boolean userVerificationRequired = false;
        boolean userPresenceRequired = true;

        RegistrationRequest registrationRequest = new RegistrationRequest(attestationObject, clientDataJSON);
        RegistrationParameters registrationParameters =
                new RegistrationParameters(serverProperty, this.pubKeyCredParams, userVerificationRequired, userPresenceRequired);

        RegistrationData registrationData;
        try {
            registrationData = webAuthnManager.parse(registrationRequest);
        } catch (DataConversionException e) {
            // If you would like to handle WebAuthn data structure parse error, please catch DataConversionException
            throw e;
        }
        try {
            webAuthnManager.validate(registrationData, registrationParameters);
        } catch (ValidationException e) {
            // If you would like to handle WebAuthn data validation error, please catch ValidationException
            throw e;
        }

        // please persist Authenticator object, which will be used in the authentication process.
        CredentialRecord credentialRecord =
                new CredentialRecordImpl( // You may create your own Authenticator implementation to save friendly authenticator name
                        registrationData.getAttestationObject(),
                        registrationData.getCollectedClientData(),
                        registrationData.getClientExtensions(),
                        registrationData.getTransports()
                );
        return credentialRecord;
    }
}