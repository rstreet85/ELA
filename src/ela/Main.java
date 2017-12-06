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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * This class is for testing, demonstrating use of error-level analysis library.
 * 
 * @author Robert Streetman
 */
public class Main {
    //Default settings
    private static enum Mode { FILE, FOLDER, ERR };             //Strings for different modes
    private static final float COMP_PCT_DEF = 0.95f;            //Default JPG recompression percentage
    private static final int DIFF_THRESH_DEF = 15;              //Default threshold for error level difference
    private static final int[] MASK_RGB = Pixel.MAGENTA.RGB();  //Default mask pixel color

    public static void main(String[] args) {
        //Check whether user wants single image file or all image files in directory
        File inputFile = new File(args[0]);
        boolean exists = inputFile.exists();
        boolean isFile = inputFile.isFile();
        boolean isFolder = inputFile.isDirectory();
        Mode mode = (exists && isFile) ? Mode.FILE : (exists && isFolder) ? Mode.FOLDER : Mode.ERR;
        String filename = null;
        
        switch (mode) {
            //Run ELA on a single image file...
            case FILE:
                filename = getFileName(args[0]);
                System.out.format("%nExamining File %s...%n", filename);

                runELA(inputFile, filename, MASK_RGB);
                System.out.format("%nFinished...%n%n");
                break;
            //Run ELA on all images in designated folder....
            case FOLDER:
                //Make a list only of jpg, png files for now...
                List<Path> imageFiles = new ArrayList();

                try (DirectoryStream<Path> stream = 
                        Files.newDirectoryStream(Paths.get(args[0]), "*.{jpg,jpeg,png}")) {
                    for (Path filePath : stream) {
                        //System.out.format("Adding File %s...%n", filePath.toString());
                        imageFiles.add(filePath);
                    }
                } catch(IOException ex) {
                    System.out.format("%nError Creating File List: %s...%n", ex.getMessage());
                }

                for (Path filePath : imageFiles) {
                    filename = getFileName(filePath.toString());

                    System.out.format("Examining File %s...", filePath.toString());
                    runELA(filePath.toFile(), filename, MASK_RGB);
                }

                System.out.println("Finished...");
                break;
        }
    }
    
    /**
     * Send this class an image File, a string for desired output file name, and an int[]
     * RGB value to run ELA on that File, masking the difference with the given RGB value
     * and saving the file to the file name.
     * 
     * @param inputFile Image file input
     * @param filename  Desired name for the output file.
     * @param maskValue RGB value to use when masking aberrant pixels
     */
    private static void runELA(File inputFile, String filename, int[] maskValue) {
        try {
            //Read image and create compressed version
            BufferedImage imgInput = ImageIO.read(inputFile);
            BufferedImage imgCompressed = ELA.GetCompressedImage(imgInput, COMP_PCT_DEF);
            
            //Get difference image and save it
            BufferedImage imgDifference = ELA.GetDifferenceImage(imgInput, imgCompressed);
            ImageIO.write(imgDifference, "jpg", new File(filename + "_difference.jpg"));

            //Mask original image with difference image and save it
            BufferedImage imgMasked = ImageUtils.MaskImages(imgInput, imgDifference, maskValue,
                    DIFF_THRESH_DEF);
            ImageIO.write(imgMasked, "jpg", new File(filename + "_masked.jpg"));
        } catch(IOException ex) {
            System.out.format("RunELA: Error Running Error Level Analysis on file %s: %s...%n", filename, ex.getMessage());
        }
    }
    
    /**
     * Send this method a string of the input file name to get a modified string for the
     * output file name.
     * 
     * @param name String, file name of input file.
     * @return String, file name for output file.
     */
    private static String getFileName(String name) {
        //TODO: Make this more advanced later, not robust enough
        int length = name.length();
        
        if (name.charAt(length - 4) == '.') {
            name = name.substring(0, length - 4);
        } else if (name.charAt(length - 5) == '.') { //For .jpeg
            name = name.substring(0, length - 5);
        }
        
        return name;
    }
}
