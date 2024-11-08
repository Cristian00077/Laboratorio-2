package chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class OllamaService {

    private final String RAW_OLLAMA_API_CHAT_URL = "http://localhost:11434/api/chat";
    private final URL ollamaUrl;
    private final OlamaListener listener;
    public static boolean NoOllamaExcept;
    
    public interface OlamaListener {
        void onMessageStarted();
        void onMessage(OllamaMessage message);
        void onFinish();
    }

    public OllamaService(OlamaListener listener) throws MalformedURLException, URISyntaxException {
        this.ollamaUrl = new URI(RAW_OLLAMA_API_CHAT_URL).toURL();
        this.listener = listener;
    }

    public void sendMessage(String message) throws IOException {
        String payload = "{\"model\": \"llama3.1\", \"messages\": [{\"role\": \"user\", \"content\": \""+ message +"\"}]}";

        HttpURLConnection connection = getConnectionForChat();

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }catch(Exception $e){
            JOptionPane.showMessageDialog(null, "Libreria Ollama no conectada", "Error", JOptionPane.INFORMATION_MESSAGE);
            NoOllamaExcept = false;
            ChatApp.messageInputField.setText("");
        }

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        listener.onMessageStarted();
        String line;
        while ((line = reader.readLine()) != null) {
            RawOllamaResponse resp = RawOllamaResponse.fromRawJson(line);
            if (resp.done) {
                listener.onFinish();
            } else {
                listener.onMessage(resp.message);
            }
        }

        reader.close();
        connection.disconnect();
    }
    private HttpURLConnection getConnectionForChat() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) ollamaUrl.openConnection();

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException ex) {
            Logger.getLogger(OllamaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

}
