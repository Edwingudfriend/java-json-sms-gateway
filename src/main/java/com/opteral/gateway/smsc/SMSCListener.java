package com.opteral.gateway.smsc;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.database.SMSDAOMySQL;
import com.opteral.gateway.model.ACK;
import com.opteral.gateway.model.SMS;
import org.apache.log4j.Logger;
import org.jsmpp.bean.*;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;

import java.sql.SQLException;

public class SMSCListener implements MessageReceiverListener {

    private static final Logger logger = Logger.getLogger(SMSCListener.class);

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {

        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {


            try
            {

                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                long id = Long.parseLong(delReceipt.getId());
                String messageId = Long.toString(id, 16);

                ACK ack = new ACK();

                ack.setIdSMSC(messageId);
                //TODO condicion de estado
                ack.setSms_status(SMS.SMS_Status.DELIVRD);
                ack.setAckNow();


                SMSDAOMySQL smsdaoMySQL = new SMSDAOMySQL();

                smsdaoMySQL.updateSMS_Status(ack);

                //TODO enviar ACK si es necesario


                logger.info("Recived confirmation delivery: '" + messageId + "' : " + delReceipt +delReceipt.getDelivered());

            }
            catch (InvalidDeliveryReceiptException e)
            {
                logger.error("Failed getting delivery receipt", e);
            }

            catch (GatewayException e)
            {
                logger.error("Failed updating sms_status on databse", e);
            } catch (SQLException e) {
                logger.error("Failed updating sms_status on databse, SQL", e);
            }

        }
        else
        {
            logger.info("Incoming SMSC message : " + new String(deliverSm.getShortMessage()));
        }

    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {

        logger.warn("Incoming SMSC alert : " + alertNotification.getCommandId() + alertNotification.toString());
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session) throws ProcessRequestException {
        return null;
    }


}
