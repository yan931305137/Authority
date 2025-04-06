package com.authority.otp.exception;

import com.authority.exception.NotFoundException;

/**
 * 模板未找到异常类。
 */
public class TemplateNotFoundException extends NotFoundException {
    /**
     * 构造函数，初始化异常信息为"template not found"。
     */
    public TemplateNotFoundException() {
        super("template not found");
    }
}
