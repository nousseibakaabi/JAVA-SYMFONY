package models;

import java.util.Date;

public class Certificat {

    int ID_Certificat;
    String Nom_Certificat;
    String Domaine_Certificat;
    String Niveau;
    Date Date_Obtention_Certificat;
    int ID_Etablissement;

    public Certificat() {
    }

    public Certificat(int ID_Certificat, String nom_Certificat, String domaine_Certificat, String niveau, Date date_Obtention_Certificat, int ID_Etablissement) {
        this.ID_Certificat = ID_Certificat;
        this.Nom_Certificat = nom_Certificat;
        this.Domaine_Certificat = domaine_Certificat;
        this.Niveau = niveau;
        this.Date_Obtention_Certificat = date_Obtention_Certificat;
        this.ID_Etablissement = ID_Etablissement;
    }
    public Certificat( String nom_Certificat, String domaine_Certificat, String niveau, Date date_Obtention_Certificat, int ID_Etablissement) {

        this.Nom_Certificat = nom_Certificat;
        this.Domaine_Certificat = domaine_Certificat;
        this.Niveau = niveau;
        this.Date_Obtention_Certificat = date_Obtention_Certificat;
        this.ID_Etablissement = ID_Etablissement;
    }




    public int getID_Certificat() {
        return ID_Certificat;
    }

    public void setID_Certificat(int ID_Certificat) {
        this.ID_Certificat = ID_Certificat;
    }

    public String getNom_Certificat() {
        return Nom_Certificat;
    }

    public void setNom_Certificat(String nom_Certificat) {
        Nom_Certificat = nom_Certificat;
    }

    public String getDomaine_Certificat() {
        return Domaine_Certificat;
    }

    public void setDomaine_Certificat(String domaine_Certificat) {
        Domaine_Certificat = domaine_Certificat;
    }

    public String getNiveau() {
        return Niveau;
    }

    public void setNiveau(String niveau) {
        Niveau = niveau;
    }

    public java.sql.Date getDate_Obtention_Certificat() {
        // Conversion de java.util.Date en java.sql.Date
        if (Date_Obtention_Certificat != null) {
            return new java.sql.Date(Date_Obtention_Certificat.getTime());
        } else {
            return null;
        }
    }

    public void setDate_Obtention_Certificat(Date date_Obtention_Certificat) {
        Date_Obtention_Certificat = date_Obtention_Certificat;
    }

    public int getID_Etablissement() {
        return ID_Etablissement;
    }

    public void setID_Etablissement(int ID_Etablissement) {
        this.ID_Etablissement = ID_Etablissement;
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "ID_Certificat=" + ID_Certificat +
                ", Nom_Certificat='" + Nom_Certificat + '\'' +
                ", Domaine_Certificat='" + Domaine_Certificat + '\'' +
                ", Niveau='" + Niveau + '\'' +
                ", Date_Obtention_Certificat=" + Date_Obtention_Certificat +
                ", ID_Etablissement=" + ID_Etablissement +
                '}';
    }
}
