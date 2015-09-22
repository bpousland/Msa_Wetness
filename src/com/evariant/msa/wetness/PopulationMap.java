package com.evariant.msa.wetness;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopulationMap {

	public static final int CBSA_CITY = 0;
	public static final int CBSA_POPULATION = 1; 
	
	Map<String, Integer> populations = new HashMap<String, Integer>();
	
	public void generateCityPopulatioMap(PropertyValues props) throws IOException {
		FileParser fp = null;
		try{
			String file = props.getProperty("CBSA_Populations_File");;
			String delim = props.getProperty("CBSA_Populations_Delimiter");
			String isregex = props.getProperty("CBSA_Populations_Regex");
	
			fp = new FileParser(file, delim, isregex);
			
			ArrayList<String> line;
			while((line = fp.getNextLine()) != null && line.size() > 0){
				String city = line.get(CBSA_CITY);
				String pop = line.get(CBSA_POPULATION);
				if(city != null && pop != null) {
					pop = pop.replace(",","");
					populations.put( city.toUpperCase(), Integer.parseInt(pop) );
				}
			}
		} catch(IOException e) {
			System.err.println("Error reading populations file: " + e.toString());
			e.printStackTrace();		
		} finally {
			fp.tryClose();
		}
	}
	
	public Integer getPopulationByCBSA(String cbsa){
		Integer p = populations.get(cbsa);
		if(p == null) {
			System.err.println("Error: population map did not constain cbsa: " + cbsa);
			return 0;
		} else {
			return p;
		}
	}
}
