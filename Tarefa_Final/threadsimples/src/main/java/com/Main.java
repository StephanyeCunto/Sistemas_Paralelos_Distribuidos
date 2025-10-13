package com;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.bytedeco.javacpp.Loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException{
        Loader.load(org.bytedeco.opencv.opencv_java.class);

        File imgFile = getResourceAsFile("/maspcomruido.png");
        Mat src = Imgcodecs.imread(imgFile.getAbsolutePath());

        Mat dst = new Mat(src.size(), src.type());
        int numThreads = Integer.parseInt(args[0]);

        parallelBlur(src, dst, numThreads);

        Imgcodecs.imwrite("saida_paralelizada_simples.png", dst);
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


    private static void parallelBlur(Mat src, Mat dst, int numThreads) {
        int height = src.rows();
        int width = src.cols();
        int blockHeight = height / numThreads;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int yStart = i * blockHeight;
            final int yEnd = (i == numThreads - 1) ? height : (yStart + blockHeight);

            Thread t = new Thread(() -> processBlock(src, dst, width, yStart, yEnd));
            t.setName("Thread Simples("+i+")");

            threads.add(t);
            t.start();
        }

        waitThreads(threads);
    }

    private static void processBlock(Mat src, Mat dst, int width, int yStart, int yEnd) {
        Rect roi = new Rect(0, yStart, width, yEnd - yStart);
        Mat subSrc = src.submat(roi);
        Mat subDst = new Mat(subSrc.size(), subSrc.type());

        Imgproc.blur(subSrc, subDst, new Size(111, 111));

     //   synchronized (dst) {
            subDst.copyTo(new Mat(dst, roi));
      //  }
    }

    private static void waitThreads(List<Thread> threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}