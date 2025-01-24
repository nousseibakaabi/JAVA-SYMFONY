package services;

import models.Certificat;
import utils.myBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertficatServices implements ICertifServices<Certificat>{
    private final Connection cnx ;
    public  CertficatServices(){ cnx = myBD.getInstance().getConnection();}

    @Override
    public void addCertificate(Certificat certificat) throws SQLException {
        String sql = "INSERT INTO certificat (Nom_Certificat, Domaine_Certificat, Niveau, Date_Obtention_Certificat, ID_Etablissement) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = cnx.prepareStatement(sql);
        // Attribution des valeurs aux paramètres de la requête
        preparedStatement.setString(1, certificat.getNom_Certificat());
        preparedStatement.setString(2, certificat.getDomaine_Certificat());
        preparedStatement.setString(3, certificat.getNiveau());
        preparedStatement.setDate(4, certificat.getDate_Obtention_Certificat());
        preparedStatement.setInt(5, certificat.getID_Etablissement());


        preparedStatement.executeUpdate();
    }


    @Override
    public void updateCertificate(Certificat certificat,int id) throws SQLException {
        String sql = "UPDATE certificat SET Nom_Certificat = ?, Domaine_Certificat = ?, Niveau = ?, Date_Obtention_Certificat = ?, ID_Etablissement = ? WHERE ID_Certificat = ?";
        PreparedStatement preparedStatement = cnx.prepareStatement(sql);
        preparedStatement.setString(1, certificat.getNom_Certificat());
        preparedStatement.setString(2, certificat.getDomaine_Certificat());
        preparedStatement.setString(3, certificat.getNiveau());
        preparedStatement.setDate(4, certificat.getDate_Obtention_Certificat());
        preparedStatement.setInt(5, certificat.getID_Etablissement());
        preparedStatement.setInt(6, id);


        preparedStatement.executeUpdate();
    }


    public void deleteCertificate(int certificateId) throws SQLException {
        String deleteCertificateQuery = "DELETE FROM certificat WHERE ID_Certificat = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(deleteCertificateQuery)) {
            preparedStatement.setInt(1, certificateId);
            preparedStatement.executeUpdate();
        }
    }



    @Override
    public List<Certificat> getAll() throws SQLException {
        String sql = "SELECT * FROM certificat";
        Statement statement = cnx.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Certificat> certificats = new ArrayList<>();
        while (rs.next()) {
            Certificat certificat = new Certificat();
            certificat.setID_Certificat(rs.getInt("ID_Certificat"));
            certificat.setNom_Certificat(rs.getString("Nom_Certificat"));
            certificat.setDomaine_Certificat(rs.getString("Domaine_Certificat"));
            certificat.setNiveau(rs.getString("Niveau"));
            certificat.setDate_Obtention_Certificat(rs.getDate("Date_Obtention_Certificat"));
            certificat.setID_Etablissement(rs.getInt("ID_Etablissement"));

            certificats.add(certificat);
        }
        return certificats;
    }


    @Override
    public Certificat getById(int idCertificat) throws SQLException {
        String sql = "SELECT `Nom_Certificat`, `Domaine_Certificat`, `Niveau`, `Date_Obtention_Certificat`, `ID_Etablissement` FROM `certificat` WHERE `ID_Certificat` = ?";
        PreparedStatement preparedStatement = cnx.prepareStatement(sql);
        preparedStatement.setInt(1, idCertificat);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String nomCertificat = resultSet.getString("Nom_Certificat");
            String domaineCertificat = resultSet.getString("Domaine_Certificat");
            String niveau = resultSet.getString("Niveau");
            Date dateObtentionCertificat = resultSet.getDate("Date_Obtention_Certificat");
            int idEtablissement = resultSet.getInt("ID_Etablissement");

            return new Certificat( nomCertificat, domaineCertificat, niveau, dateObtentionCertificat, idEtablissement);
        } else {
            return null;
        }
    }


    public int getEtablissementId(int certificateId) throws SQLException {
        String query = "SELECT ID_Etablissement FROM certificat WHERE ID_Certificat = ?";
        int etablissementId = -1; // Valeur par défaut en cas d'erreur ou si aucun résultat n'est trouvé

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, certificateId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    etablissementId = resultSet.getInt("ID_Etablissement");
                }
            }
        }
        return etablissementId;
    }
    public boolean isUniqueCertificate(String nomCertificat, String domaineCertificat, String niveau, Date dateObtention, int idEtablissement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM certificat WHERE Nom_Certificat = ? AND Domaine_Certificat = ? AND Niveau = ? AND Date_Obtention_Certificat = ? AND ID_Etablissement = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, nomCertificat);
            preparedStatement.setString(2, domaineCertificat);
            preparedStatement.setString(3, niveau);
            preparedStatement.setDate(4, dateObtention);
            preparedStatement.setInt(5, idEtablissement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) == 0;
            }
        }
        return false;
    }

    public String getEtablissementName(int etablissementId) throws SQLException {
        String query = "SELECT Nom_Etablissement FROM etablissement WHERE ID_Etablissement = ?";
        String nomEtablissement = null;

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, etablissementId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    nomEtablissement = resultSet.getString("Nom_Etablissement");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-lancer l'exception pour la gérer à un niveau supérieur si nécessaire
        }

        return nomEtablissement;
    }

    //*******************************************************************************
//adhiya ki tekteb harf/+ wehed taffichilek li yabdew bhrouf adhoukom
    public List<Certificat> getByPartialName(String partialName) throws SQLException {
        String sql = "SELECT * FROM certificat WHERE Nom_Certificat LIKE ?";
        List<Certificat> certificats = new ArrayList<>();

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, partialName + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Certificat certificat = new Certificat();
                    certificat.setID_Certificat(resultSet.getInt("ID_Certificat"));
                    certificat.setNom_Certificat(resultSet.getString("Nom_Certificat"));
                    certificat.setDomaine_Certificat(resultSet.getString("Domaine_Certificat"));
                    certificat.setNiveau(resultSet.getString("Niveau"));
                    certificat.setDate_Obtention_Certificat(resultSet.getDate("Date_Obtention_Certificat"));
                    certificat.setID_Etablissement(resultSet.getInt("ID_Etablissement"));

                    certificats.add(certificat);
                }
            }
        }

        return certificats;
    }
    public List<Certificat> trierParNom() throws SQLException {
        String sql = "SELECT * FROM certificat ORDER BY Nom_Certificat";
        return getCertificatsFromResultSet(sql);
    }

    public List<Certificat> trierParDomaine() throws SQLException {
        String sql = "SELECT * FROM certificat ORDER BY Domaine_Certificat";
        return getCertificatsFromResultSet(sql);
    }

    public List<Certificat> trierParNiveau() throws SQLException {
        String sql = "SELECT * FROM certificat ORDER BY Niveau";
        return getCertificatsFromResultSet(sql);
    }

    private List<Certificat> getCertificatsFromResultSet(String sql) throws SQLException {
        List<Certificat> certificats = new ArrayList<>();
        try (Statement statement = cnx.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Certificat certificat = new Certificat();
                certificat.setID_Certificat(rs.getInt("ID_Certificat"));
                certificat.setNom_Certificat(rs.getString("Nom_Certificat"));
                certificat.setDomaine_Certificat(rs.getString("Domaine_Certificat"));
                certificat.setNiveau(rs.getString("Niveau"));
                certificat.setDate_Obtention_Certificat(rs.getDate("Date_Obtention_Certificat"));
                certificat.setID_Etablissement(rs.getInt("ID_Etablissement"));
                certificats.add(certificat);
            }
        }
        return certificats;
    }

    public Map<String, Integer> getDomaineStatistics() {
        Map<String, Integer> domaineStats = new HashMap<>();

        try (Statement statement = cnx.createStatement();
             ResultSet rs = statement.executeQuery("SELECT Domaine_Certificat, COUNT(*) AS count FROM certificat GROUP BY Domaine_Certificat")) {

            while (rs.next()) {
                String domaine = rs.getString("Domaine_Certificat");
                int count = rs.getInt("count");
                domaineStats.put(domaine, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }

        return domaineStats;
    }


}
