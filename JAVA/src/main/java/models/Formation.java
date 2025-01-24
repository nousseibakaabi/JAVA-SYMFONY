package models;

import java.sql.Date;

public class Formation {

    private int id_formation; // Ne pas inclure dans les constructeurs
    private int id_tuteur;
    private String nom_niveau;
    private String categorie;
    private String titre;
    private String description;
    private Date date_d;
    private Date date_f;
   // private float prix;
    private String lien;

    // Constructeurs
    public Formation() {
        this.id_formation = 0; // Valeur par défaut
    }

    public Formation(int id_tuteur, String nom_niveau, String categorie, String titre, String description, Date date_d, Date date_f,  String lien) {
        this.id_formation = 0; // Valeur par défaut
        this.id_tuteur = id_tuteur;
        this.nom_niveau = nom_niveau;
        this.categorie = categorie;
        this.titre = titre;
        this.description = description;
        this.date_d = date_d;
        this.date_f = date_f;
        //this.prix = prix;
        this.lien = lien;
    }

    // Getters et Setters pour id_formation
    public int getId_formation() {
        return id_formation;
    }

    public void setId_formation(int id_formation) {
        this.id_formation = id_formation;
    }

    public int getId_tuteur() {
        return id_tuteur;
    }

    public void setId_tuteur(int id_tuteur) {
        this.id_tuteur = id_tuteur;
    }

    public String getNom_niveau() {
        return nom_niveau;
    }

    public void setNom_niveau(String nom_niveau) {
        this.nom_niveau = nom_niveau;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_d() {
        return date_d;
    }

    public void setDate_d(Date date_d) {
        this.date_d = date_d;
    }

    public Date getDate_f() {
        return date_f;
    }

    public void setDate_f(Date date_f) {
        this.date_f = date_f;
    }

   /* public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }*/

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id_formation=" + id_formation +
                ", id_tuteur=" + id_tuteur +
                ", nom_niveau='" + nom_niveau + '\'' +
                ", categorie='" + categorie + '\'' +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date_d=" + date_d +
                ", date_f=" + date_f +

                ", lien='" + lien + '\'' +
                '}';
    }
}
