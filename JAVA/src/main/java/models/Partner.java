package models;

import java.util.Date;

public class Partner {
    private int idPartner;
    private String namePartner;
    private String typePartner;
    private String description;
    private String email;
    private int tel;
    private String image;

    public Partner() {
    }

    public Partner(int idPartner, String namePartner, String typePartner, String description, String email, int tel,String image) {
        this.idPartner = idPartner;
        this.namePartner = namePartner;
        this.typePartner = typePartner;
        this.description = description;
        this.email = email;
        this.tel = tel;
        this.image = image;
    }

    public Partner( String namePartner, String typePartner, String description, String email, int tel,String image) {
        this.namePartner = namePartner;
        this.typePartner = typePartner;
        this.description = description;
        this.email = email;
        this.tel = tel;
        this.image = image;
    }
    public Partner(int idPartner, String namePartner) {
        this.idPartner = idPartner;
        this.namePartner = namePartner;
    }

    public int getIdPartner() {
        return idPartner;
    }


    public String getNamePartner() {
        return namePartner;
    }

    public String getTypePartner() {
        return typePartner;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public int getTel() {
        return tel;
    }

    public void setIdPartner(int idPartner) {
        this.idPartner = idPartner;
    }


    public void setNamePartner(String namePartner) {
        this.namePartner = namePartner;
    }

    public void setTypePartner(String typePartner) {
        this.typePartner = typePartner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "idPartner=" + idPartner +
                ", namePartner='" + namePartner + '\'' +
                ", typePartner='" + typePartner + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", tel=" + tel +
                ", image='" + image + '\'' +
                '}';
    }
}
