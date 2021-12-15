package com.rany.ops.sms.service;

import com.github.qcloudsms.SmsMultiSenderResult;
import com.rany.ops.sms.core.SmsConfigProperties;
import com.rany.ops.sms.core.constant.SmsConstant;
import com.rany.ops.sms.core.entity.SmsConfig;
import com.rany.ops.sms.core.entity.SmsMessage;
import com.rany.ops.sms.core.entity.SmsReceiver;
import com.rany.ops.sms.core.exception.SendMessageException;
import com.rany.ops.sms.core.service.SmsService;
import com.rany.ops.sms.support.QcloudSmsSupporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author shuangfei.zhu@hand-china.com 2019/12/23 17:45
 */
@Component
public class QcloudSmsServiceImpl extends SmsService {


    private final SmsConfigProperties configProperties;

    @Autowired
    public QcloudSmsServiceImpl(SmsConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public String serverType() {
        return "QCLOUD";
    }

    @Override
    public void smsSend(List<SmsReceiver> receiverAddressList, SmsConfig smsConfig, SmsMessage message, Map<String, String> args) {
        if (configProperties.getSms().isFakeAction() && StringUtils.hasText(configProperties.getSms().getFakeAccount())) {
            receiverAddressList = Collections.singletonList(new SmsReceiver().setIdd(configProperties.getSms().getFakeIdd())
                    .setPhone(configProperties.getSms().getFakeAccount()));
        } else if (configProperties.getSms().isFakeAction()) {
            return;
        }
        Map<String, List<SmsReceiver>> receivers = receiverAddressList.stream()
                .peek(item -> {
                    if (!StringUtils.hasText(item.getIdd())) {
                        item.setIdd(SmsConstant.DEFAULT_IDD);
                    }
                })
                .collect(Collectors.groupingBy(SmsReceiver::getIdd));
        // 解析参数
        receivers.forEach((idd, receiver) -> {
            String[] phones = new String[receiver.size()];
            receiver.stream().map(SmsReceiver::getPhone).collect(Collectors.toList()).toArray(phones);
            idd = idd.replace(SmsConstant.IDD_PREFIX, org.apache.commons.lang3.StringUtils.EMPTY);
            String[] params = new String[args.size()];
            List<String> argsList = getTemplateArgs();
            for (int i = 0; i < argsList.size(); i++) {
                String arg = argsList.get(i);
                if (args.containsKey(arg)) {
                    params[i] = args.get(arg);
                } else {
                    throw new SendMessageException("No arg found for template " + message.getTemplateCode() + " where arg named " + arg);
                }
            }
            SmsMultiSenderResult result = QcloudSmsSupporter.sendSms(QcloudSmsSupporter.multiSender(smsConfig), idd, phones, message.getExternalCode(), params, smsConfig.getSignName());
            if (result.result != 0) {
                throw new SendMessageException(result.errMsg);
            }
        });
        remove();
    }
}
