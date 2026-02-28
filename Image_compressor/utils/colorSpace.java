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
    public static int toRGB(double y, double cb, double cr) {
    // Re-center the Luminance (Y) channel
    double Y = y + 128.0; 
    
    // High-precision JPEG conversion constants
    int r = (int) Math.round(Y + 1.402 * cr);
    int g = (int) Math.round(Y - 0.344136 * cb - 0.714136 * cr);
    int b = (int) Math.round(Y + 1.772 * cb);

    // CRITICAL: Clamp to prevent bit-overflow (the source of neon colors)
    r = Math.max(0, Math.min(255, r));
    g = Math.max(0, Math.min(255, g));
    b = Math.max(0, Math.min(255, b));

    return (255 << 24) | (r << 16) | (g << 8) | b;
}
}