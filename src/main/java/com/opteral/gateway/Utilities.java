package com.opteral.gateway;

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

}
