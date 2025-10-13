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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws IOException  {
        Loader.load(org.bytedeco.opencv.opencv_java.class);

        File imgFile = getResourceAsFile("/maspcomruido.png");
        Mat src = Imgcodecs.imread(imgFile.getAbsolutePath());        
        Mat dst = new Mat(src.size(), src.type());

        int numThreads =Integer.parseInt(args[0]); 

     AtomicInteger threadNumber = new AtomicInteger(1);

    ForkJoinPool forkjoin = new ForkJoinPool(
            numThreads,
            pool1 -> {
                ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool1);
                worker.setName("Thread Fork Join(" + threadNumber.getAndIncrement()+")");
                return worker;
            },null,false
    );

    forkjoin.invoke(new BlurTask(src, dst, 0, src.rows()));

    Imgcodecs.imwrite("saida_paralelizada_forkjoin.png", dst);
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

    static class BlurTask extends RecursiveAction {

        private final Mat src;
        private final Mat dst;
        private final int yStart;
        private final int yEnd;

        public BlurTask(Mat src, Mat dst, int yStart, int yEnd) {
            this.src = src;
            this.dst = dst;
            this.yStart = yStart;
            this.yEnd = yEnd;
        }

        @Override
        protected void compute() {
            processBlock();
        }

        private void processBlock() {
            Rect roi = new Rect(0, yStart, src.cols(), yEnd - yStart);
            Mat subSrc = src.submat(roi);
            Mat subDst = new Mat(subSrc.size(), subSrc.type());

            Imgproc.blur(subSrc, subDst, new Size(111, 111));

            synchronized (dst) {
                subDst.copyTo(new Mat(dst, roi));
            }
        }
    }
}
