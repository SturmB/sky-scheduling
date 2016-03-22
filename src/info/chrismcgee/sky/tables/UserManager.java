package info.chrismcgee.sky.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import info.chrismcgee.dbutil.ConnectionManager;
import info.chrismcgee.dbutil.DBName;
import info.chrismcgee.sky.scheduling.beans.User;

public class UserManager {

	private final static Logger logger = Logger.getLogger(OrderDetailManager.class.getName()); // Logging!
	private static ResultSet rs = null;

	/**
	 * Standard retrieval method that gets a single User from the database.
	 * 
	 * @param userName String of the user's name.
	 * @return a single User bean.
	 * @throws SQLException
	 */
	public static User getRow(String userName) throws SQLException {

		Connection conn = ConnectionManager.getInstance().getConnection(DBName.LOGIN);
		
		// The SQL statement that will be run on the DBMS.
		String sql = "SELECT * FROM Login WHERE user_name = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){

			// Put just the user's name into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setString(1, userName);
			// Execute the SQL statement and store the results in a ResultSet.
			rs = stmt.executeQuery();
			
			
			// If there was a result returned from the query, create a User bean from it.
			if (rs.next()) {
				User bean = new User();
				bean.setUserName(rs.getString("user_name"));
				bean.setHashPass(rs.getString("hashed_pass"));
				bean.setAccessFlags(rs.getInt("access_level"));
				return bean;
			} else { // No results returned from the query.
				return null; // Just return null, which indicates a failure.
			}
			
		} catch (SQLException e) { // If there was an error trying to read from the database table.
			 logger.log(Level.SEVERE, "Error trying to read from the database table", e);
			 return null; // Again, just return null, which indicates a failure.
		} finally {
			if (rs != null) {
				rs.close(); // Make sure to close the ResultSet in case it isn't null, no matter if an error occurred or not.
			}
		}
	}

	/**
	 * Standard "insert" method for adding a new login to the User table.
	 * 
	 * @param bean The User bean that contains the necessary info for adding to the table.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean insert(User bean) throws Exception {

		Connection conn = ConnectionManager.getInstance().getConnection(DBName.LOGIN);

		// The SQL statement that will be run on the DBMS.
		String sql = "INSERT INTO Login ("
				+ "user_name, "
				+ "hashed_pass, "
				+ "access_level) "
				+ "VALUES (?, ?, ?)";

		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				){
			
			// Put all of the bean's details into the PreparedStatement object, which will be combined with the SQL statement.
			stmt.setString(1, bean.getUserName());
			stmt.setString(2, bean.getHashPass());
			stmt.setInt(3, bean.getAccessFlags());
			stmt.executeUpdate();
						
		} catch (SQLException e) { // If there was an error trying to update the database table.
			logger.log(Level.SEVERE, "Error trying to update the database table.", e);
			return false;
		}
		return true; // If everything worked fine, then return true, indicating success!
	}

	/**
	 * Standard "update" method for updating a login in the User table.
	 * 
	 * @param bean The User bean that contains the new info for updating the table.
	 * @return boolean True if the procedure was successful; false if not.
	 * @throws Exception
	 */
	public static boolean update(User bean) throws Exception {

		Connection conn = ConnectionManager.getInstance().getConnection(DBName.LOGIN);

		// The SQL statement that will be run on the DBMS.
		String sql =
				"UPDATE Login SET " +
				"user_name = ?, "
				+ "hashed_pass = ?, "
				+ "access_level = ? "
				+ "WHERE user_name = ?";
		
		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put all of the bean's details into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setString(1, bean.getUserName());
			stmt.setString(2, bean.getHashPass());
			stmt.setInt(3, bean.getAccessFlags());
			stmt.setString(4, bean.getUserName());
			
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
	 * Standard "delete" method for deleting a login from the User table.
	 * 
	 * @param userName The username, which is the primary key (id) of the job.
	 * @return boolean True if the procedure was successful; false if not.
	 */
	public static boolean delete(String userName) {

		Connection conn = ConnectionManager.getInstance().getConnection(DBName.LOGIN);

		// The SQL statement that will be run on the DBMS.
		String sql = "DELETE FROM Login WHERE user_name = ?";

		try ( // Try with resources block (requires Java 7+) for a prepared statement.
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			// Put just the username, which is the primary key (id) of the login, into the PreparedStatement object,
			// which will be combined with the SQL statement.
			stmt.setString(1, userName);
			
			int affected = stmt.executeUpdate(); // Store how many lines were affected by this query.
			
			if (affected == 1) { // Hopefully, only 1 line was deleted.
				return true; // If everything worked fine, then return true, indicating success!
			}

			return false; // In case there was a problem and the login was not removed from the database table.
			
		} catch (SQLException e) { // If there was an error trying to delete from the database table.
			 logger.log(Level.SEVERE, "Error trying to delete from the database table.", e);
			 return false;
		}
		
	}

}
