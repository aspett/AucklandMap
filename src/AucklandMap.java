import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class AucklandMap {
	private MapFrame mapFrame;
	private JPanel drawingPanel;
	private JTextArea textOutput;
	private JTextField searchField;
	private JButton searchButton;
	private int squareX = 50;
    private int squareY = 50;
    private int squareW = 35;
    private int squareH = 20;
	
	public AucklandMap() {
		final AucklandMap thisMap = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mapFrame = new MapFrame(thisMap);
				mapFrame.setVisible(true);
				
				drawingPanel = mapFrame.getDrawingPanel();
				textOutput = mapFrame.getOutputTextArea();
				searchField = mapFrame.getSearchTextField();
				searchButton = mapFrame.getSearchButton();
				
				drawingPanel.addMouseListener(new MouseAdapter() {
		            public void mouseClicked(MouseEvent evt) {
		            	textOutput.append("Clicked\n");
		            	panelClick(evt);
		            }
		        });
				drawingPanel.addMouseWheelListener(new MouseWheelListener() {
		            public void mouseWheelMoved(MouseWheelEvent evt) {
		                textOutput.append(String.format("Scrolled %s\n", evt.getWheelRotation() > 0 ? "down" : "up"));
		            }
		        });
				
				drawingPanel.addMouseListener(new MouseAdapter() {
		            public void mousePressed(MouseEvent e) {
		                moveSquare(e.getX(),e.getY());
		            }
		        });

				drawingPanel.addMouseMotionListener(new MouseAdapter() {
		            public void mouseDragged(MouseEvent e) {
		                moveSquare(e.getX(),e.getY());
		            }
		        });
			}
		});
	}
	
	public static void main(String[] args) {
		new AucklandMap();
		
	}
	
	public void drawMap(Graphics g) {    
        g.drawString("This is my custom Panel!",10,20);
        g.setColor(Color.RED);
        g.fillRect(squareX,squareY,squareW,squareH);
        g.setColor(Color.BLACK);
        g.drawRect(squareX,squareY,squareW,squareH);
	}
	
	public void panelClick(MouseEvent e) {
		if(e.getButton() == e.BUTTON1) {
			drawingPanel.repaint();
			textOutput.append("Clicked again\n");
		}
	}
	private void moveSquare(int x, int y) {
        int OFFSET = 1;
        if ((squareX!=x) || (squareY!=y)) {
            //drawingPanel.repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
        	drawingPanel.repaint();
            squareX=x;
            squareY=y;
            drawingPanel.repaint();
            //drawingPanel.repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
        } 
    }
	
}
