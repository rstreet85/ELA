# ELA - Error Level Analysis
## Description
A pure Java implementation of the Error Level Analysis method demonstrated in Krawetz, N. 'A Picture's Worth... Image Analysis and Forensics', 2007.

A suspect image is JPEG-compressed to 95%, and the difference between the original and compressed image is computed, then scaled. Areas with high error levels (non-back pixels) represent areas of potential tampering.
[Wikipedia Article](https://en.wikipedia.org/wiki/Error_level_analysis)

By default, program overlays areas of high compression error over the original image in the output (original remains unaltered).

## Example:
Original Image:
![Original Image](https://github.com/rstreet85/ELA/blob/master/test/original_background.jpg)
Doctored Image:
![Doctored Image](https://github.com/rstreet85/ELA/blob/master/test/test1.jpg)
Error-level Image:
![Error-level Image](https://github.com/rstreet85/ELA/blob/master/test/test1_difference.jpg)
Masked Output Image:
![Masked Image](https://github.com/rstreet85/ELA/blob/master/test/test1_masked.jpg)

## Usage
Single file mode example:
```
test/test1.jpg
```

Directory mode example:
```
test
```