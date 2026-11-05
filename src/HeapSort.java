public class HeapSort{
    public static void sort(int CUTOFF, int[] a) {

        int n = a.length;
        for (int k = n/2; k >= 1; k--) sink(a, k, n);
        while (n > 1)
        {
        ArrayUtils.exch(a, 1, n);
        sink(a, 1, --n);
        }
    }

    private static void sink(int[] a, int k, int n){
        while (2*k <= n){
            int j = 2*k;
            if (j < n && a[j]<a[j+1]) j++;
            if (a[k]>=a[j]) break;
            ArrayUtils.exch(a, k, j);
            k = j;
        }
    }
}
