module com.example.testuser1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires stripe.java;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires charm.glisten;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.controlsfx.controls;
    requires jBCrypt;


    opens com.example.testuser1 to javafx.fxml;
    exports com.example.testuser1;
    opens models;
    opens utils;
}
