
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

    public void updateUser(ArrayList<String> Data) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE Users SET  (full_name, password, email,username,phone_number) VALUES (?,MD5(?), ?, ?, ?)")) {
            statement.setString(1, Data.get(0));
            statement.setString(5, Data.get(1));
            statement.setString(3, Data.get(2));
            statement.setString(4, Data.get(3));
            statement.setString(2, Data.get(4));

            statement.executeUpdate();
            System.out.println("New user created");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addFriend(int my_id,int friend_id)
    {
        try (PreparedStatement statement = connection.prepareStatement("INSERT into friends_list (friend1_id,friend2_id) VALUES (?,?)")) {
            statement.setInt(1, my_id);
            statement.setInt(2, friend_id);
            statement.executeQuery();
            System.out.println("Friend added");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    public Conversation loadConversation(int id)
    {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM conversations WHERE id_conversations = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                {
                    return new Conversation(resultSet.getString(2),resultSet.getInt(1),resultSet.getString(3));
                }
                else System.out.println("Conversation no longer exists");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }


    public ArrayList<Integer> getContactsList(int id)
    {
        ArrayList<Integer> user_ids=null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM friends_list WHERE friend1_id = ? OR friend2_id = ?")) {
            statement.setInt(1, id);
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                {
                    if (resultSet.getInt(2)==id)
                    user_ids.add(resultSet.getInt(3));
                    else user_ids.add(resultSet.getInt(2));
                }
            }catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return user_ids;
    }


    public String getUserID(ArrayList<String>usernames)
    {
        String users_ids="";
        try (PreparedStatement statement = connection.prepareStatement("SELECT id_users FROM users WHERE username = ?")) {
            for (int i=0; i<usernames.size();i++) {
                statement.setString(1, usernames.get(i));
                try (ResultSet resultSet = statement.executeQuery()) {
                        users_ids=users_ids+resultSet.getInt(1)+".";
                }catch (SQLException e){
                    System.err.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return users_ids;
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
