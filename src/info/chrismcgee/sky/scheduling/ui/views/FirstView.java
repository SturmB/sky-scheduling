package info.chrismcgee.sky.scheduling.ui.views;

import java.sql.SQLException;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;

import info.chrismcgee.sky.scheduling.beans.Job;
import info.chrismcgee.sky.scheduling.ui.components.VerticalSpacedLayout;
import info.chrismcgee.sky.scheduling.util.ViewConfig;
import info.chrismcgee.sky.tables.JobManager;

@ViewConfig(uri = "firstview", displayName = "First View")
@DesignRoot
public class FirstView extends VerticalSpacedLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3850858652481769839L;
	public static final String NAME = "firstView"; // Defines this view.

	Label text = new Label();
	
	public FirstView() {
		setMargin(true);

		text.setValue("This is the FIRST view.");
		addComponent(text);
		
		try {
			Job job = JobManager.getRow("425000");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
