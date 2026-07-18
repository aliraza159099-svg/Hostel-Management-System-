package Project;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ManagerLogin extends Frame implements ActionListener {
    Label l1, l2, l3;
    TextField tf1, tf2;
    Button btn1, btn2;

    public ManagerLogin() {
        this.setTitle("Manager Login");
        this.setSize(270, 400);
        setBackground(Color.BLUE);
        setResizable(false);
        this.setLayout(new GridLayout(7, 1));

        Font font = new Font("SansSerif", Font.BOLD, 15);

        l1 = new Label("Manager Name");
        l1.setFont(font);
        this.add(l1);

        tf1 = new TextField(20);
        tf1.setFont(font);
        this.add(tf1);

        l2 = new Label("Password");
        l2.setFont(font);
        this.add(l2);

        tf2 = new TextField(20);
        tf2.setFont(font);
        tf2.setEchoChar('*');
        this.add(tf2);

        btn1 = new Button("Login");
        btn1.setFont(font);
        btn1.setBackground(Color.GREEN);
        this.add(btn1);
        btn1.addActionListener(this);

        l3 = new Label("Sign Up if you don't have account");
        l3.setForeground(Color.PINK);
        l3.setFont(font);
        this.add(l3);

        btn2 = new Button("Sign Up");
        btn2.setFont(font);
        btn2.setBackground(Color.YELLOW);
        this.add(btn2);
        btn2.addActionListener(this);

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

            String managerName = tf1.getText().trim();
            String password    = tf2.getText().trim();

            // Validation
            if (managerName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Manager Name");
                return;
            }
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Password");
                return;
            }

            // Hash password the same way SignUp does
            String hashedPassword = String.valueOf(password.hashCode());

            try (Connection conn = DatabaseManager.connect()) {

                String query = "SELECT * FROM Manager WHERE name = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, managerName);
                pstmt.setString(2, hashedPassword);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login Successful! Welcome, " + managerName);
                    this.dispose();
                    Session.getInstance().loginAsManager();
                    new ManagerMainDashboard(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Name or Password. Please try again.");
                    tf2.setText(""); // Clear password field only
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }

        } else if (e.getSource() == btn2) {
            this.dispose();
            new ManagerSignUp();
        }
    }
}
