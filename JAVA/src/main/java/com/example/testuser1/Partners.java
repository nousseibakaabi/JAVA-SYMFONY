package com.example.testuser1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.Partner;
import services.PartnerService;

import java.sql.SQLException;


public class Partners {

    private PartnerService ps = new PartnerService();
    @FXML
    private Label descLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label namePLabel;

    @FXML
    private ImageView partnerIV;

    @FXML
    private Label telLabel;

    @FXML
    private Label typePLabel;
    @FXML
    private AnchorPane partner_aff;
    private Partner partner;
    private Image image;

    public Partners(){}
    public Partners(PartnerService partnerService) {
        this.ps = partnerService;
    }

    public void setData(Partner partner)
    {
        this.partner = partner;


        namePLabel.setText(partner.getNamePartner());
        descLabel.setText(partner.getDescription());
        emailLabel.setText(partner.getEmail());
        telLabel.setText(String.valueOf(partner.getTel()));

        String path = "File:"+partner.getImage();
        image = new Image(path,323,270,false,true);
        partnerIV.setImage(image);
    }

    public void deletePartner(ActionEvent actionEvent) throws SQLException {
        ps.delete(partner.getIdPartner());
        //mise a jour de l'affichage
        GetPartner1 getPartner1Controller = (GetPartner1) partner_aff.getProperties().get("controller");
        getPartner1Controller.partnerDisplay();
    }
}
