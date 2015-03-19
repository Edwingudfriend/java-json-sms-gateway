package com.opteral.gateway;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GatewayServletTest {

    private GatewayServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter response_writer;
    private Map<String, String> parameters;

    @Before
    public void setUp() throws IOException {

        parameters = new HashMap<String, String>();
        servlet = new GatewayServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        response_writer = new StringWriter();

        when(request.getProtocol()).thenReturn("HTTP/1.1");
        when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                return parameters.get((String) invocation.getArguments()[0]);
            }
        });
        when(response.getWriter()).thenReturn(new PrintWriter(response_writer));
    }

    @Test
    public void testWithoutJSONparameter() throws Exception {
        servlet.doPost(request, response);
        assertThat(response_writer.toString(),
                containsString("Error: A valid JSON request is required"));
    }

    @Test
    public void testWithEmptyJSONparameter() throws Exception {
        parameters.put("json", "");
        servlet.doPost(request, response);
        assertThat(response_writer.toString(),
                containsString("Error: A valid JSON request is required"));
    }

    @Test
    public void testOk() throws Exception {
        parameters.put("json", "this is not a valid JSON");
        servlet.doPost(request, response);
        assertThat(response_writer.toString(),
                containsString("OK"));
    }

}
