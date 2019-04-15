//package com.oleptr.test.core;
//
//
//import java.lang.annotation.Documented;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//
///**
// * Created by gagandeep on 28/7/16.
// */
//
//public class NumberGenerator {
//
//    private long min;
//    private long max;
//
//    public NumberGenerator( )  {
//
//    }
//    public NumberGenerator(Number annotation )  {
//
//    }
//
//
//
//
//    private static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = new BigDecimal(value);
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }
//}
