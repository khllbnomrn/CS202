
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

public class Conversation {
    private int id;
    private String FilePath = "Conversation";
    private String key;
    String participants_ids;
    FileOutputStream fos;
    ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();


    public Conversation(String participants_id) {

        this.FilePath = this.FilePath + participants_id + ".txt";
        key = keyGenerator(255);
    }

    public Conversation(String FilePath, int id, String key) {
        this.id = id;
        this.FilePath = FilePath;
        this.key = key;
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public String getKey() {
        return key;
    }

    public int get_Id() {
        return id;
    }


    public String getFilePath() {
        return FilePath;
    }

    public void loadConversationfromFile() throws FileNotFoundException {
        try {
            File file = new File(FilePath);
            FileInputStream fis = new FileInputStream(file);

            try {
                ObjectInputStream OIS = new ObjectInputStream(fis);
                while (fis.read() != -1) {
                    try {
                        System.out.println((Message) OIS.readObject());
                    } catch (ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void saveConversationtoFile(Message message) throws FileNotFoundException {
        try {
            File file = new File(FilePath);
             fos= new FileOutputStream(file);

            try {
                ObjectOutput OOS = new ObjectOutputStream(fos);
                OOS.writeObject(message);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void broadcast(String message, ClientHandler sender) {

        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendMessage("[" + client + "] " + message);
        }
    }

    public static String keyGenerator(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            char randomChar = chars.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }


}
