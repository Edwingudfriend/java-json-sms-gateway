package com.opteral.gateway.model;

import com.opteral.gateway.GatewayException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ACK {


    private String ackmisdn;
    private SMS.SMS_Status sms_status;
    private Long idSMS;
    private String idSMSC;
    private Timestamp acktimestamp;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getIdSMSC() {
        return idSMSC;
    }

    public void setIdSMSC(String idSMSC) {
        this.idSMSC = idSMSC;
    }

    public SMS.SMS_Status getSms_status() {
        return sms_status;
    }

    public void setSms_status(SMS.SMS_Status sms_status) {
        this.sms_status = sms_status;
    }

    public void setAckmisdn(String ackmisdn) {
        this.ackmisdn = ackmisdn;
    }



    public void setIdSMS(String s) {

        idSMS = Long.parseLong(s);
    }

    public void setAcktimestamp(String acktimestamp) throws GatewayException {

        try {

            this.acktimestamp = new Timestamp(formatter.parse(acktimestamp).getTime());
        }
        catch (ParseException e) {

            throw new GatewayException("Error: "+acktimestamp+ " isn't in correct format (YYYY-MM-DD mm:hh)");
        }
    }

    public void setAckNow() {

        acktimestamp = new Timestamp(new Date().getTime());
    }



    public String getAckmisdn() {
        return ackmisdn;
    }


    public Long getIdSMS() {
        return idSMS;
    }

    public Date getAcktimestamp() {
        return acktimestamp;
    }

    public String getAcktimestampMysqlString() {

        return formatter.format(acktimestamp);
    }




}
