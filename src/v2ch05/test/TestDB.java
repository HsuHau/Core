package v2ch05.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class TestDB {
    public static void main(String[] args) throws IOException {
        try {
            runTest();
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
    }

    public static void runTest() throws SQLException, IOException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE Greetings (Message CHAR(20))");
            statement.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");

            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM　Greetings")) {
                if (resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                }
            }
            statement.executeUpdate("DROP TABLE Greetings");
        }
    }

    /**
     * Gets a connection from the properties specified in the file database.properties
     * @return the database connection
     */
    public static Connection getConnection() throws SQLException, IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get("database.properties"))) {
            properties.load(inputStream);
        }
        String drivers = properties.getProperty("jdbc.drivers");
        if (drivers != null) {
            System.getProperty("jdbc.drivers", drivers);
        }
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
