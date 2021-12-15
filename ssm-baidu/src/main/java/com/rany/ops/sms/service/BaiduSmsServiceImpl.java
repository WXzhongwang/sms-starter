package com.rany.ops.sms.service;

import com.baidubce.services.sms.model.SendMessageV3Response;
import com.rany.ops.sms.core.SmsConfigProperties;
import com.rany.ops.sms.core.constant.SmsConstant;
import com.rany.ops.sms.core.entity.SmsConfig;
import com.rany.ops.sms.core.entity.SmsMessage;
import com.rany.ops.sms.core.entity.SmsReceiver;
import com.rany.ops.sms.core.exception.SendMessageException;
import com.rany.ops.sms.core.service.SmsService;
import com.rany.ops.sms.support.BaiduSmsSupporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 百度云短信发送
 *
 * @author zhongshengwang
 * @description TODO
 * @date 2021/11/8 10:38 下午
 * @email 18668485565@163.com
 */
@Component
public class BaiduSmsServiceImpl extends SmsService {

    private final SmsConfigProperties configProperties;

    @Autowired
    public BaiduSmsServiceImpl(SmsConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public String serverType() {
        return "BAIDU";
    }

    @Override
    public void smsSend(List<SmsReceiver> receiverAddressList, SmsConfig smsConfig, SmsMessage message, Map<String, String> args) {
        List<String> telephoneList = new ArrayList<>();
        if (configProperties.getSms().isFakeAction() && StringUtils.hasText(configProperties.getSms().getFakeAccount())) {
            telephoneList = Collections.singletonList(configProperties.getSms().getFakeAccount());
        } else if (configProperties.getSms().isFakeAction()) {
            return;
        } else {
            for (SmsReceiver item : receiverAddressList) {
                String idd = item.getIdd();
                if (!StringUtils.hasLength(idd) || Objects.equals(idd, SmsConstant.DEFAULT_IDD)) {
                    telephoneList.add(item.getPhone());
                } else {
                    // 港澳台及国际手机号需要拼接国际冠码
                    telephoneList.add(idd + item.getPhone());
                }
            }
        }
        SendMessageV3Response response = BaiduSmsSupporter.sendSms(BaiduSmsSupporter.smsClient(smsConfig), smsConfig, message.getExternalCode(), telephoneList, args);
        if (response == null) {
            throw new SendMessageException("baidu sms send failed!");
        }
        if (!response.isSuccess()) {
            throw new SendMessageException(String.format("baidu sms send failed! code: [%s] , message:  [%s]", response.getCode(), response.getMessage()));
        }
    }
}
