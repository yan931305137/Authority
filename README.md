# Authority 多因素认证系统

## 项目简介

Authority 是一个基于 Spring Boot 开发的多因素认证系统，提供多种身份验证方式，包括：
- OTP (One-Time Password) 验证
- TOTP (Time-based One-Time Password) 验证
- WebAuthn (Web Authentication) 支持

## 功能特性

### OTP 验证
- 支持短信和邮件验证码
- 可配置的 OTP 有效期和重试次数
- 支持多种 OTP 发送方式（Twilio SMS、Email）
- 支持自定义邮件模板
- 防止频繁发送机制
- 验证码尝试次数限制

### TOTP 验证
- 基于时间的一次性密码验证（RFC 6238标准）
- 支持主流身份验证器应用（如 Google Authenticator、Microsoft Authenticator）
- 安全的密钥生成和管理
- 支持二维码扫描绑定
- 可配置的时间窗口和验证容错

### WebAuthn 支持
- 符合 FIDO2 标准的 WebAuthn 实现
- 支持生物识别和安全密钥认证
- 安全的凭证管理

## 技术栈
- Java 17
- Spring Boot 3.1.0
- Spring Data JPA
- MySQL 8.0
- WebAuthn4J 0.24.0
- Twilio SDK 10.1.5（短信服务）
- ZXing 3.5.1（二维码生成）
- Thymeleaf（模板引擎）

## 环境要求

- JDK 17 或更高版本
- Maven 3.6+
- MySQL 8.0+
- Docker

## 快速开始

### 1. 克隆项目

```bash
git clone [项目地址]
cd Authority
```

### 2. 配置数据库

项目提供了两种方式配置数据库：

#### 使用 Docker Compose（推荐）

```bash
docker-compose up -d
```

这将自动创建并启动 MySQL 容器，端口映射为 3311。

#### 手动配置

1. 创建 MySQL 数据库
2. 修改 `application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3311/authority_db
    username: root
    password: 123456
```

### 3. 配置邮件服务

在 `application.yml` 中配置邮件服务器信息：

```yaml
spring:
  mail:
    host: ${MAIL_HOST:smtp.gmail.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

otp:
  attempts: 5
  resendAllowedAfterMinutes: 1
  settings:
    - id: "email"
      name: "Email OTP"
      messageTitle: "验证码"
      messageTemplate: "email-template.html:${otp}"
      otpLength: 6
      useLetters: false
      useDigits: true
      ttlMinutes: 5
      sender: "EmailOTPSender"
```

邮件模板位于 `src/main/resources/templates/` 目录下，支持 HTML 格式。

### 4. 启动应用

```bash
mvn spring-boot:run
```

应用将在 http://localhost:9090 启动。

## 配置说明

### OTP 配置

```yaml
otp:
  attempts: 5                      # 最大尝试次数
  resendAllowedAfterMinutes: 1     # 重发等待时间（分钟）
  settings:
    - id: "sms"
      otpLength: 5                 # OTP 长度
      ttlMinutes: 3               # 有效期（分钟）
```

### TOTP 配置

```yaml
totp:
  timeStep: 30                    # 时间步长（秒）
  codeLength: 6                   # TOTP 码长度
  validPeriods: 1                # 验证窗口容错期数
  issuer: "Authority"            # 发行方名称
  algorithm: "HmacSHA1"          # 哈希算法（支持 HmacSHA1/HmacSHA256/HmacSHA512）
```

## 安全建议

1. 在生产环境中，确保使用环境变量或安全的配置管理系统来存储敏感信息。
2. 建议启用 HTTPS。
3. 定期更新依赖包版本，特别是安全相关的更新。
4. TOTP 密钥应使用安全的加密方式存储。

## 许可证

[添加许可证信息]

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交变更
4. 推送到分支
5. 创建 Pull Request

## 问题反馈

如果您在使用过程中遇到任何问题，请通过 Issues 页面提交问题。