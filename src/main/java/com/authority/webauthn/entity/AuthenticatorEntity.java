package com.authority.webauthn.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.webauthn4j.converter.AttestedCredentialDataConverter;
import com.webauthn4j.converter.CollectedClientDataConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.AuthenticatorTransport;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.statement.AttestationStatement;
import com.webauthn4j.data.client.CollectedClientData;
import com.webauthn4j.data.extension.authenticator.AuthenticationExtensionsAuthenticatorOutputs;
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientOutputs;
import com.webauthn4j.data.extension.client.RegistrationExtensionClientOutput;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;
import java.util.Set;

/**
 * AuthenticatorEntity类，用于存储和转换认证器实体信息。
 */
@Getter
@Setter
public class AuthenticatorEntity {

    /**
     * 认证器证书数据，Base64编码后的字符串。
     */
    private String attestedCredentialData;

    /**
     * 认证声明语句，Base64编码后的字符串。
     */
    private String attestationStatement;

    /**
     * 认证器传输方式，JSON字符串。
     */
    private String transports;

    /**
     * 认证器扩展信息，Base64编码后的字符串。
     */
    private String authenticatorExtensions;

    /**
     * 客户端扩展信息，JSON字符串。
     */
    private String clientExtensions;

    /**
     * 计数器值。
     */
    private long counter;

    /**
     * UV初始化标志。
     */
    private Boolean uvInitialized;
    /**
     * 备份资格标志。
     */
    private Boolean backupEligible;
    /**
     * 备份状态标志。
     */
    private Boolean backedUp;

    /**
     * 收集的客户端数据，Base64编码后的字符串。
     */
    private String collectedClientData;

    /**
     * 对象转换器。
     */
    final static ObjectConverter objectConverter = new ObjectConverter();
    /**
     * 认证器证书数据转换器。
     */
    final static AttestedCredentialDataConverter attestedCredentialDataConverter = new AttestedCredentialDataConverter(objectConverter);
    /**
     * 收集的客户端数据转换器。
     */
    final static CollectedClientDataConverter collectedClientDataConverter = new CollectedClientDataConverter(objectConverter);

    /**
     * 从CredentialRecord对象转换为AuthenticatorEntity对象。
     * @param credentialRecord CredentialRecord对象。
     * @return 转换后的AuthenticatorEntity对象。
     */
    public static AuthenticatorEntity fromCredentialRecord(CredentialRecord credentialRecord) {

        AuthenticatorEntity authenticatorEntity = new AuthenticatorEntity();

        authenticatorEntity.setUvInitialized(credentialRecord.isUvInitialized());
        authenticatorEntity.setBackupEligible(credentialRecord.isBackupEligible());
        authenticatorEntity.setBackedUp(credentialRecord.isBackupEligible());

        byte[] serialized = attestedCredentialDataConverter.convert(credentialRecord.getAttestedCredentialData());
        authenticatorEntity.attestedCredentialData = Base64.getEncoder().encodeToString(serialized);

        AttestationStatementEnvelope attestationStatementEnvelope
                = new AttestationStatementEnvelope(credentialRecord.getAttestationStatement());
        byte[] serializedEnvelope = objectConverter.getCborConverter().writeValueAsBytes(attestationStatementEnvelope);
        authenticatorEntity.attestationStatement = Base64.getEncoder().encodeToString(serializedEnvelope);

        authenticatorEntity.transports = objectConverter.getJsonConverter().writeValueAsString(credentialRecord.getTransports());

        byte[] serializedAuthenticatorExtensions = objectConverter.getCborConverter().writeValueAsBytes(credentialRecord.getAuthenticatorExtensions());
        authenticatorEntity.authenticatorExtensions = Base64.getEncoder().encodeToString(serializedAuthenticatorExtensions);

        authenticatorEntity.clientExtensions = objectConverter.getJsonConverter().writeValueAsString(credentialRecord.getClientExtensions());
        authenticatorEntity.counter = credentialRecord.getCounter();

        authenticatorEntity.collectedClientData = collectedClientDataConverter.convertToBase64UrlString(credentialRecord.getClientData());

        return authenticatorEntity;
    }

    /**
     * 将AuthenticatorEntity对象转换为CredentialRecord对象。
     * @return 转换后的CredentialRecord对象。
     */
    public CredentialRecord toCredentialRecord() {
        byte[] acdSerialized = Base64.getDecoder().decode(this.attestedCredentialData);
        AttestedCredentialData acd = attestedCredentialDataConverter.convert(acdSerialized);

        byte[] aseSerialized = Base64.getDecoder().decode(this.attestationStatement);
        AttestationStatementEnvelope ase = objectConverter.getCborConverter().readValue(aseSerialized, AttestationStatementEnvelope.class);
        Set<AuthenticatorTransport> at = objectConverter.getJsonConverter().readValue(this.transports, new TypeReference<>() {
        });

        byte[] authExtSerialized = Base64.getDecoder().decode(this.authenticatorExtensions);
        AuthenticationExtensionsAuthenticatorOutputs<RegistrationExtensionAuthenticatorOutput> authExt
                = objectConverter.getCborConverter().readValue(authExtSerialized, new TypeReference<>() {
        });

        AuthenticationExtensionsClientOutputs<RegistrationExtensionClientOutput> ce =
                objectConverter.getJsonConverter().readValue(this.clientExtensions, new TypeReference<>() {
                });


        CollectedClientData collectedClientData = collectedClientDataConverter.convert(this.collectedClientData);

        return new CredentialRecordImpl(ase.getAttestationStatement(),
                this.uvInitialized,
                this.backupEligible,
                this.backedUp, this.counter, acd, authExt,  collectedClientData, ce, at);
    }

    /**
     * 将AuthenticatorEntity对象转换为JSON字符串。
     * @return JSON字符串。
     */
    public String toJson() {
        return objectConverter.getJsonConverter().writeValueAsString(this);
    }

    /**
     * 从JSON字符串转换为AuthenticatorEntity对象。
     * @param json JSON字符串。
     * @return 转换后的AuthenticatorEntity对象。
     */
    public static AuthenticatorEntity fromJson(String json) {
        return objectConverter.getJsonConverter().readValue(json, AuthenticatorEntity.class);
    }

    /**
     * 认证声明语句封装类。
     */
    static class AttestationStatementEnvelope {

        /**
         * 认证声明语句。
         */
        @JsonProperty("attStmt")
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "fmt"
        )
        private AttestationStatement attestationStatement;

        /**
         * 构造函数。
         * @param attestationStatement 认证声明语句。
         */
        @JsonCreator
        public AttestationStatementEnvelope(@JsonProperty("attStmt") AttestationStatement attestationStatement) {
            this.attestationStatement = attestationStatement;
        }

        /**
         * 获取格式。
         * @return 格式字符串。
         */
        @JsonProperty("fmt")
        public String getFormat() {
            return attestationStatement.getFormat();
        }

        /**
         * 获取认证声明语句。
         * @return 认证声明语句。
         */
        public AttestationStatement getAttestationStatement() {
            return attestationStatement;
        }
    }
}
