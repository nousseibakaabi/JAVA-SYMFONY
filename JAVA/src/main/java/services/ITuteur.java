package services;
import models.Tuteur;

import java.sql.SQLException;
import java.util.List;
public interface ITuteur<T> {


    List<String> getNoms() throws SQLException;

    int getIDByNom(String nom,String prenom) throws SQLException;

    String getNameByID(int id) throws SQLException;

    public interface ITuteurServices <T>{
        void addTuteur(T t) throws SQLException;

        void updateTuteur(T t) throws SQLException;

        void deleteTuteur(int id_tuteur) throws SQLException;

        List<T> getAll() throws SQLException;

        T getById(int id_tuteur) throws  SQLException;

        public List<Integer> getIdTuteurByNomPrenom(String nom, String prenom);

        public List<Tuteur> getTuteurList();

        public List<Tuteur> trierParNom() throws SQLException;

        public List<Tuteur> trierParPrenom() throws SQLException;

        public List<Tuteur> trierParProfession() throws SQLException;
    }
}
