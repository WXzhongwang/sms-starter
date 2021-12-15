package com.rany.ops.sms.core.exception;

/**
 * 统一异常包装
 *
 * @author zhongshengwang
 * @description TODO
 * @date 2021/11/8 10:28 下午
 * @email 18668485565@163.com
 */
public class SendMessageException extends RuntimeException {

    public SendMessageException(String message) {
        super(message);
    }
}