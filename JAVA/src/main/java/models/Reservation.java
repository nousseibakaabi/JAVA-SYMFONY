package models;

public class Reservation {
    private int id;
    private int id_event;

    private String nameE;

    private int nb_places;
private String email;
private String name;
private int id_user;

private String imageSrc;

private  Double eventPrice;

    public Reservation(){}



    public Reservation(String name,String email, String nameE, int nb_places, double eventPrice) {
        this.nameE = nameE;
        this.nb_places = nb_places;
        this.email = email;
        this.name = name;
        this.eventPrice=eventPrice;
    }

    public Reservation(int nb_places) {

        this.nb_places = nb_places;
    }

    public Reservation(String nameE, int nb_places, String imageSrc) {
        this.nameE = nameE;
        this.nb_places = nb_places;
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public int getNb_places() {
        return nb_places;
    }

    public void setNb_places(int nb_places) {
        this.nb_places = nb_places;
    }

    public String getNameE() {
        return nameE;
    }

    public void setNameE(String nameE) {
        this.nameE = nameE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public Double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(Double eventPrice) {
        this.eventPrice = eventPrice;
    }
}
