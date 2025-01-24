package com.example.testuser1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PDFPathManager {

    private static final String CONFIG_FILE = "pdf_paths.properties";

    public static void savePDFPath(String path) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.setProperty("pdfPath", path);
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPDFPath() {
        try (InputStream input = PDFPathManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
                return properties.getProperty("pdfPath");
            } else {
                System.err.println("Le fichier " + CONFIG_FILE + " est introuvable dans le répertoire src/main/resources.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération du chemin du PDF : " + e.getMessage());
            return null;
        }
    }
}
