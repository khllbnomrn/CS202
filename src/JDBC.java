import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

public class JDBC extends Thread {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cs202";
    private static final String ROOT = "root";
    private static final String PASSWORD = "root123";

    private Connection connection=null;

    public JDBC (){}

    public Connection getConn(){
        return connection;
    }

    @Override
    public void run() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, ROOT, PASSWORD);
            System.out.println("Connected to the database.");

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
        }
        
    }

    public  boolean searchUser(String username) {
        boolean userFound = false;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                userFound = resultSet.next();
                connection.commit();
            }
        } catch (SQLException e) {
           System.err.println(e.getMessage());
        }
        return userFound;
    }

    public void getConversationList(ArrayList<Conversation> conversations) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Conversations WHERE id = ?")) {
            for (Conversation conversation : conversations) {
                statement.setInt(1, conversation.get_Id());
                try (ResultSet resultSet = statement.executeQuery()) {
                   // conversation = resultSet.next();
                    connection.commit();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
    public void addConversation(Conversation conversation) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Conversations (id, key, filepath) VALUES (?,?,?)")) {

            statement.setInt(1, conversation.get_Id());
            statement.setString(2, conversation.getKey());
            statement.setString(3, conversation.getFilePath());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void addUser(User user) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Users (full_name, password, email,username,phone_number) VALUES (?,MD5(?), ?, ?, ?)")) {
            statement.setString(1, user.getFullName());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUsername());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
            System.out.println("New user created");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateUser(User user) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE Users SET  (full_name, password, email,username,phone_number) VALUES (?,MD5(?), ?, ?, ?)")) {
            statement.setString(1, user.getFullName());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUsername());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
            System.out.println("New user created");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public User authenticateUser(String username, String password) {

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = MD5(?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                {
                    return new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                }
                else System.out.println("Credentials do not match any user");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }


    public void connectorClose() throws SQLException{
        try {
            connection.close();
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }


}
