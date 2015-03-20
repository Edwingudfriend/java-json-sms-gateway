package com.opteral.gateway.validation;

import com.opteral.gateway.GatewayException;

public interface Validator {

    boolean isURL(final String cadena) throws GatewayException;
    boolean isMsisdn(final String cadena) throws GatewayException;
}
