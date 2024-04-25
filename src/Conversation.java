import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Conversation {
    private int id;
    private String FilePath="Conversation";
    private String key=keyGenerator(255);
    ArrayList<Integer> participants = new ArrayList<Integer>();
    ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public Conversation(ArrayList<Integer> participants) {
        for (int i=0;i<participants.size();i++)
        this.FilePath = this.FilePath+participants.get(i);
        this.FilePath=this.FilePath+".txt";
    }

    public Conversation( String FilePath, int id) {
        this.id =id;
        this.FilePath = FilePath;
    }

    public void addClient(ClientHandler client){
        clients.add(client);
    }

    public void removeClient(ClientHandler client){
        clients.remove(client);
    }

    public String getKey(){
        return key;
    }

    public int get_Id() {
        return id;
    }


    public String getFilePath() {
        return FilePath;
    }


    public void broadcast(String message, ClientHandler sender) {

        for (ClientHandler client : clients) {
            if (client==sender)
                {continue;}
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
