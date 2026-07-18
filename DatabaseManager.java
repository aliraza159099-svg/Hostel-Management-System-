package Project;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_PATH =
            "A:/Java/Practice/database/HostelManagement.db";

    public static Connection connect() throws SQLException {
        File dbFile = new File(DB_PATH);
        File dbDir = dbFile.getParentFile();
        if (dbDir != null && !dbDir.exists()) {
            dbDir.mkdirs();
        }
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }

    public static void initializeTables() {
        String createRoom = """
                CREATE TABLE IF NOT EXISTS Room (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    HostelName TEXT,
                    TotalRooms INTEGER,
                    IssuedRooms INTEGER DEFAULT 0,
                    AllotedRooms INTEGER DEFAULT 0,
                    AvailableRooms INTEGER DEFAULT 0,
                    Status BOOLEAN,
                    RoomNumber INTEGER NOT NULL DEFAULT 0 UNIQUE,
                    Capacity INTEGER NOT NULL,
                    StudentId INTEGER,
                    FOREIGN KEY(StudentId) REFERENCES Student(ID)
                )
                """;
        String createStudent = """
                CREATE TABLE IF NOT EXISTS Student (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    fathername TEXT NOT NULL,
                    password TEXT NOT NULL
                )
                """;
        String createComplaint = """
                CREATE TABLE IF NOT EXISTS Complaint (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    complaint TEXT NOT NULL,
                    name TEXT NOT NULL,
                    fathername TEXT NOT NULL,
                    roomnumber INTEGER NOT NULL,
                    Status TEXT NOT NULL
                )
                """;
        String createMenu = """
                CREATE TABLE IF NOT EXISTS MessMenu (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Day TEXT NOT NULL UNIQUE,
                    Menu TEXT NOT NULL
                )
                """;
        String createRequest = """
                CREATE TABLE IF NOT EXISTS Request (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    fathername TEXT NOT NULL,
                    StudentId INTEGER NOT NULL,
                    FOREIGN KEY(StudentId) REFERENCES Student(ID)
                )
                """;
        String createVisitor = """
                CREATE TABLE IF NOT EXISTS Visitor (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    studentname TEXT NOT NULL,
                    visitorname TEXT NOT NULL,
                    Relation TEXT NOT NULL,
                    StudentId INTEGER NOT NULL,
                    visit_date TEXT DEFAULT (datetime('now', 'localtime')),
                    FOREIGN KEY(StudentId) REFERENCES Student(ID)
                )
                """;
        String createManager = """
                CREATE TABLE IF NOT EXISTS Manager (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    fathername TEXT NOT NULL,
                    cnic TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                )
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createStudent); // Student first — Room has FK to it
            stmt.execute(createRoom);
            stmt.execute(createComplaint);
            stmt.execute(createMenu);
            stmt.execute(createRequest);
            stmt.execute(createVisitor);
            stmt.execute(createManager);
        } catch (SQLException e) {
            System.out.println("Init error: " + e.getMessage());
        }
    }
}
