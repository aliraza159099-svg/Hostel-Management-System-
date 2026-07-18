package Project;
import Project.*;
import java.sql.SQLException;

public class Main {

        public static void main(String[] args) throws SQLException {
            DatabaseManager.initializeTables();
            new First();
        }
    }
