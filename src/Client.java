
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private Scanner std;
    private Vigenere vigenere= new Vigenere();

    public Client(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server.");

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            Scanner std = new Scanner(System.in);


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void start(){

        new Thread(this::receiveMessages).start();
        sendMessages();
        try {
            if (socket != null) {
                socket.close();
            }
            if (std.nextLine() != null) {
                std.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    private void sendMessages() {
        try {
            while (true) {
                std = new Scanner(System.in);
                String message = std.nextLine();
                vigenere.decrypt(message,"currentConv.getKey(");
                output.writeUTF(message);
                output.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void receiveMessages() {
        try {
            DataInputStream serverInput = new DataInputStream(socket.getInputStream());
            while (true) {
                String message = serverInput.readUTF();
                System.out.println(message);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }



    public static void main(String[] args) throws IOException{
        Client client = new Client("localhost", 12345);
        client.start();
    }
}
