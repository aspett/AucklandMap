package main.autosuggester;
/* Code for COMP261 Assignment
 */


// call repaint() on this object to invoke the drawing.

import javax.swing.JComponent;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.*;


/** NameCompleter is a small program that uses the AutoSuggestionTextField
 *  to make a text field that shows a set of possible completions.
 *  It also has an output text area that it writes into.
 */

public class NameCompleter{

    private JFrame frame;
    private JTextArea textOutput; 
    private int windowSize = 500;

    public NameCompleter(){
	// Set up a window .
	frame = new JFrame("Text entry field example");
	frame.setSize(windowSize,windowSize);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	textOutput = new JTextArea(25, 100);
	JScrollPane textSP = new JScrollPane(textOutput);
	frame.add(textSP, BorderLayout.SOUTH);

	//set up code for auto-suggest text field
	AutoSuggestionTextField<String> textField = new AutoSuggestionTextField<String>();
	textField.setAutoSuggestor(new SuburbAutoSuggestor());
	textField.setSuggestionListener(new SuggestionListener<String>() {
                        
		@Override
		public void onSuggestionSelected(String item) {
		    textOutput.append("\nSelected: " + item);
		}
                        
		@Override
		public void onEnter(String query) {
		    textOutput.append("\nQuery: " + query);
		}
		@Override
		public void onDeselect(){}

	    });

	//Set up a panel 
	JPanel panel = new JPanel();
	frame.add(panel, BorderLayout.NORTH);
	panel.add(new JLabel("Type suburb name: "));

	panel.add(textField);

	JButton button = new JButton("Quit");
	panel.add(button);
	button.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ev){System.exit(0);}});

	textOutput.setText("Possible Suburbs\n");
	textOutput.append(SuburbAutoSuggestor.suburbList());
	
	frame.setVisible(true);
    }


    public static void main(String[] arguments){
       new NameCompleter();
    }	


}
