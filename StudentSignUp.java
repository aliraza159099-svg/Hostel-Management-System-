package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentSignUp extends Frame
        implements ActionListener {

    Label l1, l2, l3;

    TextField tf1, tf2, tf3;

    Button btn1;

    public StudentSignUp() {

        this.setTitle("Student Sign Up");

        this.setSize(350, 500);

        this.setResizable(false);

        this.setBackground(Color.BLUE);

        this.setLayout(new GridLayout(7, 1));

        Font font =
                new Font("SansSerif",
                        Font.PLAIN, 15);

        l1 = new Label("Name");
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
        add(tf2);

        l3 = new Label("Set Password");
        l3.setFont(font);
        add(l3);

        tf3 = new TextField(8);

        tf3.setEchoChar('*');

        add(tf3);

        btn1 = new Button("Sign Up");

        btn1.setFont(font);

        btn1.setBackground(Color.GREEN);

        add(btn1);

        btn1.addActionListener(this);

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
        if (e.getSource() == btn1) {

            String name = tf1.getText().trim();

            String fatherName =
                    tf2.getText().trim();

            String password =
                    tf3.getText().trim();

            // Validation

            if (name.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Please Enter Student Name");

                return;
            }

            if (fatherName.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Please Enter Father Name");

                return;
            }

            if (password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Please Enter Password");

                return;
            }

            if (name.length() < 3) {

                JOptionPane.showMessageDialog(
                        null,
                        "Name must contain at least 3 characters");

                return;
            }

            if (fatherName.length() < 3) {

                JOptionPane.showMessageDialog(
                        null,
                        "Father name must contain at least 3 characters");

                return;
            }

            // Name validation

            if (!Character.isLetter(name.charAt(0))) {

                JOptionPane.showMessageDialog(
                        null,
                        "Name cannot start with number");

                return;
            }

            try (
                    Connection conn =
                            DatabaseManager.connect();

                    Statement stmt =
                            conn.createStatement()
            ) {

                // Create table

                String createTableSQL = """
                        CREATE TABLE IF NOT EXISTS Student (
                            ID INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL UNIQUE,
                            fathername TEXT NOT NULL,
                            password TEXT NOT NULL
                        )
                        """;

                stmt.execute(createTableSQL);

                // Check existing username

                String checkQuery =
                        "SELECT * FROM Student WHERE name = ?";

                PreparedStatement checkStmt =
                        conn.prepareStatement(checkQuery);

                checkStmt.setString(1, name);

                ResultSet rs =
                        checkStmt.executeQuery();

                if (rs.next()) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Username already exists");

                    return;
                }

                // Insert student

                String insertQuery = " INSERT INTO Student(name, fathername, password) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, name);
                pstmt.setString(2, fatherName);
                pstmt.setString(3, password);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Sign Up Successful");
                this.dispose();
                new StudentLogin();
            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Database Error: "
                                + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        new StudentSignUp();
    }
}
