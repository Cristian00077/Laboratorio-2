package chatbot;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GhostButton extends RoundedButton {

    private final Color borderColor;

    public GhostButton(String text, Color borderColor, Color textColor) {
        super(text, new Color(254,254,255,255), textColor);
        this.borderColor = borderColor;
        
        setBorderRadius(0);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

        g2.dispose();
    }
    
}
