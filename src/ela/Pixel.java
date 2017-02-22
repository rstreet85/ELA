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

//Convenient place for default pixel values.
public enum Pixel {
    
    BLACK(0, 0, 0),
    BLUE(0, 0, 255),
    CYAN(0, 255, 255),
    GREEN(0, 255, 0),
    MAGENTA(255, 0, 255),
    ORANGE(255, 128, 0),
    RED(255, 0, 0),
    WHITE(255, 255, 255),
    YELLOW(255, 255, 0);
    
    private int[] rgb = new int[3];
    
    Pixel(int red, int green, int blue) {
        rgb[0] = red;
        rgb[1] = green;
        rgb[2] = blue;
    }

    public int[] RGB() {
        return rgb;
    }
}