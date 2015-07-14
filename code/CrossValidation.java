/*
 * Task 11 ---Team 7 --KNN Algorithm
 * Cross Validation:
 * 	Parameter: folds and the dataset
 *  return (getAccuracy method): average accuracy for the folds of the dataset
 */
package task11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CrossValidation {
	int folds;
	 DataSet dataset;
	 double accuracy;
	 int dataSize;
	 Vector[] dataList;
	
	CrossValidation(int folds, DataSet trainData) {
		this.folds = folds;
		this.dataset =trainData;
	    this.dataSize = trainData.data.size();
	    this.dataList = trainData.data.toArray(new Vector[dataSize]);
	}
	

    

    // To cut them as 10 folds
	public double getAccuracy(double[] weight, int k, HashMap<String, double[][]> mapSim) {
		int lenTest = dataSize / folds;
		Vector[] test = new Vector[lenTest];
		Vector[] train = new Vector[dataSize - lenTest];
		
		double accuracy =0; 
		for (int i=0; i<folds; i++) {
			test =Arrays.copyOfRange(dataList,  i*lenTest, i*lenTest+lenTest);
			Vector[] part1 =Arrays.copyOfRange(dataList,  0, i*lenTest);
			Vector[] part2 = Arrays.copyOfRange(dataList,  i*lenTest+lenTest, dataSize);
			System.arraycopy(part1, 0, train, 0, part1.length);
			   System.arraycopy(part2, 0, train, part1.length, part2.length);
			      
			
			Knn knn = new Knn(dataset,train, test, weight, k, mapSim);
			double correctness =0;
			for (Vector v : test) {
				String newLabel = knn.knnCalculate(v);
				
				if (newLabel.equals(v.symAtt.get(dataset.getClassName()))) {
					correctness++;
				}
			}
			correctness = correctness/test.length;
			System.out.println("for "+i+"th test, the correctness is "+correctness);
			
					
			accuracy+= correctness;
			
		}
		return accuracy/folds;
	}






}
