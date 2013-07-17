import javax.swing.*;
import java.awt.*;


class MapPanel extends JPanel {

	private static final int width = 300;
	private static final int height = 300;
	
    public MapPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return getSize();
    }
    public static Dimension mapSize() {
    	return new Dimension(width,height);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        // Draw Text
        g.drawString("This is my custom Panel!",10,20);
    }  
    
}