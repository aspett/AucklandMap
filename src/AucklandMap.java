import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import mapgraph.*;


public class AucklandMap {
	private MapFrame mapFrame;
	private JPanel drawingPanel;
	private JTextArea textOutput;
	private JTextField searchField;
	private JButton searchButton;
	private JMenuItem openMenuItem;

	private int squareX = 50;
    private int squareY = 50;
    private int squareW = 35;
    private int squareH = 20;
    private Graph mapGraph;

	public AucklandMap() {
		mapGraph = new Graph();

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){init();}
		});

	}

	public static void main(String[] args) {
		new AucklandMap();

	}

	public void init() {
		final AucklandMap thisMap = this;
		mapFrame = new MapFrame(thisMap);
		mapFrame.setVisible(true);

		drawingPanel = mapFrame.getDrawingPanel();
		textOutput = mapFrame.getOutputTextArea();
		searchField = mapFrame.getSearchTextField();
		searchButton = mapFrame.getSearchButton();
		openMenuItem = mapFrame.getOpenMenu();

		final ActionListener searchButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textOutput.append(String.format("%s\n", searchField.getText()));
			}
		};

		searchButton.addActionListener(searchButtonListener);

		searchField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					searchButtonListener.actionPerformed(null);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}

		});

		drawingPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	textOutput.append("Clicked\n");
            	panelClick(evt);
            }
        });
		drawingPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent evt) {
                textOutput.append(String.format("Scrolled %s\n", evt.getWheelRotation() > 0 ? "down" : "up"));
                squareH -= evt.getWheelRotation();
                squareW -= evt.getWheelRotation();
                drawingPanel.repaint();
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

		ActionListener openMenuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				while(fc.getSelectedFile() == null)
					fc.showDialog(mapFrame, "Open");
				mapGraph.loadStructures(fc.getSelectedFile().getAbsolutePath());

			}
		};
		openMenuItem.addActionListener(openMenuListener);
		openMenuListener.actionPerformed(null);

		testTrie();


	}

	private void testTrie() {
		// TODO Auto-generated method stub
		TreeSet<String> roads = mapGraph.roadTrie.matchRoads("");
		for(String road : roads) {
			System.out.println(road);
		}
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
