package main.autosuggester;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * This acts like a text field which provides suggestions as you type.
 * 
 * @author David X. Wang
 * 
 * @param <E> The type of object which will populate the drop-down box, must have a display-friendly toString method.
 */
@SuppressWarnings("serial")
public class AutoSuggestionTextField<E> extends JComboBox{

	private AutoSuggestor<E> suggestor;
	private SuggestionListener<E> listener;
	private String previousText = "";
	private JTextField textField;
	private List<E> currentSuggestions = new ArrayList<E>();
	private boolean suppressEvents = false;

	public AutoSuggestionTextField(){
		super();
		//remove arrow
		setUI(new BasicComboBoxUI() {
			@Override
			protected JButton createArrowButton() {
				return new JButton() {
					@Override
					public int getWidth() {
						return 0;
					}
				};
			}

			@Override
			public void setPopupVisible(JComboBox c, boolean v) {
				// keeps the popup from coming down if there's nothing in the combo box
				if (c.getItemCount() > 0) {
					super.setPopupVisible(c, v);
				} else{
					super.setPopupVisible(c, false);
				}
			}
		});

		//properties and components
		setEditable(true);
		ItemSelectionListener lstnr = new ItemSelectionListener();
		addActionListener(lstnr);
		addItemListener(lstnr);
		textField = (JTextField) getEditor().getEditorComponent();
		textField.addKeyListener(new TextListener());
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				suppressEvents = true;
				setSelectedIndex(-1);
				textField.setText("");
				previousText = "";
				if(listener != null)
					listener.onDeselect();
				suppressEvents = false;
			}
		});
		
		//add some padding
		textField.setBorder(BorderFactory.createCompoundBorder(
		        textField.getBorder(), 
		        BorderFactory.createEmptyBorder(2, 3, 2, 3)));
	}

	public AutoSuggestor<E> getAutoSuggestor() {
		return suggestor;
	}

	public void setAutoSuggestor(AutoSuggestor<E> suggestor) {
		this.suggestor = suggestor;
	}

	public SuggestionListener<E> getSuggestionListener() {
		return listener;
	}

	public void setSuggestionListener(SuggestionListener<E> listener) {
		this.listener = listener;
	}
	
	public String getText() {
		return textField.getText();
	}

	private class TextListener extends KeyAdapter{

		@Override
		public void keyReleased(KeyEvent e) {
			if(suggestor == null)
				return;
			String text = textField.getText();
			if(text.equals(previousText))
				return;

			previousText = text;

			suppressEvents = true;

			if(getItemCount() != 0)
				removeAllItems();

			//populate drop down
			currentSuggestions = suggestor.getSuggestions(text);
			for(E item : currentSuggestions)
				addItem(item);

			setPopupVisible(false);

			if(!currentSuggestions.isEmpty())
				setPopupVisible(true);

			setSelectedItem(text);

			suppressEvents = false;
		}
	}

	private class ItemSelectionListener implements ItemListener, ActionListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(suppressEvents)
				return;
			
			previousText = textField.getText();
			
//			if(e.getStateChange() == ItemEvent.SELECTED)
//				passEvent();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(suppressEvents)
				return;
			
			if(e.getActionCommand().equals("comboBoxEdited")){
				passEvent();
			}
		}
		
		@SuppressWarnings("unchecked")
		private void passEvent(){
			if(listener == null)
				return;
			
			Object item = getSelectedItem();
			if(item instanceof String){
				listener.onEnter((String)item);
			} else{
				listener.onSuggestionSelected((E)item);
			}
		}
	}
}
