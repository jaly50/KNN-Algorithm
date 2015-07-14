/*
 * DataSet for each arff file
 * 	defines attributes and their values, mark whether they are discrete or not
 * 	store data(aka. instance, vector) as ArrayList
 */
package task11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataSet {
	public static final String patternString = "@attribute(.*)[{](.*?)[}]";
	public ArrayList<String> attribute;
	//Attribute_valueListForTheAttribute
	public HashMap<String, ArrayList<String>> attributevalue;
	// each instance
	public ArrayList<Vector> data;
	public ArrayList<Boolean> isDiscrete; 
	// number of attributes
	private int numAtt;
	
 
	DataSet() {
		attribute = new ArrayList<String>();
		attributevalue = new HashMap<String, ArrayList<String>>();
		data = new ArrayList<Vector>();
		isDiscrete = new ArrayList<Boolean>();
	}
	
	public String getClassName() {
		return this.attribute.get(this.attribute.size()-1);
	}
	
	
	// METHOD: read data from file
		public void readARFF(File file) {
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				Pattern pattern = Pattern.compile(patternString);
				while ((line = br.readLine()) != null) {
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						attribute.add(matcher.group(1).trim());
						isDiscrete.add(true);
						String[] values = matcher.group(2).split(",");
						ArrayList<String> al = new ArrayList<String>(values.length);
						for (String value : values) {
							al.add(value.trim());
						}
						attributevalue.put(matcher.group(1).trim(), al);
					} else if (line.startsWith("@attribute")) {
						String[] attrName = line.split(" ");
						attribute.add(attrName[1]);
						isDiscrete.add(false);
					} else if (line.startsWith("@data")) {
						while ((line = br.readLine()) != null) {
							if (line == "")
								continue;
							
							data.add(new Vector(line, this));
						}
					} else {
						continue;
					}
				}
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			this.setNumAtt(this.attribute.size());
			
		}

		public int getNumAtt() {
			return numAtt;
		}

		public void setNumAtt(int numAtt) {
			this.numAtt = numAtt;
		}

}
