package common;

public final class Utils {
	public static final TextRandomizer TEXT_RANDOMIZER = new TextRandomizer();

    private Utils () {
        
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
