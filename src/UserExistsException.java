public class UserExistsException extends Exception{

    public UserExistsException()
    {     
    }

    private String message = "username/user alreay exists";

    public String getMessage()
    {
        return message;
    }
}