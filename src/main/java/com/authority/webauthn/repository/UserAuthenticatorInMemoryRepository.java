package com.authority.webauthn.repository;

import com.webauthn4j.credential.CredentialRecord;

import java.util.*;

/**
 * UserAuthenticatorInMemoryRepository类，用于在内存中存储和管理用户认证器信息。
 */
public class UserAuthenticatorInMemoryRepository implements UserAuthenticatorRepository {

    /**
     * 用户认证器映射，用于存储用户名与其对应的认证器记录集。
     */
    private final Map<String, Set<CredentialRecord>> userAuthenticatorsMap = new HashMap<>();

    /**
     * 保存用户认证器记录。
     * 
     * @param username 用户名
     * @param credentialRecord 认证器记录
     */
    @Override
    public void save(String username, CredentialRecord credentialRecord) {
        userAuthenticatorsMap.putIfAbsent(username, new HashSet<>());
        userAuthenticatorsMap.get(username).add(credentialRecord);
    }

    /**
     * 根据用户名加载认证器记录集。
     * 
     * @param username 用户名
     * @return 认证器记录集
     */
    @Override
    public Set<CredentialRecord> load(String username) {
        if (!userAuthenticatorsMap.containsKey(username)) {
            return Collections.emptySet();
        }
        return userAuthenticatorsMap.get(username);
    }
}
