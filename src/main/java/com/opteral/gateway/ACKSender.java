package com.opteral.gateway;

import com.opteral.gateway.database.SMSDAO;
import com.opteral.gateway.model.ACK;
import com.opteral.gateway.model.SMS;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ACKSender {

    private static final Logger logger = Logger.getLogger(ACKSender.class);

    public static void sendACK(ACK ack, SMSDAO smsdao) throws GatewayException {

        SMS sms = smsdao.getSMS(ack.getIdSMS());

        if (sms.getAckurl().isEmpty())
            return;


        String url = null;
        try {
            url = sms.getAckurl()+"?msisdn="+sms.getMsisdn()+"&status="+sms.getSms_status().getValue()+"&subid="+sms.getSubid()+"&datetime="+ URLEncoder.encode(sms.getDatetimeLastModified().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {

            logger.error("Error: Failed coding URL on ACK"+e.getMessage());
            throw new GatewayException("Error: Failed coding URL on ACK"+e.getMessage());

        }

        Utilities.sendGet(url);
        logger.info("ACK sended for sms: "+ack.getIdSMS());

    }

}
