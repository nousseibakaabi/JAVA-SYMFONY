package services;

import com.example.testuser1.data;
import models.Apprenant;
import utils.myBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ApprenantServices implements IServiceApprenant {
    private Connection connection;


    public ApprenantServices() {
        connection = myBD.getInstance().getConnection();
    }

    @Override
    public void add(Apprenant apprenant) throws SQLException {

        String sql = "INSERT INTO apprenants (name,email,statut_niveau,formation_etudies,image) VALUES (?,?,?,?,?)";
        System.out.println(sql);
        PreparedStatement statement = connection.prepareStatement(sql);
        System.out.println(apprenant.toString());
        statement.setString(1, apprenant.getName());
        statement.setString(2, apprenant.getEmail());
        statement.setString(3, apprenant.getStatutNiveau());
        statement.setString(4, apprenant.getFormationEtudies());

        String path = data.path;
        path = path.replace("\\", "\\\\");
        statement.setString(5,   path ); // Chemin de l'image


        statement.executeUpdate();

    }

    @Override
    public List<Apprenant> getAll() throws SQLException {
        String sql = "select * from apprenants";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Apprenant> apprenants = new ArrayList<>();
        while (rs.next()) {
            System.out.println(rs.getString(1));
            Apprenant apprenant = new Apprenant();
            apprenant.setId(rs.getInt("id"));
            apprenant.setName(rs.getString("name"));
            apprenant.setEmail(rs.getString("email"));
            apprenant.setStatutNiveau(rs.getString("statut_niveau"));
            apprenant.setFormationEtudies(rs.getString("formation_etudies"));

            apprenant.setImage(rs.getString("image"));
            apprenants.add(apprenant);

        }
        return apprenants;

    }


    @Override
    public void update(Apprenant apprenant, int id) throws SQLException {
        String sql = "update apprenants set   name = ?, email = ?, statut_niveau = ?," +
                "formation_etudies = ?,image = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(2, apprenant.getName());
        preparedStatement.setString(3, apprenant.getEmail());
        preparedStatement.setString(4, apprenant.getStatutNiveau());
        preparedStatement.setString(5, apprenant.getFormationEtudies());

        String path = data.path;
        if (path == null) {
            System.out.println("path is null");
            path = "C:\\Users\\Hadhemi\\IdeaProjects\\demo\\src\\main\\resources\\images"; // Replace with your default path

        } else {
            path = path.replace("\\", "\\\\");
        }
        preparedStatement.setString(6, path);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from apprenants where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

    }


    @Override
    public Apprenant getById(int id) throws SQLException {
        String sql = "SELECT  `name`, `email`, `statut_niveau`, `formation_etudies`, `image` FROM `apprenants` WHERE `id` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {

            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String statutNiveau = resultSet.getString("statut");
            String formationEtudies = resultSet.getString("formation");
            String image = resultSet.getString("image");


            return new Apprenant(id, name, email, statutNiveau, formationEtudies, image);
        } else {
            // Handle the case when no matching record is found
            return null;
        }
    }

    Apprenant apprenant;

    /*@Override
    public Apprenant getByEmail(String Email) throws SQLException {

        Apprenant apprenant = null;
        String sql = "SELECT * FROM apprenants WHERE Email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, Email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    String password = resultSet.getString("Password");
                    String statutNiveau = resultSet.getString("StatutNiveau");
                    String formationEtudies = resultSet.getString("FormationEtudies");
                    String niveau = resultSet.getString("Niveau");

                    // Create an Apprenant object using the retrieved values
                    apprenant = new Apprenant(name, Email, password, statutNiveau, formationEtudies, niveau,1);

                }
            }
        }

        return apprenant;

    }}
     */
}

