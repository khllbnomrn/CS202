import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static void main(String[] args) {

        JDBC DB = new JDBC();
        DB.start();

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started. Listening on port 12345...");

            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler clientHandler = new ClientHandler(socket,DB);
                clientHandler.start();

            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}