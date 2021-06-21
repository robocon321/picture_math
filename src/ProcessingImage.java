import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ProcessingImage {
    private Mat mat;
    private Mat src;
    private static ProcessingImage instance = new ProcessingImage();
    private ProcessingImage(){}
    public static ProcessingImage getInstance(){
        return instance;
    }

    public ProcessingImage loadImage(String path){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        mat = Imgcodecs.imread(path);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);
        src = mat.clone();
        return this;
    }

    public ProcessingImage buildRangeImage(){
        Core.inRange(mat, new Scalar(109, 81, 37), new Scalar(163, 198, 164), mat);
        return this;
    }

    public ProcessingImage buildMorph(Size size, int typeShape,int typeMorph){
        Mat structuringEle = Imgproc.getStructuringElement(typeShape, size);
        Imgproc.morphologyEx(mat, mat, typeMorph, structuringEle);
        return this;
    }

    public ProcessingImage buildBinaryThreshold(int min, int max){
        Imgproc.threshold(mat, mat, min, max, Imgproc.THRESH_BINARY);
        return this;
    }

    public ProcessingImage buildBlur(int size){
        Imgproc.medianBlur(mat, mat, size);
        return this;
    }

    public List<Rect> boundSymbol(){
        // Find Contours
        Mat hierarchyMat = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mat, contours, hierarchyMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Rect> rects = new ArrayList<>();

        for(int i=0;i<contours.size();i++) {
            if(hierarchyMat.get(0, i)[3] == -1){
                MatOfPoint m = contours.get(i);
                double area = Imgproc.contourArea(m);
                Rect rect =  Imgproc.boundingRect(m);
                rects.add(rect);
                m = null;
                rect = null;
            }
        }
        return rects;
    }

    public void cropImage(Rect rect, String nameFile) {
        Mat cropImage = src.submat(rect);
        Mat resizeImage = new Mat();
        Imgproc.resize(cropImage, resizeImage, new Size(45, 45));
        Imgcodecs.imwrite(nameFile, resizeImage);
    }

    public void save(){
        Imgcodecs.imwrite("process/result.jpg", mat);
    }

    public Mat getDes() {
        return mat;
    }

    public Mat getSrc(){
        return src;
    }

    public static void main(String[] args) {
    }
}
