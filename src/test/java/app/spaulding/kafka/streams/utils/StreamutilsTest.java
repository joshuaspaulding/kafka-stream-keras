package app.spaulding.kafka.streams.utils;

import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import static app.spaulding.kafka.streams.utils.Streamutils.*;
import static org.junit.Assert.*;

public class StreamutilsTest {

    @Test
    public void valid_charsTest() {
        assertEquals('p', Streamutils.valid_chars[25]);
    }

    @Test
    public void tldextractTest() {
        String domain = "www.google.uk.com";
        String tld = "google";
        assertEquals(tld, tldextract(domain));
    }

    @Test
    public void padArraySmallerThanMaxTest() {
        float[] a = new float[]{1,2,3,4,5};
        float[] b = pad(a, 10);
        assertArrayEquals(new float[]{0, 0, 0, 0, 0, 1, 2, 3, 4, 5}, b, 0);
    }

    @Test
    public void padArrayBiggerThanMaxTest() {
        float[] a = new float[]{1,2,3,4,5};
        float[] b = pad(a, 3);
        assertArrayEquals(new float[]{1, 2, 3}, b, 0);
    }

    @Test
    public void padArraySameAsMaxTest() {
        float[] a = new float[]{1,2,3,4,5};
        float[] b = pad(a, 5);
        assertArrayEquals(a, b, 0);
    }

    @Test
    public void preprocessTest() {
        String domain = "google";
        float[] expected = {16, 24, 24, 16, 21, 14};
        assertArrayEquals(expected, preprocess(domain), 0);
    }

    @Test
    public void preprocessAndPadTest() {
        String domain = "google";
        int maxLength = 10;
        float[] p = preprocess(domain);

        float[] expected = {0, 0, 0, 0,16, 24, 24, 16, 21, 14};

        assertArrayEquals(expected, pad(p, maxLength), 0);

    }

    @Test
    public void indarrayTest() {
        float[] a = {0, 0, 0, 0,16, 24, 24, 16, 21, 14};
        INDArray inda = Nd4j.create(a, new int[]{10});
        System.out.println(inda);
    }

    @Test
    public void INDArraryTest() {
        INDArray oneDArray = Nd4j.create(new float[]{1,2,3,4,5,6} , new int[]{6});
    }
}