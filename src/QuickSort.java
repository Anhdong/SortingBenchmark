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
                    ArrayUtils.exch(a, i, j);
        }
        ArrayUtils.exch(a, lo, j);
        return j;
    }

    public static void sort(int CUTOFF, int[] a){
        sort(CUTOFF, a, 0, a.length - 1);
    }

    public static void sort(int CUTOFF, int[] a, int lo, int hi){
        if (hi <= lo) return;

        if (hi <= lo + CUTOFF - 1){
            InsertionSort.sort(a, lo, hi);
            return;
        }

        int j = partition(a, lo, hi);
        sort(CUTOFF,a, lo, j-1);
        sort(CUTOFF,a, j+1, hi);
    }

}
