import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

class Message implements Serializable {
    private String sender;
    private String content;
    private String date;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        date=getDate();
    }

    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}