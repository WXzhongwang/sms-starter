package com.rany.ops.sms.core.exception;

/**
 * 统一配置异常
 *
 * @author zhongshengwang
 * @description TODO
 * @date 2021/11/8 10:28 下午
 * @email 18668485565@163.com
 */
public class SmsConfigurationException extends RuntimeException {

    public SmsConfigurationException(String message) {
        super(message);
    }

    public SmsConfigurationException(Throwable throwable) {
        super(throwable);
    }

    public SmsConfigurationException(String message, Throwable e) {
        super(message, e);
    }
}