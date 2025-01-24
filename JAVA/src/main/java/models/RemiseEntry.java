package models;

public class RemiseEntry {
    private int id;
    private  String code;
    private  Double pourcentage;

    public RemiseEntry(String code, Double pourcentage) {
        this.code = code;
        this.pourcentage = pourcentage;
    }

    public String getCode() {
        return code;
    }

    public Double getPourcentage() {
        return pourcentage;
    }


}