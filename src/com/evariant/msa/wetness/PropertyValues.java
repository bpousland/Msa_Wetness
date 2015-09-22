package com.evariant.msa.wetness;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
/**
 * @author bpousland
 * 
 */
 
public class PropertyValues {
	String result = "";
	InputStream inputStream;
	Properties prop;
 
	public String getPropValues() throws IOException {
 
		try {
			prop = new Properties();
			String propFileName = "data.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
	
	public String getProperty(String propertyName) {
		// Return a property by name
		// Can return null if not found
		return prop.getProperty(propertyName);
	}
}


		
