package com.opteral.gateway;

import com.opteral.gateway.database.UserDAO;
import com.opteral.gateway.database.SMSDAO;
import com.opteral.gateway.json.JSON_SMS;
import com.opteral.gateway.json.RequestJSON;
import com.opteral.gateway.json.ResponseJSON;
import com.opteral.gateway.json.SMS_Response;
import com.opteral.gateway.model.SMS;
import com.opteral.gateway.model.User;
import com.opteral.gateway.validation.CheckerSMS;

import java.util.ArrayList;
import java.util.List;

public class RequestSMS {

    private UserDAO auth;
    private RequestJSON requestJSON;
    private ResponseJSON responseJSON;
    private CheckerSMS checkerSMS;
    private SMSDAO smsdao;
    private User user;


    public RequestSMS(RequestJSON requestJSON, UserDAO auth, SMSDAO smsdao, CheckerSMS checkerSMS) {

        this.requestJSON = requestJSON;
        this.auth = auth;
        this.smsdao = smsdao;
        this.checkerSMS = checkerSMS;

        responseJSON= new ResponseJSON(ResponseJSON.ResponseCode.OK);
    }

    public ResponseJSON process() throws GatewayException {

        auth();

        check();

        return doWork();

    }

    private void auth() throws GatewayException {

        user = auth.identify(requestJSON.getUser(), requestJSON.getPassword());

    }



    private void check() throws GatewayException {

        checkerSMS.check(requestJSON.getSms_request());
    }

    private ResponseJSON doWork() throws GatewayException {

        listProcess();


        return responseJSON;
    }

    private void listProcess() throws GatewayException {

        List<SMS_Response> sms_responses = new ArrayList<SMS_Response>() ;


        for (JSON_SMS jsonSMS : requestJSON.getSms_request())
        {
            try
            {

                SMS sms = new SMS(jsonSMS, user.getId());

                if (!sms.isTest())
                    smsdao.persist(sms);

                sms_responses.add(new SMS_Response(sms, true));


            }
            catch (Exception e) {
                sms_responses.add(new SMS_Response(jsonSMS, false));

            }
        }

        responseJSON.setSms_responses(sms_responses);
    }

}
