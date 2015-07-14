/*
 * Task 11 ---Team 7 --KNN Algorithm
 * Knn core algorithm:
 * 	Construction:
 * 		parameter:  test data, train data, attributes and its value(from DataSet), k, weight for each attributes, and similarity map
 *      Preprocessing: 
 *      	Get min attributes and max attributes from the train dataSet
 *          Normalize the train data and test data, using (xi - ximin)/(ximax - ximin), make sure they are in range[0,1]
 *  return (knnCalculate method): 
 *  	label or score for each instance(Vector) in Test data
 */
package task11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Knn {
	static double[] maxAtt;
	static double[] minAtt;

	DataSet dataSet;
	Vector[] train;
	Vector[] test;
	double[] weight;
	HashMap<String, double[][]> mapSim;
	
	int k;



	public Knn(DataSet dataset, Vector[] train, Vector[] test, double[] weight, int k, HashMap<String, double[][]> mapSim) {
		this.dataSet = dataset;
		this.train = train;
		this.test = test;
		this.k = k;
		this.weight = weight;
		this.mapSim = mapSim;

		SetMaxMinAtt(train);

		normalize(train);
		normalize(test);
		
	}

	public Knn(DataSet dataset, ArrayList<Vector> test,
			ArrayList<Vector> train, double[] weight, int k,HashMap<String, double[][]> mapSim) {
		this.dataSet = dataset;
		this.train = train.toArray(new Vector[train.size()]);
		this.test = test.toArray(new Vector[test.size()]);
		this.k = k;
		this.weight = weight;
		this.mapSim = mapSim;
		

		SetMaxMinAtt(this.train);

		normalize(this.train);
		normalize(this.test);
		
	}

	private void SetMaxMinAtt(Vector[] train) {
		maxAtt = new double[dataSet.attribute.size()];
		minAtt = new double[dataSet.attribute.size()];

		// Initailized value
		Arrays.fill(maxAtt, 0);
		Arrays.fill(minAtt, 100000);

		for (Vector v : train) {
			for (int i = 0; i < dataSet.getNumAtt(); i++) {
				String attName = dataSet.attribute.get(i);
				if (!dataSet.isDiscrete.get(i)) {
					if (maxAtt[i] < v.numAtt.get(attName)) {
						maxAtt[i] = v.numAtt.get(attName);
					}

					if (minAtt[i] > v.numAtt.get(attName)) {
						minAtt[i] = v.numAtt.get(attName);
					}

				}
			}
		}

	}

	// Get top K similarity for Test record V from TraindataSet.
	String knnCalculate(Vector v) {
		PriorityQueue<Vector> queue = new PriorityQueue<Vector>(k, comparator);
		

		for (int i = 0; i < train.length; i++) {
			Vector tmp = train[i];
			double similarity = similarity(v, tmp);
			tmp.similarity = similarity;
			if (i >= k) {
				Vector queuetop = queue.peek();
				if (queuetop.similarity < similarity) {
					queue.poll();
					queue.add(tmp);
				}
			} else {
				queue.add(tmp);
			}
		}
		
		String className = dataSet.getClassName();
		//If the class attribute is discrete 
		if (dataSet.isDiscrete.get(dataSet.getNumAtt()-1)) {
		//Get K highest similarity, then we can set new label for V based on the K vectors 
			// firstly, let's get  scores for all labels and keep the highest score and it's label
		
		//System.out.println(dataSet.getNumAtt()-1+"number of attributes, "+dataSet.attributevalue.size());
		ArrayList<String> classValues = dataSet.attributevalue.get(className);
		
		double maxScore =0;
		String maxLabel = null;
		for (String classValue: classValues) {
			 double tempScore =0;
			for (Vector vector: queue) {
				tempScore += vector.similarity  * simClass(classValue, vector.symAtt.get(className));
				//System.out.println(tempScore+"<---score, similarity-->"+vector.similarity+" "+vector.symAtt.get(className));
			}
			if (tempScore > maxScore) {
				maxScore = tempScore;
				maxLabel = classValue;
			}
			
		}
		return maxLabel;
		}
		// If the class attribute is number
		else {
			 double tempScore =0;
				for (Vector vector: queue) {
					tempScore += vector.numAtt.get(className);
				}
			tempScore = tempScore/queue.size();
			System.out.println(tempScore);
			return null;
		}
	}

	private double simClass(String c1, String c2) {
		if (c1.equals(c2))
			return 1;
		else return 0;
	}

	private Comparator<Vector> comparator = new Comparator<Vector>() {
		public int compare(Vector v1, Vector v2) {
			if (v1.similarity >= v2.similarity)
				return 1;
			else
				return -1;
		}
	};


	private  void normalize(Vector[] data) {
		for (Vector v : data) {
			for (int i = 0; i < dataSet.getNumAtt()-1; i++) {
				String attName = dataSet.attribute.get(i);
				if (!dataSet.isDiscrete.get(i)) {
					double newValue = (v.numAtt.get(attName) - minAtt[i])
							/ (maxAtt[i] - minAtt[i]);
					if (newValue < 0)
						newValue = 0;
					else if (newValue > 1) {
						newValue = 1;
					}
					v.normAtt.put(attName, newValue);
				}
			}
		}
	}


	public double similarity(Vector a, Vector b) {
		
		double score = 0;
		//The last attribute in dataSet is class, which we don't need to calculate the score
		for (int i = 0; i < dataSet.getNumAtt()-1; i++) {
			String attName = dataSet.attribute.get(i);
			if (dataSet.isDiscrete.get(i)) {
				score += weight[i]
						* (1 - sim(attName, a.symAtt.get(attName),
								b.symAtt.get(attName)));
			} else {
				score += weight[i]
						* Math.pow(
								a.normAtt.get(attName) - b.normAtt.get(attName),
								2);

			}
		}
		return 1 / Math.sqrt(score);
	}

	private double sim(String attName, String a, String b) {
		double[][] relations = mapSim.get(attName);
		int posA = dataSet.attributevalue.get(attName).indexOf(a);
		int posB = dataSet.attributevalue.get(attName).indexOf(b);
	//	System.out.println(attName+"<-name"+relations+" "+posA+" " +posB);
		return relations[posA][posB];
		
	}

}
