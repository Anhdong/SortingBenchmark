import java.util.Random;

public class ArrayUtils{
    public static void shuffle(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static void exch(int[] a, int x, int y){
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }

    public static int[][] createTestCases(int numTests, int arraySize, int[] cutoffs){
        // Generate 2D array of TestCases with Cutoffs
        int[][] testCases = new int[numTests][arraySize];
        for (int i = 0; i < numTests; i++) {
            for (int j = 0; j < arraySize; j++) {
                testCases[i][j] = j;
            }
            ArrayUtils.shuffle(testCases[i]); 
        }
        return testCases;
    }
    public static int[] createShuffleArray( int arraySize){
        // Generate a array of TestCases without Cutoffs
        int[] array = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            array[i]= i;
        }
        ArrayUtils.shuffle(array);

        return array;
    }
    public static int[] createStepArray( int lo, int hi, int step){
        // Generate a array of TestCases with step
        int arrayLength = (hi-lo+step)/step;
        int[] array = new int[arrayLength];

        for (int i = 0; i < arrayLength; i++) {
            array[i]= i*step;
        }

        return array;
    }
}
