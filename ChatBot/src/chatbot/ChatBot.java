package chatbot;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class ChatBot {

    public static void main(String[] args) throws MalformedURLException, ProtocolException, IOException, URISyntaxException {
        ChatsManager filesManager = new ChatsManager();
        filesManager.createDirectoryIfNoExists();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ChatApp(filesManager);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
