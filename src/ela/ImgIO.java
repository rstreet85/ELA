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
 * @author Robert Streetman
 */
package ela;

import java.awt.image.BufferedImage;

public class ImgIO {
    
    /*
     * Accepts BufferedImage, returns int[][][] array of RGB (0-255) values for pixels.
     */
    public static int[][][] RGBArray(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        int[][][] rgb = new int[height][width][3];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rgb[i][j] = intRGB(img.getRGB(j, i));
            }
        }
        
        return rgb;
    }
    
    /*
     * Accepts int[][][] array of RGB values, returns BufferedImage
     */
    public static BufferedImage RGBImg(int[][][] raw) {
        int height = raw.length;
        int width = raw[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                img.setRGB(j, i, (raw[i][j][0] << 16) | (raw[i][j][1] << 8) | (raw[i][j][2]));
            }
        }
        
        return img;
    }
    
    /*
     * Accepts 32-bit pixel value from BufferedImage, returns int[] array of RGB values
     */
    private static int[] intRGB(int bits) {
        int[] out = { (bits >> 16) & 0xff, (bits >> 8) & 0xff, bits & 0xff };
        return out;
    }
    
    /*
     * Accepts two RGB images. Outputs image with original pixel values from base image,
     * except where mask image pixel values exceed the threshold, where it writes the
     * mask color instead.
     */
    public static BufferedImage MaskImages(BufferedImage baseImage, BufferedImage maskImage, int[] maskColor, int threshold) {
        BufferedImage result = null;
        int height = baseImage.getHeight();
        int width = baseImage.getWidth();
        
        if (maskColor.length == 3 && height == maskImage.getHeight() && width == maskImage.getWidth()) {
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
                    
                    //If pixel madnitude > threshold, then mask w/ color
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
