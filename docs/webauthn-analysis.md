# WebAuthn模块分析文档

## 1. 业务流程概述

### 1.1 核心业务

WebAuthn模块包含两个核心业务流程：
- WebAuthn注册
- WebAuthn认证

### 1.2 注册流程

1. 客户端请求注册挑战
2. 服务端生成挑战值并返回
3. 客户端使用认证器创建凭证
4. 服务端验证凭证并保存

### 1.3 认证流程

1. 客户端请求认证挑战
2. 服务端生成挑战值并返回
3. 客户端使用认证器生成断言
4. 服务端验证断言

## 2. 数据结构分析

### 2.1 注册挑战请求

| 字段                    | 类型    | 说明                                        |
|------------------------|---------|---------------------------------------------|
| username              | String  | 用户名                                      |
| displayName           | String  | 显示名称                                    |
| authenticatorAttachment| String  | 认证器连接方式（platform/cross-platform）    |
| requireResidentKey    | Boolean | 是否要求密钥驻留                            |
| userVerification      | String  | 用户验证要求（required/preferred/discouraged）|

### 2.2 注册挑战响应

| 字段                   | 类型    | 说明                   |
|-----------------------|---------|------------------------|
| challenge             | String  | Base64编码的挑战值      |
| rp                    | Object  | 依赖方信息             |
| user                  | Object  | 用户信息               |
| pubKeyCredParams      | Array   | 支持的公钥凭证参数      |
| timeout               | Number  | 操作超时时间           |
| attestation           | String  | 认证器证明类型         |
| authenticatorSelection | Object  | 认证器选择条件         |

### 2.3 注册验证请求

| 字段     | 类型    | 说明                        |
|---------|---------|----------------------------|
| id      | String  | 凭证ID                     |
| rawId   | String  | Base64编码的原始凭证ID      |
| response | Object  | 认证器响应数据              |
| type    | String  | 公钥凭证类型                |

### 2.4 注册验证响应

| 字段         | 类型    | 说明       |
|-------------|---------|------------|
| status      | Boolean | 注册结果    |
| errorMessage| String  | 错误信息    |

### 2.5 认证挑战请求

| 字段            | 类型    | 说明           |
|----------------|---------|----------------|
| username       | String  | 用户名         |
| userVerification| String  | 用户验证要求    |

### 2.6 认证挑战响应

| 字段             | 类型    | 说明              |
|-----------------|---------|-------------------|
| challenge       | String  | Base64编码的挑战值 |
| timeout         | Number  | 操作超时时间       |
| rpId            | String  | 依赖方ID          |
| allowCredentials | Array   | 允许的凭证列表     |
| userVerification | String  | 用户验证要求       |

### 2.7 认证验证请求

| 字段     | 类型    | 说明                     |
|---------|---------|--------------------------|
| id      | String  | 凭证ID                  |
| rawId   | String  | Base64编码的原始凭证ID   |
| response | Object  | 认证器响应数据           |
| type    | String  | 公钥凭证类型             |

### 2.8 认证验证响应

| 字段         | 类型    | 说明        |
|-------------|---------|------------|
| status      | Boolean | 认证结果    |
| errorMessage| String  | 错误信息    |

## 3. 安全特性

### 3.1 挑战值生成

- 使用加密安全的随机数生成器
- 每次操作生成唯一挑战值
- 挑战值具有时效性

### 3.2 用户验证

- 支持生物识别
- 支持PIN码验证
- 可配置验证要求级别

### 3.3 防重放攻击

- 验证签名计数器
- 检查挑战值新鲜度
- 验证时间戳有效性

## 4. 配置项

### 4.1 依赖方配置

| 配置项   | 说明            |
|---------|-----------------|
| RP_ID   | 依赖方ID        |
| RP_NAME | 依赖方名称      |
| RP_ICON | 依赖方图标URL   |

### 4.2 认证器配置

| 配置项                 | 说明               |
|----------------------|-------------------|
| TIMEOUT              | 操作超时时间（毫秒） |
| USER_VERIFICATION    | 默认用户验证要求     |
| ATTESTATION_PREFERENCE| 证明类型偏好       |

## 5. 异常处理

| 异常类型                     | 说明               |
|----------------------------|-------------------|
| RegistrationFailedException | 注册失败异常       |
| AuthenticationFailedException| 认证失败异常       |
| CredentialNotFoundException  | 凭证未找到异常     |
| InvalidSignatureException    | 签名无效异常       |
| ChallengeMismatchException  | 挑战值不匹配异常   |
| TokenExpiredException       | 令牌过期异常       |
