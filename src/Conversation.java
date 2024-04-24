import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Conversation {
    private int id;
    private String FilePath;
    private String key=keyGenerator(255);

    public Conversation(String FilePath) {
        this.FilePath = FilePath;
    }

    public Conversation( String FilePath, int id) {
        this.id =id;
        this.FilePath = FilePath;
    }


    public String getKey(){
        return key;
    }

    public int getId() {
        return id;
    }


    public String getFilePath() {
        return FilePath;
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
