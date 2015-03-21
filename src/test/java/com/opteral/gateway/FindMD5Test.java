package com.opteral.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.opteral.gateway.Utilities.findMD5;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FindMD5Test {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

                {"5ebe2294ecd0e0f08eab7690d2a6ee69", "secret"},
                {"d41d8cd98f00b204e9800998ecf8427e", ""},
                {"4d186321c1a7f0f354b297e8914ab240", "hola"}

        });
    }

    @Parameterized.Parameter
    public String expected;

    @Parameterized.Parameter (value = 1)
    public String stringToEncode;



    @Test
    public void test() throws GatewayException {

        assertEquals(expected, findMD5(stringToEncode));
    }




}