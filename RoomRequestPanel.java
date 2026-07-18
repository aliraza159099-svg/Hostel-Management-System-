package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class RoomRequestPanel extends Panel implements ActionListener {
    Label name, father, id;
    TextField nameField, fatherField, idField;
    Button submitButton;

    public RoomRequestPanel() {
        this.setLayout(new BorderLayout());
        Label roomRequestLabel = new Label("You can send room requests here", Label.CENTER);
        Font font = new Font("SansSerif", Font.BOLD, 24);
        roomRequestLabel.setFont(font);
        this.add(roomRequestLabel, BorderLayout.NORTH);

        // Form panel for room request
        Panel formPanel = new Panel(new GridLayout(3, 2, 10, 10));
        name = new Label("Name:");
        name.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameField = new TextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        father = new Label("Father's Name:");
        father.setFont(new Font("SansSerif", Font.BOLD, 18));
        fatherField = new TextField();
        fatherField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        id = new Label("ID:");
        id.setFont(new Font("SansSerif", Font.BOLD, 18));
        idField = new TextField();
        idField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        formPanel.add(name);
        formPanel.add(nameField);
        formPanel.add(father);
        formPanel.add(fatherField);
        formPanel.add(id);
        formPanel.add(idField);
        this.add(formPanel, BorderLayout.CENTER);
        submitButton = new Button("Submit Room Request");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        this.add(submitButton, BorderLayout.SOUTH);
        // Add action listener for submit button (not implemented here)
        submitButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try(Connection conn = DatabaseManager.connect()){
                int Id = Integer.parseInt(idField.getText());
                String Name = nameField.getText();
                String fatherName = fatherField.getText();
                String query = "SELECT name,fathername,ID FROM Student WHERE name = ? AND  fathername = ? AND ID = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, nameField.getText());
                ps.setString(2, fatherField.getText());
                ps.setInt(3, Id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    if (rs.getInt("ID") != Id){
                        JOptionPane.showMessageDialog(null, "Enter Correct ID");
                        return;
                    }
                    else if(!(rs.getString("name").equals(Name))){
                        JOptionPane.showMessageDialog(null, "Enter Correct Name");
                        return;
                    }
                    else if(!(rs.getString("fathername").equals(fatherName))){
                        JOptionPane.showMessageDialog(null, "Enter Correct Fathername");
                        return;
                    }
                    String query3 = "SELECT StudentId FROM Request WHERE ID = ? LIMIT 1";
                    PreparedStatement pstmt1 = conn.prepareStatement(query3);
                    pstmt1.setInt(1,  Id);
                    ResultSet rs3 = pstmt1.executeQuery();
                    if(rs3.next()){
                        JOptionPane.showMessageDialog(null, "You already Requested");
                        return;
                    }
                String query1 = "SELECT RoomNumber FROM Room WHERE StudentId = ? LIMIT 1";
               PreparedStatement pstmt = conn.prepareStatement(query1);
                pstmt.setInt(1, rs.getInt("ID"));
                ResultSet rs2 = pstmt.executeQuery();
                    int room_allocated = 0;
                    if (rs2.next()) {
                        room_allocated = rs2.getInt("RoomNumber");
                    }
                if(room_allocated > 0){
                    JOptionPane.showMessageDialog(null, "You already have Room");
                    return;
                }

                String query2 = "INSERT INTO Request (name, fathername, StudentId) VALUES (?,?,?)";
                   PreparedStatement ps2 = conn.prepareStatement(query2);
                   ps2.setString(1, nameField.getText());
                   ps2.setString(2, fatherField.getText());
                   ps2.setInt(3, Id);
                   ps2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Room Request Submitted Successfully");

                }else{
                    JOptionPane.showMessageDialog(null, "Enter Correct Information");
                    return;
                }
            }catch(SQLException ex){
           System.out.println(ex.getMessage());
            }catch(NumberFormatException ex){
                System.out.println("Error: " + ex.getMessage());
            }


        }
    }
}
