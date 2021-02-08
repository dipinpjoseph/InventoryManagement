/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.db;

import ims.Constants;
import static ims.Constants.DB_URL;
import ims.Constants;
import static ims.Constants.DB_URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jennipher Oropesa Flores
 */
class BaseOrder {

    String customerName;
    double amount;
    Date paymentDate;
}

public class Orders extends BaseOrder {

    String orderType;
    String productIds;
    String productQtys;
    int[] pIdsArray;
    int[] pQtysArray;
    Inventory inv = new Inventory();

    /**
    * This method creates a new order and displays total price
    * @param cName indicates Customer name
    * @param oType specifies the order type
    * @param pIds contains product ids
    * @param pQtys contains no.of items added
    * @param pDate contains the date of purchase
    * @return success or failure status
    */
    public void addOrder(String cName, String oType, String pIds, String pQtys, Date pDate) {
        try {
            this.customerName = cName;
            this.orderType = oType;
            this.productIds = pIds;
            this.productQtys = pQtys;
            this.paymentDate = pDate;

            /*
            * Converting String to Integer array by streaming the array, 
            * parsing each element separately and collecting them to an array
            */
            this.pIdsArray = Arrays.stream(this.productIds.split(",")).mapToInt(Integer::parseInt).toArray();
            this.pQtysArray = Arrays.stream(this.productQtys.split(",")).mapToInt(Integer::parseInt).toArray();
            calculateAmount(pIdsArray, pQtysArray);

            Class.forName(Constants.JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (p_ids,p_qtys, o_type, c_name, p_date, amount) "
                    + "VALUES (?,?,?,?,?,?)");
            stmt.setString(1, this.productIds);
            stmt.setString(2, this.productQtys);
            stmt.setString(3, this.orderType);
            stmt.setString(4, this.customerName);
            stmt.setDate(5, new java.sql.Date(this.paymentDate.getTime()));
            stmt.setDouble(6, this.amount);
            System.out.println(stmt);

            int rs = stmt.executeUpdate();
            if (rs != -1) {
                System.out.format("Order Added Successfully");
                // Update Product Qtys
                for (int i = 0; i < this.pIdsArray.length; i++) {
                    int newQty = inv.getProductQty(pIdsArray[i]) - pQtysArray[i];
                    inv.updateProductQty(Integer.toString(pIdsArray[i]), Integer.toString(newQty));
                }
                JOptionPane.showMessageDialog(null, "Order Added Successfully, Total Amount: " + this.amount);
            }

        } catch (Exception ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
    * This method takes product ids and corresponding quantities added to the order 
    * and calculates the price
    *
    * @param pIdsArray indicates product Ids
    * @param pQtysArray specifies product quantities added to the order
    */
    void calculateAmount(int[] pIdsArray, int[] pQtysArray) {

        this.amount = 0;

        for (int i = 0; i < pIdsArray.length; i++) {
            double eachPrice = inv.getProductPrice(pIdsArray[i]);
            this.amount = this.amount + (eachPrice * pQtysArray[i]);
        }
    }

    /**
    * This method is for fetching order details in the specified time frame
    * @param fDate indicates start date
    * @param tDate indicates end date
    * @return Order details in the specified time frame
    */
    public DefaultTableModel getOrderData(java.sql.Date fDate, java.sql.Date tDate) {

        DefaultTableModel model = new DefaultTableModel(new String[]{"Order ID", "Customer Name", "Product Ids", "Product Qtys", "Order Type", "Date", "Amount"}, 0);

        try {

            Class.forName(Constants.JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders where p_date >=? AND p_date <= ?");
            stmt.setDate(1, fDate);
            stmt.setDate(2, tDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String o_id = rs.getString("order_id");
                String c_name = rs.getString("c_name");
                String p_qtys = rs.getString("p_qtys");
                String p_ids = rs.getString("p_ids");
                String o_type = rs.getString("o_type");
                String p_date = rs.getString("p_date");
                String amount = rs.getString("amount");
                model.addRow(new Object[]{o_id, c_name, p_ids, p_qtys, o_type, p_date, amount});
            }

        } catch (Exception ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

}
