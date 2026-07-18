package Project;

public class Session {

    // The single instance
    private static Session instance = null;
    private int studentId;
    private String studentName;
    private String fatherName;
    private String role;       // "STUDENT" or "MANAGER"
    private int roomNumber;    // 0 if not assigned


    private Session() {}

        public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Call this right after a successful student login
    public void loginAsStudent(int id, String name, String fatherName, int roomNumber) {
        this.studentId   = id;
        this.studentName = name;
        this.fatherName  = fatherName;
        this.roomNumber  = roomNumber;
        this.role        = "STUDENT";
    }

    // Call this right after a successful manager login
    public void loginAsManager() {
        this.studentId   = -1;
        this.studentName = "Manager";
        this.fatherName  = "";
        this.roomNumber  = 0;
        this.role        = "MANAGER";
    }

    // Call this on logout — wipes all data
    public void logout() {
        instance = null;
    }

    // Getters
    public int    getStudentId()   { return studentId; }
    public String getStudentName() { return studentName; }
    public String getFatherName()  { return fatherName; }
    public String getRole()        { return role; }
    public int    getRoomNumber()  { return roomNumber; }

    public boolean isLoggedIn()    { return instance != null && role != null; }
    public boolean isManager()     { return "MANAGER".equals(role); }
    public boolean isStudent()     { return "STUDENT".equals(role); }
}
