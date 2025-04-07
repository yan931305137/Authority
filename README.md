# Authority 认证系统

## 项目概述
Authority 是一个现代化的认证系统，提供多种认证方式，包括 OTP（一次性密码）、TOTP（基于时间的一次性密码）和 WebAuthn（Web 认证）等。系统采用 Spring Boot 框架开发，提供安全、可靠的身份验证服务。

## 功能模块

### OTP 模块
- 支持多种 OTP 发送方式（短信、邮件）
- 可配置的 OTP 有效期和重试次数
- 支持阿里云短信服务集成
- 灵活的消息模板配置

### TOTP 模块
- 基于时间的一次性密码实现
- 支持自定义发行方和标签
- 兼容主流身份验证器应用

### WebAuthn 模块
- 支持无密码认证
- 符合 W3C WebAuthn 规范
- 支持多种认证器（指纹、人脸识别等）
- 可配置的认证策略

## 技术栈
- Java 17
- Spring Boot 3.1.0
- Spring Data JPA
- MySQL 8.0
- WebAuthn4J
- ZXing（二维码生成）
- Hibernate Validator
- Lombok

## 环境要求
- JDK 17 或更高版本
- MySQL 8.0
- Docker（可选，用于容器化部署）

## 快速开始

### 1. 配置数据库
使用 Docker Compose 启动 MySQL：
```bash
docker-compose up -d
```

### 2. 配置应用
1. 复制 `application-example.yml` 为 `application-secret.yml`
2. 更新配置文件中的以下信息：
   - 邮箱配置（SMTP服务器信息）
   - 数据库连接信息
   - 阿里云短信配置（如需使用）

### 3. 启动应用
```bash
./mvnw spring-boot:run
```
应用将在 http://localhost:9090 启动

## 配置说明

### OTP 配置
```yaml
otp:
  attempts: 5                        # 最大重试次数
  resendAllowedAfterMinutes: 1      # 重发等待时间
  settings:
    - id: "sms"                     # 短信 OTP
      ttlMinutes: 3                 # 有效期
      otpLength: 5                  # 验证码长度
    - id: "email"                   # 邮件 OTP
      ttlMinutes: 3
      otpLength: 5
```

### WebAuthn 配置
```yaml
webauthn:
  settings:
    timeout: 60000                  # 操作超时时间
    rpId: localhost                 # Relying Party ID
    origin: 'http://localhost:3000' # 允许的源
```

## 安全说明
- 所有敏感配置应存储在 `application-secret.yml` 中
- 生产环境部署时应使用 HTTPS
- 建议启用 Spring Security 进行额外的安全防护

## 许可证
[MIT License](LICENSE)