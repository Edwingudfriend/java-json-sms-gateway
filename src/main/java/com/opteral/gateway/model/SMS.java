package com.opteral.gateway.model;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.json.JSON_SMS;

import java.sql.Date;
import java.sql.Timestamp;

import static com.opteral.gateway.database.DAOUtil.toSqlDate;
import static com.opteral.gateway.database.DAOUtil.toSqlTimeStamp;

public class SMS {

    private long id;
    private int user_id;
    private String idSMSC;
    private String msisdn;
    private String sender;
    private String text;
    private String subid;
    private String ackurl;
    private SMS_Status sms_status;
    private Timestamp datetimeScheduled;
    private boolean test;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getIdSMSC() {
        return idSMSC;
    }

    public void setIdSMSC(String idSMSC) {
        this.idSMSC = idSMSC;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getAckurl() {
        return ackurl;
    }

    public void setAckurl(String ackurl) {
        this.ackurl = ackurl;
    }

    public SMS_Status getSms_status() {
        return sms_status;
    }

    public void setSms_status(SMS_Status sms_status) {
        this.sms_status = sms_status;
    }


    public Timestamp getDatetimeScheduled() {
        return datetimeScheduled;
    }

    public void setDatetimeScheduled(Timestamp datetimeScheduled) {
        this.datetimeScheduled = datetimeScheduled;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public SMS() {
    }

    public SMS(JSON_SMS jsonSMS, int user_id) throws GatewayException {

        if (user_id < 1)
            throw new GatewayException("Authentication issue");

        this.id = jsonSMS.getId();
        this.user_id = user_id;
        this.sender = jsonSMS.getSender();
        this.msisdn = jsonSMS.getMsisdn();
        this.text = jsonSMS.getText();
        this.subid = jsonSMS.getSubid();
        this.ackurl = jsonSMS.getAck_url();
        this.datetimeScheduled = toSqlTimeStamp(jsonSMS.getDatetime());
        this.test = jsonSMS.isTest();

        if (datetimeScheduled != null)
            sms_status = SMS_Status.PROGRAMED;
        else
            sms_status = SMS_Status.ACCEPTD;

    }



    public enum SMS_Status {

        REJECTD(0),
        EXPIRED(1),
        DELETED(2),
        UNDELIV(3),
        UNKNWOWN(4),
        WAITING(5),
        PROGRAMED(6),
        ACCEPTD(7),
        ONSMSC(8),
        DELIVRD(9);

        private final int value;

        private SMS_Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SMS_Status fromInt(int num) throws IllegalArgumentException {
            for(SMS_Status t : values()){
                if( t.value == num && t.value != 0){
                    return t;
                }
            }
            throw new IllegalArgumentException("Error: "+ num +" is not status valid code");
        }

    }

}
