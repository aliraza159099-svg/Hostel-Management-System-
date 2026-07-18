package Project;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ManagerSignUp extends Frame implements ActionListener {
    Label l1, l2, l3, l4;
    TextField tf1, tf2, tf3, tf4;
    Button btn1;

    public ManagerSignUp() {
        this.setTitle("Manager SignUp");
        this.setSize(350, 500);
        this.setResizable(false);
        this.setBackground(Color.BLUE);
        this.setLayout(new GridLayout(9, 1));

        // fonts
        Font font = new Font("SansSerif", Font.BOLD, 15);

        l1 = new Label("Manager Name");
        l1.setFont(font);
        add(l1);
        tf1 = new TextField(30);
        tf1.setFont(font);
        add(tf1);

        l2 = new Label("Father Name");
        l2.setFont(font);
        add(l2);
        tf2 = new TextField(30);
        tf2.setFont(font);
        this.add(tf2);

        l4 = new Label("CNIC (without dashes)");
        l4.setFont(font);
        this.add(l4);
        tf4 = new TextField(13);
        tf4.setFont(font);
        this.add(tf4);

        l3 = new Label("Set Password");
        l3.setFont(font);
        add(l3);
        tf3 = new TextField(8);
        tf3.setFont(font);
        tf3.setEchoChar('*');
        add(tf3);

        btn1 = new Button("Sign Up");
        btn1.setFont(font);
        btn1.setBackground(Color.GREEN);
        add(btn1);
        btn1.addActionListener(this);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {

            // Read fields
            String managerName = tf1.getText().trim();
            String fatherName  = tf2.getText().trim();
            String cnic        = tf4.getText().trim();
            String password    = tf3.getText().trim();
          String  Password = String.valueOf(password.hashCode());

            // Validation
            if (managerName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Manager Name");
                return;
            }
            if (fatherName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Father Name");
                return;
            }
            if (cnic.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter CNIC");
                return;
            }
            if (cnic.length() != 13) {
                JOptionPane.showMessageDialog(null, "CNIC must be exactly 13 digits (without dashes)");
                return;
            }
            if (!cnic.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "CNIC must contain numbers only");
                return;
            }
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Password");
                return;
            }
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(null, "Password must be at least 6 characters");
                return;
            }

            // Database operations
            try (Connection conn = DatabaseManager.connect()) {

                // Check manager limit (max 3)
                String countQuery = "SELECT COUNT(*) AS total FROM Manager";
                PreparedStatement countStmt = conn.prepareStatement(countQuery);
                ResultSet rs = countStmt.executeQuery();
                if (rs.next()) {
                    int total = rs.getInt("total");
                    if (total >= 3) {
                        JOptionPane.showMessageDialog(null, "Maximum 3 Managers allowed. Cannot add more!");
                        return;
                    }
                }

                // Check if CNIC already exists
                String checkCnic = "SELECT cnic FROM Manager WHERE cnic = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkCnic);
                checkStmt.setString(1, cnic);
                ResultSet rs2 = checkStmt.executeQuery();
                if (rs2.next()) {
                    JOptionPane.showMessageDialog(null, "A Manager with this CNIC already exists!");
                    return;
                }

                // Insert new manager
                String insertQuery = "INSERT INTO Manager(name, fathername, cnic, password) VALUES (?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, managerName);
                pstmt.setString(2, fatherName);
                pstmt.setString(3, cnic);
                pstmt.setString(4, Password);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Manager Registered Successfully!");

                // Clear fields
                tf1.setText("");
                tf2.setText("");
                tf3.setText("");
                tf4.setText("");

                // Go to login
                this.dispose();
                new ManagerLogin();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }
        }
    }
}
