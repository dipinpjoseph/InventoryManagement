/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.db;

import java.sql.SQLException;
import org.junit.Test;  

/**
 *
 * @author Dipin P Joseph
 */
public class AuthTest {
    
    
    @Test
    public void loginTest() throws SQLException{
        boolean expectedStatus = false;
        Auth authTest = new Auth();
        boolean actualStatus=authTest.login_validater("u1","p1",1);
        assertEquals(expectedStatus, actualStatus);
    }
}
