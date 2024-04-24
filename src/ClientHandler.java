import java.sql.*;
import java.io.*;
import java.net.*;


public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static JDBC DB;
    private Room currentRoom = new Room("ROOM");
    private ObjectOutputStream oos=null;
    private ObjectInputStream ois=null;
    Serializer MS = new Serializer();
    User user= new User("khalil ben omrane","2801595222","aloooooo","f;:::zea","passa");

    public ClientHandler(Socket socket,JDBC DB) throws IOException {

        this.socket = socket;
        this.DB =DB;
        this.in = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());

    }

    @Override
    public void run() {
        try {



            oos = new ObjectOutputStream(out);
            //ois = new ObjectInputStream(in);


            DB.addUser(user);
            in.readUTF();
            user=DB.authenticateUser(user.getUsername(), user.getPassword());
            if (user!= null)
                System.out.println(user.getUsername()+" "+user.getPassword());
            System.out.println("client handler run test");
            handleMessages();

        } catch (IOException e) {
            System.err.println(e.getMessage());;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    private void handleConversationSelection() throws IOException {

    }

    private void handleMessages() throws IOException {
        String message;
        try {
            System.out.println("client handler handle message test");
            while ((message = in.readUTF()) != null) {
               sendMessage(message);
            }
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    private void joinRoom(Room room) {
        currentRoom = room;
        currentRoom.addClient(this);
        System.out.println(" has joined " +room.getName());
    }

    private void leaveRoom() {
            currentRoom.removeClient(this);
            System.out.println( " has left room: " + currentRoom.getName());
            currentRoom = null;
            try {
                handleConversationSelection();
            } catch (IOException e) {
                System.err.println(e.getMessage());

            }

    }

    public void databaseOperations(JDBC DB) {




    }

    public void switchRoom(Room room) {
            leaveRoom();
            joinRoom(room);
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        }catch (IOException e){
            System.err.println(e.getMessage());
        }

    }


}

