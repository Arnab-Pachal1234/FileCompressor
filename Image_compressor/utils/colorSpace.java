package utils;
import java.util.*;
public class ColorSpace 
{
    public static double getLumiance(int rgb)
    {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        /*here dc centered is used for making average almost zero and
       reduce  the size of the number of each pixel so that each number reduces and close
       to zero and bits required to store each number reduces and thus the size of the compressed file reduces
        */ 
        double ans = (0.299 * r + 0.587 * g + 0.114 * b) - 128.0;
        return ans;
    }
        /* according to get  cb and cr values we use  the rule s Cb=−0.168736R−0.331264G+0.5B
        and Cr=0.5R−0.418688G−0.081312B

        */
       public static double getCb(int rgb)
       {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        double ans = -0.168736 * r - 0.331264 * g + 0.5 * b;
        return ans;
       }
       public static double getCr(int rgb)
       {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        double ans = 0.5 * r - 0.418688 * g - 0.081312 * b;
        return ans;
       }
}