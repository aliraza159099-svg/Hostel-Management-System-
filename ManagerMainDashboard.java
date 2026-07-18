package Project;//package Project;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
//
//
import java.sql.*;


public class ManagerMainDashboard extends Frame implements ActionListener {
    Button btn1,btn3, btn4, btn5, btn6, btn7;
    Panel sidebar, contentPanel;
    Label l1;
    CardLayout cardLayout;
    boolean isManagerLoggedIn;

    public ManagerMainDashboard(boolean loginStatus) throws SQLException {
        this.isManagerLoggedIn = loginStatus;
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setSize(500, 500);
        this.setBackground(Color.BLUE);
        this.setTitle("Manager Main Dashboard");
        this.setLayout(new BorderLayout());
        // sidebar
        sidebar = new Panel();
        sidebar.setLayout(new GridLayout(1, 6, 10, 10));
        Font font = new Font("Sansarif", Font.ROMAN_BASELINE, 16);
        // Buttons and its design
        btn1 = new Button("Rooms Section");
        btn1.setFont(font);
        btn1.setBackground(Color.YELLOW);
        btn3 = new Button("Remove Students");
        btn3.setFont(font);
        btn3.setBackground(Color.YELLOW);
        btn4 = new Button("Display Students");
        btn4.setFont(font);
        btn4.setBackground(Color.YELLOW);
        btn5 = new Button("view Complaints");
        btn5.setFont(font);
        btn5.setBackground(Color.YELLOW);
        btn6 = new Button("View Visitors");
        btn6.setFont(font);
        btn6.setBackground(Color.YELLOW);
        btn7 = new Button("Mess Menu");
        btn7.setFont(font);
        btn7.setBackground(Color.YELLOW);

        sidebar.add(btn1);
        sidebar.add(btn3);
        sidebar.add(btn4);
        sidebar.add(btn5);
        sidebar.add(btn6);
        sidebar.add(btn7);

        // registering the components
        btn1.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
        btn5.addActionListener(this);
        btn6.addActionListener(this);
        btn7.addActionListener(this);

        // adding sidebar to the Frame
        this.add(sidebar, BorderLayout.NORTH);

        // Cardlayout for each buttons
        cardLayout = new CardLayout();

        Panel homePanel;
        Panel viewRequestPanel;
        Panel removeStudentPanel;
        Panel displayStudentPanel;
        Panel paymentPanel;
        Panel visitorPanel;
        Panel messMenuPanel;
        Panel complaintPanel;

        // content panel details
        Font font1 = new Font("SansSerif", Font.BOLD, 40);
        contentPanel = new Panel(cardLayout);

        // HOME PANEL
        homePanel = new Panel();
        l1 = new Label("Welcome to the Hostel Management System of PIEAS");
        l1.setFont(font1);
        homePanel.add(l1);

        // ADDING ALL PANELS TO CARDLAYOUT
        contentPanel.add(homePanel, "HOME");
        // 1
        RoomSectionPanel roomSectionPanel = new RoomSectionPanel();
        contentPanel.add(roomSectionPanel, "Room Section");
        // 2
        viewRequestPanel = new ViewRequestPanel();
        contentPanel.add(viewRequestPanel, "REQUEST");
        // 3
        removeStudentPanel = new RemoveStudentPanel();
        contentPanel.add(removeStudentPanel, "REMOVE");
        // 4
        displayStudentPanel = new DisplayStudentPanel();
        contentPanel.add(displayStudentPanel, "DISPLAY");
        // 5
        visitorPanel = new VisitorPanel();
        contentPanel.add(visitorPanel, "VISITOR");
        // 6 complaint panel
        complaintPanel = new ComplaintPanelDisplay();
        contentPanel.add(complaintPanel, "COMPLAINT");

        // 7 mess menu panel
        MessMenuPanel menu = new MessMenuPanel(true);
        contentPanel.add(menu, "MESS");

        // ADD CONTENT PANEL TO FRAME
        this.add(contentPanel, BorderLayout.CENTER);
        // Window close handler
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn1) {
            cardLayout.show(contentPanel, "Room Section");
        }
        else if (e.getSource() == btn3) {
            cardLayout.show(contentPanel, "REMOVE");
        }

        else if (e.getSource() == btn4) {
            cardLayout.show(contentPanel, "DISPLAY");
        }

        else if (e.getSource() == btn5) {
            cardLayout.show(contentPanel, "COMPLAINT");
        }

        else if (e.getSource() == btn6) {
            cardLayout.show(contentPanel, "VISITOR");
        }

        else if (e.getSource() == btn7) {
            cardLayout.show(contentPanel, "MESS");
        }
    }

    public static void main(String[] args) throws SQLException {
        DatabaseManager.initializeTables();
        Session.getInstance().loginAsManager();
        ManagerMainDashboard main = new ManagerMainDashboard(true);
    }
}

// All about Room
class RoomSectionPanel extends Panel implements ActionListener {
    CardLayout roomCard;
    Panel roomContent;
    Button addRoomBtn, requestBtn, detailsBtn;

    public RoomSectionPanel() throws SQLException {
        setLayout(new BorderLayout());

        // Top bar with 3 buttons
        Panel tabBar = new Panel(new GridLayout(1, 3, 10, 10));
        Font font = new Font("SanSerif", Font.BOLD, 18);
        font.getStyle();
        addRoomBtn = new Button("Add Room");
        addRoomBtn.setBackground(Color.CYAN);
        addRoomBtn.setFont(font);
        requestBtn = new Button("Room Requests");
        requestBtn.setBackground(Color.CYAN);
        requestBtn.setFont(font);
        detailsBtn = new Button("View Room Details");
        detailsBtn.setBackground(Color.CYAN);
        detailsBtn.setFont(font);

        tabBar.add(addRoomBtn);
        tabBar.add(requestBtn);
        tabBar.add(detailsBtn);

        add(tabBar, BorderLayout.NORTH);

        // CardLayout for sub-panels
        roomCard = new CardLayout();
        roomContent = new Panel(roomCard);
        // Default Panel when Room section button is clicked to show.
        Panel defaultPanel = new Panel();
        defaultPanel.add(new Label("Select an option above"));

        roomContent.add(defaultPanel, "DEFAULT");
        roomContent.add(new AddRoomPanel(), "ADD");
        roomContent.add(new ViewRequestPanel(), "REQUEST");
        roomContent.add(new RoomDetailsPanel(), "DETAILS");

        add(roomContent, BorderLayout.CENTER);
        // Show default first
        roomCard.show(roomContent, "DEFAULT");
        // Register listeners
        addRoomBtn.addActionListener(this);
        requestBtn.addActionListener(this);
        detailsBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addRoomBtn) {
            roomCard.show(roomContent, "ADD");
        } else if (e.getSource() == requestBtn) {
            roomCard.show(roomContent, "REQUEST");
        } else if (e.getSource() == detailsBtn) {
            roomCard.show(roomContent, "DETAILS");
        }
    }
}

// addroompanel class - MERGED: UI from first version + DB logic from second
class AddRoomPanel extends Panel implements ActionListener {

    TextField roomNo, capacity;
    Button addBtn;
    Label l1;

    AddRoomPanel() {

        setLayout(new FlowLayout());
        l1 = new Label("");
        Font labelFont = new Font("SansSerif", Font.BOLD, 24);
        Font textFont = new Font("SansSerif", Font.PLAIN, 22);
        Label roomLabel = new Label("Room Number");
        roomLabel.setFont(labelFont);
        roomNo = new TextField(10);
        roomNo.setFont(textFont);
        Label capacityLabel = new Label("Capacity");
        capacityLabel.setFont(labelFont);
        capacity = new TextField(10);
        capacity.setFont(textFont);
        addBtn = new Button("Add Room");
        addBtn.setBackground(Color.GREEN);
        addBtn.setFont(labelFont);
        add(roomLabel);
        add(roomNo);
        add(capacityLabel);
        add(capacity);
        add(addBtn);
        add(l1);
        addBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String roomText = roomNo.getText();
            String capText = capacity.getText();
            try {
                int roomNumber = Integer.parseInt(roomText.trim());
                int capacityValue = Integer.parseInt(capText.trim());

                // Validation
                if (roomNumber <= 0 || capacityValue <= 0) {
                    l1.setText("Please enter valid positive numbers");
                    l1.setForeground(Color.RED);
                    return;
                }
                else if(capacityValue > 5){
                    l1.setText("Please Enter Capacity 1-5");
                    l1.setForeground(Color.RED);
                    return;
                }

                // Call database method
                Room.getInstance().addRoom(roomNumber, capacityValue);

                // Success feedback
                l1.setText("Room added successfully");
                l1.setForeground(Color.GREEN);
                roomNo.setText("");
                capacity.setText("");
            }
            catch (NumberFormatException ex) {
                l1.setText("Please enter valid integers only");
                l1.setForeground(Color.RED);
                System.out.println("Input Error: " + ex.getMessage());
            }
            }
  }
}

// Room Details Panel - Kept full UI implementation from first version
class RoomDetailsPanel extends Panel {
    Label RoomNo, RoomCapacity, NumofStudents, Space;
    Panel dataContainer;  // make it a field

    public RoomDetailsPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(250, 250, 255));

        Panel header = new Panel();
        header.setLayout(new GridLayout(1, 4, 15, 0));
        header.setBackground(new Color(70, 130, 180));

        Font headerFont = new Font("SansSerif", Font.BOLD, 18);
        Color textColor = Color.WHITE;

        RoomNo = new Label("Room Number", Label.CENTER);
        RoomNo.setFont(headerFont); RoomNo.setForeground(textColor);
        header.add(RoomNo);

        RoomCapacity = new Label("Room Capacity", Label.CENTER);
        RoomCapacity.setFont(headerFont); RoomCapacity.setForeground(textColor);
        header.add(RoomCapacity);

        NumofStudents = new Label("Number of Students", Label.CENTER);
        NumofStudents.setFont(headerFont); NumofStudents.setForeground(textColor);
        header.add(NumofStudents);

        Space = new Label("Empty Space", Label.CENTER);
        Space.setFont(headerFont); Space.setForeground(textColor);
        header.add(Space);

        this.add(header, BorderLayout.NORTH);

        dataContainer = new Panel();
        dataContainer.setLayout(new BoxLayout(dataContainer, BoxLayout.Y_AXIS));

        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(dataContainer);
        this.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button
        Button refreshBtn = new Button("Refresh");
        refreshBtn.addActionListener(ev -> loadRooms());
        this.add(refreshBtn, BorderLayout.SOUTH);

        loadRooms(); // load on start
    }

    // Moved DB logic here so it can be called anytime
    public void loadRooms() {
        dataContainer.removeAll();

        try (Connection conn = DatabaseManager.connect()) {
            // Count students per room dynamically from Room table
            String query = "SELECT RoomNumber, Capacity, " +
                    "COUNT(StudentId) AS NumStudents " +
                    "FROM Room GROUP BY RoomNumber, Capacity";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int index = 0;
            while (rs.next()) {
                int roomNumber   = rs.getInt("RoomNumber");
                int capacity     = rs.getInt("Capacity");
                int numStudents  = rs.getInt("NumStudents");
                int emptySpace   = capacity - numStudents;

                dataContainer.add(createRoomRow(
                        " " + roomNumber,
                        " " + capacity,
                        " " + numStudents,
                        " " + emptySpace,
                        index
                ));
                index++;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        dataContainer.revalidate();
        dataContainer.repaint();
    }

    private Panel createRoomRow(String RoomNo, String Capacity,
                                String NoStudents, String EmptySpace, int rowIndex) {
        Panel row = new Panel(new GridLayout(1, 4));
        Font font = new Font("SansSerif", Font.CENTER_BASELINE, 14);

        Label room     = new Label(RoomNo,     Label.CENTER); room.setFont(font);
        Label capacity = new Label(Capacity,   Label.CENTER); capacity.setFont(font);
        Label students = new Label(NoStudents, Label.CENTER); students.setFont(font);
        Label space    = new Label(EmptySpace, Label.CENTER); space.setFont(font);

        row.add(room); row.add(capacity);
        row.add(students); row.add(space);

        row.setBackground(rowIndex % 2 == 0 ? Color.GREEN : new Color(245, 245, 245));
        return row;
    }
}

// 3.remove student panel class - Kept full implementation from first version
class RemoveStudentPanel extends Panel implements ActionListener {
    Label l1, l2, l3, l4, message;
    TextField tf1, tf2, tf3, tf4;
    Button btn1;

    RemoveStudentPanel() {
        setLayout(new GridLayout(5, 2));

        Font labelFont = new Font("SansSerif", Font.BOLD, 24);
        Font textFont = new Font("SansSerif", Font.PLAIN, 22);

        l1 = new Label("Student Name");
        l1.setFont(labelFont);

        tf1 = new TextField(20);
        tf1.setFont(textFont);

        l2 = new Label("Father Name");
        l2.setFont(labelFont);

        tf2 = new TextField(20);
        tf2.setFont(textFont);

        btn1 = new Button("Remove Student");
        btn1.setFont(labelFont);
        btn1.setBackground(Color.GREEN);
        btn1.setForeground(Color.BLACK);
        btn1.addActionListener(this);

        message = new Label(" ");
        message.setForeground(Color.RED);
        message.setFont(labelFont);

        add(l1);
        add(tf1);
        add(l2);
        add(tf2);
        add(btn1);
        add(message);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            try(Connection conn = DatabaseManager.connect()){
                String query = "SELECT * FROM Student WHERE name = ? AND fathername = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, tf1.getText());
                pstmt.setString(2, tf2.getText());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String queryname = "DELETE FROM Student WHERE name = ? AND fathername = ?";
                    PreparedStatement pstmt1 = conn.prepareStatement(queryname);
                    pstmt1.setString(1, tf1.getText());
                    pstmt1.setString(2, tf2.getText());
                    pstmt1.executeUpdate();
                    int roomnumber = Session.getInstance().getRoomNumber();
                    Room.getInstance().deallocateRoom(roomnumber);
                    message.setText("Student and His Data has been removed");
                }else {
                    message.setText("            This Student is not registered");
                }
            }catch(SQLException ex){
                   System.out.println("Error: " + ex.getMessage());
            }
        }

        }
    }


// 4.display students panel - Kept full UI from first version
class DisplayStudentPanel extends Panel {
    Label id, name, father, fee;

    public DisplayStudentPanel() {
        this.setLayout(new BorderLayout());

        // header panel
        Panel headerPanel = new Panel();
        headerPanel.setLayout(new GridLayout(1, 4));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        Font font = new Font("Sansarif", Font.ROMAN_BASELINE, 15);
        id = new Label("id", Label.CENTER);
        id.setFont(font);
        headerPanel.add(id);
        name = new Label("Name", Label.CENTER);
        name.setFont(font);
        headerPanel.add(name);
        father = new Label("Father", Label.CENTER);
        father.setFont(font);
        headerPanel.add(father);
        fee = new Label("Fee", Label.CENTER);
        fee.setFont(font);
        headerPanel.add(fee);
        this.add(headerPanel, BorderLayout.NORTH);

        // data panel , here all the data of the students will appear
        Panel dataContainer = new Panel();
        dataContainer.setLayout(new BoxLayout(dataContainer, BoxLayout.Y_AXIS));
        // ScrolPane for more data
        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(dataContainer);
        this.add(scrollPane, BorderLayout.CENTER);
        try(Connection conn = DatabaseManager.connect()){
            String query = "SELECT * FROM STUDENT";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                dataContainer.add(createStudentRow(" " + rs.getInt("ID"), " " + rs.getString("name"), " " + rs.getString("fathername"), "1223", rs.getInt("ID") ));
            }
        }catch(SQLException e){
            System.out.println("Error: " + e);
        }


    }

    // Helper method: Creates ONE row panel for a student
    private Panel createStudentRow(String id, String name, String father, String fee, int rowIndex) {

        // Create a panel for this row
        Panel row = new Panel();

        // Use GridLayout(1, 4) to match the header's 4 columns
        row.setLayout(new GridLayout(1, 4));

        // Create labels for each piece of data (centered)
        Label lblId = new Label(id, Label.CENTER);
        Label lblName = new Label(name, Label.CENTER);
        Label lblFather = new Label(father, Label.CENTER);
        Label lblFee = new Label(fee, Label.CENTER);

        // Set the same font as header for consistency
        Font font = new Font("SansSerif", Font.CENTER_BASELINE, 14);
        lblId.setFont(font);
        lblName.setFont(font);
        lblFather.setFont(font);
        lblFee.setFont(font);

        // Add labels to the row
        row.add(lblId);
        row.add(lblName);
        row.add(lblFather);
        row.add(lblFee);
        // for good visuality
        if (rowIndex % 2 == 0) {
            row.setBackground(Color.GREEN);
        } else {
            row.setBackground(new Color(245, 245, 245)); // Very light gray
        }
        return row;
    }
}

// 6 Visitor Panel - Kept from first version (empty but structured)
class VisitorPanel extends Panel {
    Label SudentName, StudentID, VisitorName,Relation, Date;

    VisitorPanel() {
        this.setLayout(new BorderLayout());
        Panel header = new Panel(new GridLayout(1, 5, 10, 0));
        header.setBackground(new Color(120, 200, 160)); // Medium sea green
        Font headerFont = new Font("SansSerif", Font.BOLD, 16);
        header.setFont(headerFont);
        SudentName = new Label("Student Name", Label.CENTER);
        SudentName.setFont(headerFont);
        SudentName.setForeground(Color.WHITE);
        header.add(SudentName);
        StudentID = new Label("Student ID", Label.CENTER);
        StudentID.setFont(headerFont);
        StudentID.setForeground(Color.WHITE);
        header.add(StudentID);
        VisitorName = new Label("Visitor Name", Label.CENTER);
        VisitorName.setFont(headerFont);
        VisitorName.setForeground(Color.WHITE);
        header.add(VisitorName);
        Relation = new Label("Relation", Label.CENTER);
        Relation.setFont(headerFont);
        Relation.setForeground(Color.WHITE);
        header.add(Relation);
        Date = new Label("Date", Label.CENTER);
        Date.setFont(headerFont);
        Date.setForeground(Color.WHITE);
        header.add(Date);
        this.add(header, BorderLayout.NORTH);

        Panel dataContainer = new Panel();
        dataContainer.setLayout(new BoxLayout(dataContainer, BoxLayout.Y_AXIS));
        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(dataContainer);
        this.add(scrollPane, BorderLayout.CENTER);
        try(Connection conn = DatabaseManager.connect()){
            String query = "SELECT * FROM Visitor";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeQuery();
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                dataContainer.add(createVisitorRow(" " + rs.getString("studentname"), " " + rs.getInt("StudentId"), " " + rs.getString("visitorname"), " " + rs.getString("Relation"), " " + rs.getDate("visit_date"), rs.getInt("ID")));
            }
        }catch(SQLException e){
            System.out.println("Error: " + e);

        }


    }

    // method to create rows for visitor details
    private Panel createVisitorRow(String studentName, String studentID, String visitorName, String relationship,
                                   String date, int rowIndex) {
        Panel row = new Panel(new GridLayout(1, 4));
        Label lblStudentName = new Label(studentName, Label.CENTER);
        Label lblStudentID = new Label(studentID, Label.CENTER);
        Label lblVisitorName = new Label(visitorName, Label.CENTER);
        Label lblRelationship = new Label(relationship, Label.CENTER);
        Label lblDate = new Label(date, Label.CENTER);
        Font font = new Font("SansSerif", Font.CENTER_BASELINE, 14);
        lblStudentName.setFont(font);
        lblStudentID.setFont(font);
        lblVisitorName.setFont(font);
        lblRelationship.setFont(font);
        lblDate.setFont(font);
        row.add(lblStudentName);
        row.add(lblStudentID);
        row.add(lblVisitorName);
        row.add(lblRelationship);
        row.add(lblDate);
        if (rowIndex % 2 == 0) {
            row.setBackground(Color.GREEN);
        } else {
            row.setBackground(new Color(245, 245, 245)); // Very light gray
        }
        return row;
    }
}



// 7. Complaint Panel - Kept from first version (empty but structured)
class ComplaintPanelDisplay extends Panel {
    Panel dataContainer;

    public ComplaintPanelDisplay() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 247, 250));

        // ── Header ──────────────────────────────────────────
        Panel header = new Panel(new GridLayout(1, 5, 0, 0));
        header.setBackground(new Color(44, 62, 80));

        Font headerFont = new Font("SansSerif", Font.BOLD, 16);
        String[] cols = {"Student Name", "Father Name", "Room No", "Complaint", "Status"};

        for (String col : cols) {
            Label lbl = new Label(col, Label.CENTER);
            lbl.setFont(headerFont);
            lbl.setForeground(Color.WHITE);
            header.add(lbl);
        }
        this.add(header, BorderLayout.NORTH);

        // ── Scrollable data area ─────────────────────────────
        dataContainer = new Panel();
        dataContainer.setLayout(new BoxLayout(dataContainer, BoxLayout.Y_AXIS));

        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(dataContainer);
        this.add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DatabaseManager.connect()) {
            String query = "SELECT * FROM Complaint";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            int index = 0;
            while (rs.next()) {
                dataContainer.add(createComplaintRow(" " + rs.getString("name"), " " + rs.getString("fathername"), rs.getInt("roomnumber"), " " + rs.getString("complaint"),index, rs.getInt("ID")," " + rs.getString("Status")));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }
    private Panel createComplaintRow(String studentName, String fatherName,
                                     int roomNumber, String complaint,
                                     int rowIndex, int complaintId, String status) {

        Panel row = new Panel(new GridLayout(1, 5, 8, 0));
        row.setPreferredSize(new Dimension(0, 55));
        row.setBackground(rowIndex % 2 == 0 ? Color.WHITE : new Color(235, 240, 245));

        Font dataFont = new Font("SansSerif", Font.PLAIN, 14);

        Label lblName      = new Label(studentName,          Label.CENTER);
        Label lblFather    = new Label(fatherName,           Label.CENTER);
        Label lblRoom      = new Label("Room " + roomNumber, Label.CENTER);
        Label lblComplaint = new Label(complaint,            Label.CENTER);

        lblName.setFont(dataFont);
        lblFather.setFont(dataFont);
        lblRoom.setFont(dataFont);
        lblComplaint.setFont(dataFont);

        Button statusBtn = new Button(status); // load actual status from DB
        statusBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        applyStatusStyle(statusBtn, status);   // set correct color on load

        statusBtn.addActionListener(ev -> {
            String newStatus = statusBtn.getLabel().equals("Pending") ? "Resolved" : "Pending";
            try (Connection conn = DatabaseManager.connect()) {
                String update = "UPDATE Complaint SET Status = ? WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(update);
                ps.setString(1, newStatus);
                ps.setInt(2, complaintId);     //  update correct row
                ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            statusBtn.setLabel(newStatus);
            applyStatusStyle(statusBtn, newStatus);
        });

        row.add(lblName);
        row.add(lblFather);
        row.add(lblRoom);
        row.add(lblComplaint);
        row.add(statusBtn);
        return row;
    }


    private void applyStatusStyle(Button btn, String status) {
        if (status.equals("Pending")) {
            btn.setBackground(new Color(220, 80, 60));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(60, 180, 100));
            btn.setForeground(Color.WHITE);
        }
    }

}
