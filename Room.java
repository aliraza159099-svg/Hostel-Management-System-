package Project;

import java.sql.*;
import java.util.ArrayList;

public class Room {

    private ArrayList<Integer> roomNumber = new ArrayList<>();
    private ArrayList<Integer> roomCapacity = new ArrayList<>();

    private static String hostelName = "A-Hostel";
    private static Room instance = null;
    private static int issuedRooms = 0;
    private static int availableRooms = 0;
    private static int totalRooms = 0;

    private static ArrayList<Boolean> status = new ArrayList<>();
    private static ArrayList<Integer> allotedRoom = new ArrayList<>();

    private int issuedRoomNumber;
    int getIssuedRoomNumber() {
        return issuedRoomNumber;
    }

    public Room() {

    }
    public static Room getInstance() {
        if (instance == null) {
            instance = new Room();

        }
        return instance;
    }
public int getTotalRooms() {
        return totalRooms;
}
    public void addRoom(int roomnumber, int roomcapacity) {

        if (roomnumber > 0 && roomcapacity > 0) {

            roomNumber.add(roomnumber);
            roomCapacity.add(roomcapacity);

            allotedRoom.add(0);

            totalRooms++;
            availableRooms++;

            status.add(false);
            String insertSQL = """
                    INSERT INTO Room
                    (HostelName, TotalRooms, IssuedRooms,
                     AllotedRooms, AvailableRooms,
                     Status, RoomNumber, Capacity)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            try (
                    Connection conn = DatabaseManager.connect();
                    Statement stmt = conn.createStatement();
                    PreparedStatement pstmt =
                            conn.prepareStatement(insertSQL)) {



                // Insert data
                pstmt.setString(1, hostelName);
                pstmt.setInt(2, totalRooms);
                pstmt.setInt(3, issuedRooms);
                pstmt.setInt(4, 0);
                pstmt.setInt(5, availableRooms);
                pstmt.setBoolean(6, false);
                pstmt.setInt(7, roomnumber);
                pstmt.setInt(8, roomcapacity);

                pstmt.executeUpdate();

                System.out.println("Room added successfully");

            } catch (SQLException e) {
                System.out.println("Database Error: "
                        + e.getMessage());
            }

        } else {
            System.out.println(
                    "Room number or capacity is invalid");
        }
    }

    public void allocateRoom() {
        loadRoomsFromDB();
        for (int i = 0; i < totalRooms; i++) {

            if (!status.get(i)) {
                issuedRoomNumber = roomNumber.get(i);
                updateAllocateRoom(i);
                updateStatus(i);
                issuedRooms++;
                availableRooms--;
               // System.out.println("Room allocated successfully");
                return;
            }
        }

        System.out.println("Rooms not available");
    }

    private void updateAllocateRoom(int index) {

        int temp = allotedRoom.get(index);

        temp++;

        allotedRoom.set(index, temp);
    }

    private void updateStatus(int index) {

        if (allotedRoom.get(index).equals(roomCapacity.get(index))) {

            status.set(index, true);
        }
    }
    public void deallocateRoom(int roomnumber) {
        for(int i = 0; i < totalRooms; i++) {
            if (roomNumber.get(i) == roomnumber) {
                issuedRooms--;
                availableRooms++;
                status.set(i,false);
            }
        }

    }
    private void loadRoomsFromDB() {
        // clear everything first to avoid duplicates
        roomNumber.clear();
        roomCapacity.clear();
        allotedRoom.clear();
        status.clear();
        totalRooms = 0;

        try (Connection conn = DatabaseManager.connect()) {
            String query = "SELECT RoomNumber, Capacity, AllotedRooms FROM Room";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int rNum     = rs.getInt("RoomNumber");
                int capacity = rs.getInt("Capacity");
                int alloted  = rs.getInt("AllotedRooms");

                roomNumber.add(rNum);
                roomCapacity.add(capacity);
                allotedRoom.add(alloted);

                // if alloted == capacity then room is full
                status.add(alloted >= capacity);
                totalRooms++;
            }

        } catch (SQLException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Room r = new Room();
        r.addRoom(101, 3);
        r.addRoom(102, 2);
        r.allocateRoom();
    }

}
