import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
        
        // Resize bound
        List<Rect> results = new ArrayList<>();
        for(Rect r : rects) {
        	Dimension d = getDimension(r);
        	int w = d.width;
        	int h = d.height;
        	if (w / h > 3) {
        		results.add(new Rect(new Point(r.tl().x, r.tl().y + h/2 - w/2), new Point(r.br().x, r.br().y - h/2 + w/2)));
        	} else if(h / w > 3) {
        		results.add(new Rect(new Point(r.tl().x + w/2 - h/2, r.tl().y), new Point(r.br().x - w/2 + h/2, r.br().y)));
        	} else {
        		results.add(r);
        	}
        }
        
        // Find sign =
//        List<Point> pos = new ArrayList<>();
//        for(int i = 0; i < results.size() - 1; i++) {
//        	for(int j = i + 1; j < results.size(); j++) {
//        		System.out.println(isContain(results.get(i), results.get(j)));
//        		if(isContain(results.get(i), results.get(j))) pos.add(new Point(i, j));
//        	}
//        }
//        
//        for(Point p : pos) {
//        	Rect r1 = results.get((int) p.x);
//        	Rect r2 = results.get((int) p.y);
//        	
//        	double xTL = r1.tl().x - r2.tl().x < 0 ? r1.tl().x : r2.tl().x;
//        	double yTL = r1.tl().y - r2.tl().y < 0 ? r1.tl().y : r2.tl().y;
//        	double xBR = r1.br().x - r2.br().x > 0 ? r1.tl().x : r2.tl().x;
//        	double yBR = r1.br().y - r2.br().y > 0 ? r1.tl().y : r2.tl().y;
//        	
//        	results.add(new Rect(new Point(xTL, yTL), new Point(xBR, yBR)));
//        	
//        	results.remove(r1);
//        	results.remove(r2);
//        }
        
        return results;
    }
    
    public Dimension getDimension(Rect r) {
    	return new Dimension((int)(r.br().x - r.tl().x), (int) (r.br().y - r.tl().y));
    }
    
    public boolean isContain(Rect r1, Rect r2) {
    	return false;
    }

    public Mat drawBoundNumber(){
    	// Clear file
    	File f = new File("output");
    	for(File child : f.listFiles()) child.delete();
    	
    	// Save file
        Mat result = new Mat();
        List<Rect> rects = boundSymbol();
        Imgproc.cvtColor(mat, result, Imgproc.COLOR_GRAY2BGR);
        for(Rect rect : rects){
            Imgproc.rectangle(result, rect.tl(), rect.br(), new Scalar(0,0,255), 3);
            cropImage(rect, "output/"+new Date().getTime()+".jpg");
        }
        return result;
    }

    public void saveBound(){
        Imgcodecs.imwrite("bound/bound.jpg", drawBoundNumber());
    }

    public void cropImage(Rect rect, String nameFile) {
    	try {
			Thread.sleep(100);
	        Mat cropImage = mat.submat(rect);
	        Mat resizeImage = new Mat();
	        Imgproc.resize(cropImage, resizeImage, new Size(45, 45));
	        Imgcodecs.imwrite(nameFile, resizeImage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
}
