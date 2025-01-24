package services;

import models.Etablissement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IEtabServices<T> {

    void addSchool(T t) throws SQLException;

    void updateSchool(T t, int id) throws SQLException;

    void deleteSchool(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws SQLException;

    List<String> getNoms() throws SQLException;

    int getIDByNom(String nom) throws SQLException;
    String getNameByID(int id) throws SQLException;

    String getAdrByID(int id) throws SQLException;

    Map<String, Integer> getNombreCertificatsParEtablissement() throws SQLException;

    int getNombreCertificatsByEtablissement(int idEtablissement) throws SQLException;
    boolean isUniqueEtablissement(String nomEtablissement, String adresseEtablissement, String typeEtablissement, int telEtablissement, String directeurEtablissement, Date dateFondation) throws SQLException;

    List<Etablissement> getEtablissementList();

    Etablissement getByName(String nomEtablissement) throws SQLException;

    List<Etablissement> getByPartialName(String partialName) throws SQLException;

    List<Etablissement> sortByNom() throws SQLException;

    List<Etablissement> sortByNombreCertificats() throws SQLException;

    List<Etablissement> sortByTypeEtablissement() throws SQLException;

    Etablissement getByyName(String nomEtablissement) throws SQLException;

    Etablissement mapResultSetToEtablissement(ResultSet rs) throws SQLException;
}
