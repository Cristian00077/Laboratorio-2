package chatbot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.BorderFactory;

public class RoundedButton extends JButton {

    private Color bgColor;
    private Color hoverBgColor, hoverTextBgColor;
    private Color originalBgColor, originalTextColor;
    private double borderRadius;

    public RoundedButton(String text, Color bgColor, Color textColor) {
        super(text);
        this.originalTextColor = this.hoverTextBgColor = textColor;
        this.bgColor = bgColor;
        this.originalBgColor = bgColor;
        this.hoverBgColor = bgColor.brighter();
        this.borderRadius = 36;

        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(textColor);

        // Add hover effect with MouseAdapter
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBgColorNoOverride(hoverBgColor);
                setForeground(hoverTextBgColor);
//                setBorderRadius(40);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBgColorNoOverride(originalBgColor);
                setForeground(originalTextColor);

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), this.borderRadius, this.borderRadius));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150, 40); // Example size
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        this.originalBgColor = bgColor;
    }
    
    private void setBgColorNoOverride(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setBorderRadius(double borderRadius) {
        this.borderRadius = borderRadius;
    }

    public void setHoverBgColor(Color hoverColor, Color textColor) {
        this.hoverBgColor = hoverColor;
        this.hoverTextBgColor = textColor;
    }
}
