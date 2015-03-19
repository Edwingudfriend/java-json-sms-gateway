package com.opteral.gateway;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "com.opteral.gateway.servlet.GatewayServlet",
        urlPatterns = "/gateway"
)
public class GatewayServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String jsonString = request.getParameter("json");

        if (jsonString != null && !jsonString.isEmpty())
            response.getWriter().print("OK");
        else
            response.getWriter().print("Error: A valid JSON request is required");


    }
}
