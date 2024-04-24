import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class Client {
    private DataOutputStream out;
    private Scanner consoleInput = new Scanner(System.in);
    private DataInputStream in;
    private Socket socket;
    private User user=new User();
    Vigenere vigenere= new Vigenere();
    private String serverAddress;
    private int port;

    public Client(String serverAddress, int port) throws IOException{

        this.serverAddress = serverAddress;
        this.port = port;
        try {
            socket = new Socket(serverAddress, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }catch (SocketException e){
            System.err.println(e.getMessage());
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

    }


    public void start() {
    try {

            Thread messageHandler = new Thread(this::handleMessages);
            messageHandler.start();

            String userInput;
            while ((userInput = consoleInput.nextLine()) != null) {
                out.writeUTF(userInput);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {

            try {
                if (socket != null) {
                    socket.close();
                }
                if (consoleInput != null) {
                    consoleInput.close();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void handleMessages() {
        try {
            String message;
            System.out.println("client handle message test");
            while ((message =in.readUTF()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

  /*  public User loadUser() throws IOException{

        try {
            String choice;
            while ((choice = consoleInput.readLine())!=null){
                switch (choice) {

                    case "1":
                        try {
                            out.println("login");
                            return login();
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }

                    case "2":
                        try {
                            out.println("signup");
                            return signup();
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }

                }
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    public User login() throws IOException{
        try {
            System.out.println("username :");
            out.println(consoleInput.readLine());
            System.out.println("password :");
            out.println(consoleInput.readLine());
            return MS.deserializeUser(in.readLine());

        }catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;

    }

   /* public User signup() throws IOException{
        try {
            System.out.println("Full name");
            String FN= in.readLine();
            System.out.println("Phone Number");
            String PN= in.readLine();
            System.out.println("Email");
            String EM= in.readLine();
            System.out.println("Username");
            String UN= in.readLine();
            System.out.println("Password");
            String PA= in.readLine();
            User user = new User(FN,PN,EM,UN,null,PA);

            MS.serializeUser(user,"");
            return user;

        }catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }*/


    public static void main(String[] args) throws IOException{
        Client client = new Client("localhost", 12345);
        client.start();
    }
}
