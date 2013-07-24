package main;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.DefaultListModel;
import mapgraph.*;


public class AucklandMap {
	private MapFrame mapFrame;
	private JPanel drawingPanel;
	private JTextArea textOutput;
	private JTextField searchField;
	private JButton searchButton;
	private JMenuItem openMenuItem;
	private DefaultListModel roadList;
	private JList roadListPanel;

	private int mouseX;
	private int mouseY;
    private Graph mapGraph;

	public AucklandMap() {
		mapGraph = new Graph(mapFrame);

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				init();
			}
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
		roadList = mapFrame.getRoadList();
		roadListPanel = mapFrame.getRoadListPanel();


		final ActionListener searchButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roadList.clear();
				for(String road : mapGraph.roadTrie.matchRoads(searchField.getText())) {
					roadList.addElement(String.format("%s\n",road));
				}
			}
		};

		searchButton.addActionListener(searchButtonListener);

		searchField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {searchButtonListener.actionPerformed(null);}
			public void keyReleased(KeyEvent arg0) {searchButtonListener.actionPerformed(null);}
			public void keyTyped(KeyEvent arg0) {searchButtonListener.actionPerformed(null);}

		});

		drawingPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	//panelClick(evt);
            }
        });
		drawingPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	Location originBefore = mapGraph.getViewingDimensions().getOrigin();
            	double scaleBefore = mapGraph.getViewingDimensions().getScale();
            	double zoomBefore = mapGraph.getViewingDimensions().getZoom();
            	Point centrePoint = new Point(mapFrame.getWidth()/2, mapFrame.getHeight()/2);
            	Location centreLocBefore = Location.newFromPoint(centrePoint, originBefore, scaleBefore*zoomBefore);
            	
            	if(e.getWheelRotation() > 0) mapGraph.zoomOut();
            	else mapGraph.zoomIn();
            	
            	Location originAfter = mapGraph.getViewingDimensions().getOrigin();
            	double scaleAfter = mapGraph.getViewingDimensions().getScale();
            	double zoomAfter = mapGraph.getViewingDimensions().getZoom();
            	Location centreAfter = Location.newFromPoint(centrePoint, originAfter, scaleAfter*zoomAfter);
            	
            	double newX = centreLocBefore.x - centreAfter.x;
            	double newY = centreLocBefore.y - centreAfter.y;
            	/*newX*=0.33;
            	newY*=0.33;*/
            	
            	Location newOriginOffset = new Location(originBefore.x+((centreAfter.x-centreLocBefore.x)/3),originBefore.y+((centreAfter.y-centreLocBefore.y)/3));
            	//Location newOriginOffset = new Location(originBefore.x-newX,originBefore.y-newY);
            	mapGraph.setViewingDimensions(new ViewingDimensions(newOriginOffset, scaleAfter, zoomAfter));
                    	
            	/*int x = e.getX();
                int y = e.getY();
                ViewingDimensions before,after;
                before = mapGraph.getViewingDimensions();
                double scaleBefore = before.getScale();
                Point oldPoint = new Point(x,y);
                Location oldLocation = Location.newFromPoint(oldPoint,  mapGraph.getViewingDimensions().getOrigin(), scaleBefore*before.getZoom());
            	if(e.getWheelRotation() > 0) mapGraph.zoomOut();
            	else mapGraph.zoomIn();
            	
            	after = mapGraph.getViewingDimensions();
            	double scaleAfter = after.getScale();

            	Point newPoint = oldLocation.getPoint(mapGraph.getViewingDimensions().getOrigin(), scaleAfter);
            	Point pDiff = new Point(oldPoint.x - newPoint.x, oldPoint.y - newPoint.y);
            	Location lDiff = Location.newFromPoint(pDiff, mapGraph.getViewingDimensions().getOrigin(), scaleAfter);
            	System.out.printf("%f, %f\n", lDiff.x, lDiff.y);
            	Location oldOrigin = mapGraph.getViewingDimensions().getOrigin();
            	Location newOrigin = new Location(oldOrigin.x - lDiff.x, oldOrigin.y - lDiff.y);
            	
            	mapGraph.setViewingDimensions(new ViewingDimensions(newOrigin, scaleAfter, after.getZoom()));
            	
            	System.out.printf("Point before: %s, Point after: %s\n", oldPoint, newPoint);*/
            	
                drawingPanel.repaint();
            	
            }
        });
		

		drawingPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	mouseX = e.getX();
            	mouseY = e.getY();
            	//System.out.printf("Started @ (%d, %d)\n", mouseX, mouseY);
            }
        });

		drawingPanel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() - mouseX;
                int y = e.getY() - mouseY;
                ViewingDimensions d = mapGraph.getViewingDimensions();
                Location newOrigin = Location.newFromPoint(new Point(x,y), d.getOrigin(), d.getScale());
                
                mapGraph.setViewingDimensions(new ViewingDimensions(newOrigin, d.getScale(), d.getZoom()));
                mouseX = e.getX();
                mouseY = e.getY();
                drawingPanel.repaint();
            }
        });



		ActionListener openMenuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				while(fc.getSelectedFile() == null)
					fc.showDialog(mapFrame, "Open");
				mapGraph = new Graph(mapFrame);
				mapGraph.loadStructures(fc.getSelectedFile().getAbsolutePath());
				drawingPanel.repaint();

			}
		};
		openMenuItem.addActionListener(openMenuListener);
		openMenuListener.actionPerformed(null);

		//TODO do something now.


	}



	public void drawMap(Graphics g) {
        /*g.drawString("This is my custom Panel!",10,20);
        g.setColor(Color.RED);
        g.fillRect(squareX,squareY,squareW,squareH);
        g.setColor(Color.BLACK);
        g.drawRect(squareX,squareY,squareW,squareH);*/
		mapGraph.draw(g, new Dimension(drawingPanel.getWidth(), drawingPanel.getHeight()));
	}

	public void panelClick(MouseEvent e) {
		if(e.getButton() == e.BUTTON1) {
			drawingPanel.repaint();
			textOutput.append("Clicked again\n");
		}
	}

}
