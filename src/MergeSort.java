public class MergeSort {
    private static void merge(int[] a, int[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)                    a[k] = aux[j++];
            else if (j > hi)                a[k] = aux[i++];
            else if (aux[j] < aux[i])       a[k] = aux[j++];
            else                            a[k] = aux[i++];
        }
    }

    public static void sort(int CUTOFF, int[] a, int[] aux, int lo, int hi) {
        if (hi <= lo) return;

        if (hi <= lo + CUTOFF - 1) {
            InsertionSort.sort(a, lo, hi); 
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(CUTOFF, a, aux, lo, mid);
        sort(CUTOFF, a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    public static void sort(int CUTOFF, int[] a) {
        int[] aux = new int[a.length]; 
        sort(CUTOFF, a, aux, 0, a.length - 1);
    }
}
