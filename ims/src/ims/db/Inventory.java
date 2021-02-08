/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.db;

import ims.Constants;
import static ims.Constants.DB_URL;
import com.mysql.cj.protocol.Resultset;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jennipher Oropesa Flores
 */
public class Inventory {

    /**
    * This method fetches product details from the database
    * @return details from Inventory database
    * @throws ClassNotFoundException if exception occurs while loading class
    * @throws SQLException if any database access error occurs
    */
    public DefaultTableModel getProductData() throws ClassNotFoundException, SQLException {

        DefaultTableModel model = new DefaultTableModel(new String[]{"Product ID", "Name", "Description", " Price", "QTY"}, 0);

        Class.forName(Constants.JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL, "root", "");
        Statement stmt = (Statement) conn.createStatement();
        String query = "SELECT * FROM inventory";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String p_id = rs.getString("p_id");
            String p_price = rs.getString("p_price");
            String p_qty = rs.getString("p_qty");
            String p_desc = rs.getString("p_desc");
            String p_name = rs.getString("p_name");
            model.addRow(new Object[]{p_id, p_name, p_desc, p_price, p_qty});
        }
        return model;

    }

    /**
    * This method adds a new product to the database
    * @param productName contains item name
    * @param productPrice contains price of item
    * @param productQty contains the no.of items
    * @param productDesc contains product description
    * @return success or failure status
    */
    public boolean addProduct(String productName, String productPrice, String productQty, String productDesc) throws ClassNotFoundException, SQLException {

        Class.forName(Constants.JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL, "root", "");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO inventory (p_name, p_price, p_qty, p_desc) "
                + "VALUES (?,?,?,?)");
        stmt.setString(1, productName);
        stmt.setFloat(2, Float.parseFloat(productPrice));
        stmt.setInt(3, Integer.parseInt(productQty));
        stmt.setString(4, productDesc);
        int rs = stmt.executeUpdate();
        if (rs != -1) {
            System.out.format("Product Added Successfully");
            return true;
        }
        return false;
    }

    /**
    * This method updates product quantity in the database
    * @param productId contains unique id of the product
    * @param productQty contains the no.of items
    * @return success or failure status
    */
    public boolean updateProductQty(String productId, String productQty) {

        try {
            Class.forName(Constants.JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, "root", "");
            PreparedStatement stmt = conn.prepareStatement("UPDATE inventory SET p_qty = ? WHERE p_id = ?");
            stmt.setInt(2, Integer.parseInt(productId));
            stmt.setInt(1, Integer.parseInt(productQty));
            int rs = stmt.executeUpdate();
            if (rs != -1) {
                System.out.format("Product Updated Successfully");
                return true;
            }

        } catch (Exception ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
    * This method deletes product from the database
    * @param productId contains unique id of the product
    * @return success or failure status
    */
    public boolean deleteProduct(String productId) {
        try {
            Class.forName(Constants.JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, "root", "");
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM inventory WHERE p_id =?");
            stmt.setInt(1, Integer.parseInt(productId));
            int rs = stmt.executeUpdate();
            if (rs != -1) {
                System.out.format("Product Deleted Successfully");
                return true;
            }

        } catch (Exception ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
    * This method retrieves the price of products by product id
    * @param pId contains unique id of the product
    * @return product price
    */
    public double getProductPrice(int pId) {
        try {
            Class.forName(Constants.JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT p_price FROM inventory WHERE p_id =?");
            stmt.setInt(1, pId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Double.parseDouble(rs.getString("p_price"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return 0;
    }

    /**
    * This method retrieves product quantity details by product id
    * @param pId contains unique id of the product
    * @return product quantity left in stock
    */ 
    public int getProductQty(int pId) {
        try {
            Class.forName(Constants.JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT p_qty FROM inventory WHERE p_id =?");
            stmt.setInt(1, pId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Integer.parseInt(rs.getString("p_qty"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return 0;
    }
}
