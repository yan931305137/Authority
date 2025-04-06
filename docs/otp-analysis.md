# OTP模块分析文档

## 1. 业务流程概述

### 1.1 核心业务

OTP模块包含两个核心业务流程：
- 发送OTP验证码
- 验证OTP验证码

### 1.2 发送方式

支持三种发送方式：
- 邮件发送（EmailOtpSender）
- 短信发送（TwilioOtpSender）
- 测试发送（DummyOtpSender）

## 2. 数据结构分析

### 2.1 发送OTP请求

| 字段 | 类型 | 说明 |
|------|------|------|
| type | String | 发送类型，用于选择发送方式 |
| destination | String | 目标地址（邮箱或手机号） |

### 2.2 发送OTP响应

| 字段 | 类型 | 说明 |
|------|------|------|
| sessionId | String | 会话ID，用于后续验证 |
| destination | String | 目标地址 |
| expireTime | Long | 过期时间戳 |
| attempts | Integer | 剩余验证尝试次数 |

### 2.3 验证OTP请求

| 字段 | 类型 | 说明 |
|------|------|------|
| sessionId | String | 会话ID |
| otp | String | 验证码 |

### 2.4 验证OTP响应

| 字段 | 类型 | 说明 |
|------|------|------|
| result | Boolean | 验证结果 |
| remainingAttempts | Integer | 剩余验证尝试次数 |

## 3. 安全特性

### 3.1 OTP生成规则

- 支持配置OTP长度
- 可配置是否使用字母
- 可配置是否使用数字
- 使用UUID生成唯一会话ID

### 3.2 频率限制

- 支持配置重发等待时间
- 验证尝试次数限制
- 验证码有效期限制

## 4. 配置项

### 4.1 Twilio配置

| 配置项 | 说明 |
|--------|------|
| TWILIO_MESSAGING_SERVICE_SID | Twilio消息服务ID |
| TWILIO_ACCOUNT_SID | Twilio账户ID |
| TWILIO_AUTH_TOKEN | Twilio认证令牌 |

### 4.2 OTP配置

| 配置项 | 说明 |
|--------|------|
| otpLength | OTP长度 |
| useLetters | 是否使用字母 |
| useDigits | 是否使用数字 |
| ttlMinutes | 有效期（分钟） |
| attempts | 最大尝试次数 |
| resendAllowedAfterMinutes | 重发等待时间（分钟） |

## 5. 异常处理

| 异常类型 | 说明 |
|----------|------|
| SenderNotFoundException | 发送器未找到 |
| SessionNotFoundException | 会话未找到或已过期 |
| OtpVerifyAttemptsExceeded | 超过最大验证尝试次数 |
| FrequentSendingForbidden | 发送过于频繁 |
| SendOtpException | OTP发送异常 |