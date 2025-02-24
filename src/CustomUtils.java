public class CustomUtils {
    public static boolean arrayContains(char[] array, char x) {
        for (char c: array) {
            if (c == x) {
                return true;
            }
        }
        return false;
    }
}