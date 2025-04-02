package api.media.kg.util;

import java.util.regex.Pattern;

public class PhoneUtil {
    public static boolean isPhone(String value) {
        String phoneRegex = "^(998|996)\\d{9}$";
        return Pattern.matches(phoneRegex, value);
    }
}
