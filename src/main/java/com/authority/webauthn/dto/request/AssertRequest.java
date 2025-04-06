package com.authority.webauthn.dto.request;

import lombok.Data;

/**
 * AssertRequest类，用于处理身份验证请求。
 */
@Data
public class AssertRequest {
    /**
     * 请求的唯一标识符。
     */
    private String id;
    /**
     * 请求的原始标识符。
     */
    private String rawId;

    /**
     * 响应对象，包含身份验证结果。
     */
    private Response response;

    /**
     * 请求的类型。
     */
    private String type;

    /**
     * 响应类，包含身份验证响应的详细信息。
     */
    @Data
    public static class Response {
        /**
         * 认证器数据。
         */
        private String authenticatorData;
        /**
         * 客户端数据JSON。
         */
        private String clientDataJSON;
        /**
         * 签名数据。
         */
        private String signature;
        /**
         * 用户句柄。
         */
        private String userHandle;
    }
}
