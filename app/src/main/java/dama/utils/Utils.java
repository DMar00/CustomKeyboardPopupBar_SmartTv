package dama.utils;

public class Utils {
    public static String colorToString(int color){
        //Convert int color in String #RRGGBB
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static boolean isPresent(int[] array, int target) {
        for (int num : array) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }

}
