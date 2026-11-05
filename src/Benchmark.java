import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Benchmark {
    @FunctionalInterface
    interface SorterFunction {
        void sort(int cutoff, int[] arr);
    }

    public static void main(String[] args) throws IOException {
        //Handle ComandLine Inputs
        // 0.Quicksort/MergeSort 1.numTests, 2.arraySize, 345. cutoff range(lo,hi,step)

        if (args.length < 6) throw new IllegalArgumentException("Missing arguments! Usage: java Benchmark <(Q)uicksort/(M)ergeSort> <NumTests> <ArraySize> <Cutoff(lo,hi,step)>");
        SorterFunction targetAlgorithm = switch (args[0]) {
            case "Q" -> QuickSort::sort;
            case "M" -> MergeSort::sort;
            default -> throw new IllegalArgumentException("Sort alogrithm must be Q or M. Received: " + args[0]);
        };

        int numTests = Integer.parseInt(args[1]);
        int arraySize = Integer.parseInt(args[2]);
        int[] cutoffs = ArrayUtils.createStepArray(Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]));

        String fileName = (args[0].equals("Q") ? "quicksort" : "mergesort") + "_result.csv";
        
        //JUICY TUI
        System.out.println(BANNER);
        System.out.println(lineBreak);
        printInfo(args[0], numTests, arraySize, cutoffs);
        System.out.println(lineBreak);

        // GENERATE TESTCASEs
        System.out.println("Generate TestCases");
        int[][] testCases = ArrayUtils.createTestCases(numTests, arraySize, cutoffs);

        // JIT Warmup
        System.out.println("JIT Warm up");
        warmup(targetAlgorithm);

        // PREPARE THE CSV & RUN BENCHMARK
        System.out.println("Prepare CSV file and Running Benchmark...");
        
        // Using try-with-resources to automatically close the file
        try (PrintWriter csv = new PrintWriter(new FileWriter(fileName))) {
            //Write header
            csv.println("TestIndex,CutoutSize,TimeNs");

            for (int testIndex = 0; testIndex < numTests; testIndex++) {
                for (int cutoff : cutoffs) {
                    // MUST clone the original test case for every single cutoff attempt
                    int[] targetArray = testCases[testIndex].clone();
                    
                    long startTime = System.nanoTime();
                    targetAlgorithm.sort(cutoff, targetArray);
                    long endTime = System.nanoTime();
                    
                    long duration = endTime - startTime;
                    
                    csv.println(testIndex + "," + cutoff + "," + duration); 
                }
            }
        } 
        
        System.out.println("Benchmark complete. Results saved to " + fileName);
    }

    public static int warmup(SorterFunction targetAlgorithm) {
        int checksum = 0; //Prevent Java to terminate deadcode
        int[] testArray = ArrayUtils.createShuffleArray(1000);

        // 10,000 iterations is the standard threshold for top-tier JIT compilation
        for (int i = 0; i < 10_000; i++) {
            ArrayUtils.shuffle(testArray);
            targetAlgorithm.sort(15, testArray);
            checksum += testArray[0];
        }
        return checksum;
    }



    //JUICY STUFF
    private static final String BANNER = """
░█▀▀░█▀█░█▀▄░▀█▀░▀█▀░█▀█░█▀▀░░░█▀▄░█▀▀░█▀█░█▀▀░█░█░█▄█░█▀█░█▀▄░█░█
░▀▀█░█░█░█▀▄░░█░░░█░░█░█░█░█░░░█▀▄░█▀▀░█░█░█░░░█▀█░█░█░█▀█░█▀▄░█▀▄
░▀▀▀░▀▀▀░▀░▀░░▀░░▀▀▀░▀░▀░▀▀▀░░░▀▀░░▀▀▀░▀░▀░▀▀▀░▀░▀░▀░▀░▀░▀░▀░▀░▀░▀
    """;
    private static final String lineBreak = "-----------------------------------------------------------";
    private static void printInfo(String A,int numTests, int arraySize, int[] cutoffs){
        System.out.println("• Algorithm: " + A);
        System.out.println("• Number of tests: " + numTests);
        System.out.println("• Test Arrays size: " + arraySize);
        System.out.println("• CutOffs: "+ Arrays.toString(cutoffs));
    }
}
