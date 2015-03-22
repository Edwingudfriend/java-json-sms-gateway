package com.opteral.gateway;

import com.opteral.gateway.json.JSON_SMS;
import com.opteral.gateway.json.OutParser;
import com.opteral.gateway.json.RequestJSON;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@Ignore
public class FunctionalTest {

    private static final Logger logger = Logger.getLogger(FunctionalTest.class);

    @Test
    public void testFuncional() throws MalformedURLException {
        logger.info("Iniciando TEST |||||||||||||||||||| ------->>>>>>>>>>>>>>>> ");

        RequestJSON requestJSON = new RequestJSON();

        requestJSON.setUser("amalio");
        requestJSON.setPassword("secret");
        requestJSON.setSms_request(new ArrayList<JSON_SMS>());

        JSON_SMS  jsonsms = new JSON_SMS();

        jsonsms.setId(0);
        jsonsms.setMsisdn("34646548725");
        jsonsms.setSender("sender");
        jsonsms.setText("The SMS text with an ñ");
        jsonsms.setAck_url("http://www.anurl.com/ack");
        jsonsms.setSubid("subid1");
        jsonsms.setDatetime(null);
        jsonsms.setTest(false);


        for (int i=0; i<10; i++)
        {
            requestJSON.getSms_request().add(jsonsms);
        }

        String cadenaJSON = OutParser.getJSON(requestJSON);

        URL url = new URL("http://api.opteral.com/gateway");
        String post = "json="+cadenaJSON;


        try {
            logger.info(postRequest(url, post));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    private String postRequest(URL url, String post) throws IOException {


        InputStream inputStream = null;


        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(post);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();


            inputStream = conn.getInputStream();


            return inputStreamToString(inputStream);


        }
        catch (Exception e) {
            return e.getMessage();
        }
        finally
        {
            if (inputStream != null) {
                inputStream.close();
            }
        }


    }

    private String inputStreamToString(InputStream is) throws IOException {

        String line = "";
        StringBuilder total = new StringBuilder();


        BufferedReader rd = new BufferedReader(new InputStreamReader(is));


        while ((line = rd.readLine()) != null) {
            total.append(line);
        }


        return total.toString();
    }

}
