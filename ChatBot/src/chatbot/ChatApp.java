package chatbot;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChatApp extends JFrame implements OllamaService.OlamaListener {

    private final int SIDEBAR_WIDTH = 400;
    private final ChatsManager chatsManager;
    private final JPanel chatContentPanel;
    private final JScrollPane scrollPane;
    public static JTextField messageInputField;
    private ChatManager currentChat;
    private static final int MAX_HISTORIAL = 10; 
    private static String[] historial = new String[MAX_HISTORIAL];
    private static int indiceHistorial = 0;

    private final ArrayList<GhostButton> chatsBtns;

    private final OllamaService ollamaService;
    private OllamaMessage message;

    public ChatApp(ChatsManager chatsManager) throws MalformedURLException, URISyntaxException {
        this.chatsManager = chatsManager;
        this.chatsBtns = new ArrayList<>();
        this.ollamaService = new OllamaService(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, getHeight()));
        sidebar.setMinimumSize(new Dimension(SIDEBAR_WIDTH, getHeight()));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

        RoundedButton newChatButton = new RoundedButton("Nuevo chat", new Color(51, 153, 255), Color.WHITE);
        newChatButton.setMinimumSize(new Dimension(SIDEBAR_WIDTH, 40));
        newChatButton.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 40));
        newChatButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(newChatButton);
        sidebar.add(new JSeparator());

        RoundedButton historyButton = new RoundedButton("Historial", new Color(51, 153, 255), Color.WHITE);
        historyButton.setMinimumSize(new Dimension(SIDEBAR_WIDTH, 40));
        historyButton.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 40));
        historyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyButton.addActionListener(e -> mostrarHistorial());  
        sidebar.add(historyButton);

        add(sidebar, BorderLayout.WEST);

        chatContentPanel = new JPanel();
        chatContentPanel.setBackground(Color.WHITE);
        chatContentPanel.setLayout(new BoxLayout(chatContentPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(chatContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel messageInputPanel = new JPanel();
        messageInputPanel.setLayout(new BorderLayout());
        messageInputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        messageInputField = new CustomTextField(ALLBITS);
        messageInputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageInputPanel.add(messageInputField, BorderLayout.CENTER);

        JButton sendButton = new RoundedButton("Enviar", Color.red, Color.white);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        sendButton.setBackground(new Color(51, 153, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(new SendMessageActionListener());
        messageInputPanel.add(sendButton, BorderLayout.EAST);

        add(messageInputPanel, BorderLayout.SOUTH);  

        setVisible(true);

        CargarChats(sidebar);
    }

    private void CargarChats(JPanel sidebar) {
        var chatsNames = this.chatsManager.getChats();

        if (chatsNames.isEmpty()) {
            return ;
        }
        for (String chat : chatsNames) {
            var btn = getChatButton(sidebar, chat);
            sidebar.add(btn, BorderLayout.PAGE_END);
            chatsBtns.add(btn);
        }
        CargarChatSeleccionado(sidebar, chatsNames.get(0));
    }

    private GhostButton getChatButton(JPanel sidebar, String title) {
        GhostButton chatButton = new GhostButton(title, Color.white, Color.BLACK);

        chatButton.setHoverBgColor(Color.red, Color.white);

        chatButton.setHorizontalAlignment(SwingConstants.LEFT);
        chatButton.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 40));
        chatButton.setMinimumSize(new Dimension(SIDEBAR_WIDTH, 40));

        chatButton.addActionListener((e) -> {
            CargarChatSeleccionado(sidebar, title);
        });

        return chatButton;
    }

    private void CargarChatSeleccionado(JPanel sidebar, String chatName) {
        ChatManager chat;
        try {
            chat = chatsManager.getChatFromTitle(chatName);
        } catch (IOException ex) {
            Logger.getLogger(ChatApp.class.getName()).log(Level.SEVERE, null, ex);
            return ;
        }
        currentChat = chat;

        for (GhostButton comp : chatsBtns) {
            if (chat.getTitle() == null ? comp.getText() == null : chat.getTitle().equals(comp.getText())) {
                comp.setBgColor(Color.yellow);
            } else {
                comp.setBgColor(Color.WHITE);
            }
        }
        renderChats();
        sidebar.repaint();
    }

    private void renderChats() {
        if (currentChat == null) {
           return;
        }
        messageInputField.setText("");
        chatContentPanel.removeAll();
        for (OllamaMessage message : currentChat.getMessages()) {
            
            AñadirMensajeAlChat(message.getContent(), message.getRole(), false);
        }
    }

    @Override
    public void onMessageStarted() {
        message = new OllamaMessage();
    }

    @Override
    public void onMessage(OllamaMessage upComing) {
        boolean isFirst = "".equals(message.getContent());

        message.setContent(message.getContent() + upComing.getContent());
        message.setRole(upComing.getRole());

        AñadirMensajeAlChat(message.getContent() + upComing.getContent(), "assistant", !isFirst);
                chatContentPanel.repaint();
    }

    @Override
    public void onFinish() {
        if (this.currentChat == null) {
    this.currentChat = new ChatManager("Titulo de chat");
}
    this.currentChat.addNewMessage(message);

        currentChat.addNewMessage(message);
        AñadirMensajeAlChat(message.getContent(), message.getRole(), true);
    }
    class SendMessageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String userMessage = messageInputField.getText();
            if (!userMessage.trim().isEmpty()) {
                AñadirMensajeAlChat(userMessage, "usuario", false);
                try {
                    ollamaService.sendMessage(userMessage);
                    messageInputField.setText("");

                } catch (IOException ex) {
                    Logger.getLogger(ChatApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void AñadirMensajeAlChat(String message, String sender, boolean deleteLast) {
        if ("usuario".equals(sender)) {
            if (indiceHistorial < MAX_HISTORIAL) {
            historial[indiceHistorial++] = message;
        } else {
            System.arraycopy(historial, 1, historial, 0, MAX_HISTORIAL - 1);
            historial[MAX_HISTORIAL - 1] = message;
            }
        }

        chatContentPanel.add(new MessagePanel(message, sender));

        if (deleteLast && chatContentPanel.getComponentCount() > 1) {
            chatContentPanel.remove(chatContentPanel.getComponentCount() - 2);
        }
        chatContentPanel.revalidate();
        chatContentPanel.repaint();
        scrollPane.revalidate();
       
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }
        private static void mostrarHistorial() {
            System.out.println("\n--- Historial de la conversación ---");
            for (int i = 0; i < indiceHistorial; i++) {
                System.out.println(historial[i]);
            }
            if (indiceHistorial == 0) {
            JOptionPane.showMessageDialog(null, "El historial está vacío.", "Historial de Conversación", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder historialTexto = new StringBuilder("<html><body style='width: 400px;'>");
        for (int i = 0; i < indiceHistorial; i++) {
            historialTexto.append(historial[i]).append("<br>");
        }
        historialTexto.append("</body></html>");
        JOptionPane.showMessageDialog(null, new JLabel(historialTexto.toString()), "Historial de Conversación", JOptionPane.INFORMATION_MESSAGE);
        
    }

    
    
    
    
    
    


    
    
    class MessagePanel extends JPanel {

        public MessagePanel(String message, String sender) {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));  
            
            if(OllamaService.NoOllamaExcept){
                JLabel messageLabel = new JLabel("<html><p style='width: 500px;'>" + message + "</p></html>"); 
                messageLabel.setOpaque(true);
                messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
                if ("user".equals(sender)) {
                    messageLabel.setBackground(new Color(173, 216, 230)); 
                    messageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                    setLayout(new FlowLayout(FlowLayout.RIGHT)); 
                } else {
                    messageLabel.setBackground(new Color(240, 240, 240)); 
                    messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    setLayout(new FlowLayout(FlowLayout.LEFT)); 
                }
                add(messageLabel, BorderLayout.CENTER);
            }
        }
    }

}
