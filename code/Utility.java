package task11;

import java.util.Random;

public class Utility {
	public static int sim(String a, String b) {
		return a.equals(b) ? 1 : 0;

	}

	public static void shuffleArray(Vector[] train) {
		{
		    Random rnd = new Random();
		    for (int i = train.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      // Simple swap
		      Vector a = train[index];
		      train[index] = train[i];
		      train[i] = a;
		    }
		  }

		
	}





}
