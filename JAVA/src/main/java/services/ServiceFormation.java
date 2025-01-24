package services;

import models.Formation;
import utils.myBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation {
    private Connection cnx;

    public ServiceFormation() {
        this.cnx = myBD.getInstance().getConnection();
        if (this.cnx == null) {
            // Handle the case where connection is not properly initialized
            throw new IllegalStateException("Database connection is not properly initialized");
        }
    }

    public void addFormation(Formation formation) throws SQLException {
        String sql = "INSERT INTO formation (id_tuteur, nom_niveau, categorie, titre, description, date_d, date_f, lien) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, formation.getId_tuteur());
            preparedStatement.setString(2, formation.getNom_niveau());
            preparedStatement.setString(3, formation.getCategorie());
            preparedStatement.setString(4, formation.getTitre());
            preparedStatement.setString(5, formation.getDescription());
            preparedStatement.setDate(6, formation.getDate_d());
            preparedStatement.setDate(7, formation.getDate_f());

            preparedStatement.setString(8, formation.getLien());
            // Execution de la requete
            preparedStatement.executeUpdate();
        }
    }



    public void updateFormation(Formation formation, int id) throws SQLException {
        String sql = "UPDATE formation SET id_tuteur = ?, nom_niveau = ?, categorie = ?, titre = ?, description = ?, date_d = ?, date_f = ? , lien = ? WHERE id_formation = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, formation.getId_tuteur());
            preparedStatement.setString(2, formation.getNom_niveau());
            preparedStatement.setString(3, formation.getCategorie());
            preparedStatement.setString(4, formation.getTitre());
            preparedStatement.setString(5, formation.getDescription());
            preparedStatement.setDate(6, formation.getDate_d());
            preparedStatement.setDate(7, formation.getDate_f());
            preparedStatement.setString(8, formation.getLien());
            preparedStatement.setInt(9, id);

            // Execution de la requete
            preparedStatement.executeUpdate();
        }
    }

    public void deleteFormation(int id_formation) throws SQLException {
        String sql = "DELETE FROM formation WHERE id_formation = ?";
        PreparedStatement preparedStatement = cnx.prepareStatement(sql);
        preparedStatement.setInt(1, id_formation);
        preparedStatement.executeUpdate();
    }

    public List<Formation> getAll() throws SQLException {
        String sql = "SELECT * FROM formation";
        Statement statement = cnx.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Formation> formations = new ArrayList<>();

        while (rs.next()) {
            Formation formation = new Formation();

            formation.setId_formation(rs.getInt("id_formation"));
            formation.setId_tuteur(rs.getInt("id_tuteur"));
            formation.setNom_niveau(rs.getString("nom_niveau"));
            formation.setCategorie(rs.getString("categorie"));
            formation.setTitre(rs.getString("titre"));
            formation.setDescription(rs.getString("description"));
            formation.setDate_d(rs.getDate("date_d"));
            formation.setDate_f(rs.getDate("date_f"));
            formation.setLien(rs.getString("lien"));

            formations.add(formation);
        }
        return formations;
    }

    public Formation getById(int id_formation) throws SQLException {
        String sql = "SELECT id_tuteur, nom_niveau, categorie, titre, description, date_d, date_f, lien FROM formation WHERE id_formation = ?";
        PreparedStatement preparedStatement = cnx.prepareStatement(sql);
        preparedStatement.setInt(1, id_formation);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int id_tuteur = resultSet.getInt("id_tuteur");
            String nom_niveau = resultSet.getString("nom_niveau");
            String categorie = resultSet.getString("categorie");
            String titre = resultSet.getString("titre");
            String description = resultSet.getString("description");
            Date date_d = resultSet.getDate("date_d");
            Date date_f = resultSet.getDate("date_f");
            String lien = resultSet.getString("lien");

            return new Formation(id_tuteur, nom_niveau, categorie, titre, description, date_d, date_f,  lien);
        } else {
            return null;
        }
    }

    public List<String> getTitres() throws SQLException {
        List<String> titrefo = new ArrayList<>();
        String sql = "SELECT titre FROM formation"; // Requête SQL pour récupérer tous les titres
        try (Statement statement = cnx.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                String titre = rs.getString("titre");
                titrefo.add(titre);
            }
        }

        return titrefo;
    }

    public List<Formation> getFormationList() {
        List<Formation> formations = new ArrayList<>();
        String sql = "SELECT id_tuteur, nom_niveau, categorie, titre, description, date_d, date_f,  lien  FROM formation";

        try {

            try (Statement statement = cnx.createStatement();
                 ResultSet rs = statement.executeQuery(sql)) {

                while (rs.next()) {
                    Formation formation = new Formation();
                    formation.setId_formation(rs.getInt("id_formation"));
                    formation.setId_tuteur(rs.getInt("id_tuteur"));
                    formation.setNom_niveau(rs.getString("nom_niveau"));
                    formation.setCategorie(rs.getString("categorie"));
                    formation.setTitre(rs.getString("titre"));
                    formation.setDescription(rs.getString("description"));
                    formation.setDate_d(rs.getDate("date_d"));
                    formation.setDate_f(rs.getDate("date_f"));
                    formation.setLien(rs.getString("lien"));

                    formations.add(formation);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return formations;

    }

    private List<Formation> getFormationFromResultSet(String sql) throws SQLException {
        List<Formation> formations = new ArrayList<>();
        try (Statement statement = cnx.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Formation formation = new Formation();
                formation.setId_formation(rs.getInt("id_formation"));
                formation.setId_tuteur(rs.getInt("id_tuteur"));
                formation.setNom_niveau(rs.getString("nom_niveau"));
                formation.setCategorie(rs.getString("categorie"));
                formation.setTitre(rs.getString("titre"));
                formation.setDescription(rs.getString("description"));
                formation.setDate_d(rs.getDate("date_d"));
                formation.setDate_f(rs.getDate("date_f"));
                formation.setLien(rs.getString("lien"));

                formations.add(formation);
            }
        }
        return formations;
    }


    public List<Formation> trierParCategorie() throws SQLException {
        String sql = "SELECT * FROM formation ORDER BY categorie ASC";
        return getFormationFromResultSet(sql);
    }

    public List<Formation> trierParNiveau() throws SQLException {
        String sql = "SELECT * FROM formation ORDER BY nom_niveau ASC";
        return getFormationFromResultSet(sql);
    }




}
