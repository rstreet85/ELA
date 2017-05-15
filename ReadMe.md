# ELA - Error Level Analysis
##Description
A pure Java implementation of the Error Level Analysis method demonstrated in Krawetz, N. 'A Picture's Worth... 
Image Analysis and Forensics', 2007.

A suspect image is JPEG-compressed to 95%, and the difference between the original and compressed image is computed,
 then scaled. Areas with high error levels (non-back pixels) represent areas of potential tampering.

By default, program overlays areas of high compression error over the original image in the output (original remains unaltered).

## Usage
Single file mode example:

Linux (Debian): java -jar ELA.jar [filename]

Windows:

macOS:

Directory mode example:

Linux (Debian):

Windows:

macOS: