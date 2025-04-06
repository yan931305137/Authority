package com.authority.totp.services;

import com.authority.totp.exception.UserNotFoundException;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base32;
import com.authority.totp.config.TotpConfiguration;
import com.authority.totp.dto.RegisteredTotp;
import com.authority.totp.repository.RegisteredTotpRepository;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Optional;

/**
 * TotpService类提供了TOTP服务，包括注册、生成密钥、恢复密钥和验证TOTP的功能。
 */
@Service
@Log4j2
@AllArgsConstructor
public class TotpService {

    private RegisteredTotpRepository totpRepository;

    private TimeBasedOneTimePasswordGenerator generator;

    private TotpConfiguration totpConfiguration;

    private static final String URI_TEMPLATE = "otpauth://totp/{0}:{1}@{2}?secret={3}&issuer={0}";

    /**
     * 注册TOTP，生成并保存密钥，返回TOTP URI。
     * @param username 用户名
     * @return TOTP URI
     */
    public URI register(String username) {
        Optional<RegisteredTotp> registeredTotpOptional = totpRepository.findById(username);
        final RegisteredTotp registeredTotp;
        if(registeredTotpOptional.isPresent()) {
            registeredTotp = registeredTotpOptional.get();
        } else {
            registeredTotp = new RegisteredTotp();
            registeredTotp.setUsername(username);
        }
        final String secret = generateKey();
        registeredTotp.setSecret(secret);
        totpRepository.save(registeredTotp);
        final String issuerLabelEncoded = URLEncoder.encode(totpConfiguration.getIssuerLabel(), StandardCharsets.UTF_8);
        final String usernameEncoded = URLEncoder.encode(username, StandardCharsets.UTF_8);
        final String issuerEncoded = URLEncoder.encode(totpConfiguration.getIssuer(), StandardCharsets.UTF_8);

        final String uriStr = MessageFormat.format(URI_TEMPLATE, issuerLabelEncoded, usernameEncoded, issuerEncoded, secret);
        return URI.create(uriStr);
    }

    /**
     * 生成密钥。
     * @return 密钥字符串
     */
    public String generateKey() {
        final Key key;
        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(generator.getAlgorithm());
            final int macLengthInBytes = Mac.getInstance(generator.getAlgorithm()).getMacLength();
            keyGenerator.init(macLengthInBytes * 8);

            key = keyGenerator.generateKey();
            return new Base32().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据密钥字符串恢复密钥。
     * @param keyStr 密钥字符串
     * @return 密钥对象
     */
    public Key restoreKey(String keyStr) {
        byte[] b = new Base32().decode(keyStr);
        return new SecretKeySpec(b, 0, b.length, generator.getAlgorithm());
    }

    /**
     * 验证TOTP。
     * @param username 用户名
     * @param totp TOTP值
     * @return 验证结果
     * @throws UserNotFoundException 用户不存在异常
     */
    public boolean verify(String username, Integer totp) throws UserNotFoundException {
        Optional<RegisteredTotp> registeredTotpOptional = totpRepository.findById(username);
        if(registeredTotpOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        RegisteredTotp registeredTotp = registeredTotpOptional.get();
        Key key = restoreKey(registeredTotp.getSecret());

        final int generatedTotp;
        try {
            generatedTotp = generator.generateOneTimePassword(key, Instant.now());
        } catch (InvalidKeyException e) {
            log.error("TOTP生成错误发生", e);
            throw new RuntimeException(e);
        }

        System.out.println(generatedTotp);
        System.out.println(totp);
        return generatedTotp == totp;
    }
}
