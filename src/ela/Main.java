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
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Main {
    
    //Default settings
    private static enum Mode { FILE, FOLDER, ERR };
    private static final float COMPRESSION_PERCENT_DEFAULT = 0.95f;
    private static final int DIFF_THRESH_DEFAULT = 15;
    private static final int[] MASK_RGB = Pixel.MAGENTA.RGB();

    public static void main(String[] args) {
        //Check whether user wants single image file or all image files in directory
        File inputFile = new File(args[0]);
        boolean exists = inputFile.exists();
        boolean isFile = inputFile.isFile();
        boolean isFolder = inputFile.isDirectory();
        Mode mode = (exists && isFile) ? Mode.FILE : (exists && isFolder) ? Mode.FOLDER : Mode.ERR;
        
        if (mode.equals(Mode.FILE)) {   //Single file mode
            String filename = getFileName(args[0]);
            System.out.println("\nExamining File " + filename + "...");
            
            runELA(inputFile, filename, MASK_RGB);
            System.out.println("\nFinished...\n\n");
        } else if (mode.equals(Mode.FOLDER)) {  //Directory mode
            //Make a list only of jpg, png files for now...
            List<Path> imageFiles = new ArrayList();
            
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(args[0]), "*.{jpg,jpeg,png}")) {
                for (Path filePath : stream) {
                    System.out.println("Adding File " + filePath.toString() + "...");
                    imageFiles.add(filePath);
                }
            } catch(IOException ex) {
                System.out.println("\nError Creating File List...\n" + ex.getMessage() + "\n");
            }
            
            for (Path filePath : imageFiles) {
                String filename = getFileName(filePath.toString());
                
                System.out.println("Examining File " + filePath.toString() + "...");
                runELA(filePath.toFile(), filename, MASK_RGB);
            }
            
            System.out.println("Finished...");
        } else {
            System.out.println("Unknown mode...\nFinished...\n");
        }
    }
    
    /*
        Runs error level analysis for file using settings
    */
    private static void runELA(File inputFile, String filename, int[] maskValue) {
        try {
            //Read image and create compressed version
            BufferedImage imgInput = ImageIO.read(inputFile);
            BufferedImage imgCompressed = ELA.GetCompressedImage(imgInput, COMPRESSION_PERCENT_DEFAULT);
            
            //Get difference image and save it
            BufferedImage imgDifference = ELA.GetDifferenceImage(imgInput, imgCompressed);
            ImageIO.write(imgDifference, "jpg", new File(filename + "_difference.jpg"));

            //Mask original image with difference image and save it
            BufferedImage imgMasked = ImgIO.MaskImages(imgInput, imgDifference, maskValue, DIFF_THRESH_DEFAULT);
            ImageIO.write(imgMasked, "jpg", new File(filename + "_masked.jpg"));
        } catch(IOException ex) {
            System.out.println("Error Running Error Level Analysis..." + ex.getMessage() + "\n");
        }
    }
    
    /*
        Trim file extension from file name
    */
    private static String getFileName(String name) {
        int length = name.length();
        
        if (name.charAt(length - 4) == '.') {
            name = name.substring(0, length - 4);
        } else if (name.charAt(length - 5) == '.') { //For .jpeg
            name = name.substring(0, length - 5);
        }
        
        return name;
    }
}
