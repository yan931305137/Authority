package com.authority.totp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GeneratekeyDto {
    private String key;
    private String timestamp;
}
