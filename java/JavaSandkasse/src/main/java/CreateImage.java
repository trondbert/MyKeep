import static java.lang.Math.pow;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

/**
 * @author trond.
 */
public class CreateImage {

    public static void main(final String[] args) {
        try {
            final FileReader fileReader = new FileReader("/tmp/inversemod.csv");
            final Scanner scanner = new Scanner(fileReader);
            final String[][] pixels = new String[500][500];
            int height = 0;
            while (scanner.hasNextLine()) {
                pixels[height] = scanner.nextLine().split(";");
                height++;
            }
            final List<BufferedImage> bufferedImage = getOneImagePerValue(pixels, height);
            int picIndex = 1;
            for (final BufferedImage image : bufferedImage) {
                final ImageWriter imageWriter = new PNGImageWriter(new PNGImageWriterSpi());
                final String padded = "00000000" + picIndex;
                final File file = new File("/tmp/pic" + padded.substring(padded.length() - 4) + ".png");
                imageWriter.setOutput(new FileImageOutputStream(file));
                imageWriter.write(image);
                picIndex++;
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }

    }

    private static List<BufferedImage> getOneImage(final String[][] pixels, final int height) {
        final BufferedImage bufferedImage = new BufferedImage(height, height, BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < height; col++) {
                final int value = Integer.parseInt(pixels[row][col]);
                final int adjusted = value * 255 / 50;
                final double rgb = adjusted * (pow(2, 16) + pow(2, 8) + 1);
                bufferedImage.setRGB(row, col, (int) rgb);
            }
        }
        return Collections.singletonList(bufferedImage);
    }


    private static List<BufferedImage> getOneImagePerValue(final String[][] pixels, final int height) {
        final List<BufferedImage> objects = new ArrayList<>();
        for (int value = 1; value < 46; value++) {
            final BufferedImage bufferedImage = new BufferedImage(height, height, BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < height; col++) {
                    final int pixelIn = Integer.parseInt(pixels[row][col]);
                    final double rgb = pixelIn == value ? pow(2, 24) - 1 : 0;
                    bufferedImage.setRGB(row, col, (int) rgb);
                }
            }
            objects.add(bufferedImage);
        }
        return objects;
    }

}
