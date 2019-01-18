package utils;

public final class StringUtils {
    private StringUtils() {
        
    }
    
    public static boolean isTrivial(String value) {
        return value == null || value.trim().length() == 0;
    }
}
