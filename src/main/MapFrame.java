package main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew
 */
@SuppressWarnings({ "unused", "serial" })
public class MapFrame extends JFrame {

	private AucklandMap owner;
	private String loadingString;

    /**
     * Creates new form Map
     */
    public MapFrame(AucklandMap owner) {
    	this.owner = owner;
    	loadingString = "";
    	
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        drawingPanel = new JPanel(){
        	public void paintComponent(Graphics g) {
        		super.paintComponent(g);
        		owner.drawMap(g);
        	}
        };
        searchPanel = new JPanel(){
        	public void paintComponent(Graphics g) {
        		super.paintComponent(g);
        		g.drawString(loadingString, 10, 20);
        	}
        };
        searchLabel = new JLabel();
        searchButton = new JButton();
        searchTextField = new JTextField();
        jScrollPane1 = new JScrollPane();
        outputTextArea = new JTextArea();
        menuBar = new JMenuBar();

        roadList = new DefaultListModel();
        roadListPanel = new JList(roadList);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(255, 255, 153));
        setResizable(false);
        getContentPane().setLayout(new GridBagLayout());

        drawingPanel.setBackground(new Color(230, 230, 230));
        drawingPanel.setBorder(BorderFactory.createLineBorder(new Color(102,102,102)));
        drawingPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        drawingPanel.setMinimumSize(new Dimension(800, 600));
        drawingPanel.setPreferredSize(new Dimension(800, 600));
        drawingPanel.setLayout(new GridBagLayout());

        searchPanel.setBackground(new Color(102, 102, 102, 155));
        searchPanel.setForeground(new Color(255, 255, 255));
        searchPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        searchPanel.setLayout(new GridBagLayout());

        searchLabel.setText("Search:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        searchPanel.add(searchLabel, gridBagConstraints);

        searchButton.setText("Go");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        searchPanel.add(searchButton, gridBagConstraints);

        searchTextField.setText("Road Name");
        searchTextField.setToolTipText("");
        searchTextField.setMinimumSize(new Dimension(50, 30));
        searchTextField.setPreferredSize(new Dimension(150, 30));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        searchPanel.add(searchTextField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 666;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(1, 1, 569, 1);
        drawingPanel.add(searchPanel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(drawingPanel, gridBagConstraints);

        jScrollPane1.setAutoscrolls(true);

        /*outputTextArea.setEditable(false);
        outputTextArea.setColumns(20);
        outputTextArea.setRows(5);*/
        roadListPanel.setVisibleRowCount(5);
        jScrollPane1.setViewportView(roadListPanel);


        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        openMenuItem = new JMenuItem("Open");
        fileMenu.add(openMenuItem);
        setJMenuBar(menuBar);


        pack();
    }// </editor-fold>

    public void setLoadingMessage(String str) {
    	loadingString = str;
    	searchPanel.repaint();
    }
    
    public JPanel getDrawingPanel() {
    	return this.drawingPanel;
    }
    public JTextArea getOutputTextArea() {
    	return this.outputTextArea;
    }
    public JButton getSearchButton() {
    	return this.searchButton;
    }
    public JTextField getSearchTextField() {
    	return this.searchTextField;
    }
    public JMenuItem getOpenMenu() {
    	return this.openMenuItem;
    }
    public DefaultListModel getRoadList() {
    	return this.roadList;
    }
    public JList getRoadListPanel() {
    	return this.roadListPanel;
    }

    // Variables declaration - do not modify
    private JPanel drawingPanel;
    private JLabel explanationLabel;
    private JPanel headerPanel;
    private JScrollPane jScrollPane1;
    private JTextArea outputTextArea;
    private JButton searchButton;
    private JLabel searchLabel;
    private JPanel searchPanel;
    private JTextField searchTextField;
    private JMenuBar menuBar;
    private JMenuItem openMenuItem;
    private JList roadListPanel;
    private DefaultListModel roadList;
    // End of variables declaration
}
