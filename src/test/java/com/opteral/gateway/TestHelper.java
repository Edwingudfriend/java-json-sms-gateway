package com.opteral.gateway;



import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class TestHelper {

    public static int EXCEPTION_EXPECTED = 1;
    public static int NORMAL = 0;

    public static String getFromFile(String fileName)
    {
        String result = "";

        ClassLoader classLoader = TestHelper.class.getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String genString(int num) {

        char aChar = 'a';
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < num; i++) {
            sb.append(aChar);
        }
        return sb.toString();
    }
}
