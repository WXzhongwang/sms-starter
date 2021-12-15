# sms-starter

采用自动装配的理念，快速接入短信,支持厂商：

- 阿里云
- 百度云
- 七牛

自动装配：

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.rany.ops.sms.core.SmsAutoConfiguration
```

关注：

SmsService.java

```
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

```