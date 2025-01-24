package models;

import java.util.Date;

public class Etablissement {

    int ID_Etablissement;

    String Img_Etablissement;
    String Nom_Etablissement;
    String Adresse_Etablissement;
    String Type_Etablissement;
    int Tel_Etablissement;
    String Directeur_Etablissement;

    Date Date_Fondation;


    public Etablissement(int ID_Etablissement, String img_Etablissement, String nom_Etablissement, String adresse_Etablissement, String type_Etablissement, int tel_Etablissement, String directeur_Etablissement, Date date_Fondation) {
        this.ID_Etablissement = ID_Etablissement;
        Img_Etablissement = img_Etablissement;
        Nom_Etablissement = nom_Etablissement;
        Adresse_Etablissement = adresse_Etablissement;
        Type_Etablissement = type_Etablissement;
        Tel_Etablissement = tel_Etablissement;
        Directeur_Etablissement = directeur_Etablissement;
        Date_Fondation = date_Fondation;
    }

    public Etablissement( String img_Etablissement, String nom_Etablissement, String adresse_Etablissement, String type_Etablissement, int tel_Etablissement, String directeur_Etablissement, Date date_Fondation) {
        Img_Etablissement = img_Etablissement;
        Nom_Etablissement = nom_Etablissement;
        Adresse_Etablissement = adresse_Etablissement;
        Type_Etablissement = type_Etablissement;
        Tel_Etablissement = tel_Etablissement;
        Directeur_Etablissement = directeur_Etablissement;
        Date_Fondation = date_Fondation;
    }

   

    public Etablissement() {
    }


//*******************
    public int getID_Etablissement() {
        return ID_Etablissement;
    }

    public void setID_Etablissement(int ID_Etablissement) {
        this.ID_Etablissement = ID_Etablissement;
    }
    //*******************
    public String getImg_Etablissement() {
        return Img_Etablissement;
    }

    public void setImg_Etablissement(String img_Etablissement) {
        Img_Etablissement = img_Etablissement;
    }
    //*******************
    public String getNom_Etablissement() {
        return Nom_Etablissement;
    }

    public void setNom_Etablissement(String nom_Etablissement) {
        Nom_Etablissement = nom_Etablissement;
    }
    //*******************
    public String getAdresse_Etablissement() {
        return Adresse_Etablissement;
    }

    public void setAdresse_Etablissement(String adresse_Etablissement) {
        Adresse_Etablissement = adresse_Etablissement;
    }
    //*******************
    public String getType_Etablissement() {
        return Type_Etablissement;
    }

    public void setType_Etablissement(String type_Etablissement) {
        Type_Etablissement = type_Etablissement;
    }
    //*******************
    public int getTel_Etablissement() {
        return Tel_Etablissement;
    }

    public void setTel_Etablissement(int tel_Etablissement) {
        Tel_Etablissement = tel_Etablissement;
    }
    //*******************
    public String getDirecteur_Etablissement() {
        return Directeur_Etablissement;
    }

    public void setDirecteur_Etablissement(String directeur_Etablissement) {
        Directeur_Etablissement = directeur_Etablissement;
    }

    //*******************
    public java.sql.Date getDate_Fondation() {
        if (Date_Fondation != null) {
            return new java.sql.Date(Date_Fondation.getTime());
        } else {
            return null;
        }
    }

    public void setDate_Fondation(Date date_Fondation) {
        Date_Fondation = date_Fondation;
    }

//*******************

    @Override
    public String toString() {
        return "Etablissement{" +
                "ID_Etablissement=" + ID_Etablissement +
                ", Img_Etablissement='" + Img_Etablissement + '\'' +
                ", Nom_Etablissement='" + Nom_Etablissement + '\'' +
                ", Adresse_Etablissement='" + Adresse_Etablissement + '\'' +
                ", Type_Etablissement='" + Type_Etablissement + '\'' +
                ", Tel_Etablissement=" + Tel_Etablissement +
                ", Directeur_Etablissement='" + Directeur_Etablissement + '\'' +
                ", Date_Fondation=" + Date_Fondation +
                '}';
    }


}
