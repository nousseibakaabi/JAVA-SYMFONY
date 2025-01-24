package models;

public class Niveau {
    private int id;
    private String name;
    private String prerequis;
    private String duree;
    private int nbformation;
    private Certificat certificatAll;
    private int id_certificat;
    private String description;
    private String image;
    public Niveau(){}

    public Certificat getCertificatAll() {
        return certificatAll;
    }

    public void setCertificatAll(Certificat certificatAll) {
        this.certificatAll = certificatAll;
    }

    public Niveau(int id, String name, String prerequis, String duree, int nbformation, int id_certificat, String description, String image) {
        this.id = id;
        this.name = name;
        this.prerequis = prerequis;
        this.duree = duree;
        this.nbformation = nbformation;
        this.id_certificat = id_certificat;
        this.description = description;
        this.image = image;
    }
    public Niveau(String name, String prerequis, String duree, int nbformation, int id_certificat, String description, String image) {
        this.name = name;
        this.prerequis = prerequis;
        this.duree = duree;
        this.nbformation = nbformation;
        this.id_certificat = id_certificat;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrerequis() {
        return prerequis;
    }

    public void setPrerequis(String prerequis) {
        this.prerequis = prerequis;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public int getNbformation() {
        return nbformation;
    }

    public void setNbformation(int nbformation) {
        this.nbformation = nbformation;
    }

    public int getCertificat() {
        return id_certificat;
    }

    public void setCertificat(int certificat) {
        this.id_certificat = certificat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Niveau{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prerequis='" + prerequis + '\'' +
                ", duree='" + duree + '\'' +
                ", nbformation=" + nbformation +
                ", certificat='" + id_certificat + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
