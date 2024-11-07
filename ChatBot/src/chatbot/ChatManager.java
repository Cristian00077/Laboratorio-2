package chatbot;
import java.util.ArrayList;
public class ChatManager {

    private String chatTitle;
    private final ArrayList<OllamaMessage> messages;

    public ChatManager() {
        this.chatTitle = "no title";
        this.messages = new ArrayList<>();
    }
    public ChatManager(String chatTitle) {
        this.chatTitle = chatTitle;
        this.messages = new ArrayList<>();
    }
    public void addNewMessage(OllamaMessage message) {
        this.messages.add(message);
    }
    public ArrayList<OllamaMessage> getMessages() {
        return this.messages;
    }
    public String getTitle() {
        return this.chatTitle;
    }
    public void setTitle(String title) {
        this.chatTitle = title;
    }
}
