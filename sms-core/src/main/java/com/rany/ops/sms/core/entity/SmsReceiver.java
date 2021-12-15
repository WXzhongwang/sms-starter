package com.rany.ops.sms.core.entity;

/**
 * 短信接收
 *
 * @author zhongshengwang
 * @description TODO
 * @date 2021/11/8 10:28 下午
 * @email 18668485565@163.com
 */
public class SmsReceiver {

    /**
     * 短信接收者：电话号码
     */
    private String phone;
    /**
     * 短信接收者：国际冠码
     */
    private String idd = "+86";

    public String getPhone() {
        return phone;
    }

    public SmsReceiver setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getIdd() {
        return idd;
    }

    public SmsReceiver setIdd(String idd) {
        this.idd = idd;
        return this;
    }
}
