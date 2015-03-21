package com.opteral.gateway.database;

import com.opteral.gateway.LoginException;
import com.opteral.gateway.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.opteral.gateway.database.EntitiesHelper.assertUser;
import static org.junit.Assert.assertNull;

@RunWith(AbstractDbUnitTemplateTestCase.DataSetsTemplateRunner.class)
public class UserDAOMySQLTest extends AbstractDbUnitTemplateTestCase {

    @Before
    public void init()
    {
        id = 456;
    }

    @Test
    @DataSets(setUpDataSet="/dataset/user.xml")
    public void testAuthOK() throws Exception {
        User user =  userDAO.identify("amalio", "secret") ;
        assertUser(user);
    }

    @Test (expected = LoginException.class)
    @DataSets(setUpDataSet="/dataset/user.xml")
    public void testAuthFail() throws Exception {
        User user =  userDAO.identify("amalio", "wrongpassword") ;
        assertNull(user);
    }


}