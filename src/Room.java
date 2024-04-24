import java.util.*;
import java.io.*;
import java.net.*;
public class Room {
    private String name;
    private List<ClientHandler> clients = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcast(String message, ClientHandler sender) {

        for (ClientHandler client : clients) {
            if (client==sender)
                {continue;}
            client.sendMessage("[" + name + "] " + message);
        }
    }
}