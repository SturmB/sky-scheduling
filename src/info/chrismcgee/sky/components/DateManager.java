package info.chrismcgee.sky.components;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import info.chrismcgee.sky.tables.JobManager;

public class DateManager {

	// Set the constants; variables that never vary (change).
	private static final java.util.Date[] EMPTY_DATE_ARRAY = new java.util.Date[0];
	public static final LocalDate HOLD_DATE = LocalDate.of(9999, 12, 31);
	public static final LocalDate PROOF_DATE = LocalDate.of(9999, 12, 30);
	// Global variables for comparing dates.
	private static LocalDate holdDateComparator = LocalDate.now();
	private static LocalDate proofDateComparator = LocalDate.now();
	private static LocalDate today = LocalDate.now();
	// Formatters that are used in several methods.
	private static DateTimeFormatter displayFmt = DateTimeFormatter.ofPattern("MM/dd/yy");
	private static DateTimeFormatter sqlFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	// Logging!
	private final static Logger logger = Logger.getLogger(JobManager.class.getName());

	
	/**
	 * Turns a date String into a LocalDate object.
	 * 
	 * @param dateStr A parse-able String representation of a date.
	 * @return the equivalent LocalDate object.
	 */
	public static LocalDate getLocalDate(String dateStr) {
		// Three possibilities: "On Hold", "Proofs", and an actual date.
		if (dateStr.equals("On Hold")) return HOLD_DATE;
		if (dateStr.equals("Proofs")) return PROOF_DATE;
		return LocalDate.parse(dateStr, displayFmt);
	}

	/**
	 * Turns a date String into a SQL Date object.
	 * 
	 * @param dateStr A parse-able String representation of a date.
	 * @return the equivalent SQL Date object.
	 */
	public static Date getSqlDate(String dateStr) {
		logger.log(Level.CONFIG, "getSqlDate (DateManager) with a Date String of " + dateStr);
		
		return Date.valueOf(dateStr);
	}

	/**
	 * Turns a LocalDate object into a SQL Date object.
	 * 
	 * @param lDate A LocalDate object of a particular date.
	 * @return the equivalent SQL Date object.
	 */
	public static Date localDateToSqlDate(LocalDate lDate) {
		String sDate = getSqlFormattedDate(lDate);
		return getSqlDate(sDate);
	}

	/**
	 * Takes a LocalDate object and converts it into a more human-readable String.
	 * 
	 * @param convertDate The LocalDate object to convert.
	 * @return Formatted String of LocalDate object.
	 */
	public static String getDisplayDate(LocalDate convertDate) {
		return convertDate.format(displayFmt);
	}

	/**
	 * Takes a SQL Date object and converts it into a more human-readable String.
	 * 
	 * @param convertDate The SQL Date object to convert.
	 * @return Formatted String of LocalDate object.
	 */
	public static String getDisplayDate(Date convertDate) {
		return getDisplayDate(convertDate.toLocalDate());
	}

	/**
	 * Turns a LocalDate object into a String that is formatted specifically for SQL usage.
	 * 
	 * @param convertDate The LocalDate object to convert.
	 * @return A SQL-formatted String of the date.
	 */
	public static String getSqlFormattedDate(LocalDate convertDate) {
		return convertDate.format(sqlFmt);
	}

	/**
	 * A simple method that returns a String representation of today's date.
	 * 
	 * @return Today's date as a human-readable String.
	 */
	public static String getToday() {
		return getDisplayDate(today);
	}

	/**
	 * Takes a String representation of a day and returns the next calendar day as a String.
	 * 
	 * @param oldDateStr The original String representation of the Date.
	 * @return String representation of the following day.
	 */
	public static String getNextDay(String oldDateStr) {
		LocalDate oldDate = getLocalDate(oldDateStr);
		LocalDate newDate = oldDate.plusDays(1);
		String newDateStr = getDisplayDate(newDate);
		
		return newDateStr;
	}

	/**
	 * Takes a String representation of a day and returns the same day in the next calendar month as a String.
	 * 
	 * @param oldDateStr The original String representation of the Date.
	 * @return String representation of the same day in the following month.
	 */
	public static String getNextMonth(String oldDateStr) {
		LocalDate oldDate = getLocalDate(oldDateStr);
		LocalDate newDate = oldDate.plusMonths(1);
		String newDateStr = getDisplayDate(newDate);
		
		return newDateStr;
	}

	/**
	 * Takes a String representation of a day and returns the same day in the next calendar year as a String.
	 * 
	 * @param oldDateStr The original String representation of the Date.
	 * @return String representation of the same day in the following year.
	 */
	public static String getNextYear(String oldDateStr) {
		LocalDate oldDate = getLocalDate(oldDateStr);
		LocalDate newDate = oldDate.plusYears(1);
		String newDateStr = getDisplayDate(newDate);
		
		return newDateStr;
	}

	/**
	 * Takes a String representation of a day and returns the previous calendar day as a String.
	 * 
	 * @param oldDateStr The original String representation of the Date.
	 * @return String representation of the preceding day.
	 */
	public static String getPreviousDay(String oldDateStr) {
		LocalDate oldDate = getLocalDate(oldDateStr);
		LocalDate newDate = oldDate.minusDays(1);
		String newDateStr = getDisplayDate(newDate);
		
		return newDateStr;
	}

	/**
	 * Takes a String representation of a day and returns the same day in the previous calendar month as a String.
	 * 
	 * @param oldDateStr The original String representation of the Date.
	 * @return String representation of the same day in the preceding month.
	 */
	public static String getPreviousMonth(String oldDateStr) {
		LocalDate oldDate = getLocalDate(oldDateStr);
		LocalDate newDate = oldDate.minusMonths(1);
		String newDateStr = getDisplayDate(newDate);
		
		return newDateStr;
	}

	/**
	 * Takes a String representation of a day and returns the same day in the previous calendar year as a String.
	 * 
	 * @param oldDateStr The original String representation of the Date.
	 * @return String representation of the same day in the preceding year.
	 */
	public static String getPreviousYear(String oldDateStr) {
		LocalDate oldDate = getLocalDate(oldDateStr);
		LocalDate newDate = oldDate.minusYears(1);
		String newDateStr = getDisplayDate(newDate);
		
		return newDateStr;
	}

	/**
	 * Converts a java.util.Date object into a more human-readable String.
	 * 
	 * @param jDate The java.util.Date object to convert.
	 * @return A String representation of that date.
	 */
	public static String jDateToString(java.util.Date jDate) {
		LocalDate convertedDate = jDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return getDisplayDate(convertedDate);
	}

	/**
	 * Converts a String representation of a Date into a java.util.Date object
	 * 
	 * @param stringDate A Date in a String format.
	 * @return A java.util.Date object of that Date.
	 */
	public static java.util.Date stringToJDate (String stringDate) {
		return java.util.Date.from(getLocalDate(stringDate).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Converts a String representation of a Date into a SQL Date object
	 * 
	 * @param stringDate A Date in a String format.
	 * @return A SQL Date object of that Date.
	 */
	public static Date usDateStringToSqlDate (String stringDate) {
		return localDateToSqlDate(getLocalDate(stringDate));
	}

	/**
	 * Takes two java.util.Date objects and returns an array of all the dates between them. (inclusive?)
	 * 
	 * @param startDate The earlier of two java.util.Date objects.
	 * @param endDate The later of two java.util.Date objects.
	 * @return An Array of all Dates between the two given.
	 */
	public static java.util.Date[] getDateRange(java.util.Date startDate, java.util.Date endDate) {
		// Convert the java.util.Date objects to Joda Time's LocalDate objects
		// since they are much easier to deal with.
		LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		// Prepare a List to contain the dates, which will be converted to an Array later.
		List<java.util.Date> dates = new ArrayList<java.util.Date>();
		
		long days = ChronoUnit.DAYS.between(startLocalDate, endLocalDate); // Gets the number of days between the dates.
		for (int i = 0; i <= days; i++) {
			// Add each date to the List object. 
			LocalDate d = startLocalDate.plusDays(i);
			dates.add(java.util.Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		}
		
		// Convert the List to an Array and return it.
		return dates.toArray(EMPTY_DATE_ARRAY);
	}

	/**
	 * Simply checks to see if a given Date matches the special "On Hold" date specified in the field list.
	 * 
	 * @param sqlDate The SQL Date object to check.
	 * @return boolean: true if the Date is the special "On Hold" date; false if not.
	 */
	public static boolean isOnHoldDate (Date sqlDate) {
		holdDateComparator = sqlDate.toLocalDate();
		return holdDateComparator.equals(HOLD_DATE);
	}

	/**
	 * Simply checks to see if a given Date matches the special "Proofing" date specified in the field list.
	 * 
	 * @param sqlDate The SQL Date object to check.
	 * @return boolean: true if the Date is the special "Proofing" date; false if not.
	 */
	public static boolean isProofingDate (Date sqlDate) {
		proofDateComparator = sqlDate.toLocalDate();
		return proofDateComparator.equals(PROOF_DATE);
	}

	/**
	 * Takes a single date and returns an Array of two dates
	 * that represent the beginning and end of the week
	 * to which that single date belongs.
	 * 
	 * @param date The LocalDate object from which to derive its week.
	 * @return An Array of two dates--the beginning and end of the week from which the original date was taken.
	 */
	public static LocalDate[] getWeek (LocalDate date) {
		
		// Prime the array.
		LocalDate[] week = new LocalDate[2];
		
		// Find out how many days away from the first day of the week (0) that the given date is.
		int delta = date.getDayOfWeek().getValue() == 7 ? 0 : date.getDayOfWeek().getValue();
		logger.log(Level.CONFIG, "Day of week integer is " + delta);
		// Change the date variable to the first date of its week.
		date = date.minusDays(delta);

		for (int i = 0; i < 2; i++) {
			// Put in the first day and last day of the week into the Array.
			week[i] = date;
			date = date.plusDays(6);
		}
		
		logger.log(Level.CONFIG, "Week[0] is " + week[0].toString());
		logger.log(Level.CONFIG, "Week[1] is " + week[1].toString());
		
		return week;
	}

	/**
	 * Takes a single date and returns an Array of dates of the week
	 * to which that single date belongs.
	 * 
	 * @param date The LocalDate object from which to derive its week.
	 * @return An Array of dates in the week from which the original date was taken.
	 */
	public static LocalDate[] getFullWeek (LocalDate date) {
		
		// Prime the array.
		LocalDate[] week = new LocalDate[7];
		
		// Find out how many days away from the first day of the week (0) that the given date is.
		int delta = date.getDayOfWeek().getValue() == 7 ? 0 : date.getDayOfWeek().getValue();
		// Change the date variable to the first date of its week.
		date = date.minusDays(delta);

		for (int i = 0; i < 7; i++) {
			// Put each date of the week into the Array.
			week[i] = date;
			date = date.plusDays(1);
		}
		
		return week;
	}

}
