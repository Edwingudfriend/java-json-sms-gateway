package com.opteral.gateway;

import com.opteral.gateway.database.SMSDAOMySQL;
import com.opteral.gateway.smsc.SMSCImp;
import com.opteral.gateway.smsc.SMSCListener;
import com.opteral.gateway.smsc.SMSCSessionListener;
import javafx.application.Application;
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

public class GatewayServletListener implements ServletContextListener {


    private static final Logger logger = Logger.getLogger(GatewayServletListener.class);
    public static final AtomicBoolean iniciado = new AtomicBoolean(false);
    private static SMPPSession session = null;
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private Sender sender;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        loadConfig();

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

    private void loadConfig()
    {
        try {
            Utilities.getConfig(false);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed loading configuration file");
        }
    }

    private void setupSMPP()
    {

        try
        {
            session = new SMPPSession();

            session.setMessageReceiverListener(new SMSCListener());

            session.setTransactionTimer(5000L);

            session.addSessionStateListener(new SMSCSessionListener());

            session.connectAndBind(Config.SMSC_IP, Config.SMSC_PORT, new BindParameter(BindType.BIND_TRX, Config.SMSC_USERNAME, Config.SMSC_PASSWORD, "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));

            iniciado.compareAndSet(false, true);

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
        logger.info("Running scheduled task - sending SMS");

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
