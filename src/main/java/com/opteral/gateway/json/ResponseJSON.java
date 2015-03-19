package com.opteral.gateway.json;


import com.opteral.gateway.GatewayException;
import com.opteral.gateway.LoginException;

public class ResponseJSON {

    private ResponseCode response_code;
    private String msg;



    public ResponseJSON(ResponseCode response_code, String msg) {
        this.response_code = response_code;
        this.msg = msg;
    }

    public ResponseJSON(Exception exception) {
        this.response_code = ResponseCode.ERROR_GENERAL;
        this.msg = exception.getMessage();
    }

    public ResponseJSON(LoginException loginException) {
        this.response_code = ResponseCode.ERROR_LOGIN;
        this.msg = loginException.getMessage();
    }

    public ResponseCode getResponse_code() {
        return response_code;
    }

    public void setResponse_code(ResponseCode response_code) {
        this.response_code = response_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public enum ResponseCode {

        ERROR_GENERAL(-2),
        ERROR_LOGIN(-1),
        OK(1);

        private int value;

        ResponseCode(int value) {

            this.value = value;

        }

        public int getValue() {
            return value;
        }

        public static ResponseCode fromInt(int num) throws IllegalArgumentException {
            for(ResponseCode t : values()){
                if( t.value == num ){
                    return t;
                }
            }
            throw new IllegalArgumentException(num +" is not a valid response code");
        }

    }
}
