package api.media.kg.util;

import java.util.regex.Pattern;

public class EmailUtil {
    public static boolean isEmail(String value) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, value);
    }
}
