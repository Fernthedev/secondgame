package com.github.fernthedev;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameMathUtil {


    public static float absDif(float x, float x2) {
        return Math.abs(x - x2);
    }

    public static int absDif(int x, int x2) {
        return Math.abs(x - x2);
    }

    public static double absDif(double x, double x2) {
        return Math.abs(x - x2);
    }

    public static long absDif(long x, long x2) {
        return Math.abs(x - x2);
    }

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */
    public static float clamp(float var, float min, float max) {
        //System.out.println(min + " is min");
        //System.out.println(var + " is normal");
        if(var >= max) {
          //  System.out.println(max + " is max");
            return max;
        }
        else return Math.max(var, min);
    }

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */
    public static double clamp(double var, double min, double max) {
        //System.out.println(min + " is min");
        //System.out.println(var + " is normal");
        if(var >= max) {
            //  System.out.println(max + " is max");
            return max;
        }
        else return Math.max(var, min);
    }

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */
    public static long clamp(long var, long min, long max) {
        //System.out.println(min + " is min");
        //System.out.println(var + " is normal");
        if(var >= max) {
            //  System.out.println(max + " is max");
            return max;
        }
        else return Math.max(var, min);
    }

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */
    public static int clamp(int var, int min, int max) {
        //System.out.println(min + " is min");
        //System.out.println(var + " is normal");
        if(var >= max) {
            //  System.out.println(max + " is max");
            return max;
        }
        else return Math.max(var, min);
    }
}
