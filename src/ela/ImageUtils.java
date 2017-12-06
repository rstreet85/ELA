/**
 * Copyright 2017 Robert Streetman
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ela;

import java.awt.image.BufferedImage;

/**
 * This class contains utility methods for moving data into/out of image files.
 * 
 * @author Robert Streetman
 */
//TODO:Add exceptions for bad input
public class ImageUtils {
    
    /**
     * Send this method a BufferedImage to get an RGB array (value 0-255).
     * 
     * @param img   BufferedImage, the input image from which to extract RGB
     * @return      A 3-dimensional array of RGB values from image
     */
    public static int[][][] RGBArray(BufferedImage img) {
        int[][][] rgb = null;
        int height = img.getHeight();
        int width = img.getWidth();
        
        if (height > 0 && width > 0) {
            rgb = new int[height][width][3];

            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    rgb[row][column] = intRGB(img.getRGB(column, row));
                }
            }
        }
        
        return rgb;
    }
    
    /**
     * Send this method an array of RGB pixels (int) to get a BufferedImage.
     * 
     * @param raw   int[][][] representing RGB pixels of image.
     * @return BufferedImage built from RGB array
     */
    public static BufferedImage RGBImg(int[][][] raw) {
        BufferedImage img = null;
        int height = raw.length;
        int width = raw[0].length;
        
        if (height > 0 && width > 0 || raw[0][0].length == 3) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    img.setRGB(column, row, (raw[row][column][0] << 16)
                            | (raw[row][column][1] << 8) | (raw[row][column][2]));
                }
            }
        }
        
        return img;
    }
    
    /**
     * Send this method a 32-bit pixel value from BufferedImage to get the RGB.
     * 
     * @param bits  The 32-bit BufferedImage pixel value
     * @return      RGB values extracted from pixel  
     */
    private static int[] intRGB(int bits) {
        //Java rgb values are actually 4 bytes (r,g,b,a) pressed into one 32-bit integer
        int[] rgb = { (bits >> 16) & 0xff, (bits >> 8) & 0xff, bits & 0xff };
        
        //Don't propagate bad pixel values
        for (int i = 0; i < 3; i++) {
            if (rgb[i] < 0) {
                rgb[i] = 0;
            } else if (rgb[i] > 255) {
                rgb[i] = 255;
            }
        }
        
        return rgb;
    }
    
    /**
     * Send this method a BufferedImage base, a BufferedImage mask, an int[] mask color,
     * and an int threshold to mask the base image with all pixels in the mask image that
     * meet/exceed the threshold with the supplied color.
     * 
     * @param baseImage     The base image which is to be masked over.
     * @param maskImage     The masking image. This is a difference image.
     * @param maskColor     The RGB pixel values desired for the mask color.
     * @param threshold     Max pixel value (r+g+b) allowed before marking pixel as changed.
     * @return              BufferedImage where the base image has 'changed' pixels masked.
     */
    public static BufferedImage MaskImages(BufferedImage baseImage, BufferedImage maskImage,
            int[] maskColor, int threshold) {
        BufferedImage result = null;
        int height = baseImage.getHeight();
        int width = baseImage.getWidth();
        
        if (maskColor.length == 3 && height == maskImage.getHeight()
                && width == maskImage.getWidth()) {
            int[][][] imgOrig = RGBArray(baseImage);
            int[][][] imgMask = RGBArray(maskImage);
            int[][][] imgResult = new int[height][width][3];
            
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    //Measure total magnitude of pixel
                    int sumMaskPixel = 0;
                    
                    for (int band = 0; band < 3; band++) {
                        sumMaskPixel += imgMask[r][c][band];
                    }
                    
                    //If pixel magnitude > threshold, then mask w/ color
                    if (sumMaskPixel > threshold) {
                        imgResult[r][c] = maskColor;
                    } else {
                        imgResult[r][c] = imgOrig[r][c];
                    }
                }
            }
            
            result = RGBImg(imgResult);
        }
        
        return result;
    }
}
