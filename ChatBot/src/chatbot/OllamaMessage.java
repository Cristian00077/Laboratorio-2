package chatbot;
public class OllamaMessage {

    private String role;
    private String content;
    public OllamaMessage() {
        role = "";
        content = "";
    }
    public OllamaMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getRole() {
        return this.role;
    }
    public String getContent() {
        return this.content;
    }
}
