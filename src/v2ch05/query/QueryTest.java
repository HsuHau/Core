package v2ch05.query;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * This program demonstrates several complex database queries.
 */
public class QueryTest {
    private static final String allQuery = "SELECT Books.Price, Books.Tile FROM Books";

    private static final String authorPublisherQuery = "SELECT Books.Price, Books.Title"
            + "FROM　Books, BooksAuthors, Authors, Publishers"
            + "WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN"
            + "AND Books.Publisher_Id = Publishers.Publisher_Id AND Authors.Name = ?"
            + "AND Publishers.Name = ?";

    private static final String authorQuery
            = "SELECT Books.Price, Books.Title FROM Books, BookAuthors, Authors"
            + "WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN"
            + "AND Publishers.Name = ?";

    private static final String publisherQuery
            = "SELECT Books.Price, Books.Title FROM Books, Publishers"
            + "AND Books.Publisher_Id = Publishers.Publisher_Id AND Authors.Name = ?";

    private static final String priceUpdate
            = "UPDATE Books "
            + "SET Price = Price + ?"
            + "WHERE Books.Publisher_Id = (SELECT Publisher_Id FROM Publishers WHERE Name = ?)";

    private static Scanner in;
    private static ArrayList<String> authors = new ArrayList<>();
    private static ArrayList<String> publishers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try (Connection connection = getConnection()) {
            in = new Scanner(System.in);
            authors.add("Any");
            publishers.add("Any");
            try (Statement statement = connection.createStatement()) {

                // Fill the authors array list
                String query = "SELECT Name FROM Authors";
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        authors.add(resultSet.getString(1));
                    }
                }

                // Fill the publishers array list
                query = "SELECT Name FROM Publishers";
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        publishers.add(resultSet.getString(1));
                    }
                }
            }
            boolean done = false;
            while (done) {
                System.out.println("Q)uery C)hange prices E)xit: ");
                String input = in.next().toUpperCase();
                if (input.equals("Q")) {
                    executeQuery(connection);
                } else if (input.equals("C")) {
                    changePrices(connection);
                } else {
                    done = true;
                }
            }
        } catch (SQLException e) {
            for (Throwable throwable : e) {
                System.out.println(throwable.getMessage());
            }
        }
    }

    /**
     * Executes the selected query
     * @param connection the database connection
     */
    private static void executeQuery(Connection connection) throws SQLException
    {
        String author = select("Authors:", authors);
        String publisher = select("Publishers:", publishers);
        PreparedStatement preparedStatement;
        if (!author.equals("Any") && !publisher.equals("Any"))
        {
            preparedStatement = connection.prepareStatement(authorPublisherQuery);
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, publisher);
        }
        else if (!author.equals("Any") && publisher.equals("Any"))
        {
            preparedStatement = connection.prepareStatement(authorQuery);
            preparedStatement.setString(1, author);
        }
        else if (author.equals("Any") && !publisher.equals("Any"))
        {
            preparedStatement = connection.prepareStatement(publisherQuery);
            preparedStatement.setString(1, publisher);
        }
        else
        {
            preparedStatement = connection.prepareStatement(allQuery);
        }
        try (ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next())
            {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }

    /**
     * Executes an update statement to change prices
     * @param connection the database connection
     */
    public static void changePrices(Connection connection) throws SQLException
    {
        String publisher = select("Publishers:", publishers.subList(1, publishers.size()));
        System.out.println("Change prices by: ");
        double priceChange = in.nextDouble();
        PreparedStatement preparedStatement = connection.prepareStatement(priceUpdate);
        preparedStatement.setDouble(1, priceChange);
        preparedStatement.setString(2, publisher);
        int r = preparedStatement.executeUpdate();
        System.out.println(r + " records updated.");
    }

    /**
     * Asks the user to select a string
     * @param prompt the prompt to display
     * @param options the options from which the user can choose
     * @return the option that the uer chose
     */
    public static String select(String prompt, List<String> options)
    {
        while (true)
        {
            System.out.println(prompt);
            for (int i = 0; i < options.size(); i++) {
                System.out.printf("%2d) %s%n", i + 1, options.get(i));
            }
            int sel = in.nextInt();
            if (sel > 0 && sel <= options.size())
            {
                return options.get(sel - 1);
            }
        }
    }

    /**
     * Gets a connection from the properties specified in the file database.properties
     * @return the database connection
     */
    public static Connection getConnection() throws SQLException, IOException
    {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get("database.properties")))
        {
            properties.load(inputStream);
        }

        String drivers = properties.getProperty("jdbc.drivers");
        if (drivers != null)
        {
            System.setProperty("jdbc.drivers", drivers);
        }
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
