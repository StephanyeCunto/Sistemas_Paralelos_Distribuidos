package com;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.bytedeco.javacpp.Loader;

public class Main {
    public static void main(String[] args) throws IOException {
        Loader.load(org.bytedeco.opencv.opencv_java.class);

        File imgFile = getResourceAsFile("/maspcomruido.png");
        Mat src = Imgcodecs.imread(imgFile.getAbsolutePath());  
        Mat dst = new Mat();

        Imgproc.blur(src, dst, new Size(111, 111));
 
        Imgcodecs.imwrite("saida_paralelizada_sequencial.png", dst);
    }

    private static File getResourceAsFile(String resourcePath) throws IOException {
        InputStream is = Main.class.getResourceAsStream(resourcePath);

        File tempFile = Files.createTempFile("temp_image", ".png").toFile();
        tempFile.deleteOnExit();

        try (FileOutputStream os = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }
}