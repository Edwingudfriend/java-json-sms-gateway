package com.opteral.gateway;

import com.opteral.gateway.database.SMSDAOMySQL;
import com.opteral.gateway.smsc.SMSCImp;
import com.opteral.gateway.smsc.SMSCListener;
import com.opteral.gateway.smsc.SMSCSessionListener;
import org.apache.log4j.Logger;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Config implements ServletContextListener {

    public static final int MAX_SMS_SIZE = 140;
    public static final int SUBID_MAX_SIZE = 20;
    public static final int SENDER_MAX_SIZE = 11;


    private static final Logger logger = Logger.getLogger(Config.class);
    public static final AtomicBoolean iniciado = new AtomicBoolean(false);
    private static SMPPSession session = null;
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private Sender sender;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext sc = servletContextEvent.getServletContext();

        SMSDAOMySQL smsdao = new SMSDAOMySQL();
        SMSCImp smscImp= new SMSCImp();


        sender = new Sender(smsdao, smscImp);

        setupSMPP();

        setupWorkers();

        logger.info("|||||||||||||| GATEWAY STARTED ||||||||||||||");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        logger.info("|||||||||||||| GATEWAY STOPPING ||||||||||||||");
        disconnectSMPP();
        scheduledExecutorService.shutdown();
        logger.info("|||||||||||||| GATEWAY STOPPED||||||||||||||");
    }

    private void setupSMPP()
    {

        try
        {
            session = new SMPPSession();

            session.setMessageReceiverListener(new SMSCListener());

            session.setTransactionTimer(5000L);

            session.addSessionStateListener(new SMSCSessionListener());

            session.connectAndBind("87.222.103.149", 8056, new BindParameter(BindType.BIND_TRX, "pavel", "wpsd", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));

            iniciado.compareAndSet(false,true);

        }
        catch (IOException e)
        {
            logger.error("Failed connect and bind to host", e);
        }
        catch (Exception e)
        {
            logger.error("Failed connect and bind to host", e);
        }

    }

    private void setupWorkers()
    {


        final Runnable runTasks = new Runnable()
        {
            public void run() {

                tryToReconnect();
                sendSMSScheduled();


            }
        };


        scheduledExecutorService.scheduleWithFixedDelay(runTasks,0, 5, TimeUnit.SECONDS);


    }

    private void tryToReconnect()
    {
        if (session.getSessionState() != SessionState.BOUND_TRX)
        {
            try {
                logger.info("Running scheduled task - trying to reconnect");

                setupSMPP();
            } catch (Exception e) {
                logger.error("Could not reconnect");
            }
        }
    }


    private void sendSMSScheduled()
    {
        logger.info("Ejecutando tarea programada - envíos pendientes");

        sender.send(new java.sql.Date(Instant.now().toEpochMilli()));
    }

    public static SMPPSession getSession() {
        return session;
    }



    private void disconnectSMPP()
    {
        session.unbindAndClose();
    }
}
