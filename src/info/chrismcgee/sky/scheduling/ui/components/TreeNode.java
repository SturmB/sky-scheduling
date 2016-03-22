package info.chrismcgee.sky.scheduling.ui.components;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class TreeNode extends HorizontalLayout {
	


	/**
	 * Serialization!
	 */
	private static final long serialVersionUID = 232800657274822961L;
	
	private CheckBox checkbox;
	private Label label;
	
	
	/**
	 * @param checkbox
	 * @param label
	 */
	public TreeNode(String name, boolean isChecked) {
		super();
		
		checkbox = new CheckBox(null, isChecked);
		label = new Label(name);
		
		addComponent(checkbox);
		addComponent(label);
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return label.getValue();
	}

}
