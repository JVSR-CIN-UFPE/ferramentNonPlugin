package br.edu.ufcg.dsc;

/**
 @author Jefferson Almeida - jra at cin dot ufpe dot br
 * Velocity preprocessor is used to TARGET
 * Antenna preprocessor is used to mobile media.
 */
public enum Lines {
	MOBILE_MEDIA("mobilemedia"),
	DEFAULT("default"),
	TARGET("target"),
	;
	
	private String label;
	
	Lines(String value) {
		this.label = value;
	}
	
	/**
	 * Similar to valuesOf method but using the internal string that defines the Line
	 * 
	 * @param value Value to search the Line
	 * @return Return Line if is founded
	 * @throws If didn't find any Line with the value 
	 */
	public static Lines getEnum(String value) {
		for(Lines line : Lines.values()) {
			if(line.label.equals(value)) {
				return line;
			}
		}
		
		throw new IllegalArgumentException();
	}
}
