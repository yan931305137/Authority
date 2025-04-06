package com.authority.webauthn.repository;

import com.webauthn4j.credential.CredentialRecord;

import java.util.Set;

/**
 * 用户认证器存储库接口
 */
public interface UserAuthenticatorRepository {

    /**
     * 保存用户认证器
     * @param username 用户名
     * @param authenticator 认证器
     */
    void save(String username, CredentialRecord authenticator);

    /**
     * 加载用户认证器
     * @param username 用户名
     * @return 认证器集合
     */
    Set<CredentialRecord> load(String username);
}
