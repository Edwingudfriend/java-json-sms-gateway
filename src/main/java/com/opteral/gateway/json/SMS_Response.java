package com.opteral.gateway.json;

import com.opteral.gateway.model.SMS;

public class SMS_Response {

    private boolean request_ok;
    private SMS.SMS_Status status;
    private long id;
    private String subid;

    public SMS_Response(SMS sms, boolean request_ok) {

        this.request_ok = request_ok;
        this.status = sms.getEstadoSMS();
        this.id = sms.getId();
        this.subid = sms.getSubid();

    }

    public SMS_Response(JSON_SMS jsonsms, boolean request_ok) {
        this.request_ok = request_ok;
        this.status = null;
        this.id = jsonsms.getId();
        this.subid = jsonsms.getSubid();

    }

    public boolean isRequest_ok() {
        return request_ok;
    }

    public void setRequest_ok(boolean request_ok) {
        this.request_ok = request_ok;
    }

    public SMS.SMS_Status getStatus() {
        return status;
    }

    public void setStatus(SMS.SMS_Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }
}
