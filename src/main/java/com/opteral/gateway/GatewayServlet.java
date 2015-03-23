package com.opteral.gateway;

import com.opteral.gateway.database.SMSDAOMySQL;
import com.opteral.gateway.database.UserDAOMySQL;
import com.opteral.gateway.json.InParser;
import com.opteral.gateway.json.OutParser;
import com.opteral.gateway.json.RequestJSON;
import com.opteral.gateway.json.ResponseJSON;
import com.opteral.gateway.validation.CheckerSMS;
import com.opteral.gateway.validation.ValidatorImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@WebServlet(
        name = "com.opteral.gateway.servlet.GatewayServlet",
        urlPatterns = "/gateway"
)
public class GatewayServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doWork(request,response);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        doWork(request,response);

    }

    private void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = null;
        response.setCharacterEncoding("UTF-8");

        try
        {
            request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();

            return;
        }

        try
        {
            String jsonString = request.getParameter("json");

            if (jsonString != null && !jsonString.isEmpty())
            {
                RequestJSON requestJSON = InParser.getRequestJSON(jsonString);

                RequestSMS requestSMS = new RequestSMS(requestJSON,
                        new UserDAOMySQL(),
                        new SMSDAOMySQL(),
                        new CheckerSMS(new ValidatorImp()));

                response.getWriter().print(OutParser.getJSON(requestSMS.process()));
            }
            else
                throw new GatewayException("Error: A valid JSON request is required");
        }
        catch (Exception e) {
            response.getWriter().print(OutParser.getJSON(new ResponseJSON(e)));
        }

    }

}
