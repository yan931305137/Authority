package com.authority.otp.utils;

import com.authority.otp.config.OtpSettings;
import com.authority.otp.exception.FrequentSendingForbidden;
import com.authority.otp.services.OtpService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Log4j2
@Component
public class EmailGenerator {

    public String EmailFilter(OtpSettings otpSettings) throws FrequentSendingForbidden {

        String messageTemplate = otpSettings.getMessageTemplate();
        int colonIndex = messageTemplate.indexOf(':');
        String filePath = messageTemplate.substring(0, colonIndex).trim();

        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = OtpService.class.getResourceAsStream("/templates/" + filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            log.error("读取OTP电子邮件模板文件时未找到文件", e);
            throw new FrequentSendingForbidden();
        } catch (IOException e) {
            log.error("读取OTP电子邮件模板文件时出错", e);
            throw new FrequentSendingForbidden();
        }
        return content.toString();
    }
}
