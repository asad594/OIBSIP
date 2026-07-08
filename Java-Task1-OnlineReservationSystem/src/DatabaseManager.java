import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    // Uses Windows Authentication instead of a password
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=TrainDB;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found.");
        }
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            
            // Create users table
            String createUsers = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' and xtype='U') " +
                                 "CREATE TABLE users (" +
                                 "id INT IDENTITY(1,1) PRIMARY KEY, " +
                                 "username VARCHAR(50) NOT NULL UNIQUE, " +
                                 "password VARCHAR(50) NOT NULL)";
            stmt.executeUpdate(createUsers);

            // Create trains table
            String createTrains = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='trains' and xtype='U') " +
                                  "CREATE TABLE trains (" +
                                  "train_number INT PRIMARY KEY, " +
                                  "train_name VARCHAR(100) NOT NULL)";
            stmt.executeUpdate(createTrains);

            // Create reservations table
            String createReservations = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='reservations' and xtype='U') " +
                                        "CREATE TABLE reservations (" +
                                        "pnr VARCHAR(20) PRIMARY KEY, " +
                                        "passenger_name VARCHAR(100) NOT NULL, " +
                                        "train_number INT NOT NULL, " +
                                        "class_type VARCHAR(20) NOT NULL, " +
                                        "date_of_journey DATE NOT NULL, " +
                                        "source_station VARCHAR(50) NOT NULL, " +
                                        "destination_station VARCHAR(50) NOT NULL, " +
                                        "FOREIGN KEY (train_number) REFERENCES trains(train_number))";
            stmt.executeUpdate(createReservations);

            // Insert default admin user if not exists
            String checkAdmin = "SELECT COUNT(*) FROM users WHERE username='admin'";
            try (ResultSet rs = stmt.executeQuery(checkAdmin)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO users (username, password) VALUES ('admin', 'admin')");
                }
            }

            // Insert dummy trains if not exists
            String checkTrains = "SELECT COUNT(*) FROM trains";
            try (ResultSet rs = stmt.executeQuery(checkTrains)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO trains (train_number, train_name) VALUES (12001, 'Shatabdi Express')");
                    stmt.executeUpdate("INSERT INTO trains (train_number, train_name) VALUES (12951, 'Rajdhani Express')");
                    stmt.executeUpdate("INSERT INTO trains (train_number, train_name) VALUES (12627, 'Karnataka Express')");
                    stmt.executeUpdate("INSERT INTO trains (train_number, train_name) VALUES (12801, 'Purushottam Express')");
                }
            }
            
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            System.err.println("Please ensure the 'TrainDB' database is created in SQL Server.");
        }
    }
}
