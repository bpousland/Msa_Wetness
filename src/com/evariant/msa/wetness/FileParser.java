package com.evariant.msa.wetness;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
	
	String filename = "";
	String match_pattern = "";
	String isregx = "";
	
	BufferedReader br = null;
	
    URL location = SortMsa.class.getProtectionDomain().getCodeSource().getLocation();
	
	
	public FileParser(String file, String delim, String isregex) throws IOException{
		match_pattern = delim;
		filename = location.getFile() + file;
		isregx = isregex;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch(Exception e) {
			throw e;
		}
	}
	
	public ArrayList<String> getNextLine() throws IOException{
		String line = "";
		ArrayList<String> result = new ArrayList<String>();
		
		if(br != null && (line = br.readLine()) != null) {
			if(isregx.equals("true")){
				Matcher m = Pattern.compile(match_pattern).matcher(line);
				while (m.find()){
					result.add(m.group(1));
				}
			} else {
				String[] s = line.split(match_pattern);
				result = new ArrayList<String>(Arrays.asList(s));
			}
		}
			
		return result;
	}
	
	public void tryClose(){
		if(br != null) {
			try {
				br.close();
			} catch(IOException e_close) {
				System.out.println("Error closing file: " + filename);
			}
		}

	}
}
