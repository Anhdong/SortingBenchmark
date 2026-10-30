import java.util.Random;

public class QuickSort{
    private static int partition(int[] a, int lo, int hi){
        int i = lo, j = hi+1;
        while (true)
        {
            while (a[++i] < a[lo])
                if (i == hi) break;
                while (a[lo] < a[--j])
                    if (j == lo) break;
                    if (i >= j) break;
                    exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    public static void sort(int CUTOFF, int[] a){
        shuffle(a);
        sort(CUTOFF, a, 0, a.length - 1);
    }

    private static void sort(int CUTOFF, int[] a, int lo, int hi){
        if (hi <= lo) return;

        if (hi <= lo + CUTOFF - 1){
            InsertionSort.sort(a, lo, hi);
            return;
        }

        int j = partition(a, lo, hi);
        sort(CUTOFF,a, lo, j-1);
        sort(CUTOFF,a, j+1, hi);
    }

    private static void exch(int[] a, int x, int y){
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }

    public static void shuffle(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
