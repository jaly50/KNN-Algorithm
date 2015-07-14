/*
 * Task 11 ---Team 7 --KNN Algorithm
 * Test program, run 3 parts 
 * 	1. production Selection : 
 * 			A. cross validation(10 folds), output each accuracy and the average accuracy 
 * 			B. get label {C1,C2,C3,C4,C5} for test data
 *  2. production introduction for binary file:
 *  		A. cross validation(10 folds), output each accuracy and the average accuracy 
 * 			B. get label{0,1} for test data 
 *  3. production introduction for real file: 
 * 			Output score for test data, it's score is kvote = average of the label of k nearest neighbors.
 */
package task11;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		productSelection();
		productIntroduction();
		productIntroduction_Real();

	}

	private static void productIntroduction_Real() {
		System.out
				.println("------Testing the Product Introduction, to Get real Score:------------");

		// Test trainARFF
		DataSet trainData = new DataSet();
		trainData.readARFF(new File("./trainProdIntro.real.arff"));

		DataSet testData = new DataSet();
		testData.readARFF(new File("./testProdIntro.real.arff"));

		double[] weight = new double[] { 1, 1, 15, 40, 1, 1, 1, 1 };

		// load similaritymatrix
		HashMap<String, double[][]> mapSim = loadSimMatrixForB();

		System.out
				.println("----------To get score of those test data---------");

		Knn knn = new Knn(trainData, testData.data, trainData.data, weight, 5,
				mapSim);
		for (Vector v : testData.data) {

			String newLabel = knn.knnCalculate(v);
			// System.out.println(newLabel);
		}

	}

	private static void productIntroduction() {
		System.out
				.println("------Testing the Product Introduction_Binary file:------------");

		// Test trainARFF
		DataSet trainData = new DataSet();
		trainData.readARFF(new File("./trainProdIntro.binary.arff"));

		DataSet testData = new DataSet();
		testData.readARFF(new File("./testProdIntro.binary.arff"));

		double[] weight = new double[] { 1, 1, 15, 50, 1, 1, 1, 1 };

		// load similaritymatrix
		HashMap<String, double[][]> mapSim = loadSimMatrixForB();

		// Shuffle the train data
		Collections.shuffle(trainData.data);

		// Doing crossvalidation to train data
		CrossValidation cv = new CrossValidation(10, trainData);
		double accuracy = cv.getAccuracy(weight, 5, mapSim);
		System.out
				.println("Testing through cross validation, The accuracy is: "
						+ accuracy);

		System.out.println();
		System.out
				.println("----------To get labels of those test data---------");

		Knn knn = new Knn(trainData, testData.data, trainData.data, weight, 5,
				mapSim);
		for (Vector v : testData.data) {

			String newLabel = knn.knnCalculate(v);
			System.out.println(newLabel);
		}

	}

	private static void productSelection() {
		System.out.println("------Testing the Product Selection:------------");

		// Test trainARFF
		DataSet trainData = new DataSet();
		trainData.readARFF(new File("./trainProdSelection.arff"));

		DataSet testData = new DataSet();
		testData.readARFF(new File("./testProdSelection.arff"));

		double[] weight = new double[] { 3, 0, 1, 100, 8, 0.2 };

		// load similaritymatrix
		HashMap<String, double[][]> mapSim = loadSimMatrix();

		// System.out.println(mapSim.get("Service_type"));

		// Shuffle the train data
		Collections.shuffle(trainData.data);

		// Doing crossvalidation to train data
		CrossValidation cv = new CrossValidation(10, trainData);
		double accuracy = cv.getAccuracy(weight, 5, mapSim);
		System.out
				.println("Testing through cross validation, The accuracy is: "
						+ accuracy);

		System.out.println();
		System.out
				.println("----------To get labels of those test data---------");

		Knn knn = new Knn(trainData, testData.data, trainData.data, weight, 5,
				mapSim);
		for (Vector v : testData.data) {

			String newLabel = knn.knnCalculate(v);
			System.out.println(newLabel);
		}

	}

	private static HashMap<String, double[][]> loadSimMatrix() {
		HashMap<String, double[][]> mapSim = new HashMap<String, double[][]>();
		String attributeName = "LifeStyle";
		double[][] relations = new double[][] { { 1, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0 },

				{ 0, 0, 0, 0, 1 },

		};
		mapSim.put(attributeName, relations);

		attributeName = "Type";
		relations = new double[][] { { 1, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0 },
				{ 0, 0, 1, 0, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1 } };
		mapSim.put(attributeName, relations);
		return mapSim;
	}

	private static HashMap<String, double[][]> loadSimMatrixForB() {
		HashMap<String, double[][]> mapSim = new HashMap<String, double[][]>();
		String attributeName = "Service_type";
		double[][] relations = new double[][] { { 1, 0.2, 0.2, 0, 0.1 }, // Fund
				{ 0.2, 1, 0.1, 0, 0.3 }, // Loan
				{ 0.2, 0.1, 1, 0, 0.2 }, // CD
				{ 0, 0, 0, 1, 0 },// Bank_account
				{ 0.1, 0.3, 0.2, 0, 1 },// Mortgage

		};
		mapSim.put(attributeName, relations);

		attributeName = "Customer";
		relations = new double[][] { { 1, 0.1, 0, 0.1, 0.2 }, // student
				{ 0.1, 1, 0, 0.2, 0.2 }, // business
				{ 0, 0, 1, 0, 0 }, // other
				{ 0.1, 0.2, 0, 1, 0.1 }, // doctor
				{ 0.2, 0.2, 0, 0.1, 1 } }; // professional
		mapSim.put(attributeName, relations);

		attributeName = "Size";
		relations = new double[][] { { 1, 0, 0.1 }, // small
				{ 0, 1, 0.1 }, // large
				{ 0.1, 0.1, 1 } // medium
		};
		mapSim.put(attributeName, relations);

		attributeName = "Promotion";
		relations = new double[][] { { 1, 0.8, 0.1, 0.5 },// Web&Email
				{ 0.8, 1, 0, 0 },// Full
				{ 0.1, 0, 1, 0.4 },// Web
				{ 0.5, 0, 0.4, 1 } // None
		};
		mapSim.put(attributeName, relations);
		return mapSim;

	}
}
