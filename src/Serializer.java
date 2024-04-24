import java.io.*;

public class Serializer {

    public void serializeMessage(Message message, String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.err.println("Error occurred while serializing message: " + e.getMessage());
        }
    }

    public void serializeUser(User user, String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(user);
        } catch (IOException e) {
            System.err.println("Error occurred while serializing user: " + e.getMessage());
        }
    }

    public User deserializeUser(String fileName) {
        User user = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            user = (User) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while deserializing user: " + e.getMessage());
        }
        return user;
    }

    public Message deserializeMessage(String fileName) {
        Message message = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            message = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred while deserializing message: " + e.getMessage());
        }
        return message;
    }
}
