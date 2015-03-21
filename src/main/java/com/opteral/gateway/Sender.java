package com.opteral.gateway;

import com.opteral.gateway.database.SMSDAO;
import com.opteral.gateway.database.UserDAO;
import com.opteral.gateway.model.SMS;
import com.opteral.gateway.model.User;
import com.opteral.gateway.smsc.SMSC;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Sender {

    private static final Logger logger = Logger.getLogger(Sender.class);

    private SMSDAO smsdao;
    private SMSC smsc;

    public Sender(SMSDAO smsdao, SMSC smsc) {

        this.smsdao = smsdao;
        this.smsc = smsc;
    }

    public void envia(java.sql.Date aFecha)  {

        try {

            procesaLista(smsdao.getSMSForSend(aFecha));

        }
        catch (Exception e) {

          logThis(e.getMessage());
        }


    }



    private void procesaLista(List<SMS> lista)  {

        for (SMS sms : lista) {

            try
            {
                procesaSMS(sms);

            } catch (Exception e) {

                logThis(e.getMessage());
            }

        }
    }

    private void procesaSMS(SMS sms) throws GatewayException, IOException, SQLException {

        smsc.sendSMS(sms);

        if (sms.getIdSMSC() != null && !sms.getIdSMSC().isEmpty()) {

            sms.setSms_status(SMS.SMS_Status.ONSMSC);
            smsdao.persist(sms);
        }
    }



    private void logThis(String mensaje)
    {
        logger.error(mensaje);
    }








}
