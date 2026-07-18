package Project;
import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentMainDashboard extends Frame implements ActionListener {
    Button btn1, btn2, btn3,btn5,btn6;
    Panel contentPanel;
    CardLayout cardLayout;
    ProfilePanel profilePanel;
    public StudentMainDashboard() {
        cardLayout = new CardLayout();
        contentPanel = new Panel(cardLayout);
        // Set layout FIRST before adding components
        this.setLayout(new BorderLayout());
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setSize(500, 500);
        this.setBackground(new Color(20, 120, 120));
        this.setTitle("Student Main Dashboard");

        Font font = new Font("Sansarif", Font.BOLD, 20);

        // Sidebar panel for buttons (use FlowLayout or GridLayout)
        Panel sidebar = new Panel(new GridLayout(1, 6, 10, 10));
        sidebar.setBackground(Color.DARK_GRAY);

        btn1 = new Button("Profile");
        btn1.setFont(font);
        btn1.setBackground(Color.CYAN);
        btn1.addActionListener(this);
        sidebar.add(btn1);

        btn2 = new Button("Request Room");
        btn2.setFont(font);
        btn2.setBackground(Color.CYAN);
        btn2.addActionListener(this);
        sidebar.add(btn2);

        btn3 = new Button("Mess Menu");
        btn3.setFont(font);
        btn3.setBackground(Color.CYAN);
        btn3.addActionListener(this);
        sidebar.add(btn3);


       btn5 = new Button("Write Complaints") ;
       btn5.setFont(font);
       btn5.setBackground(Color.CYAN);
       btn5.addActionListener(this);
       sidebar.add(btn5);
        btn6 = new Button("Visitor");
        btn6.setFont(font);
        btn6.setBackground(Color.CYAN);
        btn6.addActionListener(this);
        sidebar.add(btn6);

        // Add sidebar to NORTH
        this.add(sidebar, BorderLayout.NORTH);

        // CardLayout for content switching
        cardLayout = new CardLayout();
        contentPanel = new Panel(cardLayout);

        // Add panels with string identifiers
        profilePanel = new ProfilePanel();

        contentPanel.add(new ProfilePanel(), "PROFILE");
        contentPanel.add(new RoomRequestPanel(), "REQUEST");
        contentPanel.add(new MessMenuPanel(false), "MESS");
        contentPanel.add(new ComplaintsPanel(), "COMPLAINTS");
        contentPanel.add(new AddVisitorPanel(), "VISITOR");

        // Add content panel to CENTER
        this.add(contentPanel, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Use stored cardLayout reference + correct panel
        if (e.getSource() == btn1) {
            profilePanel.refresh();
            cardLayout.show(contentPanel, "PROFILE");
        } else if (e.getSource() == btn2) {
            cardLayout.show(contentPanel, "REQUEST");
        } else if (e.getSource() == btn3) {
            cardLayout.show(contentPanel, "MESS");
        } else if (e.getSource() == btn5) {
            cardLayout.show(contentPanel, "COMPLAINTS");
        }  else if (e.getSource() == btn6) {
            cardLayout.show(contentPanel, "VISITOR");
        }
    }

    public static void main(String[] args) {
        DatabaseManager.initializeTables();
        new StudentMainDashboard();
    }
}

// Student Profile Panel
class ProfilePanel extends Panel {
    Label name1, father1, id1, room1;
    public ProfilePanel() {
        this.setLayout(new BorderLayout());
        Font font  = new Font("SansSerif", Font.BOLD, 20);
        Font font1 = new Font("SansSerif", Font.BOLD, 25);
        Font font2 = new Font("SansSerif", Font.PLAIN, 20);
        Label profileLabel = new Label("Student Profile", Label.CENTER);
        profileLabel.setForeground(Color.BLUE);
        profileLabel.setBackground(Color.WHITE);
        profileLabel.setFont(font1);
        this.add(profileLabel, BorderLayout.NORTH);
        Panel leftPanel = new Panel(new GridLayout(8, 1));
        Label name   = new Label("Name: ");           name.setFont(font);
        Label father = new Label("Father's Name: ");  father.setFont(font);
        Label id     = new Label("ID: ");             id.setFont(font);
        Label room   = new Label("Room No: ");        room.setFont(font);
        // these labels will be updated by refresh()
        name1   = new Label(""); name1.setFont(font2);   name1.setForeground(Color.GREEN);
        father1 = new Label(""); father1.setFont(font2); father1.setForeground(Color.ORANGE);
        id1     = new Label(""); id1.setFont(font2);     id1.setForeground(Color.ORANGE);
        room1   = new Label(""); room1.setFont(font2);   room1.setForeground(Color.ORANGE);
        leftPanel.add(name);
        leftPanel.add(name1);
        leftPanel.add(father);
        leftPanel.add(father1);
        leftPanel.add(id);
        leftPanel.add(id1);
        leftPanel.add(room);
        leftPanel.add(room1);

        this.add(leftPanel, BorderLayout.CENTER);

        // load data for the first time
        refresh();
    }

    // call this every time profile tab is opened
    public void refresh() {
        name1.setText("  " + Session.getInstance().getStudentName());
        father1.setText("  " + Session.getInstance().getFatherName());
        id1.setText("  " + Session.getInstance().getStudentId());
        room1.setText("  " + Session.getInstance().getRoomNumber());
    }


}

// Complaints Panel - file complaints and view status
class ComplaintsPanel extends Panel implements ActionListener {
    TextArea complaintsArea;
    Button submitButton;

    public ComplaintsPanel() {
        this.setLayout(new BorderLayout());
        Font font = new Font("SansSerif", Font.BOLD, 24);
        Label complaintsLabel = new Label("Complaints Page", Label.CENTER);
        complaintsLabel.setFont(font);
        this.add(complaintsLabel, BorderLayout.NORTH);
        complaintsArea = new TextArea("Enter your complaint here...");
        complaintsArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        this.add(complaintsArea, BorderLayout.CENTER);
        submitButton = new Button("Submit Complaint");
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(Color.GREEN);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        this.add(submitButton, BorderLayout.SOUTH);
        // Add action listener for submit button (not implemented here)
        submitButton.addActionListener(this);
        complaintsArea.addFocusListener(new FocusListener() {

            // when user clicks on the field
            public void focusGained(FocusEvent e) {
                if (complaintsArea.getText().equals("Enter your complaint here...")) {
                    complaintsArea.setText("");                    // clear placeholder
                    complaintsArea.setForeground(Color.BLACK);     // switch to normal color
                }
            }

            // when user clicks away from the field
            public void focusLost(FocusEvent e) {
                if (complaintsArea.getText().isEmpty()) {
                    complaintsArea.setText("Enter student name");  // bring placeholder back
                    complaintsArea.setForeground(Color.GRAY);      // back to gray
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String Complaints = complaintsArea.getText();
            if (Complaints.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Complaint");
            }
            else{
                try(Connection conn = DatabaseManager.connect()){
                    String insertquery = "INSERT INTO Complaint(complaint,name,fathername,roomnumber,Status) VALUES(?,?,?,?,?)";
                    PreparedStatement pstmt =  conn.prepareStatement(insertquery);
                    pstmt.setString(1, Complaints);
                    pstmt.setString(2, Session.getInstance().getStudentName());
                    pstmt.setString(3,Session.getInstance().getFatherName());
                    pstmt.setInt(4,Session.getInstance().getRoomNumber());
                 pstmt.setString(5,"Pending");
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Complaint Submitted Successfully");
                }catch(SQLException ex){
                        System.out.println("Error " + ex.getMessage());

                }
            }

        }
    }
}

// Visitor Panel - manage visitor entries and exits
class AddVisitorPanel extends Panel {
    Label visitorLabel, studentname, visitorname, studentId, relation;
    TextField visitorTf, studentnameTf, studentIdTf, relationTf;
    Button submitButton;

    public AddVisitorPanel() {
        this.setLayout(new BorderLayout());

        // Title
        Font font = new Font("SansSerif", Font.BOLD, 24);
        visitorLabel = new Label("Visitor Management", Label.CENTER);
        visitorLabel.setFont(font);
        this.add(visitorLabel, BorderLayout.NORTH);

        // Form Panel
        Panel formPanel = new Panel(new GridLayout(4, 2, 10, 10));
        Font font1 = new Font("SansSerif", Font.BOLD, 18);

        studentname = new Label("Student Name:");
        studentname.setFont(font1);
        studentnameTf = new TextField(25);
        studentnameTf.setFont(font1);

        visitorname = new Label("Visitor Name:");
        visitorname.setFont(font1);
        visitorTf = new TextField(25);
        visitorTf.setFont(font1);

        studentId = new Label("Student ID:");
        studentId.setFont(font1);
        studentIdTf = new TextField(25);
        studentIdTf.setFont(font1);

        relation = new Label("Relation:");
        relation.setFont(font1);
        relationTf = new TextField(25);
        relationTf.setFont(font1);

        formPanel.add(studentname);
        formPanel.add(studentnameTf);
        formPanel.add(visitorname);
        formPanel.add(visitorTf);
        formPanel.add(studentId);
        formPanel.add(studentIdTf);
        formPanel.add(relation);
        formPanel.add(relationTf);
        this.add(formPanel, BorderLayout.CENTER);

        // Submit Button
        submitButton = new Button("Submit Visitor Entry");
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        this.add(submitButton, BorderLayout.SOUTH);

        // Action Listener
        submitButton.addActionListener(e -> {
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String studentName = studentnameTf.getText().trim();
            String visitorTfName = visitorTf.getText().trim();
            String studentIdStr = studentIdTf.getText().trim();
            String relationStr = relationTf.getText().trim();

            // Validation
            if (studentName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Student Name");
                return;
            } else if (visitorTfName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Visitor Name");
                return;
            } else if (studentIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Student ID");
                return;
            } else if (relationStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Relation with Student");
                return;
            }

            // Parse Student ID
            int studID;
            try {
                studID = Integer.parseInt(studentIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Student ID must be a number");
                return;
            }

            // Database Operations
            try (Connection conn = DatabaseManager.connect()) {
                // Check if student exists
                String query = "SELECT name, ID FROM Student WHERE name = ? AND ID = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, studentName);
                pstmt.setInt(2, studID);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Student not found! Check name and ID.");
                    return;
                }
                int id = rs.getInt("ID");

                // Insert Visitor with StudentId directly
                String query2 = "INSERT INTO Visitor(studentname, visitorname, Relation, StudentId,visit_date) VALUES (?,?,?,?,?)";
                PreparedStatement pstmt2 = conn.prepareStatement(query2);
                pstmt2.setString(1, studentName);
                pstmt2.setString(2, visitorTfName);
                pstmt2.setString(3, relationStr);
                pstmt2.setInt(4, id);
                pstmt2.setString(5, dateTime);
                pstmt2.executeUpdate();

                JOptionPane.showMessageDialog(null, "Visitor Entry Submitted Successfully!");
                submitButton.setLabel("Visitor Entry Submitted");

                // Clear fields after successful submission
                studentnameTf.setText("");
                visitorTf.setText("");
                studentIdTf.setText("");
                relationTf.setText("");
                submitButton.setLabel("Submit Visitor Entry");

            } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            }
        });
    }
}

