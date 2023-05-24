package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.service.FaceDetectionService;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


@Service
public class FaceDetectionServiceImpl implements FaceDetectionService {

//    private Resource faceResource = (Resource) new ClassPathResource("haarcascades/haarcascade_frontalface_alt.xml");

    public File detectAndCropFace(MultipartFile imageFile) throws IOException {

//        CascadeClassifier faceCascade = new CascadeClassifier(Objects.requireNonNull(getClass().getResource("/haarcascades/haarcascade_frontalface_alt.xml")).getPath());
        CascadeClassifier faceCascade = new CascadeClassifier ();
        byte[] imageData = imageFile.getBytes();
        Mat image = Imgcodecs.imdecode(new MatOfByte(imageData), Imgcodecs.IMREAD_COLOR);


        // Use OpenCV to process the image
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);


        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(grayImage, faces);


        if (faces.empty()) {
            return null;
        }


        Rect faceRect = faces.toArray()[0];
        Mat croppedImage = new Mat(image, faceRect);


        File outputFile = File.createTempFile("face_", ".jpg");
        Imgcodecs.imwrite(outputFile.getAbsolutePath(), croppedImage);

        return outputFile;
    }
}

