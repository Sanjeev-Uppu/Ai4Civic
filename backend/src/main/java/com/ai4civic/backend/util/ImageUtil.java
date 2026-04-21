package com.ai4civic.backend.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static File convertToSafeImage(File inputFile) throws IOException {

        BufferedImage image = ImageIO.read(inputFile);

        if (image == null) {
            System.out.println("⚠ Unsupported image, skipping...");
            return null;
        }

        image = resizeIfNeeded(image, 1000, 1000);

        File output = File.createTempFile("converted_", ".png");
        ImageIO.write(image, "png", output);

        return output;
    }

    private static BufferedImage resizeIfNeeded(BufferedImage img, int maxW, int maxH) {

        int width = img.getWidth();
        int height = img.getHeight();

        if (width <= maxW && height <= maxH) return img;

        float ratio = Math.min((float) maxW / width, (float) maxH / height);

        int newW = Math.round(width * ratio);
        int newH = Math.round(height * ratio);

        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, newW, newH, null);
        g.dispose();

        return resized;
    }
}