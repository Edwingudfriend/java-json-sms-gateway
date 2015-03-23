package com.opteral.gateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

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

}
