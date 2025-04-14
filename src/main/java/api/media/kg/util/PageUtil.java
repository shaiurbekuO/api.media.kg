package api.media.kg.util;

public class PageUtil {
    public static int getPage(int value) {
        return value <= 0 ? 1 : value-1;
    }
}
