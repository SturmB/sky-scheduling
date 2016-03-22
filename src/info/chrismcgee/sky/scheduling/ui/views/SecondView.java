package info.chrismcgee.sky.scheduling.ui.views;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import info.chrismcgee.sky.scheduling.util.ViewConfig;

@ViewConfig(uri = "secondview", displayName = "Second View")
@DesignRoot
public class SecondView extends VerticalLayout implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7276038288055718024L;
	public static final String NAME = "secondView"; // Defines this view.

	Label text = new Label();

	public SecondView() {
		setMargin(true);
		
		text.setValue("This is the SECOND view.");
		addComponent(text);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
