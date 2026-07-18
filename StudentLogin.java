package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import Project.*;

public class StudentLogin extends Frame
        implements ActionListener {

    Label l1, l2, l3;

    TextField tf1, tf2;

    Button btn1, btn2;

    // Constructor

    public StudentLogin() {

        this.setTitle("Student Login");

        this.setSize(270, 400);

        this.setBackground(Color.BLUE);

        this.setResizable(false);

        this.setLayout(new GridLayout(7, 1));

        Font font =
                new Font("SansSerif",
                        Font.PLAIN, 15);

        l1 = new Label("                      Student Name");

        l1.setFont(font);

        add(l1);

        tf1 = new TextField(20);

        tf1.setFont(font);

        add(tf1);
        l2 = new Label("                       Password");

        l2.setFont(font);
        add(l2);

        tf2 = new TextField(20);

        tf2.setEchoChar('*');

        add(tf2);

        btn1 = new Button("Login");

        btn1.setFont(font);

        btn1.setBackground(Color.GREEN);

        add(btn1);

        btn1.addActionListener(this);

        l3 = new Label(
                "  Sign Up if you don't have account");

        l3.setForeground(Color.PINK);

        l3.setFont(font);

        add(l3);

        btn2 = new Button("Sign Up");

        btn2.setFont(font);

        btn2.setBackground(Color.YELLOW);

        add(btn2);

        btn2.addActionListener(this);

        this.addWindowListener(
                new WindowAdapter() {

                    public void windowClosing(
                            WindowEvent e) {

                        System.exit(0);
                    }
                });

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // LOGIN BUTTON

        if (e.getSource() == btn1) {

            String name = tf1.getText().trim();

            String pass = tf2.getText().trim();

            // Validation

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Student Name");
                return;
            }

            if (pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Password");
                return;
            }

            try (Connection conn = DatabaseManager.connect()) {
                // Login query
                String query = "SELECT * FROM Student WHERE name = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setString(2, pass);
                ResultSet rs = pstmt.executeQuery();
                String query2 = "SELECT RoomNumber FROM Room WHERE StudentId = ? LIMIT 1";
                pstmt = conn.prepareStatement(query2);
                pstmt.setInt(1, rs.getInt("ID"));
                ResultSet rs2 = pstmt.executeQuery();
                int roomnumber = rs2.getInt("RoomNumber");
                if (rs.next()) {
                    // Populate session with the student's actual DB data
                    Session.getInstance().loginAsStudent(
                            rs.getInt("ID"),
                            rs.getString("name"),
                            rs.getString("fathername"),
                            roomnumber
                    );
                    JOptionPane.showMessageDialog(null, "Login Successful");
                    this.dispose();


                    new StudentMainDashboard();

                } else {

                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }

            } catch (SQLException ex) {

                System.out.println("Database Error: " + ex.getMessage());
            }

        }

        // SIGNUP BUTTON

        else if (e.getSource() == btn2) {

            this.dispose();

            new StudentSignUp();
        }
    }


}
