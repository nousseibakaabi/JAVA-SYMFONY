package models;

public class Apprenant {

    private int id;

    private String name;
    private String email;
    private String statutNiveau;
    private String formationEtudies;


    private String image;



    public Apprenant(){}

    public Apprenant(int id,  String name, String email, String statutNiveau, String formationEtudies, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.statutNiveau = statutNiveau;
        this.formationEtudies = formationEtudies;
        this.image = image;

    }
    public Apprenant( String name, String email, String statutNiveau, String formationEtudies,String image) {

        this.name = name;
        this.email = email;
        this.statutNiveau = statutNiveau;
        this.formationEtudies = formationEtudies;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatutNiveau() {
        return statutNiveau;
    }

    public void setStatutNiveau(String statutNiveau) {
        this.statutNiveau = statutNiveau;
    }

    public String getFormationEtudies() {
        return formationEtudies;
    }

    public void setFormationEtudies(String formationEtudies) {
        this.formationEtudies = formationEtudies;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Apprenant{" +
                "id=" + id +

                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", statutNiveau='" + statutNiveau + '\'' +
                ", formationEtudies='" + formationEtudies + '\'' +

                ", image='" + image + '\'' +
                '}';
    }
}
