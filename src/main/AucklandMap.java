package main;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;
import main.autosuggester.AutoSuggestionTextField;
import main.autosuggester.SuggestionListener;
import mapgraph.*;


public class AucklandMap {
	private MapFrame mapFrame;
	private JPanel drawingPanel;
	private JTextArea textOutput;
	private AutoSuggestionTextField<String> searchField;
	private JButton searchButton;
	private JMenuItem openMenuItem;

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
            	IntersectionNode closest = mapGraph.intersectionQuad.find(loc);
            	boolean closeEnough = false;
            	if(closest != null) {
	            	Point locPoint = closest.getLocation().getPoint(vd.getOrigin(), vd.getScale());
	            	Point2D p2 = (Point2D)p;
	            	Point2D loc2 = (Point2D) locPoint;
	            	double dist = p2.distance(loc2);
	            	System.out.printf("p2 %s, loc2 %s, dist %f\n", p2,loc2,dist);
	            	if(p2.distance(loc2) < 7) closeEnough = true;
            	
            	/* DEBUG System.out.printf("Clicked at point %s\n" +
            			" = Location %s\n" +
            			" = Node %s\n" +
            			" = Nodes: %s\n\n", p, loc, closest == null ? "null" : closest.toString(), all);*/
	            	
            	}
            	
            	
            	if(closest != null && closeEnough) {
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
				Road r = mapGraph.roadTrie.getRoad(str);
				if(r != null) {
					r.setSelected(true);
					drawingPanel.repaint();
					textOutput.setText("");
					String roadInformation = String.format("Road information:\n" +
							"ID: %d, Name: %s, City/Town: %s,\nTotal length: %f, #Segments: %d", r.getId(), r.getName(), r.getCity(), r.getLength(), r.getSegments().size());
					textOutput.append(roadInformation);
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
