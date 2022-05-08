package com.mc2022.template;

public class StrideLengths {
    // From https://www.verywellfit.com/set-pedometer-better-accuracy-3432895
    static int[] MEN_STRIDE_5 = new int[]{
            25,
            25,
            26,
            26,
            27,
            27,
            27,
            28,
            28,
            29,
            29,
            29,
    };

    static int[] MEN_STRIDE_6 = new int[]{
            30,
            31,
            31,
            32,
            32,
            33,
            34,
            34,
            34,
            35,
            35,
            36,
    };

    static int[] WOMEN_STRIDE_5 = new int[]{
            25,
            25,
            26,
            26,
            26,
            27,
            27,
            28,
            28,
            28,
            29,
            29,
    };

    static int[] WOMEN_STRIDE_6 = new int[]{
            30,
            30,
            31,
            31,
            31,
            32,
            32,
            33,
            33,
            34,
            34,
            35,
    };

    private static float inchToMeter(float inch) {
        float meter;
        meter = (float) (inch / 39.37);
        return meter;
    }
    /*
     * gender: 0 for men and 1 for women
     * feet: 5 or 6
     * inches: 0-11
     */
    public static float strideMeter(int gender,int feet, int inches) {
        if (gender==0){
            if (feet==5) {
                return inchToMeter(MEN_STRIDE_5[inches]);
            }
            else if (feet==6) {
                return inchToMeter(MEN_STRIDE_6[inches]);
            }
        }
        else if (gender==1){
            if (feet==5) {
                return inchToMeter(WOMEN_STRIDE_5[inches]);
            }
            else if (feet==6) {
                return inchToMeter(WOMEN_STRIDE_6[inches]);
            }
        }
        return -1;
    }

    public static float strideInch(int gender, int feet, int inches) {
        if (gender==0){
            if (feet==5) {
                return MEN_STRIDE_5[inches];
            }
            else if (feet==6) {
                return MEN_STRIDE_6[inches];
            }
        }
        else if (gender==1){
            if (feet==5) {
                return WOMEN_STRIDE_5[inches];
            }
            else if (feet==6) {
                return WOMEN_STRIDE_6[inches];
            }
        }
        return -1;
    }

}