package info.chrismcgee.dbutil;

public enum DBName {
	
	// The names of the databases available.
	JOB_ORDERS("job_orders_2014"), LOGIN("logins");

	private String value; // Stores the database name we're working with.
	
	/**
	 * When/if this class is instantiated, this constructor stores the database name
	 * 
	 * @param value
	 */
	private DBName(String value) {
		this.value = value;
	}

	/**
	 * Standard getter.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Standard setter.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
