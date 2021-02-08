/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.ui;

import ims.db.Auth;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;
import ims.ui.WHView;

/**
 *
 * @author Jennipher Oropesa Flores
 */
public class Login extends JFrame implements ActionListener {

    JPanel panel;
    JLabel user_label, role_label, password_label, message;
    JTextField userName_text, role_text;
    JPasswordField password_text;
    JButton submit, cancel;
    JComboBox role_dd;

    public Login() {

        // Username Label
        user_label = new JLabel();
        user_label.setText("User Name :");
        userName_text = new JTextField();
        userName_text.setBorder(new TextFieldBorder());

        // Password Label
        password_label = new JLabel();
        password_label.setText("Password :");
        password_text = new JPasswordField();

        // Role Label
        role_label = new JLabel();
        role_label.setText("Role :");
        String roles[] = { "Administrator", "Store Manager", "Warehouse Operator" };
        role_dd = new JComboBox(roles);

        // Submit
        submit = new JButton("SUBMIT");
        panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBorder(new EmptyBorder(10, 30, 10, 30));

        GridLayout layout = new GridLayout(9, 1);

        panel.setLayout(layout);

        JLabel header = new JLabel("Jenni's Cosmetics - Authentication");
        header.setFont(new Font("Serif", Font.BOLD, 22));

        header.setForeground(Color.BLUE);
        panel.add(header);
        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);
        panel.add(role_label);
        panel.add(role_dd);
        panel.add(new JLabel());
        panel.add(submit);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Authentication - Jenni's Consmetics");
        setSize(450, 350);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userName = userName_text.getText();
        String password = password_text.getText();
        String role = (String) role_dd.getSelectedItem();
        int role_int = 0;
        switch(role){
            case "Administrator" -> role_int = 0;
            case "Store Manager" -> role_int = 1;
            case "Warehouse Operator" -> role_int = 2;
        }
        Auth auth = new Auth();
        boolean loginStatus = false;
        try {
            loginStatus = auth.login_validater(userName, password, role_int);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (loginStatus) {
            JOptionPane.showMessageDialog(this, "Login Successful");
            this.dispose();
            switch(role_int){
                case 0 -> {
                    new AdminFrame().setVisible(true);
                }
                case 1 -> {
                    new SMFrame().setVisible(true);
                }
                case 2 -> {
                    try {
                        new WHView();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Authentication Failure, Try Again");
        }
    }

}
