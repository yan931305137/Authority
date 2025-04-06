package com.authority.webauthn.dto.request;

import lombok.Data;

/**
 * 凭证请求类
 */
@Data
public class CredentialRequest {
    private String id;
    private String rawId;

    private Response response;

    private String type;

    /**
     * 响应类
     */
    @Data
    public static class Response {
        private String attestationObject;
        private String clientDataJSON;
    }
}

