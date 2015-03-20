package com.opteral.gateway.validation;


import org.apache.commons.validator.UrlValidator;

public class ValidatorImp implements Validator {


    private String[] schemes = {"http","https"};

    private UrlValidator urlValidator = new UrlValidator(schemes);



    @Override
    public boolean isURL(String url) {

        return urlValidator.isValid(url);
    }
}
