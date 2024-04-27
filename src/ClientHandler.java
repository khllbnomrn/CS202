
import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;


public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static JDBC DB;
    private ObjectOutputStream oos=null;
    private ObjectInputStream ois=null;
    private Conversation currentConv=null;
    private User user;
    private String request;
    private ArrayList<String> Data;
    private Vigenere vigenere= new Vigenere();


    public ClientHandler(Socket socket,JDBC DB, String request) throws IOException {

        this.socket = socket;
        this.DB =DB;
        this.in = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());
        this.request=request;
    }

    @Override
    public void run() {
        try {

            oos = new ObjectOutputStream(out);
            ois = new ObjectInputStream(in);
            try {
                Thread requestHandlerThread = new Thread(() -> {
                    try {
                        handleClientRequests(Data);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                requestHandlerThread.start();
            }catch (Exception e)
            {System.err.println(e.getMessage());}



        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    private void handleConversationSelection() throws IOException {
            out.write(currentConv.get_Id());
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

    private void handleClientRequests(ArrayList <String> Data) throws IOException{
        try {
            try {
            while (true) {

                switch (request) {
                    case "LOGIN":
                        handleLogin(Data);
                        break;
                    case "SIGNUP":
                        handleSignUp(Data);
                        break;
                    case "ADDFRIEND":
                        addFriend(Data);
                        break;
                    case "NEWCONVERSATION":
                        NewConversation(Data);
                        break;
                    case "LOGOUT":
                        try {
                            in.close();
                            out.close();
                            User user = null;
                            currentConv = null;
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "UPDATEPROFILE":
                        updateUser(Data);
                        break;

                }
            }
        }catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void handleLogin(ArrayList<String> Data)  {

        if (DB.searchUser(Data.get(0)))
        {
            User user = DB.authenticateUser("username", "password");
            if (user!=null) {
                try{
                    oos.writeObject(user);
                    sendMessage("LOGIN_SUCCESS");
                }catch(IOException e)
                {System.err.println(e.getMessage());}
            } else {

                sendMessage("LOGIN_FAILED");
            }
        }
    }

    public void NewConversation(ArrayList<String> usernames){

        String participant_ids=DB.getUserID(usernames);
        currentConv=new Conversation(participant_ids);
        DB.addConversation(currentConv);
    }

    private void handleSignUp(ArrayList<String> Data) throws SQLException{

        user = new User (Data.get(1),Data.get(3),Data.get(2), Data.get(0),Data.get(4));
        DB.addUser(user);
        System.out.println("user added");
        try{
            oos.writeObject(user);
        }catch(IOException e)
    {System.err.println(e.getMessage());}

    }

    public void addFriend(ArrayList<String> Data) throws NumberFormatException{
        try {
            DB.addFriend(Integer.valueOf(Data.get(0)),Integer.valueOf(Data.get(1)));
        }catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    public void logout(){
        this.user = null;
        this.currentConv=null;

    }

    public void updateUser(ArrayList<String> Data){
        DB.updateUser(Data);
    }

    public void newConv(){

    }

    private void joinConv(Conversation conv) {
        currentConv = conv;
        currentConv.addClient(this);
        try {
            handleConversationSelection();
        }catch (IOException e)
        {System.err.println(e.getMessage());}

        System.out.println(" has joined " +conv.getFilePath());
    }

    private void leaveConv() {
        currentConv.removeClient(this);
        System.out.println( " has left room: " + currentConv.getFilePath());
        currentConv = null;
        try {
            handleConversationSelection();
        } catch (IOException e) {
            System.err.println(e.getMessage());

        }

    }

    public void databaseOperations(JDBC DB) {




    }

    public void switchConv(Conversation conversation) {
        leaveConv();
        joinConv(conversation);
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        }catch (IOException e){
            System.err.println(e.getMessage());
        }

    }


}
