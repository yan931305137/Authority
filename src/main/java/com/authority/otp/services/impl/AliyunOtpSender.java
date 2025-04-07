package com.authority.otp.services.impl;

import com.authority.otp.services.OtpSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.models.*;
import com.aliyun.sdk.service.dysmsapi20170525.*;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Service("AliyunOTPSender")
public class AliyunOtpSender implements OtpSender {

    @Value("${Aliyun.uri}")
    private String uri; // 阿里云短信服务URI

    @Value("${Aliyun.templateCode}")
    private String templateCode; // 短信模板代码

    @Value("${Aliyun.region}")
    private String region; // 区域

    @Value("${Aliyun.accessKeyId}")
    private String keyId; // 访问密钥ID

    @Value("${Aliyun.accessKeySecret}")
    private String keySecret; // 访问密钥秘密

    @Override
    public void sendOTP(String destination, String messageBody, String messageTitle) {
        // 创建静态凭证提供者
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(keyId)
                .accessKeySecret(keySecret)
                .build());

        // 创建异步客户端
        AsyncClient client = AsyncClient.builder()
                .credentialsProvider(provider)
                .region(region)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(uri)
                )
                .build();
        // 创建发送短信请求
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(messageTitle)
                .templateCode(templateCode)
                .phoneNumbers(destination)
                .templateParam(messageBody)
                .build();

        // 发送短信
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        try {
            // 获取响应
            SendSmsResponse resp = response.get();
            log.info("sent message {}", new Gson().toJson(resp));
        } catch (Exception e) {
            // 记录错误信息
            log.error("发送OTP失败: {}", e.getMessage(), e);
        } finally {
            // 关闭客户端
            client.close();
        }
    }
}
