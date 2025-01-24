package com.example.testuser1;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCode {

    @FXML
    private ImageView qrCodeIV;

    @FXML
    private TextField qrTF;

    @FXML
    void qrGenerate(ActionEvent event) {
        try {
            // Générer le code QR
            String qrContent = qrTF.getText();
            String cleanedQrContent = qrContent.replaceAll("[^a-zA-Z0-9]", "_");

            //byte[] qrContentBytes = qrContent.getBytes(StandardCharsets.UTF_8);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, com.google.zxing.BarcodeFormat.QR_CODE, 200, 200);

            // Écrire le code QR dans un fichier
            /*Path path = FileSystems.getDefault().getPath("C:\\Users\\user\\IdeaProjects\\ProjetPIDEV\\src\\main\\resources\\images", cleanedQrContent + ".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);*/


            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF));
            Image image = convertToJavaFXImage(bufferedImage);


            qrCodeIV.setImage(image);


        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    private Image convertToJavaFXImage(BufferedImage bufferedImage) {
        WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }

        return writableImage;
    }

    }