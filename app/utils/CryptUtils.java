package utils;

import org.apache.commons.lang3.RandomStringUtils;

import play.libs.Crypto;

public final class CryptUtils {

    private static final String SALT_SEPARATOR = ":";
    private static final int SALT_LENGTH = 10;

    public static String salt(String text) {
        return RandomStringUtils.random(SALT_LENGTH, true, true) + SALT_SEPARATOR + text;
    }

    public static String desalt(String saltedText) {
        int index = saltedText.indexOf(SALT_SEPARATOR);
        if (index != -1 && saltedText.length() >= index) {
            return saltedText.substring(index + 1);
        }
        
        // Should we throw exception if we could not desalt? For now silently returning the salted text.
        return saltedText;
    }

    public static String encrypt(String clearText) {
        return Crypto.encryptAES(clearText);
    }

    public static String decrypt(String cipher) {
        return Crypto.decryptAES(cipher);
    }
}
