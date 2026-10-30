public class InsertionSort {
    
    // Sorts the entire array
    public static void sort(int[] a){
        int N = a.length;
        for (int i = 0; i < N; i++)
            for (int j = i; j > 0; j--)
                if (a[j] < a[j-1]) exch(a, j, j-1);
                else break;
    }
    
    // Sorts subarray from lo -> hi
    public static void sort(int[] a, int lo, int hi){
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo; j--) {
                if (a[j] < a[j-1]) {
                    exch(a, j, j-1);
                } else {
                    break;
                }
            }
        }
    }

    private static void exch(int[] a, int x, int y){
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }
}
