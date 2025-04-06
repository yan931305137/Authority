package com.authority.webauthn.repository;

import com.authority.webauthn.entity.AuthenticatorEntity;
import com.authority.webauthn.entity.WebAuthnAuthenticatorEntity;
import com.webauthn4j.credential.CredentialRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户认证器RDBMS存储库实现类。
 * 
 */
@Repository("userAuthenticatorRepository")
@AllArgsConstructor
public class UserAuthenticatorRDBMSRepository implements UserAuthenticatorRepository {

    /**
     * 用户认证器JPA存储库。
     */
    private final UserAuthenticatorJPARepository userAuthenticatorJPARepository;

    /**
     * 保存用户认证器信息。
     * 
     * @param username 用户名。
     * @param credentialRecord 认证器记录。
     */
    @Override
    public void save(String username, CredentialRecord credentialRecord) {
        WebAuthnAuthenticatorEntity webAuthnAuthenticatorEntity = new WebAuthnAuthenticatorEntity();
        webAuthnAuthenticatorEntity.setUsername(username);
        webAuthnAuthenticatorEntity.setAuthenticator(AuthenticatorEntity.fromCredentialRecord(credentialRecord).toJson());
        userAuthenticatorJPARepository.save(webAuthnAuthenticatorEntity);
    }

    /**
     * 根据用户名加载认证器记录。
     * 
     * @param username 用户名。
     * @return 认证器记录集合。
     */
    @Override
    public Set<CredentialRecord> load(String username) {
        List<WebAuthnAuthenticatorEntity> webAuthenticators = userAuthenticatorJPARepository.getAllByUsername(username);
        return webAuthenticators.stream()
                .map(wa -> AuthenticatorEntity.fromJson(wa.getAuthenticator()))
                .map(AuthenticatorEntity::toCredentialRecord).collect(Collectors.toSet());
    }
}
