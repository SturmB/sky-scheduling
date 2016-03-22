package info.chrismcgee.sky.tables;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TreeTable;

import info.chrismcgee.dbutil.ConnectionManager;
import info.chrismcgee.dbutil.DBName;
import info.chrismcgee.sky.components.DateManager;
import info.chrismcgee.sky.enums.PrintingCompany;
import info.chrismcgee.sky.scheduling.beans.Job;
import info.chrismcgee.sky.scheduling.beans.OrderDetail;

/**
 * @author Marketing
 *
 * This class is a set of static methods that interface with the database table "Job".
 * It is mostly all CRUD methods.
 */
public class JobManager {

	private static ResultSet rs = null; // The result set from a successful SQL query.
	private final static Logger logger = Logger.getLogger(JobManager.class.getName());

	/**
	 * Standard retrieval method that gets a single Job from the database along with its OrderDetails.
	 * 
	 * @param jobId String of the order number used to look up the job.
	 * @return a single Job bean.
	 * @throws SQLException
	 */
	public static Job getRow(String jobId) throws SQLException {
		
		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT * FROM Job WHERE job_id = ?";
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		logger.log(Level.INFO, "Catalog: " + conn.getCatalog());
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put just the order number into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setString(1, jobId);
			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			
			// If there was a result returned from the query, create a Job bean from it.
			if (rs.next()) {
				Job bean = new Job();
				List<OrderDetail> detailList = OrderDetailManager.getRows(jobId);
				bean.setShipDate(rs.getDate("ship_date"));
				bean.setJobId(jobId);
				bean.setCustomerName(rs.getString("customer_name"));
				bean.setCustomerPO(rs.getString("customer_po"));
				bean.setProofSpecDate(rs.getTimestamp("proof_spec_date"));
				bean.setJobCompleted(rs.getTimestamp("job_completed"));
				bean.setPrintingCompany(PrintingCompany.getPrintingCompany(rs.getInt("printing_company")));
				bean.setOverruns(rs.getBoolean("overruns"));
				bean.setOrderDetailList(detailList);
				return bean;
			} else { // No results returned from the query.
				return null; // Just return null, which indicates a failure.
			}
			
		} catch (SQLException e) { // If there was an error trying to read from the database table.
			logger.log(Level.SEVERE, "Error trying to read from the database table.", e);
			return null; // Again, just return null, which indicates a failure.
		} finally {
			if (rs != null) {
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
			}
		}
	}

	/**
	 * Standard "insert" method for adding a new job to the Job table.
	 * 
	 * @param bean The Job bean that contains the necessary info for adding to the table.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean insert(Job bean) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.
		
		// The SQL statement that will be run on the DBMS.
		String sql = "INSERT INTO Job ("
				+ "ship_date, "
				+ "job_id, "
				+ "customer_name, "
				+ "customer_po, "
				+ "proof_spec_date, "
				+ "job_completed, "
				+ "printing_company, "
				+ "overruns) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				){
			
			// Put all of the bean's details into the PreparedStatement object, which will be combined with the SQL statement.
			stmt.setDate(1, bean.getShipDate());
			stmt.setString(2, bean.getJobId());
			stmt.setString(3, bean.getCustomerName());
			stmt.setString(4, bean.getCustomerPO());
			stmt.setTimestamp(5, bean.getProofSpecDate());
			stmt.setTimestamp(6, bean.getJobCompleted());
			stmt.setInt(7, bean.getPrintingCompany().getValue());
			stmt.setBoolean(8, bean.isOverruns());
			stmt.executeUpdate();
						
		} catch (SQLException e) { // If there was an error trying to update the database table.
			logger.log(Level.SEVERE, "Error trying to update the database table.", e);
			return false;
		}
		
		// Insert all of the Job's OrderDetail items into its table.
		for (OrderDetail od : bean.getOrderDetailList()) {
			OrderDetailManager.insert(od);
		}
		
		return true; // If everything worked fine, then return true, indicating success!
	}

	/**
	 * Standard "update" method for updating a job in the Job table.
	 * 
	 * @param bean The Job bean that contains the new info for updating the table.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean update(Job bean) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql =
				"UPDATE Job SET "
				+ "ship_date = ?, "
				+ "customer_name = ?, "
				+ "customer_po = ?, "
				+ "proof_spec_date = ?, "
				+ "job_completed = ?, "
				+ "printing_company = ?, "
				+ "overruns = ? "
				+ "WHERE job_id = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put all of the bean's details into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, bean.getShipDate());
			stmt.setString(2, bean.getCustomerName());
			stmt.setString(3, bean.getCustomerPO());
			stmt.setTimestamp(4, bean.getProofSpecDate());
			stmt.setTimestamp(5, bean.getJobCompleted());
			stmt.setInt(6, bean.getPrintingCompany().getValue());
			stmt.setBoolean(7, bean.isOverruns());
			stmt.setString(8, bean.getJobId());
			
			int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.

			// Now also update the Job's OrderDetail items in its table.
			for (OrderDetail od : bean.getOrderDetailList()) {
				OrderDetailManager.update(od);
			}

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
	 * Set a Job as being completed.
	 * 
	 * @param bean The Job bean that contains the new completion date for updating the table.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws SQLException
	 */
	public static boolean setCompleted(Job bean) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql =
				"UPDATE Job "
				+ "SET job_completed = ? "
				+ "WHERE job_id = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put just the timestamp of when the job was completed into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setTimestamp(1, bean.getJobCompleted());
			stmt.setString(2, bean.getJobId());
			
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
	 * Only 1 job will be deleted, but all of its associated items will also be deleted.
	 * 
	 * @param jobId The order number, which is the primary key (id) of the job.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean delete(String jobId) throws Exception {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql = "DELETE FROM Job WHERE job_id = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// First, delete all of the job's associated items and, if successful,
			// continue deleting the job itself.
			if (OrderDetailManager.delete(jobId))
			{
				// Put just the order number, which is the primary key (id) of the job, into the PreparedStatement object,
				// which will be combined with the SQL statement.
				stmt.setString(1, jobId);
				
				int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.
				
				if (affected == 1) { // Hopefully, only 1 line was deleted.
					return true; // If everything worked fine, then return true, indicating success!
				}
			}
			return false; // In case there was a problem and the job was not removed from the database table.
			
		} catch (SQLException e) { // If there was an error trying to delete from the database table.
			logger.log(Level.SEVERE, "Error trying to delete from the database table.", e);
			return false;
		}
		
	}

	/**
	 * Gets all jobs from the database table that ship on a specified date.
	 * 
	 * @param theDate LocalDate object of the ship date.
	 * @return List of Sub-Lists. The main List is just two items long.
	 * The first Sub-List is of all jobs. The second Sub-List only contains incomplete jobs.
	 * @throws SQLException
	 */
	public static List<List<Job>> getAllJobsByDate(LocalDate theDate) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT ship_date, "
				+ "job_id, "
				+ "customer_name, "
				+ "proof_spec_date, "
				+ "job_completed, "
				+ "product_id, "
				+ "product_detail, "
				+ "num_colors, "
				+ "quantity, "
				+ "item_completed "
				+ ""
				+ "FROM Job AS j "
				+ "JOIN OrderDetail AS o "
				+ "ON j.job_id = o.order_id "
				+ "WHERE ship_date = ? "
				+ "ORDER BY proof_spec_date";
		
		// Convert the LocalDate object to a SQL Date.
		java.sql.Date sqlDate = java.sql.Date.valueOf(DateManager.getSqlFormattedDate(theDate));
		List<Job> jobList = new ArrayList<Job>(); // Prepare a List for all of the Jobs.
		List<Job> incompletedJobsList = new ArrayList<Job>(); // Prepare a List for only the incomplete Jobs.
		List<List<Job>> jobArray = new ArrayList<List<Job>>(2); // Prepare a two-item List to hold the previous two.
		String lastJob = ""; // Start with an empty variable so the if statement will fire at least once.
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// All we need is the date, so put that into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, sqlDate);

			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			// Go through each result in the ResultSet (row) and create a Job bean from it,
			// then add each bean to the bean List.
			while (rs.next()) {
				// If the next item in the result set has the same job number as the last one,
				// skip it and move on to the next one.
				String jobId = rs.getString("job_id");
				if (!jobId.equals(lastJob)) {
					// If this is a new job, then reset the "lastJob" tracker
					// for the next item once this iteration in the loop is done.
					lastJob = jobId;
					// Get all of the OrderDetail items for this Job so it can be added
					// to the Job bean a little further down.
					List<OrderDetail> detailList = OrderDetailManager.getRows(jobId);
					Job bean = new Job(); // Prime the Job bean.
					
					bean.setShipDate(rs.getDate("ship_date"));
					bean.setJobId(jobId);
					bean.setCustomerName(rs.getString("customer_name"));
					bean.setProofSpecDate(rs.getTimestamp("proof_spec_date"));
					bean.setJobCompleted(rs.getTimestamp("job_completed"));
					bean.setOrderDetailList(detailList);
					jobList.add(bean); // Add this Job bean to the main List of all jobs for the day.
					
					if (bean.getJobCompleted() == null) {
						// If the job is also incomplete, then add it to the incomplete List, too.
						incompletedJobsList.add(bean);
					}
				}
			}
			
			// Once all of the results have been sifted through, add both the "full job list"
			// and the "incompleted jobs list" to the encompassing List.
			jobArray.add(jobList);
			jobArray.add(incompletedJobsList);
			
			logger.log(Level.CONFIG, "Length of all jobs (0): " + jobArray.get(0));
			logger.log(Level.CONFIG, "Length of incomplete jobs (1): " + jobArray.get(1));
			
			return jobArray;
			
		} catch (SQLException err) { // If there was an error trying to read from the database table(s).
			logger.log(Level.SEVERE, "SQL Exception occurred when retreiving data from database.", err);
			return null; // Just return null, which indicates a failure.
		} finally {
			if (rs != null) {
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
			}
		}
	}

	/**
	 * Gets all jobs from the database table that ship on a specified date.
	 * 
	 * @param theDate LocalDate object of the ship date.
	 * @return List of all Jobs.
	 * @throws SQLException
	 */
	public static List<Job> getJobsByDate(LocalDate theDate) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.
		
		logger.log(Level.INFO, "Getting Jobs by Date: " + theDate.toString());

		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT ship_date, "
				+ "job_id, "
				+ "customer_name, "
				+ "proof_spec_date, "
				+ "job_completed, "
				+ "product_id, "
				+ "product_detail, "
				+ "num_colors, "
				+ "quantity, "
				+ "item_completed "
				+ ""
				+ "FROM Job AS j "
				+ "JOIN OrderDetail AS o "
				+ "ON j.job_id = o.order_id "
				+ "WHERE ship_date = ? "
				+ "ORDER BY proof_spec_date";
		
		// Convert the LocalDate object to a SQL Date.
		java.sql.Date sqlDate = java.sql.Date.valueOf(DateManager.getSqlFormattedDate(theDate));
		List<Job> jobList = new ArrayList<Job>(); // Prepare a List for all of the Jobs.
		String lastJob = ""; // Start with an empty variable so the if statement will fire at least once.
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// All we need is the date, so put that into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, sqlDate);

			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			// Go through each result in the ResultSet (row) and create a Job bean from it,
			// then add each bean to the bean List.
			while (rs.next()) {
				// If the next item in the result set has the same job number as the last one,
				// skip it and move on to the next one.
				String jobId = rs.getString("job_id");
				if (!jobId.equals(lastJob)) {
					// If this is a new job, then reset the "lastJob" tracker
					// for the next item once this iteration in the loop is done.
					lastJob = jobId;
					// Get all of the OrderDetail items for this Job so it can be added
					// to the Job bean a little further down.
					List<OrderDetail> detailList = OrderDetailManager.getRows(jobId);
					Job bean = new Job(); // Prime the Job bean.
					
					bean.setShipDate(rs.getDate("ship_date"));
					bean.setJobId(jobId);
					bean.setCustomerName(rs.getString("customer_name"));
					bean.setProofSpecDate(rs.getTimestamp("proof_spec_date"));
					bean.setJobCompleted(rs.getTimestamp("job_completed"));
					bean.setOrderDetailList(detailList);
					jobList.add(bean); // Add this Job bean to the main List of all jobs for the day.
				}
			}
			
			logger.log(Level.INFO, "Job list's first hit: " + jobList.get(0).getCustomerName());
			
			return jobList;
			
		} catch (SQLException err) { // If there was an error trying to read from the database table(s).
			logger.log(Level.SEVERE, "SQL Exception occurred when retreiving data from database.", err);
			return null; // Just return null, which indicates a failure.
		} finally {
			if (rs != null) {
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
			}
		}
	}

	/**
	 * Checks to see if any Jobs have a Ship Date equal to the given Date.
	 * 
	 * @param date LocalDate object of the day to see if any Jobs exist.
	 * @return A boolean that is True if there is at least 1 Job that ships on the given Date.
	 * @throws SQLException
	 */
	public static boolean jobsExist (LocalDate date) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT TOP 1 job_id "
				+ "FROM Job "
				+ "WHERE ship_date = ?";

		// Convert the LocalDate object to a SQL Date.
		java.sql.Date sqlDate = java.sql.Date.valueOf(DateManager.getSqlFormattedDate(date));
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// All we need is the date, so put that into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, sqlDate);

			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			// If at least one row was returned, then return a boolean true.
			if (rs.next()) {
				return true;
			}
			
			// Otherwise, return a boolean false, indicating there are NO jobs on that date.
			return false;
			
		} catch (SQLException err) { // If there was an error trying to read from the database table.
			logger.log(Level.SEVERE, "Error attempting to retrieve TOP 1 job_id from database.", err);
			return false; // Just return null, which indicates a failure.
		} finally {
			if (rs != null)
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
		}
	}

	/**
	 * Returns the number of Jobs that list a Ship Date of the given Date.
	 * 
	 * @param date The Date for which to see if any Jobs have a Ship Date listed.
	 * @return An integer representing the number of Jobs that ship on that date.
	 * @throws SQLException
	 */
	public static int getNumJobs (Date date) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT COUNT(*) AS 'num_jobs' "
				+ "FROM Job "
				+ "WHERE ship_date = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// All we need is the date, so put that into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, date);
			
			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();

			int numJobs = 0; // Assume that there are no results to start with.

			if (rs.next()) {
				// If a result was returned, then get that number of Jobs and return it.
				numJobs = rs.getInt("num_jobs");
				logger.log(Level.CONFIG, "Number of Jobs for the day: " + numJobs);
				return numJobs;
			}
			
			return 0; // If there were no results, then assume 0 Jobs ship on that date.
			
		} catch (SQLException err) { // If there was an error trying to read from the database table.
			logger.log(Level.SEVERE, "Error attempting to get the total number of jobs for a given day.", err);
			return 0; // Return 0, which indicates either failure or no results.
		} finally {
			if (rs != null)
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
		}
	}

	/**
	 * Returns the number of completed Jobs that list a Ship Date of the given Date.
	 * 
	 * @param date The Date for which to see if any completed Jobs have a Ship Date listed.
	 * @return An integer representing the number of completed Jobs that ship on that date.
	 * @throws SQLException
	 */
	public static int getCompletedJobs (Date date) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT COUNT(*) AS 'completed_jobs' "
				+ "FROM Job "
				+ "WHERE ship_date = ? "
				+ "AND job_completed IS NOT NULL";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// All we need is the date, so put that into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, date);

			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				// If a result was returned, then get that number of completed Jobs and return it.
				return rs.getInt("completed_jobs");
			}
			
			return 0; // If there were no results, then assume 0 completed Jobs ship on that date.

		} catch (SQLException err) { // If there was an error trying to read from the database table.
			logger.log(Level.SEVERE, "Error attempting to get the total number of completed jobs for a given day.", err);
			return 0; // Return 0, which indicates either failure or no results.
		} finally {
			if (rs != null)
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
		}
	}

	/**
	 * Returns the number of Jobs that list a Ship Date between two given Dates.
	 * 
	 * @param weekDates An Array of two Dates between which to see if any Jobs have a Ship Date listed.
	 * @return An integer representing the number of Jobs that ship between those dates.
	 * @throws SQLException
	 */
	public static int weeklyNumJobs(LocalDate[] weekDates) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT COUNT(*) AS 'weekly_num_jobs' "
				+ "FROM Job "
				+ "WHERE ship_date BETWEEN ? AND ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// We need two dates, so put them from the given Array into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, DateManager.localDateToSqlDate(weekDates[0]));
			stmt.setDate(2, DateManager.localDateToSqlDate(weekDates[1]));

			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				// If a result was returned, then get that number of Jobs and return it.
				return rs.getInt("weekly_num_jobs");
			}
			
			return 0; // If there were no results, then assume 0 Jobs ship between those dates.

		} catch (SQLException err) { // If there was an error trying to read from the database table.
			logger.log(Level.SEVERE, "Error attempting to get the total number of jobs for a given week.", err);
			return 0; // Return 0, which indicates either failure or no results.
		} finally {
			if (rs != null)
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
		}
	}

	/**
	 * Returns the number of completed Jobs that list a Ship Date between two given Dates.
	 * 
	 * @param weekDates An Array of two Dates between which to see if any completed Jobs have a Ship Date listed.
	 * @return An integer representing the number of completed Jobs that ship between those dates.
	 * @throws SQLException
	 */
	public static int weeklyCompletedJobs(LocalDate[] weekDates) throws SQLException {
		
		Connection conn = ConnectionManager.getInstance().getConnection(DBName.JOB_ORDERS); // The connection to the database.

		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT COUNT(*) AS 'weekly_completed_jobs' "
				+ "FROM Job "
				+ "WHERE ship_date BETWEEN ? AND ? "
				+ "AND job_completed IS NOT NULL";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// We need two dates, so put them from the given Array into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setDate(1, DateManager.localDateToSqlDate(weekDates[0]));
			stmt.setDate(2, DateManager.localDateToSqlDate(weekDates[1]));

			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				// If a result was returned, then get that number of Jobs and return it.
				return rs.getInt("weekly_completed_jobs");
			}
			
			return 0; // If there were no results, then assume 0 completed Jobs ship between those dates.

		} catch (SQLException err) { // If there was an error trying to read from the database table.
			logger.log(Level.SEVERE, "Error attempting to get the total number of completed jobs for a given week.", err);
			return 0; // Return 0, which indicates either failure or no results.
		} finally {
			if (rs != null)
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
		}
	}

	public static void jobCheckboxChanged(ValueChangeEvent event, Job job, TreeTable treeTable) {
		
		// True if the change was an un-checked box to a checked box, false if the other way 'round.
		boolean boxChecked = (boolean)event.getProperty().getValue();
		// If the box was checked, then this variable stores the current timestamp. Otherwise, it just stores null.
		Timestamp completedStamp = boxChecked ? new Timestamp(System.currentTimeMillis()) : null;
		
		logger.log(Level.INFO, "Job " + job.getJobId() + " changed; Storing timestamp " + completedStamp + " to the Job object.");
		// TODO Here is where the code goes for setting the Job bean's completed property,
		// then updating the database.
		job.setJobCompleted(completedStamp);
		
		// Also set the completed timestamp property for each of the job's OrderDetail items if they haven't already been set.
		for (OrderDetail orderDetail : job.getOrderDetailList()) {
			
			logger.log(Level.INFO, "Setting the completion timestamp for the OrderDetail " + orderDetail.getProductId() + " to " + System.currentTimeMillis());
			
			// Set the OrderDetail's checkbox to the same as the Job.
			((CheckBox)treeTable.getItem(orderDetail.getId()).getItemProperty("Name / Product").getValue()).setValue(boxChecked);
			// If this changes the checkbox state, then the listener will pick up on it
			// and fire the OrderDetail-specific method for updating its bean & the database.
		}
		
	}

}
