package chatbot;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;

public class ChatsManager {

    private final String chatsFile;

    public ChatsManager() {
        FileSystemView view = FileSystemView.getFileSystemView();
        File file = view.getHomeDirectory();
        String desktopPath = file.getPath();

        this.chatsFile = desktopPath + File.separator + "ChatBot" + File.separator + "messages" + File.separator;
    }

    public void createDirectoryIfNoExists() {
        File directory = new File(chatsFile);

        if (directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directorio creada correctamente " + chatsFile);
            } else {
                System.out.println("Error para crear directorio: " + chatsFile);
            }
        } else {
            System.out.println("Directorio ya existe: " + chatsFile);
        }
    }

    public void saveChat(ChatManager chat) throws IOException {
        File chatFile = new File(this.chatsFile + File.separator + chat.getTitle().replaceAll(" CHAT ", "|") + ".chat");

        try (BufferedWriter output = new BufferedWriter(new FileWriter(chatFile))) {
            output.write(chat.getTitle() );
            output.write(String.valueOf(chat.getMessages().size()) );

            for (OllamaMessage message : chat.getMessages()) {
                output.write("start@" + message.getRole() );
                output.write(message.getContent() );
                output.write("end@" + message.getRole() );
            }
        }
    }

    public ChatManager getChatFromTitle(String chatTitle) throws IOException {
        File chatFile = new File(this.chatsFile + File.separator + chatTitle.replaceAll(" ", "_") + ".chat");

        System.out.println(chatFile);
        if (!chatFile.exists()) {
            throw new FileNotFoundException("No encontrado " + chatTitle);
        }

        ChatManager chat = new ChatManager(chatTitle);
        try (BufferedReader input = new BufferedReader(new FileReader(chatFile))) {
            String title = input.readLine();
            chat.setTitle(title);

            int messageCount = Integer.parseInt(input.readLine());

            for (int i = 0; i < messageCount; i++) {
                String startLine = input.readLine();
                if (startLine.startsWith("start@")) {
                    String role = startLine.substring(6);

                    StringBuilder content = new StringBuilder();
                    String contentLine;
                    while ((contentLine = input.readLine()) != null && !contentLine.startsWith("end@")) {
                        content.append(contentLine).append("\n");
                    }
                    OllamaMessage message = new OllamaMessage(role, content.toString().trim());
                    chat.addNewMessage(message);
                }
            }
        }
        return chat;
    }
    public ArrayList<String> getChats() {
        File directory = new File(this.chatsFile);
        
        var chatsList = new ArrayList<String>();

        if (directory.exists() && directory.isDirectory()) {
            FilenameFilter chatFileFilter = (File dir, String name) -> name.endsWith(".chat");

            File[] chatFiles = directory.listFiles(chatFileFilter);

            if (chatFiles != null && chatFiles.length > 0) {
                for (File chatFile : chatFiles) {
                    chatsList.add(chatFile.getName().split(".chat")[0].replaceAll("_", " "));
                }
            }
        } else {
            System.out.println("No existe");
        }
        return chatsList;
    }
}
