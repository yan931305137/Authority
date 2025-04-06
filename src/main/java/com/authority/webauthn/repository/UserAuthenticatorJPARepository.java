package com.authority.webauthn.repository;

import com.authority.webauthn.entity.WebAuthnAuthenticatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户认证器JPA存储库
 */
public interface UserAuthenticatorJPARepository extends JpaRepository<WebAuthnAuthenticatorEntity, WebAuthnAuthenticatorEntity> {
    /**
     * 根据用户名获取所有认证器
     * @param username 用户名
     * @return 认证器列表
     */
    List<WebAuthnAuthenticatorEntity> getAllByUsername(String username);
}
