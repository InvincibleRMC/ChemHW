import java.util.Comparator;

public class StrinArrayComparator implements Comparator<String[]> {
    @Override
    public int compare(String[] array1, String[] array2) {
        // get the second element of each array, andtransform it into a Double
        Double d1 = (double) array1[0].length();
        Double d2 = (double) array2[0].length();
        // since you want a descending order, you need to negate the 
        // comparison of the double
        return -d1.compareTo(d2);
        // or : return d2.compareTo(d1);
    }
}