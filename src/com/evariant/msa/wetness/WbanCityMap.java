package com.evariant.msa.wetness;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WbanCityMap {

	// WBAN|WMO|CallSign|ClimateDivisionCode|ClimateDivisionStateCode|ClimateDivisionStationCode|
	// Name|State|Location|Latitude|Longitude|GroundHeight|StationHeight|Barometer|TimeZone
	public static final int WBAN_CITY_WBAN = 0;
	public static final int WBAN_CITY_NAME = 6; 
	public static final int WBAN_CITY_STATE = 7; 
	
	Map<String, String> cities = new HashMap<String, String>();
	
	public void generateWbanCityMap(PropertyValues props) throws IOException {
		FileParser fp = null;;
		try{
			String file = props.getProperty("WBAN_Cities_List_File");;
			String delim = props.getProperty("WBAN_Cities_List_Delimiter");
			String isregex = props.getProperty("WBAN_Cities_List_Regex");
			fp = new FileParser(file, delim,isregex);
			
			ArrayList<String> line;
			while((line = fp.getNextLine()) != null && line.size() > 0){
				String name = line.get(WBAN_CITY_NAME);
				name += ", " + line.get(WBAN_CITY_STATE);
				String wban = line.get(WBAN_CITY_WBAN);
				if(name != null && wban != null)
					cities.put( wban, name );
			}
		}catch(IOException e){
			System.err.println("Error reading populations file: " + e.toString());
			e.printStackTrace();				
		} finally{
			fp.tryClose();
		}
	}
	
	public String getCityStateByWBAN(String wban) throws Exception{
		String c = cities.get(wban);
		if(c == null) {
			throw new Exception("Error: map did not constain wban: " + wban);
		} else {
			return c;
		}
	}
}
