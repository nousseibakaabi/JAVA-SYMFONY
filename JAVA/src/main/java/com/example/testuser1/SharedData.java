package com.example.testuser1;

public class SharedData {
    private static String cheminPDF;

    public static String getCheminPDF() {
        return cheminPDF;
    }

    public static void setCheminPDF(String cheminPDF) {
        SharedData.cheminPDF = cheminPDF;
    }
}