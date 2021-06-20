import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ProcessingImage {
    private Mat mat = new Mat();
    private Mat src = new Mat();
    private ProcessingImage instance = new ProcessingImage();
    private ProcessingImage(){}
    public ProcessingImage getInstance(){
        return instance;
    }

    public ProcessingImage loadImage(String path){
        mat = Imgcodecs.imread(path);
        src = mat.clone();
        return this;
    }

    public ProcessingImage rangeImage(){
        Core.inRange(mat, new Scalar(110, 32, 23), new Scalar(145, 204, 168), mat);
        return this;
    }

    public ProcessingImage morph(Size size, int typeShape,int typeMorph, Mat mat){
        Mat structuringEle = Imgproc.getStructuringElement(typeShape, size);
        Imgproc.morphologyEx(mat, mat, typeMorph, structuringEle);
        return this;
    }

    public ProcessingImage binaryThreshold(int min, int max){
        Imgproc.threshold(mat, mat, min, max, Imgproc.THRESH_BINARY);
        return this;
    }

    public List<Rect> boundSymbol(){
        // Find Contours
        Mat hierarchyMat = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mat, contours, hierarchyMat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Rect> rects = new ArrayList<>();

        for(int i=0;i<contours.size();i++) {
            MatOfPoint m = contours.get(i);
            double area = Imgproc.contourArea(m);
            Rect rect;
//            if(area > 500 && area < 3000 && hierarchyMat.get(0, i)[3] == -1) {
                rect =  Imgproc.boundingRect(m);
                rects.add(rect);
//            }
            m = null;
            rect = null;
        }
        return rects;
    }

    public void cropImage(Rect rect, String nameFile) {
        Mat cropImage = src.submat(rect);
        Mat resizeImage = new Mat();
        Imgproc.resize(cropImage, resizeImage, new Size(45, 45));
        Imgcodecs.imwrite(nameFile, resizeImage);
    }

    public Mat getDes() {
        return mat;
    }

    public Mat getSrc(){
        return src;
    }
}
