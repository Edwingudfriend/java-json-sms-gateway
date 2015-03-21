package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.LoginException;
import com.opteral.gateway.model.User;

public interface UserDAO {

    public User identify(String user, String password) throws GatewayException;

}
