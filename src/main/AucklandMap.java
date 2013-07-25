package main;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private int mouseX2;
	private int mouseY2;
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
            	ViewingDimensions vd = mapGraph.getViewingDimensions();
            	Point p = new Point(evt.getX(),evt.getY());
            	Location loc = Location.newFromPoint(p, vd.getOrigin(), vd.getScale());
            	IntersectionNode closest = mapGraph.intersectionQuad.find(loc);
            	System.out.printf("Clicked at point %s\n" +
            			" = Location %s\n" +
            			" = Node %s\n\n", p, loc, closest == null ? "null" : closest.toString());
            }
        });
		drawingPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	ViewingDimensions vd = mapGraph.getViewingDimensions();
            	Location bOrigin = vd.getOrigin();

            	double bScale = vd.getScale();
            	double bZoom = vd.getZoom();

            	Location centre = Location.newFromPoint(new Point(drawingPanel.getWidth()/2, drawingPanel.getHeight()/2), bOrigin, bScale);
            	Point centrePoint = centre.getPoint(bOrigin, bScale);

            	if(e.getWheelRotation() < 0) {
            		vd.setZoom(vd.getZoom()*1.10);
            		bScale *= 1.10;
            	}
            	else {
            		vd.setZoom(vd.getZoom()*0.90);
            		bScale *= 0.90;
            	}

            	Point centreScreen = new Point(drawingPanel.getWidth()/2, drawingPanel.getHeight()/2);
            	Location centreScreenLoc = Location.newFromPoint(centreScreen, bOrigin, bScale);

            	System.out.printf("Screen centre: %s | Map centre: %s\n", centreScreenLoc, centre);

            	Location oldPan = vd.getPan();
            	Location newPan = new Location(oldPan.x + (centreScreenLoc.x-centre.x), oldPan.y + (centreScreenLoc.y-centre.y));
            	vd.setPan(newPan);

            	mapGraph.setViewingDimensions(vd);

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
			public void mouseMoved(MouseEvent e) {
				mouseX2 = e.getX();
				mouseY2 = e.getY();
				drawingPanel.repaint();
			}
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() - mouseX;
                int y = e.getY() - mouseY;
                ViewingDimensions d = mapGraph.getViewingDimensions();
                Location newPan = Location.newFromPoint(new Point(x,y), d.getPan(), d.getScale());

                mapGraph.setViewingDimensions(new ViewingDimensions(d.getOrigin(), newPan, d.getScale(), d.getZoom()));
                mouseX = e.getX();
                mouseY = e.getY();
                drawingPanel.repaint();
            }
        });

		roadListPanel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				String selectedValue = (String)roadListPanel.getSelectedValue();
				if(selectedValue != null) {
					selectedValue = selectedValue.trim();
					Road r = mapGraph.roadTrie.getRoad(selectedValue);
					r.setSelected(true);
					drawingPanel.repaint();
				}
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

		//DEBUG
		ViewingDimensions vd = mapGraph.getViewingDimensions();
		g.drawString(String.format("%s", Location.newFromPoint(new Point(mouseX2,mouseY2), vd.getOrigin(), vd.getScale())),
				(int) (new Dimension(drawingPanel.getWidth(), drawingPanel.getHeight()).getWidth()-300), 100);
	}

	public void panelClick(MouseEvent e) {
		if(e.getButton() == e.BUTTON1) {
			drawingPanel.repaint();
			textOutput.append("Clicked again\n");
		}
	}

}
