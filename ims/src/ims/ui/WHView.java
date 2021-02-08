/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.ui;

import ims.db.Inventory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Jennipher Oropesa Flores
 */
public class WHView extends JFrame implements ActionListener {

    JLabel header;
    JPanel northPanel, southPanel, leftPanel, rightPanel;
    JTextField productName, productDesc, productQty, productPrice, productUpdateQty, productId;
    JButton addProd, updateQty, deleteProd;
    JTable jt;
    Inventory inv = new Inventory();

    public WHView() throws ClassNotFoundException, SQLException {

        header = new JLabel("Warehouse Management");
        header.setFont(new Font("Serif", Font.BOLD, 22));

        northPanel = new JPanel();
        northPanel.setBackground(Color.white);
        northPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        northPanel.add(header);
        add(northPanel, BorderLayout.NORTH);

        leftPanel = new JPanel();
        leftPanel.setBackground(Color.white);
        leftPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        jt = new JTable();
        //jt.setBounds(30,40,200,300);   
        jt.setModel(inv.getProductData());
        JScrollPane sp = new JScrollPane(jt);
        leftPanel.add(sp);
        add(leftPanel, BorderLayout.WEST);

        rightPanel = new JPanel();
        rightPanel.setBackground(Color.ORANGE);
        rightPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        rightPanel.setLayout(new GridLayout(22, 1));

        rightPanel.add(new JLabel("Add New Product"));
        rightPanel.add(new JLabel("Name"));
        productName = new JTextField();
        rightPanel.add(productName);

        rightPanel.add(new JLabel("Description"));
        productDesc = new JTextField();
        rightPanel.add(productDesc);

        rightPanel.add(new JLabel("Quantity"));
        productQty = new JTextField();
        rightPanel.add(productQty);

        rightPanel.add(new JLabel("Price"));
        productPrice = new JTextField();
        rightPanel.add(productPrice);

        addProd = new JButton("Add Product");
        rightPanel.add(new JLabel(""));
        rightPanel.add(addProd);
        rightPanel.add(new JLabel(""));
        addProd.addActionListener(this);

        rightPanel.add(new JLabel("Update/Delete Product"));
        rightPanel.add(new JLabel("Product Id"));
        productId = new JTextField();
        rightPanel.add(productId);

        rightPanel.add(new JLabel("New Quantity"));
        productUpdateQty = new JTextField();
        rightPanel.add(productUpdateQty);

        updateQty = new JButton("Update Qty");
        rightPanel.add(new JLabel(""));
        rightPanel.add(updateQty);
        updateQty.addActionListener(this);
        
               deleteProd = new JButton("Delete Product");
        rightPanel.add(new JLabel(""));
        rightPanel.add(deleteProd);
        deleteProd.addActionListener(this);

        add(rightPanel, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WareHouse Management - Jenni's Consmetics");
        setSize(700, 500);
        setVisible(true);
        setFont(new Font("Serif", Font.PLAIN, 14));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getActionCommand() == "Add Product") {
                if (inv.addProduct(productName.getText(), productPrice.getText(), productQty.getText(), productDesc.getText())) {
                    JOptionPane.showMessageDialog(this, "Product Added Successfully");
                    jt.setModel(inv.getProductData());
                } else {
                    JOptionPane.showMessageDialog(this, "Product Addition Failed");
                }
            }
            if (e.getActionCommand() == "Update Qty") {
                if (inv.updateProductQty(productId.getText(), productUpdateQty.getText())) {
                    JOptionPane.showMessageDialog(this, "Product Updated Successfully");
                    jt.setModel(inv.getProductData());
                } else {
                    JOptionPane.showMessageDialog(this, "Product Update Failed");
                }
            }
                        if (e.getActionCommand() == "Delete Product") {
                if (inv.deleteProduct(productId.getText())) {
                    JOptionPane.showMessageDialog(this, "Product Deleted Successfully");
                    jt.setModel(inv.getProductData());
                } else {
                    JOptionPane.showMessageDialog(this, "Product Deletion Failed");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Product Addition Failed");
            Logger.getLogger(WHView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

