import java.sql.*;
import java.io.*;
import java.net.*;


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

            Thread requestHandlerThread = new Thread(this::handleClientRequests);
            requestHandlerThread.start();


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

    private void handleClientRequests() throws IOException{
        try {
            while (true) {

                switch (request) {
                    case "LOGIN":
                        handleLogin();
                        break;
                    case "SIGNUP":
                        handleSignUp();
                        break;
                    case "ADDFRIEND":
                        addFriend();
                        break;
                    case "NEWCONVERSATION":
                        NewConversation();
                        break;
                    case "LOGOUT":
                        try{
                            in.close();
                            out.close();
                            User user= null;
                            currentConv= null;
                        }catch(Exception e)
                        {System.err.println(e.getMessage());}
                        break;
                    case "UPDATEPROFILE":
                        updateUser();
                        break;
                
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void handleLogin() {
       
        if (DB.searchUser("username"))
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

    public void NewConversation(){
        currentConv=new conversation();
        DB.addConversation(currentConv);
    }

    private void handleSignUp() throws UserExistsException{
        
        
        try{
            DB.addUser(user);
        }catch(UserExistsException e)
        {
            System.err.println(e.getMessage());
        }    
            
        
    }

    public void addFriend(){
        
    }

    public void logout(){
        this.user = null;
        this.currentConv=null;

    }

    public void updateUser(){
            DB.updateUser(user);
    }

    public void newConv(){

    }

    private void joinConv(Conversation conv) {
        currentConv = conv;
        currentConv.addClient(this);
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

