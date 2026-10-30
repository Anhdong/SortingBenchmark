import java.util.Arrays;

public class Main{
    public static void main(String[] args) {
        int[] nums = {4,1,6,12,75,2,3,3,12,52,23,5,81,12,4,13,2};
        QuickSort.sort(4,nums);
        System.out.print(Arrays.toString(nums));
    }
}
