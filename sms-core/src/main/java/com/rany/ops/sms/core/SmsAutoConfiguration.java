package com.rany.ops.sms.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 开启自动装配
 *
 * @author zhongshengwang
 * @description TODO
 * @date 2021/11/8 10:28 下午
 * @email 18668485565@163.com
 */
@Configuration
@ComponentScan(basePackages = "com.rany.ops.sms")
public class SmsAutoConfiguration {
}
