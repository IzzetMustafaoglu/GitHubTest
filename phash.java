
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class phash {
    public static void main(String[] args) {
        // OpenCV kütüphanesini yükleme
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Resimleri okuma
        Mat image1 = Imgcodecs.imread("monalisa.jpg");
        Mat image2 = Imgcodecs.imread("monalisa2.png");

        // Resimleri gri tonlamalıya dönüştürme
        Mat grayImage1 = new Mat();
        Mat grayImage2 = new Mat();
        Imgproc.cvtColor(image1, grayImage1, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(image2, grayImage2, Imgproc.COLOR_BGR2GRAY);

        // Resim hash'lerini hesaplama (resmi herhangi bir yönde döndürerek)
        String hash1 = dHash(grayImage1);
        String hash2 = dHash(grayImage2);

        // Hamming mesafesini hesaplama
        int hammingDistance = hammingDistance(hash1, hash2);

        // Benzerlik yüzdesini hesaplama
        double maxHashBits = Math.max(hash1.length(), hash2.length());
        double similarity = 1.0 - (hammingDistance / maxHashBits);

        System.out.println("Benzerlik Yüzdesi: " + similarity * 100 + "%");
    }

    // dHash hesaplama
    public static String dHash(Mat image) {
        int width = 8, height = 8;
        Imgproc.resize(image, image, new Size(width + 1, height));
        StringBuilder hash = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double leftPixel = image.get(y, x)[0];
                double rightPixel = image.get(y, x + 1)[0];
                hash.append(leftPixel < rightPixel ? "1" : "0");
            }
        }

        return hash.toString();
    }

    // Hamming mesafesi hesaplama
    public static int hammingDistance(String hash1, String hash2) {
        if (hash1.length() != hash2.length()) {
            throw new IllegalArgumentException("Hashes must be of the same length");
        }

        int distance = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                distance++;
            }
        }

        return distance;
    }
}