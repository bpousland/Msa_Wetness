/**
 * 
 */
package com.evariant.msa.wetness;

/**
 * @author bpousland
 *
 */
public class SortMsa {

	static PropertyValues properties = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {			
			// Read the properties file of this project		
			properties = new PropertyValues();
			properties.getPropValues();

		    // Get a map of all MSAs with a rainfall total
			RainfallMap rainfall = new RainfallMap();
			rainfall.generateWbanRainfallMap(properties);
			rainfall.printSortedTotals();
			
		} catch (Exception e) {
			// properties file not found
			System.out.println("Error: " + e.toString());
			return;
		}
		
		
	}

}
