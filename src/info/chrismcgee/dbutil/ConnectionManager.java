package info.chrismcgee.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

	// For logging!
	private final static Logger logger = Logger.getLogger(ConnectionManager.class.getName());

	// The single and only instance of the connection manager; starts off as null.
	private static ConnectionManager instance = null;

	// These constants are for connecting to the database.
	// Username & Password are obvious.
	// The Connection String constants define the address to which jdbc will be connecting,
	//   which includes the database name.
	private final String USERNAME = "web";
	private final String PASSWORD = "web";
	private final String H_CONN_STRING = "jdbc:hsqldb:data/";
	private final String M_CONN_STRING = "jdbc:mysql://apache.local/";
	private final String S_CONN_STRING = "jdbc:sqlserver://192.168.0.248;databaseName=";

	// Now set the actual database type that we will be using,
	//   with a custom-defined enum.
	private DBType dbType = DBType.MSSQL;

	// The actual connection instance starts life as null (doesn't exist).
	private Connection conn = null;

	// The constructor for the ConnectionManager remains empty.
	//   (Although I don't really know why.)
	public ConnectionManager() {
	}

	/**
	 * This static method gets a singleton instance of the ConnectionManager.
	 * 
	 * @return	ConnectionManager
	 */
	public static ConnectionManager getInstance() {
		
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	/**
	 * A simple method that defines which type of database this
	 * instance of the connection will use.
	 * 
	 * @param dbType	The type of database enum
	 */
	public void setDBType(DBType dbType) {
		
		this.dbType = dbType;
	}

	/**
	 * This method opens the connection to the database, depending on which
	 * of the DBType enums is selected above.
	 * 
	 * @return boolean	Whether or not the connection was made to the database.
	 * @throws ClassNotFoundException If the class cannot be found for the SQLServerDriver.
	 */
	private boolean openConnection(DBName dbName) throws ClassNotFoundException {
		
		// For Java versions below 7.
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		// If the above line throws a "ClassNotFoundException", refer to
		// http://stackoverflow.com/questions/19626808/getting-a-noclassdeffounderror-after-installing-vaadin/19630339#19630339
		// TL;DR: Just add the JDBC .jar file to the WEB-INF/lib folder in the Deployment Assembly of this project's Properties.

		logger.log(Level.INFO, "Opening connection.");
		
		try {
			switch (dbType) { // Check the DBType...
			// and open a connection to the corresponding type of database.
			//   Returns true if the DBType corresponds correctly
			//   and if the connection was made. 
			case MYSQL:
				conn = DriverManager.getConnection(M_CONN_STRING + dbName.getValue(), USERNAME, PASSWORD);
				return true;

			case HSQLDB:
				conn = DriverManager.getConnection(H_CONN_STRING + dbName.getValue(), USERNAME, PASSWORD);
				return true;
				
			case MSSQL:
				conn = DriverManager.getConnection(S_CONN_STRING + dbName.getValue(), USERNAME, PASSWORD);
				return true;

			default: // If the wrong type of enum was specified, return false.
				return false;
			}
		}
		catch (SQLException e) {
			// If there was an error connecting to the database,
			//   log it and return false.
			logger.log(Level.SEVERE, "Error getting a connection to the database", e);
			return false;
		}

	}

	/**
	 * Gets a singleton connection to the database
	 * 
	 * @return	Connection
	 */
	public Connection getConnection(DBName dbName) {
		
		logger.log(Level.CONFIG, "Getting connection...");
		
		if (conn == null) { // If a connection does not yet exist...
			logger.log(Level.INFO, "No connection yet; creating one.");
			try {
				if (openConnection(dbName)) { // Try to open a connection. And if that succeeds...
					logger.log(Level.INFO, "Connection opened.");
					return conn; // Return the connection instance.
				} else { // Otherwise, return null.
					logger.log(Level.WARNING, "Connection to the database failed.");
					return null;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, "ClassNotFound exception", e);
				return null;
			}
		}
		// If a connection already exists, just return it. (Singleton)
		return conn;
	}
	
	/**
	 * Simply closes the connection to the database.
	 */
	public void close() {
		
		if (conn != null) {
			logger.log(Level.INFO, "Closing connection");
			try { // Attempt to close the connection and set its variable to null.
				conn.close();
				conn = null;
				logger.log(Level.INFO, "Connection closed.");
			} catch (Exception e) {
				// If an exception occurs when trying to close the connection, do nothing (except log it).
				logger.log(Level.SEVERE, "Exception when trying to CLOSE the database connection.", e);
			}
		}
	}

	/**
	 * A static method that processes SQL exceptions.
	 * It is customizable by being in a static method and could be very handy.
	 * 
	 * @param e	The SQL Exception object that was thrown.
	 */
	public static void processException(SQLException e) {
		
		logger.log(Level.SEVERE, "Error message: " + e.getMessage());
		logger.log(Level.SEVERE, "Error code: " + e.getErrorCode());
		logger.log(Level.SEVERE, "SQL state: " + e.getSQLState());
		
	}

}
