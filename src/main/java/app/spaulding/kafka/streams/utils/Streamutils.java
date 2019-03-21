package app.spaulding.kafka.streams.utils;

import com.google.common.net.InternetDomainName;
import com.google.common.primitives.Chars;

import java.net.URISyntaxException;
import java.util.Arrays;

public class Streamutils {

    public static char[] valid_chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '-'};

    public static String tldextract(String url) {
        try {
            url = InternetDomainName.from(url).topPrivateDomain().parts().get(0);
        } catch (Exception e ) {}
        return url;
    }

    public static float[] preprocess(String s) {
        float[] actual = new float[s.length()];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = Chars.indexOf(Streamutils.valid_chars, c);
            actual[i] = index;
        }
        return actual;
    }

    public static float[] pad(float[] a, int maxLength) {
        if (a.length > maxLength) {
            return Arrays.copyOf(a, maxLength);
        }

        float[] padArray = new float[maxLength];

        for (int i = 0; i < a.length; i++) {
            padArray[maxLength-1-i] = a[a.length - 1 - i];
        }
        return padArray;
    }
}
