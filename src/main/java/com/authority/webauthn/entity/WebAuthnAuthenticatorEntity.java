package com.authority.webauthn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * WebAuthnAuthenticatorEntity实体类，用于存储WebAuthn认证器信息。
 * 
 */
@Entity
@Table(name = "webauthn_authenticators")
@IdClass(WebAuthnAuthenticatorEntity.class)
@Getter
@Setter
public class WebAuthnAuthenticatorEntity implements Serializable {

    /**
     * 用户名，作为复合主键的一部分。
     */
    @Id
    @Column(name = "username")
    private String username;

    /**
     * 认证器，作为复合主键的一部分。
     */
    @Id
    @Column(name = "authenticator")
    private String authenticator;

}
