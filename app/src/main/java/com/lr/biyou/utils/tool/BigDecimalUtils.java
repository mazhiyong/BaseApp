package com.lr.biyou.utils.tool;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {
    public static BigDecimal add(String s1, String s2) {
        BigDecimal bigDecimal1 = new BigDecimal(s1);
        BigDecimal bigDecimal2 = new BigDecimal(s2);
        return bigDecimal1.add(bigDecimal2);
    }

    public static BigDecimal divide(String s1, RoundingMode roundingMode, int mul) {
        BigDecimal bigDecimal1 = new BigDecimal(s1);
        return bigDecimal1.divide(BigDecimal.TEN.pow(mul), roundingMode);
    }

    public static BigDecimal mul(String s1, int mul) {
        BigDecimal bigDecimal1 = new BigDecimal(s1);
        return bigDecimal1.multiply(BigDecimal.TEN.pow(mul));
    }
}
