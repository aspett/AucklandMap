package main;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

import assignment2.astar.PathFinder;
import main.autosuggester.AutoSuggestionTextField;
import main.autosuggester.SuggestionListener;
import mapgraph.*;
import indexstructures.roadtrie.*;


public class AucklandMap {
	
	public static boolean SHOWASTARVISITED = false;
	public static boolean SHOWARTICULATION = false;

	public static int ASSIGNMENT = 2;
	
	private MapFrame mapFrame;
	private JPanel drawingPanel;
	private JTextArea textOutput;
	private AutoSuggestionTextField<String> searchField;
	private JButton searchButton;
	private JMenuItem openMenuItem, assig1menu, assig2menu, astarmenu, runartmenu, clearastarmenu;
	private PathFinder selectedPath = null;

	private int mouseX;
	private int mouseY;
	@SuppressWarnings("unused")
	private int mouseX2;
	@SuppressWarnings("unused")
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
		assig1menu = mapFrame.getAssignmentOneMenuItem();
		assig2menu = mapFrame.getAssignmentTwoMenuItem();
		astarmenu = mapFrame.getAstarMenu();
		runartmenu = mapFrame.getArticulationMenu();
		clearastarmenu = mapFrame.getClearAstarMenu();


		final ActionListener searchButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchField.getSuggestionListener().onSuggestionSelected(searchField.getText());
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
            	if(mapGraph.intersectionQuad == null) return;
            	IntersectionNode closest = mapGraph.intersectionQuad.find(loc);
            	boolean closeEnough = false;
            	if(closest != null) {
	            	Point locPoint = closest.getLocation().getPoint(vd.getOrigin(), vd.getScale());
	            	Point2D p2 = (Point2D)p;
	            	Point2D loc2 = (Point2D) locPoint;
	            	double dist = p2.distance(loc2);
	            	//TODO debug System.out.printf("p2 %s, loc2 %s, dist %f\n", p2,loc2,dist);
	            	if(p2.distance(loc2) < 7) closeEnough = true;
            	
            	/* DEBUG System.out.printf("Clicked at point %s\n" +
            			" = Location %s\n" +
            			" = Node %s\n" +
            			" = Nodes: %s\n\n", p, loc, closest == null ? "null" : closest.toString(), all);*/
	            	
            	}
            	 	
            	if(closest != null && closeEnough) {

            		if(AucklandMap.ASSIGNMENT == 1) {
	            		IntersectionNode.setSelectedNode(closest);
	                	drawingPanel.repaint();
	                	textOutput.setText("");
	                	StringBuilder allRoads = new StringBuilder("");
		            	for(String s : closest.getAllRoads()) {
		            		allRoads.append(s+", ");
		            	}
		            	String out = String.format("Intersection information:\n" +
		            			"ID: %d,\nRoads connected by it:\n%s", closest.getID(), allRoads);
		            	textOutput.append(out);
            		} else {
            			if(evt.getButton() == MouseEvent.BUTTON1) {
            				if(closest != IntersectionNode.getEndNode() && closest != IntersectionNode.getStartNode()) {
            					IntersectionNode.setStartNode(closest);
            					
            				}
            			} else if(evt.getButton() == MouseEvent.BUTTON3) {
            				if(closest != IntersectionNode.getEndNode() && closest != IntersectionNode.getStartNode()) {
            					IntersectionNode.setEndNode(closest);
            				}
            			}
            			if(IntersectionNode.getStartNode() != null && IntersectionNode.getEndNode() != null) {
            				PathFinder pf = new PathFinder(IntersectionNode.getStartNode(), IntersectionNode.getEndNode());
            				pf.buildPath(mapGraph);
            				mapGraph.setPath(pf);
            				textOutput.setText(pf.getDirections());
            			} else {
            				mapGraph.setPath(null);
            			}
            			drawingPanel.repaint();
            		}
            	}
            }
        });
		drawingPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	ViewingDimensions vd = mapGraph.getViewingDimensions();
            	Location bOrigin = vd.getOrigin();

            	double bScale = vd.getScale();

            	Location centre = Location.newFromPoint(new Point(drawingPanel.getWidth()/2, drawingPanel.getHeight()/2), bOrigin, bScale);

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

            	//System.out.printf("Screen centre: %s | Map centre: %s\n", centreScreenLoc, centre);

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
				drawingPanel.repaint(drawingPanel.getWidth()-300, 100, drawingPanel.getWidth(), 130);
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

		searchField.setSuggestionListener(new SuggestionListener<String>() {

			@Override
			public void onSuggestionSelected(String query) {
				selectRoad(query);
			}

			@Override
			public void onEnter(String query) {
				selectRoad(query);
			}

			@Override
			public void onDeselect() {
			}
			
			public void selectRoad(String str) {
				str = str.trim();
				RoadGroup r = mapGraph.roadTrie.getRoad(str);
				if(r != null) {
					RoadGroup.setSelected(r);
					drawingPanel.repaint();
					textOutput.setText("");
					String roadInformation = String.format("Road information:\n" +
							"Name: %s, City/Town: %s,\nTotal length: %f, #Segments: %d", r.getName(), r.getCity(), r.getLength(), r.getSegments().size());
					textOutput.append(roadInformation);
				}
			}
			
		});
		
		ActionListener assig1MenuListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textOutput.setText("");
				textOutput.append("Assignment 1 Mode ON!\nYou are now able to click on intersections individually.");
				AucklandMap.ASSIGNMENT = 1;
				mapGraph.setPath(null);
				IntersectionNode.setStartNode(null);
				IntersectionNode.setEndNode(null);
			}
		};
		assig1menu.addActionListener(assig1MenuListener);
		ActionListener assig2MenuListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textOutput.setText("");
				textOutput.append("Assignment 2 Mode ON!\nYou are now able to click on pairs of intersections with left mouse, and right mouse to find the shortest path");
				AucklandMap.ASSIGNMENT = 2;
				mapGraph.setPath(null);
				IntersectionNode.setStartNode(null);
				IntersectionNode.setEndNode(null);
			}
		};
		assig2menu.addActionListener(assig2MenuListener);
		
		astarmenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SHOWASTARVISITED = !SHOWASTARVISITED;
				if(SHOWASTARVISITED) astarmenu.setText("Turn ASTAR debugging OFF");
				else astarmenu.setText("Turn ASTAR debugging ON");
				drawingPanel.repaint();
			}
			
		});
		
		clearastarmenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mapGraph.resetPath();
				IntersectionNode.setStartNode(null);
				IntersectionNode.setEndNode(null);
				textOutput.setText("");
				drawingPanel.repaint();
			}
			
		});
		
		runartmenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SHOWARTICULATION = !SHOWARTICULATION;
				if(SHOWARTICULATION) runartmenu.setText("Hide articulation points");
				else runartmenu.setText("Show articulation points");
				drawingPanel.repaint();
			}
		});

		ActionListener openMenuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open data folder");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				while(fc.getSelectedFile() == null)
					fc.showDialog(mapFrame, "Open data folder");
				
				mapGraph = new Graph(mapFrame);
				mapGraph.setOwner(thisMap);
				mapGraph.loadStructures(fc.getSelectedFile().getAbsolutePath());
				mapGraph.attachAutoSuggestor(searchField);
				
				drawingPanel.repaint();

			}
		};
		openMenuItem.addActionListener(openMenuListener);
		openMenuListener.actionPerformed(null);


	}



	public void drawMap(Graphics g) {
		mapGraph.draw(g, new Dimension(drawingPanel.getWidth(), drawingPanel.getHeight()));

		/*//DEBUG
		ViewingDimensions vd = mapGraph.getViewingDimensions();
		g.drawString(String.format("%s", Location.newFromPoint(new Point(mouseX2,mouseY2), vd.getOrigin(), vd.getScale())),
				(int) (new Dimension(drawingPanel.getWidth(), drawingPanel.getHeight()).getWidth()-300), 100);*/
	}

	public void panelClick(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			drawingPanel.repaint();
			textOutput.append("Clicked again\n");
		}
	}
	
	public Graph getMapGraph() {
		return mapGraph;
	}
	
	public void outputText(String str) {
		if(textOutput != null) textOutput.append(str+"\n");
	}

}
