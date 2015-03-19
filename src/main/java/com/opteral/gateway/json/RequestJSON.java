package com.opteral.gateway.json;

import java.util.List;

public class RequestJSON {

    private String user;
    private String password;
    private List<JSON_SMS> sms_request;



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<JSON_SMS> getSms_request() {
        return sms_request;
    }

    public void setSms_request(List<JSON_SMS> sms_request) {
        this.sms_request = sms_request;
    }
}
