package com.rany.ops.sms.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rany.ops.sms.core.SmsConfigProperties;
import com.rany.ops.sms.core.constant.SmsConstant;
import com.rany.ops.sms.core.entity.SmsConfig;
import com.rany.ops.sms.core.entity.SmsMessage;
import com.rany.ops.sms.core.entity.SmsReceiver;
import com.rany.ops.sms.core.exception.SendMessageException;
import com.rany.ops.sms.core.exception.SmsConfigurationException;
import com.rany.ops.sms.core.service.SmsService;
import com.rany.ops.sms.support.AliyunSmsSupporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhongshengwang
 * @description TODO
 * @date 2021/11/8 10:38 下午
 * @email 18668485565@163.com
 */
@Component
public class AliyunSmsServiceImpl extends SmsService {

    private static final String OK = "OK";

    private final SmsConfigProperties configProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public AliyunSmsServiceImpl(SmsConfigProperties configProperties,
                                ObjectMapper objectMapper) {
        this.configProperties = configProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public String serverType() {
        return "ALIYUN";
    }

    @Override
    public void smsSend(List<SmsReceiver> receiverAddressList, SmsConfig smsConfig, SmsMessage message, Map<String, String> args) {
        if (configProperties.getSms().isFakeAction()) {
            return;
        }
        List<String> telephoneList;
        if (configProperties.getSms().isFakeAction() && StringUtils.hasText(configProperties.getSms().getFakeAccount())) {
            telephoneList = Collections.singletonList(configProperties.getSms().getFakeIdd()
                    .replace(SmsConstant.IDD_PREFIX, SmsConstant.ALIYUN_PREFIX) + configProperties.getSms().getFakeAccount());
        } else {
            telephoneList = receiverAddressList.stream()
                    .map(item -> StringUtils.hasText(item.getIdd()) ?
                            item.getIdd().replace(SmsConstant.IDD_PREFIX, SmsConstant.ALIYUN_PREFIX) + item.getPhone() :
                            SmsConstant.DEFAULT_IDD.replace(SmsConstant.IDD_PREFIX, SmsConstant.ALIYUN_PREFIX) + item.getPhone())
                    .collect(Collectors.toList());
        }
        try {
            SendSmsResponse sendSmsResponse = AliyunSmsSupporter
                    .sendSms(AliyunSmsSupporter.acsClient(smsConfig), smsConfig, message.getExternalCode(), telephoneList, args, objectMapper);
            if (sendSmsResponse.getCode() == null || !OK.equals(sendSmsResponse.getCode())) {
                throw new SendMessageException(objectMapper.writeValueAsString(sendSmsResponse));
            }
        } catch (ClientException | JsonProcessingException e) {
            throw new SmsConfigurationException("send occur an error, please check the sms configuration", e);
        }
    }
}
