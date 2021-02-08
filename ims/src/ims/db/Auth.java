/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.db;
import ims.Constants;
import static ims.Constants.DB_URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 *
 * @author Jennipher Oropesa Flores
 */
public class Auth {
    
    /**
    * This method validates login by matching the credentials with values stored in database
    * @param username indicates username
    * @param pass specifies password
    * @param role contains corresponding user_role
    * @return success or failure status
    * @throws SQLException if any database access error occurs
    */
    public boolean login_validater(String username, String pass, int role) throws SQLException{
        try {
            Class.forName(Constants.JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, "root", "");
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM auth WHERE userId=? AND password=? AND role=?")) {
                stmt.setString(1, username);
                stmt.setString(2, pass);
                stmt.setInt(3, role);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next())
                {
                    System.out.format("Login Successfull");
                    return true;
                }
            }
            System.out.println("Login Failure");
        //This catches exceptions, if any
        }   catch (Exception ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

            

    
    
}
