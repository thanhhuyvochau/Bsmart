package fpt.project.bsmart.controller;



import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


import net.sourceforge.tess4j.util.LoadLibs;
import nu.pattern.OpenCV;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import java.io.*;
import java.util.ArrayList;

import java.util.List;

@RestController
@RequestMapping("/api/ocr")
public class OCRController {

    @Autowired
    Tesseract tesseract;


    @PostMapping("/ocr")
    public   List<String> ocr(@RequestParam("file") MultipartFile imageFile) throws IOException, TesseractException {

        List<String> stringList = new ArrayList<>();

        // Load OpenCV library
        OpenCV.loadShared();

        // Convert MultipartFile to File
        File tempFile = convertMultipartFileToFile(imageFile);

        // Load image using OpenCV
        Mat image = Imgcodecs.imread(tempFile.getAbsolutePath());
        // Convert image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Apply threshold to image
        Mat threshImage = new Mat();
        Imgproc.threshold(grayImage, threshImage, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        String vie = readImageWithVie(threshImage);
        String eng = readImageWithEng(threshImage);
        stringList.add(vie) ;
        stringList.add(eng) ;
        return stringList;


    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    private String readImageWithVie(Mat threshImage) throws IOException, TesseractException {
        File tempFile2 = File.createTempFile("temp", ".png");
        Imgcodecs.imwrite(tempFile2.getAbsolutePath(), threshImage);
        ITesseract tesseract = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        tesseract.setLanguage("vie");
        String text = tesseract.doOCR(new File(tempFile2.getAbsolutePath()));
        return text;

    }

    private String readImageWithEng(Mat threshImage) throws IOException, TesseractException {
        File tempFile2 = File.createTempFile("temp", ".png");
        Imgcodecs.imwrite(tempFile2.getAbsolutePath(), threshImage);
        ITesseract tesseract = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        tesseract.setLanguage("eng");
        String text = tesseract.doOCR(new File(tempFile2.getAbsolutePath()));
        return text;
    }


}