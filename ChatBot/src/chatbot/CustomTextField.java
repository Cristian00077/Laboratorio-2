package chatbot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
        super(columns); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        setBackground(Color.WHITE); 
        setForeground(Color.DARK_GRAY); 

        setFont(new Font("Arial", Font.PLAIN, 16));

        setText("Envia un mensaje");
        setForeground(Color.GRAY); 

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals("Envia un mensaje")) {
                    setText(""); 
                    setForeground(Color.BLACK); 
                }
                setBackground(new Color(230, 240, 255)); 
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText("Envia un mensaje");
                    setForeground(Color.GRAY); 
                }
                setBackground(Color.WHITE); 
            }
        });

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = getText();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); 

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
    }

    public static CustomTextField createCustomTextField(int columns) {
        return new CustomTextField(columns);
    }
}
