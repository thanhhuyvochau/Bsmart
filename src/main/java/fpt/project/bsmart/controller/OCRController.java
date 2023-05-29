package fpt.project.bsmart.controller;



import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/ocr/")
public class OCRController {

    @Autowired
    Tesseract tesseract;

    @PostMapping("/ocr")
    public String ocr(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {


        Tesseract tesseract = new Tesseract();


        tesseract.setDatapath("C://Users//nguye//OneDrive//Desktop//tessdata");


        tesseract.setLanguage("vie+eng");


        BufferedImage image = ImageIO.read(file.getInputStream());


        String result = tesseract.doOCR(image);

        return result;

    }



}