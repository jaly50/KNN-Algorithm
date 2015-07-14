/*
 * Task 11- team 7
 * Vector is instance, it represents each records
 * We store numeric and symbolic attributes separately in map
 * normAtt is the normalized value for numeric attributes
 * 
 */
package task11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vector {
	// numeric attribute and it's value
	Map<String, Double> numAtt;  
	//The normalized attribute and it's value, range in [0,1]
	Map<String, Double> normAtt;
	//Symbolic attributes and it's value
	Map<String, String> symAtt;
	//it's similarity with other vector, it changes whenever we compare it with another vector
	double similarity;
	// The dataSet it belongs.  We store it to get the list of attributes and attribute values
	DataSet set;


   /*
    * construct a Vector(instance) from a line from file and it's DataSet
    */
	public Vector(String line, DataSet set) {
		numAtt = new HashMap<String, Double>();
		normAtt = new HashMap<String, Double>();
		symAtt =new HashMap<String, String>();
		this.set = set;
		 ArrayList<Boolean> isDiscrete = set.isDiscrete;
		 ArrayList<String> attribute =set.attribute;
		String[] row = line.split(",");
		for (int i=0; i<row.length; i++) {
			if (isDiscrete.get(i)) {
				symAtt.put(attribute.get(i), row[i]);
			}
			else {
				numAtt.put(attribute.get(i), Double.parseDouble(row[i]));
			}
		}
	}
	





    /*
     * To validate whether this label is same as that label (used for cross validation)
     */
	public boolean validate(Vector that) {
		String labelName = this.set.getClassName();
		return this.symAtt.get(labelName).equals(that.symAtt.get(labelName));
	}
	

	


}
