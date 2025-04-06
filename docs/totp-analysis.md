# TOTP模块分析文档

## 1. 业务流程概述

### 1.1 核心业务

TOTP模块包含两个核心业务流程：
- TOTP密钥生成与绑定
- TOTP验证码验证

### 1.2 密钥生成与绑定流程

1. 客户端请求生成TOTP密钥
2. 服务端生成密钥并返回密钥信息
3. 客户端展示二维码供用户扫描
4. 用户使用验证器扫描绑定
5. 客户端请求验证并保存绑定

### 1.3 验证码验证流程

1. 客户端获取用户输入的验证码
2. 服务端验证时间窗口内的验证码
3. 返回验证结果

## 2. 数据结构分析

### 2.1 密钥生成请求

| 字段 | 类型 | 说明 |
|------|------|------|
| username | String | 用户名 |
| issuer | String | 发行方标识 |

### 2.2 密钥生成响应

| 字段 | 类型 | 说明 |
|------|------|------|
| secretKey | String | Base32编码的密钥 |
| qrCodeUrl | String | 二维码图片URL |
| otpAuthUrl | String | TOTP认证URL |
| period | Number | 验证码更新周期（秒） |
| digits | Number | 验证码位数 |

### 2.3 绑定验证请求

| 字段 | 类型 | 说明 |
|------|------|------|
| username | String | 用户名 |
| code | String | 当前验证码 |
| secretKey | String | 待绑定的密钥 |

### 2.4 绑定验证响应

| 字段 | 类型 | 说明 |
|------|------|------|
| status | Boolean | 绑定结果 |
| errorMessage | String | 错误信息 |

### 2.5 验证码验证请求

| 字段 | 类型 | 说明 |
|------|------|------|
| username | String | 用户名 |
| code | String | 验证码 |

### 2.6 验证码验证响应

| 字段 | 类型 | 说明 |
|------|------|------|
| status | Boolean | 验证结果 |
| errorMessage | String | 错误信息 |

## 3. 安全特性

### 3.1 密钥生成

- 使用加密安全的随机数生成器
- 密钥长度符合RFC 4226规范
- Base32编码确保兼容性

### 3.2 验证码生成

- 基于时间窗口的动态验证码
- 支持自定义验证码位数
- 支持自定义更新周期

### 3.3 防重放攻击

- 验证码具有时效性
- 每个时间窗口验证码唯一
- 支持时间偏移容错

## 4. 配置项

### 4.1 TOTP参数配置

| 配置项 | 说明 |
|--------|------|
| DIGITS | 验证码位数 |
| PERIOD | 更新周期（秒） |
| ALGORITHM | 哈希算法（SHA1/SHA256/SHA512） |

### 4.2 验证配置

| 配置项 | 说明 |
|--------|------|
| TIME_WINDOW | 验证时间窗口 |
| MAX_TIME_STEP_MISMATCH | 最大时间步长偏差 |
| REUSE_WINDOW | 重复使用窗口时间 |

## 5. 异常处理

| 异常类型 | 说明 |
|----------|------|
| InvalidSecretKeyException | 无效密钥异常 |
| VerificationFailedException | 验证失败异常 |
| TimeWindowExceededException | 时间窗口超出异常 |
| CodeMismatchException | 验证码不匹配异常 |
| ReuseDetectedException | 重复使用检测异常 |