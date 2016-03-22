/**
 * 
 */
package info.chrismcgee.sky.scheduling.ui.components;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;

import info.chrismcgee.sky.scheduling.beans.Job;
import info.chrismcgee.sky.scheduling.beans.OrderDetail;
import info.chrismcgee.sky.scheduling.ui.views.TreeTableView;
import info.chrismcgee.sky.tables.JobManager;

/**
 * @author Marketing
 *
 */
public class ProductionTreeTable extends TreeTable {

	/**
	 * Serialization!
	 */
	private static final long serialVersionUID = -8456241351283529486L;

	private final static Logger logger = Logger.getLogger(ProductionTreeTable.class.getName()); // Logging!

	private SimpleStringFilter filter = null;
	private Filterable f = null;
	
	
	/**
	 * Constructor
	 */
	public ProductionTreeTable() {
		super();
		
		// Set the columns.
		addContainerProperty("Name / Product", TreeNode.class, "");
		addContainerProperty("Job # / Detail", String.class, "");
		addContainerProperty("Print Type", String.class, "");
		addContainerProperty("Colors", Long.class, 0L);
		addContainerProperty("Quantity", Long.class, 0L);
		addContainerProperty("Total", Long.class, 0L);
		/*		addGeneratedColumn("TotalGen", new Table.ColumnGenerator() {

		  @Override
		  public Object generateCell(Table source, Object itemId, Object columnId) {
		    // TODO Auto-generated method stub
		    long colors = (Long) source.getItem(itemId).getItemProperty("Colors").getValue();
		    long quantity = (Long) source.getItem(itemId).getItemProperty("Quantity").getValue();
		    return new Label("" + (colors * quantity));
		  }
		});*/

	}
	

	HorizontalLayout createNode (String name, boolean isChecked) {
		HorizontalLayout layout = new HorizontalLayout();
		
		layout.addComponent(new CheckBox(null, isChecked));
		layout.addComponent(new Label(name));
		
		return layout;
	}
	
	public void changeDayTo (final Date date) {
		// Get a list of jobs on a particular date.
//		LocalDate tempDate = LocalDate.of(2016, 03, 01); // Remove this after we have the program get the current date.
//		LocalDate tempDate = LocalDate.now();
		filter = null; // Reset the filter.
		List<Job> jobsList = null;
		try {
			jobsList = JobManager.getJobsByDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (jobsList != null && jobsList.size() > 0) {
			
			removeAllItems(); // Clear out the TreeTable first.
			
			for (final Job j : jobsList) {
			    // Go through each item in the Jobs list and add it
			    // (along with all of its OrderDetail items) to the TreeTable.
//				boolean firstOrderCompleted = j.getOrderDetailList().get(0).getItemCompleted() != null; // The first OrderDetail's completion status.
				final Object jobId = addItem(new Object[] {new TreeNode(j.getCustomerName(), j.getJobCompleted() != null), j.getJobId(),
						"", null, null, null}, null);
				for (final OrderDetail od : j.getOrderDetailList()) {
					final Object odId = addItem(new Object[] {new TreeNode(od.getProductId(), od.getItemCompleted() != null), od.getProductDetail(),
							od.getPrintType().getValue(), od.getNumColors(), od.getQuantity(), (od.getNumColors() * od.getQuantity())}, null);
//					logger.log(Level.INFO, "jobId is: " + jobId.toString());
//					logger.log(Level.INFO, "odId is: " + odId.toString());
//					logger.log(Level.INFO, "job to string: " + getItem(jobId).toString());
//					logger.log(Level.INFO, "job can have children?: " + areChildrenAllowed(jobId));
					setParent(odId, jobId);
//					logger.log(Level.INFO, "Set parent successfully?: " + parentSet);
					setChildrenAllowed(odId, false);
					setCollapsed(odId, false);
				}
				setCollapsed(jobId, false);
//				logger.log(Level.INFO, "Property ID for 'Name / Product': " + getContainerDataSource().getContainerProperty(j, "Name / Product").toString());
			}
			HierarchicalContainer dataSource = (HierarchicalContainer)getContainerDataSource();
			dataSource.setIncludeParentsWhenFiltering(true);
			filterTable("");
//			logger.log(Level.INFO, "Container Data Source: " + getContainerDataSource());
//			logger.log(Level.INFO, "Container Data Source, Stringized: " + getContainerDataSource().toString());
//			logger.log(Level.INFO, "Container Data Source, Size: " + getContainerDataSource().size());
//			logger.log(Level.INFO, "Container Data Source, Class Name: " + getContainerDataSource().getClass().getName());
			logger.log(Level.INFO, "Property ID 0's class: " + getContainerDataSource().getContainerPropertyIds().toArray()[0].getClass().getName());
			logger.log(Level.INFO, "Property ID 0's name: " + getContainerDataSource().getContainerPropertyIds().toArray()[0].toString());
		}
	}

	public void filterTable (String searchText) {
		// TODO Auto-generated method stub
		f = (Filterable) getContainerDataSource();
		
		// Remove old filter
		if (filter != null) {
			f.removeContainerFilter(filter);
		}
		
		// Set new filter for the "Name / Product" column
		filter = new SimpleStringFilter("Name / Product", searchText, true, false);
		f.addContainerFilter(filter);
		
		Notification.show("Results narrowed.", Type.TRAY_NOTIFICATION);
	}

}
