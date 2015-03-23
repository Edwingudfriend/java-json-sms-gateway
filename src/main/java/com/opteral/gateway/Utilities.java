package com.opteral.gateway;





import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.Properties;



public class Utilities {

    public static String findMD5(String arg) throws GatewayException {

        MessageDigest algorithm = null;
        try
        {
            algorithm = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
            throw new GatewayException("Cannot find digest algorithm");
        }
        byte[] defaultBytes = arg.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuilder hexString = new StringBuilder();

        for (byte aMessageDigest : messageDigest) {
            String hex = Integer.toHexString(0xff & aMessageDigest);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String sendGet(String url) throws GatewayException {

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();


            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "SMS Gateway");

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            throw new GatewayException("Error: Failed sending ACK on "+ url);
        }


    }

    public static void getConfig(boolean test) throws IOException {

        Properties properties = new Properties();
        InputStream input = null;

        if (test)
            input = Utilities.class.getClassLoader().getResourceAsStream("gateway.properties");
        else
            input = new FileInputStream("etc/jjsg/gateway.properties");

        properties.load(input);

        Config.SMSC_IP = properties.getProperty("smsc_ip");
        Config.SMSC_PORT = Integer.parseInt(properties.getProperty("smsc_port"));
        Config.SMSC_USERNAME = properties.getProperty("smsc_username");
        Config.SMSC_PASSWORD = properties.getProperty("smsc_password");

        Config.MAX_SMS_SIZE = Integer.parseInt(properties.getProperty("sms_max_size"));
        Config.SENDER_MAX_SIZE = Integer.parseInt(properties.getProperty("sender_max_size"));
        Config.SUBID_MAX_SIZE = Integer.parseInt(properties.getProperty("subid_max_size"));


    }

    public static String getFromFile(String fileName)
    {
        String result = "";

        ClassLoader classLoader = Utilities.class.getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
