package com.evariant.msa.wetness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class RainfallMap {

	// Column: Wban,YearMonthDay,Hour,Precipitation,PrecipitationFlag
	public static final int PRECIP_WBAN = 0;
	public static final int PRECIP_DATE = 1; 
	public static final int PRECIP_HOUR = 2;
	public static final int PRECIP_VALUE = 3; 
	
	public Map<String, Float> rainmap = new HashMap<String, Float>();
	private WbanCityMap cities;
	private PopulationMap populations;
	
	public void generateWbanRainfallMap(PropertyValues props) throws IOException {
		FileParser fp = null;
		
		try {
			
			// Get a map of each wban to it's city string
			cities = new WbanCityMap();
			cities.generateWbanCityMap(props);
			
			// Get the population for each MSA city
			populations = new PopulationMap();
			populations.generateCityPopulatioMap(props);
	
			
			String file = props.getProperty("MSA_Precip_Data_File");;
			String pattern = props.getProperty("MSA_Precip_Data_Delimiter");
			String isregex = props.getProperty("MSA_Precip_Data_Regex");
	
			fp = new FileParser(file, pattern, isregex);
			
			ArrayList<String> line;
			Float total_per_wban = 0.0f;
			Float current_rainfall;
			String current_wban = "";
			String last_wban = "";
	        Integer current_hour;
			
			// The assumptions are that the rainfall totals are grouped by WBAN
	        // and the precipitation data file only contains data for the month of interest, ie. 201505
	        // no no need to consider the date
			while((line = fp.getNextLine()) != null && line.size() > 0) {
				String rain = line.get(PRECIP_VALUE);
				String hour = line.get(PRECIP_HOUR);
				
				if(rain ==  null || hour == null) continue;
				
				try {
					current_rainfall = Float.parseFloat(rain);
			        current_hour = Integer.parseInt(hour);
				} catch(NumberFormatException e) {
					continue;
				}
				
		        // Does the precipitation value exist?
				if(current_rainfall != null) {
					
					// is this an hour of interest?
					if(current_hour != null && (8 <= current_hour && current_hour <= 23 )) {
	
						current_wban = line.get(PRECIP_WBAN);
						if(last_wban.length() == 0) last_wban = current_wban;
							
						if(!current_wban.equals(last_wban) && total_per_wban > 0) {
							// push the last wban total and reset 
							putRainTotal(last_wban, total_per_wban);
							last_wban = current_wban;
							total_per_wban = 0.0f;
							total_per_wban += current_rainfall;
						} else {
							total_per_wban += current_rainfall;
						}
					}				
				}	        
			}
			
			// Push the last wban total he have
			putRainTotal(last_wban, total_per_wban);
		} catch(IOException e) {
			System.err.println("Error reading precip file: " + e.toString());
			e.printStackTrace();
		} finally {
			fp.tryClose();
		}
	}
	
	public void printSortedTotals() {
		SortedSet<Map.Entry<String, Float>> sortedset = new TreeSet<Map.Entry<String, Float>>(
	            new Comparator<Map.Entry<String, Float>>() {
	                @Override
	                public int compare(Map.Entry<String, Float> e1,
	                        Map.Entry<String, Float> e2) {
	                    return e1.getValue().compareTo(e2.getValue());
	                }
	            });

		sortedset.addAll(rainmap.entrySet());
		System.out.println("Sorted: " + sortedset);
	}
	
	void putRainTotal(String last_wban, Float total) {
		
		try {
			String city = cities.getCityStateByWBAN(last_wban);
			Integer pop = populations.getPopulationByCBSA(city);
			
			if(city != null && pop > 0)
				rainmap.put(last_wban, total * pop);
		} catch(Exception e) {
			System.err.println("Error calculating rain total: " + e.toString());
		}
	}
}
