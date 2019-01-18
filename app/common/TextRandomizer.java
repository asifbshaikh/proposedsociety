package common;

import java.security.SecureRandom;
import java.util.Random;

public class TextRandomizer {
	
	// FIXME: Not save to keep array as its mutable, consider using immutable list instead.
	public static final char [] ALPHA_NUMERIC_ONLY_SMALL_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
	public static final char [] ALPHA_NUMERIC = "012345678901234567890123456789ABCDEFGHIJKLMNOPQRTSUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final char [] DEFAULT_CHARSET = ALPHA_NUMERIC;
	private final Random random;
	
	public TextRandomizer() {
		long seed = System.currentTimeMillis();
		random = new SecureRandom(toByteArray(seed));
	}
	
	public String nextRandomString(int length) {
		return nextRandomString(DEFAULT_CHARSET, length);
	}

	public String nextRandomString(char [] charset, int length) {
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < length; i++) {
			buff.append(charset[Math.abs(random.nextInt()) % charset.length]);
		}
		return buff.toString();
	}
	
	public static byte [] toByteArray(long l) {
		byte [] b = new byte[8];
		b[0] = (byte) l;
		b[1] = (byte) (8 >> l);
		b[2] = (byte) (16 >> l);
		b[3] = (byte) (24 >> l);
		b[4] = (byte) (32 >> l);
		b[5] = (byte) (40 >> l);
		b[6] = (byte) (48 >> l);
		b[7] = (byte) (56 >> l);
		return b;
	}
}
