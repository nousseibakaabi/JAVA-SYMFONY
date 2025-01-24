package models;

import java.sql.Date;

public class Tuteur {

    private int id_tuteur;

    private String nom;

    private String prenom;

    private Date date_naisc;

    private int tlf;

    private String profession;

    private String email;

    private String image;

    public Tuteur ()

    {

    }

    public Tuteur(int id_tuteur, String nom, String prenom, Date date_naisc, int tlf, String profession, String email,String image) {
        this.id_tuteur = id_tuteur;
        this.nom = nom;
        this.prenom = prenom;
        this.date_naisc = date_naisc;
        this.tlf = tlf;
        this.profession = profession;
        this.email = email;
        this.image = image;
    }


    public Tuteur(String nom, String prenom, Date date_naisc, int tlf, String profession, String email, int idTuteur,String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.date_naisc = date_naisc;
        this.tlf = tlf;
        this.profession = profession;
        this.email = email;
        this.id_tuteur = idTuteur;
        this.image = image;


    }

    public Tuteur(String nom, String prenom, Date date, int tlf, String profession, String email,String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.date_naisc = date;
        this.tlf = tlf;
        this.profession = profession;
        this.email = email;
        this.image = image;
    }

    public Tuteur(String nom, String prenom, Date date, int tlf, String profession, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.date_naisc = date;
        this.tlf = tlf;
        this.profession = profession;
        this.email = email;
        this.image="";
    }




    public int getId_tuteur() {
        return id_tuteur;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }



    public Date getDate_naisc() {
        // Conversion de java.util.Date en java.sql.Date
        if (date_naisc != null) {
            return new Date(date_naisc.getTime());
        } else {
            return null;
        }
    }

    public int getTlf() {
        return tlf;
    }

    public String getProfession() {
        return profession;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }


    public void setId_tuteur(int id_tuteur) {
        this.id_tuteur = id_tuteur;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDate_naisc(java.util.Date date_naisc) {
       date_naisc = date_naisc;
    }

    public void setDate_naisc(Date date_naisc) {
        this.date_naisc = date_naisc;
    }

    public void setTlf(int tlf) {
        this.tlf = tlf;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setImage(String image) {this.image = image;}

    @Override
    public String toString() {
        return "Tuteur{" +
                "id_tuteur=" + id_tuteur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", date_naisc=" + date_naisc +
                ", tlf=" + tlf +
                ", profession='" + profession + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
