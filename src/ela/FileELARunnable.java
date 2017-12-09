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
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This task will let a program multithread the ELA process on files.
 *
 * @author Robert Streetman
 */
public class FileELARunnable implements Runnable {
    private final File IMAGE_FILE;
    private final String FILE_NAME;
    private final float COMP_PCT_DEF;
    private final int DIFF_THRESH_DEF;
    private final int[] PIXEL;
    
    FileELARunnable(String filename, File file, int[] pix, float compression, int thresh) {
        FILE_NAME = filename;
        PIXEL = pix;
        IMAGE_FILE = file;
        COMP_PCT_DEF = compression;
        DIFF_THRESH_DEF = thresh;
    }
    
    @Override
    public void run() {
        System.out.format("%nExamining File %s...%n", FILE_NAME);
        
        try {
            //Read image and create compressed version
            BufferedImage imgInput = ImageIO.read(IMAGE_FILE);
            BufferedImage imgCompressed = ELA.GetCompressedImage(imgInput, FILE_NAME, COMP_PCT_DEF);
            
            //Get difference image and save it
            BufferedImage imgDifference = ELA.GetDifferenceImage(imgInput, imgCompressed);
            ImageIO.write(imgDifference, "jpg", new File(FILE_NAME + "_difference.jpg"));

            //Mask original image with difference image and save it
            BufferedImage imgMasked = ImageUtils.MaskImages(imgInput, imgDifference, PIXEL,
                    DIFF_THRESH_DEF);
            ImageIO.write(imgMasked, "jpg", new File(FILE_NAME + "_masked.jpg"));
        } catch(IOException ex) {
            System.out.format("RunELA: Error Running Error Level Analysis on file %s: %s...%n", FILE_NAME, ex.getMessage());
        }
    }
}
