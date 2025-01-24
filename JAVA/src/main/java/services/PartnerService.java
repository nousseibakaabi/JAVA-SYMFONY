package services;

import com.example.testuser1.data;
import models.Partner;
import utils.myBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartnerService implements IServices<Partner>{

    private Connection connection;

    public PartnerService() {
        connection = myBD.getInstance().getConnection();
    }
    @Override
    public void add(Partner partner) throws SQLException {
        String sql = "INSERT INTO `partner`(`namePartner`,`typePartner`,`description`,`email`,`tel`,`image`) VALUES(?,?,?,?,?,?)";
        System.out.println(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, partner.getNamePartner());
        preparedStatement.setString(2, partner.getTypePartner());
        preparedStatement.setString(3, partner.getDescription());
        preparedStatement.setString(4, partner.getEmail());
        preparedStatement.setInt(5, partner.getTel());
        preparedStatement.setString(6, partner.getImage());
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(Partner partner,int id) throws SQLException {
        String sql = "update partner set namePartner = ?,  typePartner = ?, description = ?, email = ?," +
                "tel = ?, image=? where idPartner = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, partner.getNamePartner());
        preparedStatement.setString(2, partner.getTypePartner());
        preparedStatement.setString(3, partner.getDescription());
        preparedStatement.setString(4, partner.getEmail());
        preparedStatement.setInt(5, partner.getTel());
        String path = data.path1;
        if (path == null) {
            System.out.println("path is null");
            path = "C:\\Users\\user\\Downloads\\logo.png"; // Replace with your default path

        } else {
            path = path.replace("\\", "\\\\");
        }
        preparedStatement.setString(6, path);

        preparedStatement.setInt(7, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        // Supprimer d'abord les événements associés au partenaire
        String deleteEventsQuery = "DELETE FROM event WHERE idPartnerCE = ?";
        try (PreparedStatement eventStatement = connection.prepareStatement(deleteEventsQuery)) {
            eventStatement.setInt(1, id);
            eventStatement.executeUpdate();
        }

        // Ensuite, supprimer le partenaire lui-même
        String deletePartnerQuery = "DELETE FROM partner WHERE idPartner = ?";
        try (PreparedStatement partnerStatement = connection.prepareStatement(deletePartnerQuery)) {
            partnerStatement.setInt(1, id);
            partnerStatement.executeUpdate();
        }
    }


    @Override
    public List<Partner> getAll() throws SQLException {
        String sql = "select * from partner";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Partner> partners = new ArrayList<>();
        while (rs.next()) {
            Partner p = new Partner();
            p.setIdPartner(rs.getInt("idPartner"));
            p.setNamePartner(rs.getString("namePartner"));
            p.setTypePartner(rs.getString("typePartner"));
            p.setDescription(rs.getString("description"));
            p.setEmail(rs.getString("email"));
            p.setTel(rs.getInt("tel"));
            p.setImage(rs.getString("image"));


            partners.add(p);
        }
        return partners;
    }

    @Override
    public Partner getById(int id) throws SQLException {
        String sql = "SELECT  `idPartner`,`namePartner`, `typePartner`, `description`,`email`,`tel`,`image` " +
                "FROM `partner` WHERE `idPartner` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int idPartner = resultSet.getInt("idPartner");
            String namePartner = resultSet.getString("namePartner");
            String typePartner = resultSet.getString("typePartner");
            String email = resultSet.getString("email");
            int tel = resultSet.getInt("tel");
            String description = resultSet.getString("description");
            String image = resultSet.getString("image");

            return new Partner(id, namePartner, typePartner, description,email,tel,image);
        } else {
            // Handle the case when no matching record is found
            return null;
        }
    }

    @Override
    public  List<String> getName() throws SQLException {
        List<String> nomsPartners = new ArrayList<>();
        String sql = "SELECT  `namePartner`" +
                "FROM `partner`";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                String nomPartners = rs.getString("namePartner");
                nomsPartners.add(nomPartners);
            }
        }

        return nomsPartners;
    }
    @Override
    public int getIDByNom(String name) throws SQLException {
        String sql = "SELECT idPartner FROM partner WHERE namePartner = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("idPartner");
                } else {
                    // Gérer le cas où aucun enregistrement correspondant n'est trouvé
                    throw new SQLException("Aucun partenaire trouvé avec le nom spécifié : " + name);
                }
            }
        }
    }

    @Override
    public String getNameByID(int id) throws SQLException {
        String sql = "SELECT namePartner FROM partner WHERE idPartner = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("namePartner");
                } else {
                    // Gérer le cas où aucun enregistrement correspondant n'est trouvé
                    throw new SQLException("Aucun établissement trouvé avec l'id spécifié : " + id);
                }
            }
        }
    }

    @Override
    public List<Partner> getByPartialName(String partialName) throws SQLException {
        List<Partner> partners = new ArrayList<>();
        String query = "SELECT * FROM partner WHERE namePartner  LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, partialName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Partner p = new Partner();
                    p.setIdPartner(rs.getInt("idPartner"));
                    p.setNamePartner(rs.getString("namePartner"));
                    p.setTypePartner(rs.getString("typePartner"));
                    p.setDescription(rs.getString("description"));
                    p.setEmail(rs.getString("email"));
                    p.setTel(rs.getInt("tel"));
                    p.setImage(rs.getString("image"));

                    partners.add(p);
                }
            }
        }

        return partners;
    }

    @Override
    public List<Partner> getAllDESC() throws SQLException {
        String sql = "select * from partner ORDER BY namePartner DESC";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Partner> partners = new ArrayList<>();
        while (rs.next()) {
            Partner p = new Partner();
            p.setIdPartner(rs.getInt("idPartner"));
            p.setNamePartner(rs.getString("namePartner"));
            p.setTypePartner(rs.getString("typePartner"));
            p.setDescription(rs.getString("description"));
            p.setEmail(rs.getString("email"));
            p.setTel(rs.getInt("tel"));
            p.setImage(rs.getString("image"));


            partners.add(p);
        }
        return partners;
    }


    @Override
    public List<Partner> getAllASC() throws SQLException {
        String sql = "select * from partner ORDER BY namePartner ASC";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Partner> partners = new ArrayList<>();
        while (rs.next()) {
            Partner p = new Partner();
            p.setIdPartner(rs.getInt("idPartner"));
            p.setNamePartner(rs.getString("namePartner"));
            p.setTypePartner(rs.getString("typePartner"));
            p.setDescription(rs.getString("description"));
            p.setEmail(rs.getString("email"));
            p.setTel(rs.getInt("tel"));
            p.setImage(rs.getString("image"));


            partners.add(p);
        }
        return partners;
    }
    @Override
   public List<String> getByDate(java.sql.Date date) throws SQLException{
        return null;
    }



}
