package info.chrismcgee.sky.scheduling.ui.views;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import info.chrismcgee.sky.scheduling.ui.components.ProductionTreeTable;
import info.chrismcgee.sky.scheduling.util.ViewConfig;

@ViewConfig(uri = "inproduction", displayName = "In Production")
public class TreeTableView extends CssLayout implements View {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 5490245198914445643L;
	
	public static final String NAME = "inProduction"; // Default views have an empty name.
	private final static Logger logger = Logger.getLogger(TreeTableView.class.getName());
	private final static int ROW_HEIGHT = 38;
	private final static int DEFAULT_MARGIN = 37;

	private ProductionTreeTable treeTable;
	private ProgressBar progressBar = new ProgressBar();
	private Date selectedDate;

	public TreeTableView() {
		
		Responsive.makeResponsive(this);
		
		int userAccess = 255; // For testing purposes only.
		int treeTableWidth = (60 * 16) - (2 * DEFAULT_MARGIN);
		
		final int MARK_AS_DONE = 1 << 0;

		boolean userCanMarkAsDone = (userAccess & MARK_AS_DONE) > 0;

		setWidth("100%");
		setHeight("90%");
		addStyleName("responsiveWidth");
		setResponsive(true);
		
		
		/**
		 * Outer Vertical Layout to have everything Centered.
		 */
		VerticalLayout outerLayout = new VerticalLayout();
		outerLayout.setWidth("100%");
		outerLayout.setHeight(100.0f, Unit.PERCENTAGE);
//		outerLayout.addStyleName("innerFrame");
		
		
		/**
		 * Inner CssLayout to hold both Vertical Layouts.
		 */
		CssLayout innerLayout = new CssLayout();
		innerLayout.addStyleName("innerFrame");
		innerLayout.setHeight(100.0f, Unit.PERCENTAGE);
		
		/**
		 * This vertical layout should contain only the search field and the treetable.
		 */
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setResponsive(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setWidthUndefined();
		mainLayout.addStyleName("height-adjust");
//		mainLayout.setHeight(100.0f, Unit.PERCENTAGE);
//		mainLayout.addStyleName("treetableBox");
		
		
		/**
		 * Search field
		 */
		
		TextField searchField = new TextField();
		searchField.setWidth(50.0f, Unit.EM);
		searchField.setInputPrompt("Search");
		searchField.addTextChangeListener(event -> {
			logger.log(Level.INFO, "Text changed to: " + event.getText());
			treeTable.filterTable(event.getText());
		});
		mainLayout.addComponent(searchField);
		mainLayout.setComponentAlignment(searchField, Alignment.TOP_CENTER);
		
		
		progressBar.setCaption("Loading:");
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		mainLayout.addComponent(progressBar);
		mainLayout.setComponentAlignment(progressBar, Alignment.TOP_CENTER);
		
		/**
		 * Treetable
		 */

		treeTable = new ProductionTreeTable();
		treeTable.changeDayTo(new Date());
		treeTable.setWidth(treeTableWidth, Unit.PIXELS);
		

		boolean partiallyCompleted = false; // To help set the Job's checkbox to the "indeterminate" style if only some of its OrderDetails are done.

		logger.log(Level.INFO, "Size of the Data Source: " + treeTable.getContainerDataSource().size());


		// Limit the size of the table.
//		treeTable.setPageLength(treeTable.getContainerDataSource().size());
//		treeTable.setPageLength((int)getHeight() / ROW_HEIGHT);
		treeTable.setHeight(100.0f, Unit.PERCENTAGE);


		mainLayout.addComponent(treeTable);
		mainLayout.setComponentAlignment(treeTable, Alignment.TOP_CENTER);
		
		// Expand ratio.
		mainLayout.setExpandRatio(treeTable, 1.0f);

		// Now add the vertical layout to this view.
		innerLayout.addComponent(mainLayout);
		
		
		
		/**
		 * Vertical layout for the calendar.
		 */
		VerticalLayout calLayout = new VerticalLayout();
		calLayout.setMargin(true);
		calLayout.setSpacing(true);
		calLayout.setWidthUndefined();
//		calLayout.addStyleName("itemBox");
		
		/**
		 * Calendar.
		 */
		InlineDateField cal = new InlineDateField();
		cal.addValueChangeListener(this::changeDay);
		
		calLayout.addComponent(cal);
//		calLayout.setWidth(100.0f, Unit.PERCENTAGE);
		calLayout.setComponentAlignment(cal, Alignment.MIDDLE_CENTER);
		
		innerLayout.addComponent(calLayout);
		outerLayout.addComponent(innerLayout);
		outerLayout.setComponentAlignment(innerLayout, Alignment.MIDDLE_CENTER);
		
		addComponent(outerLayout);
	}
	
	private void changeDay (ValueChangeEvent event) {
		
		selectedDate = (Date) event.getProperty().getValue();
		logger.log(Level.INFO, "Calendar Value changed. New value: " + selectedDate.toString());
		
		progressBar.setVisible(true); // Give the user some visual hint about loading taking place
		
		// Perform the data load in a separate thread
		loadDataInNewThread();
	}
	
	private void loadDataInNewThread() {
		new Thread(() -> {
			
			// This is needed because we are modifying the UI from a different thread:
			UI.getCurrent().access(() -> {
				treeTable.changeDayTo(selectedDate);
				progressBar.setVisible(false);
			});
			
		}).start();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Unused currently?
		
	}
	
}
