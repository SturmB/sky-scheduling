package info.chrismcgee.sky.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TreeTable;

import info.chrismcgee.dbutil.ConnectionManager;
import info.chrismcgee.dbutil.DBName;
import info.chrismcgee.sky.enums.PrintType;
import info.chrismcgee.sky.scheduling.beans.Job;
import info.chrismcgee.sky.scheduling.beans.OrderDetail;

/**
 * @author Marketing
 *
 * This class is a set of static methods that interface with the database table "OrderDetail".
 * It is mostly all CRUD methods.
 */
public class OrderDetailManager {

	private static ResultSet rs = null; // The result set from a successful SQL query.
	private final static Logger logger = Logger.getLogger(OrderDetailManager.class.getName()); // Logging!

	/**
	 * Standard "insert" method for adding a new job's item to the OrderDetail table.
	 * 
	 * @param bean The OrderDetail bean that contains the necessary info for adding to the table.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean insert(OrderDetail bean) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.
		
		// The SQL statement that will be run on the DBMS.
		String sql = "INSERT INTO OrderDetail ("
				+ "order_id, "
				+ "product_id, "
				+ "product_detail, "
				+ "print_type, "
				+ "num_colors, "
				+ "quantity, "
				+ "item_completed, "
				+ "proof_num, "
				+ "proof_date, "
				+ "thumbnail) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ResultSet keys = null; // Stores the database-created primary key when the entry is created.
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				){
			
			// Put all of the bean's details into the PreparedStatement object, which will be combined with the SQL statement.
			stmt.setString(1, bean.getOrderId());
			stmt.setString(2, bean.getProductId());
			stmt.setString(3, bean.getProductDetail());
			stmt.setInt(4, PrintType.getIntValue(bean.getPrintType()));
			stmt.setLong(5, bean.getNumColors());
			stmt.setLong(6, bean.getQuantity());
			stmt.setTimestamp(7, bean.getItemCompleted());
			stmt.setInt(8, bean.getProofNum());
			stmt.setTimestamp(9, bean.getProofDate());
			stmt.setString(10, bean.getThumbnail());
			
			int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.
			
			if (affected == 1) { // Hopefully, only 1 line was inserted.
				keys = stmt.getGeneratedKeys(); // Get the primary keys that the DBMS assigned for these rows (records).
				// Again, hopefully there is only one, but it always returns a List of them anyway.
				keys.next(); // Move to the first key.
				int newKey = keys.getInt(1); // Get the actual value of that key.
				bean.setId(newKey); // Update the Java bean of this item so it has the same primary key.
			} else {
				logger.log(Level.CONFIG, "No rows affected");
				return false; // In case there was a problem and no items were added to the database table.
			}
			
		} catch (SQLException e) { // If there was an error trying to update the database table.
			logger.log(Level.SEVERE, "Error trying to update the database table.", e);
			return false;
		} finally {
			if (keys != null) keys.close(); // Make sure the keys object is closed in case it isn't null.
		}
		
		return true; // If everything worked fine, then return true, indicating success!
	}

	/**
	 * Standard "update" method for updating a job's item to the OrderDetail table.
	 * 
	 * @param bean The OrderDetail bean that contains the new info for updating the table.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean update(OrderDetail bean) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql =
				"UPDATE OrderDetail SET "
				+ "order_id = ?, "
				+ "product_id = ?, "
				+ "product_detail = ?, "
				+ "print_type = ?, "
				+ "num_colors = ?, "
				+ "quantity = ?, "
				+ "item_completed = ?, "
				+ "proof_num = ?, "
				+ "proof_date = ?, "
				+ "thumbnail = ? "
				+ "WHERE id = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put all of the bean's details into the PreparedStatement object, which will be combined with the SQL statement.
			stmt.setString(1, bean.getOrderId());
			stmt.setString(2, bean.getProductId());
			stmt.setString(3, bean.getProductDetail());
			stmt.setInt(4, PrintType.getIntValue(bean.getPrintType()));
			stmt.setLong(5, bean.getNumColors());
			stmt.setLong(6, bean.getQuantity());
			stmt.setTimestamp(7, bean.getItemCompleted());
			stmt.setInt(8, bean.getProofNum());
			stmt.setTimestamp(9, bean.getProofDate());
			stmt.setString(10, bean.getThumbnail());
			stmt.setInt(11, bean.getId());
			
			int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.
			
			if (affected == 1) { // Hopefully, only 1 line was updated.
				return true; // If everything worked fine, then return true, indicating success!
			} else {
				return false; // In case there was a problem and no items were updated in the database table.
			}
			
		} catch (SQLException e) { // If there was an error trying to update the database table.
			logger.log(Level.SEVERE, "Error trying to update the database table.", e);
			return false;
		}
		
	}

	/**
	 * A small "update" method for setting a job's item as completed.
	 * 
	 * @param bean The OrderDetail bean that contains the info on the item that will be set as completed.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean setItemCompleted(OrderDetail bean) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.
		
		// The SQL statement that will be run on the DBMS.
		String sql =
				"UPDATE OrderDetail SET item_completed = ? "
				+ "WHERE id = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put just the timestamp of when the item was completed into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setTimestamp(1, bean.getItemCompleted());
			stmt.setInt(2, bean.getId()); // The primary key (id) of the item in the database table.
			
			int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.
			
			if (affected == 1) { // Hopefully, only 1 line was updated.
				return true; // If everything worked fine, then return true, indicating success!
			} else {
				return false; // In case there was a problem and the item was not updated in the database table.
			}
			
		} catch (SQLException e) { // If there was an error trying to update the database table.
			logger.log(Level.SEVERE, "Error trying to update the database table.", e);
			return false;
		}
		
	}

	/**
	 * Standard "delete" method for deleting a job's item from the OrderDetail table.
	 * This version takes an id (primary key) of the item, so only 1 item will be deleted.
	 * 
	 * @param id The primary key (id) of the job's item.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean delete(int id) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.
		
		// The SQL statement that will be run on the DBMS.
		String sql = "DELETE FROM OrderDetail WHERE id = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put just the primary key (id) of the job's item into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setInt(1, id); // The primary key (id) of the item in the database table.
			
			int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.
			
			if (affected == 1) { // Hopefully, only 1 line was deleted.
				return true; // If everything worked fine, then return true, indicating success!
			} else {
				return false; // In case there was a problem and the item was not removed from the database table.
			}
			
		} catch (SQLException e) { // If there was an error trying to delete from the database table.
			logger.log(Level.SEVERE, "Error trying to delete from the database table.", e);
			 return false;
		}
		
	}

	/**
	 * Standard "delete" method for deleting a job's items from the OrderDetail table.
	 * This version takes an Order ID String, so 1 or more items will be deleted.
	 * 
	 * @param orderID The job order number associated with the item(s) to be deleted.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean delete(String orderID) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.
		
		// The SQL statement that will be run on the DBMS.
		String sql = "DELETE FROM OrderDetail WHERE order_id = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put just the Job's order number into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setString(1, orderID);
			
			int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.
			
			if (affected >= 1) { // Hopefully, at least 1 line was deleted.
				return true; // If everything worked fine, then return true, indicating success!
			}
			return false; // In case there was a problem no items were removed from the database table.
			
		} catch (SQLException e) { // If there was an error trying to delete from the database table.
			logger.log(Level.SEVERE, "Error trying to delete from the database table.", e);
			return false;
		}
		
	}

	/**
	 * Gets 1 or more rows from the database table of items that have a specified order number.
	 * 
	 * @param jobId The String value of the order number from which to get its associated items.
	 * @return beanList A list of the items that are associated with the order number.
	 * @throws SQLException
	 */
	public static List<OrderDetail> getRows(String jobId) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.
		
		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT * FROM OrderDetail WHERE order_id = ?";
		// The list of OrderDetail beans associated with the Order Number that will be returned.
		List<OrderDetail> beanList = new ArrayList<OrderDetail>();
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// Put just the Job's order number into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setString(1, jobId);
			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			
			// Go through each result in the ResultSet (row) and create an OrderDetail bean from it,
			// then add each bean to the bean List.
			while (rs.next()) {
				OrderDetail bean = new OrderDetail();
				bean.setId(rs.getInt("id"));
				bean.setOrderId(jobId);
				bean.setProductId(rs.getString("product_id"));
				bean.setProductDetail(rs.getString("product_detail"));
				bean.setPrintType(PrintType.getPrintType(rs.getInt("print_type")));
				bean.setNumColors(rs.getLong("num_colors"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setItemCompleted(rs.getTimestamp("item_completed"));
				bean.setProofNum(rs.getInt("proof_num"));
				bean.setProofDate(rs.getTimestamp("proof_date"));
				bean.setThumbnail(rs.getString("thumbnail"));
				
				beanList.add(bean); // Add each bean to the bean List.
			}
			
			return beanList; // If everything worked fine, then return the List of OrderDetail beans, indicating success!
			
		} catch (SQLException e) { // If there was an error trying to read from the database table.
			logger.log(Level.SEVERE, "Error trying to read from the database table.", e);
			 return null; // Just return null, which indicates a failure.
		} finally {
			if (rs != null) {
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
			}
		}
	}

	public static void orderDetailCheckboxChanged(ValueChangeEvent event, OrderDetail orderDetail, TreeTable treeTable, Job job) {
		
		// True if the change was an un-checked box to a checked box, false if the other way 'round.
		boolean boxChecked = (boolean)event.getProperty().getValue();
		// If the box was checked, then this variable stores the current timestamp. Otherwise, it just stores null.
		Timestamp completedStamp = boxChecked ? new Timestamp(System.currentTimeMillis()) : null;
		
		logger.log(Level.INFO, "OrderDetail " + orderDetail.getProductId() + " checked; Storing timestamp " + completedStamp + " to the OrderDetail object.");
		// TODO Here is where the code goes for setting the OrderDetail bean's completed property,
		// then updating the database.
		orderDetail.setItemCompleted(completedStamp);
		
		// TODO Check all siblings to see if they all match this one's now.
		// If so, then change the parent (Job) box.

		// Get a list of all siblings first.
		List<OrderDetail> siblings = job.getOrderDetailList();
		boolean mixed = false; // True if at least one of the OrderDetail items is checked and at least one of the others is not.
		boolean allChecked = boxChecked; // True if all of the siblings are checked.
		boolean noneChecked = !boxChecked; // True if none of the siblings are checked.

		// Go through each sibling to see what we should set the parent Job to.
		for (OrderDetail od : siblings) {
			if ((od.getItemCompleted() != null) != boxChecked) {
				// If the current sibling doesn't match the first.
				mixed = true;
			}
			if (!allChecked || (od.getItemCompleted() == null)) {
				allChecked = false;
			}
			if (!noneChecked || (od.getItemCompleted() != null)) {
				noneChecked = false;
			}
		}
		
		if (allChecked) {
			((CheckBox)treeTable.getItem(orderDetail.getOrderId()).getItemProperty("Name / Product").getValue()).setValue(true);
		} else {
			((CheckBox)treeTable.getItem(orderDetail.getOrderId()).getItemProperty("Name / Product").getValue()).setValue(false);
		}
/*		if (noneChecked) {
			((CheckBox)treeTable.getItem(orderDetail.getOrderId()).getItemProperty("Name / Product").getValue()).setValue(false);
		}*/
	}

}
