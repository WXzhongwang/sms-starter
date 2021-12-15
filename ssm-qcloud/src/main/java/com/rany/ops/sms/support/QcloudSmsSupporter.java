package com.rany.ops.sms.support;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.rany.ops.sms.core.entity.SmsConfig;
import com.rany.ops.sms.core.exception.SmsConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 腾讯短信发送支持
 *
 * @author qingsheng.chen 2019/1/21 星期一 20:31
 */
public class QcloudSmsSupporter {

    private QcloudSmsSupporter() {
    }

    /**
     * 腾讯短信
     */
    public static SmsMultiSender multiSender(SmsConfig smsConfig) {
        return new SmsMultiSender(Integer.parseInt(smsConfig.getAccessKey()), smsConfig.getAccessKeySecret());
    }

    /**
     * 腾讯发送短信  群发一次请求最多支持 200 个号码
     */
    public static SmsMultiSenderResult sendSms(SmsMultiSender smsMultiSender, String idd, String[] receiverAddress, String templateId, String[] params, String smsSign) {
        Assert.isTrue(StringUtils.isNotBlank(templateId), "invalid template id");
        try {
            return smsMultiSender.sendWithParam(idd, receiverAddress, Integer.parseInt(templateId), params, smsSign, "", "");
        } catch (Exception e) {
            throw new SmsConfigurationException(e);
        }
    }
}
