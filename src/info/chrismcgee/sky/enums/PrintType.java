package info.chrismcgee.sky.enums;

public enum PrintType {
	
	// The 7 primary types of printing
	SCREEN_CUPS("Screen Cups"), SCREEN_NAPKINS("Screen Napkins"), PAD("Pad"),
	HOTSTAMP("Hotstamp"), OFFSET_CUPS("Offset Cups"), OFFSET_NAPKINS("Offset Napkins"),
	DIGITAL("Digital");

	private String value; // Stores the print type we're working with.
	
	/**
	 * When/if this class is instantiated, this constructor stores the print type
	 * 
	 * @param value
	 */
	private PrintType(String value) {
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

	/**
	 * Takes a readable String value of the print type and converts it to an enum.
	 * 
	 * @param x The human-readable String value of the print type.
	 * @return The enum of that print type.
	 */
	public static PrintType getPrintType (String x)
	{
		switch (x)
		{
			case "Screen Cups": return SCREEN_CUPS;
			case "Screen Napkins": return SCREEN_NAPKINS;
			case "Pad": return PAD;
			case "Hotstamp": return HOTSTAMP;
			case "Offset Cups": return OFFSET_CUPS;
			case "Offset Napkins": return OFFSET_NAPKINS;
			case "Digital": return DIGITAL;
			default: return null;
		}
	}
	
	/**
	 * Takes an integer representation of the print type and converts it to an enum.
	 * 
	 * @param x The integer value of the print type.
	 * @return The enum of that print type.
	 */
	public static PrintType getPrintType (int x)
	{
		switch (x)
		{
			case 0: return SCREEN_CUPS;
			case 1: return PAD;
			case 2: return HOTSTAMP;
			case 3: return OFFSET_CUPS;
			case 4: return OFFSET_NAPKINS;
			case 5: return DIGITAL;
			case 6: return SCREEN_NAPKINS;
			default: return PAD;
		}
	}

	/**
	 * Takes an enum of the print type and converts it to its corresponding integer value.
	 * 
	 * @param printType The enum of the print type.
	 * @return An integer of that print type. 
	 */
	public static int getIntValue(PrintType printType)
	{
		switch (printType)
		{
			case SCREEN_CUPS: return 0;
			case PAD: return 1;
			case HOTSTAMP: return 2;
			case OFFSET_CUPS: return 3;
			case OFFSET_NAPKINS: return 4;
			case DIGITAL: return 5;
			case SCREEN_NAPKINS: return 6;
			default: return 1;
		}
	}

}
