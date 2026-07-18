package Project;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
class ViewRequestPanel extends Panel  {
    Label id, name, father, action;
    Panel dataContainer;
    ScrollPane scrollPane;

    public ViewRequestPanel() {
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
        action = new Label("Action", Label.CENTER);
        action.setFont(font);
        headerPanel.add(action);

        dataContainer = new Panel();
        dataContainer.setLayout(new BoxLayout(dataContainer, BoxLayout.Y_AXIS));
        scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(dataContainer);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(headerPanel, BorderLayout.NORTH);
        Button refreshBtn = new Button("Refresh");
        refreshBtn.addActionListener(ev -> loadRequests()); // reload on click
        this.add(refreshBtn, BorderLayout.SOUTH);
        loadRequests();

    }

    // Helper method: Creates ONE row panel for a student
    private Panel createRequestRow(String id, String name,
                                   String father, int studentId,
                                   int rowIndex, boolean alreadyAssigned) {

        Panel row = new Panel(new GridLayout(1, 4));
        row.setBackground(rowIndex % 2 == 0 ? Color.GREEN : new Color(245, 245, 245));

        Font font = new Font("SansSerif", Font.CENTER_BASELINE, 14);

        Label lblId     = new Label(id,     Label.CENTER); lblId.setFont(font);
        Label lblName   = new Label(name,   Label.CENTER); lblName.setFont(font);
        Label lblFather = new Label(father, Label.CENTER); lblFather.setFont(font);

        Button actionButton = new Button("Add to Hostel");
        actionButton.setFont(font);

        // If already assigned, disable immediately on load
        if (alreadyAssigned) {
            actionButton.setLabel("Added");
            actionButton.setBackground(Color.BLUE);
            actionButton.setEnabled(false);
        } else {
            actionButton.addActionListener(ev -> {
                Room.getInstance().allocateRoom();
                int allocatedRoom = Room.getInstance().getIssuedRoomNumber();

                if (allocatedRoom == 0) {
                    JOptionPane.showMessageDialog(null, "No rooms available");
                    return;
                }

                try (Connection conn = DatabaseManager.connect()) {
                    String query = "UPDATE Room SET StudentId = ? WHERE RoomNumber = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, studentId);
                    stmt.setInt(2, allocatedRoom);
                    stmt.executeUpdate();

                    String deleteQuery = "DELETE FROM Request WHERE StudentId = ?";
                    PreparedStatement ps = conn.prepareStatement(deleteQuery);
                    ps.setInt(1, studentId);
                    ps.executeUpdate();

                    Session.getInstance().loginAsStudent(
                            Session.getInstance().getStudentId(),
                            Session.getInstance().getStudentName(),
                            Session.getInstance().getFatherName(),
                            allocatedRoom
                    );

                    // Disable button after successful allocation
                    actionButton.setLabel("Added");
                    actionButton.setBackground(Color.BLUE);
                    actionButton.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Room " + allocatedRoom + " assigned successfully");

                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            });
        }

        row.add(lblId);
        row.add(lblName);
        row.add(lblFather);
        row.add(actionButton);
        return row;
    }

    private void loadRequests() {
        dataContainer.removeAll();

        try (Connection conn = DatabaseManager.connect()) {
            String query = "SELECT * FROM Request";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            int index = 0;
            while (rs.next()) {
                int studentId = rs.getInt("StudentId");

                // Check if this student already has a room assigned
                boolean alreadyAssigned = isRoomAlreadyAssigned(conn, studentId);

                dataContainer.add(createRequestRow(
                        " " + rs.getInt("ID"),
                        " " + rs.getString("name"),
                        " " + rs.getString("fathername"),
                        studentId,
                        index,
                        alreadyAssigned  // pass the flag
                ));
                index++;
            }

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        dataContainer.revalidate();
        dataContainer.repaint();
    }

    // Checks if a student already has a room in the Room table
    private boolean isRoomAlreadyAssigned(Connection conn, int studentId) {
        try {
            String query = "SELECT COUNT(*) FROM Room WHERE StudentId = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // true if room found
            }
        } catch (SQLException ex) {
            System.out.println("Error checking room: " + ex.getMessage());
        }
        return false;
    }

}
