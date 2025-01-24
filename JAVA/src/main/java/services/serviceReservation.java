package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.Event;
import models.RemiseEntry;
import models.Reservation;
import utils.myBD;
public class serviceReservation {

    private Connection cnx;

    public serviceReservation() {
        cnx = myBD.getInstance().getConnection();
    }


    public Reservation add(Reservation r ,int eventId,String eventName , String image,int idUser ,String name , String email , double eventPrice) {
        String sql = "INSERT INTO `reservation` (`name`, `email`, `nb_places`, `nameE` , `id_event`, `imageSrc`,id_user , eventPrice ) VALUES (?,?,?, ?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, r.getNb_places());
            preparedStatement.setString(4, eventName);
            preparedStatement.setInt(5, eventId);
            preparedStatement.setString(6, image);
            preparedStatement.setInt(7,idUser);
            preparedStatement.setDouble(8,eventPrice);



            if (preparedStatement.executeUpdate() > 0) {
                // Retrieve the auto-generated ID of the newly added reservation
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    r.setId(generatedKeys.getInt(1));
                }
                System.out.println("Insertion Reservation successful.");
            } else {
                System.out.println("Problem Add Reservation");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while executing the SQL query: " + e.getMessage());
        }
        return r;
    }



    public void update(Reservation r ) {
        String sql = "update reservation set nb_places= ? where id = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);

            preparedStatement.setInt(1, r.getNb_places());
            preparedStatement.setInt(2,r.getId());
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Update Reservation successful.");

            } else {
                System.out.println("Problem Update Reservation");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while executing the SQL query: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "delete from reservation where id = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Delete Reservation successful.");

            } else {
                System.out.println("Problem Delete Reservation");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while executing the SQL query: " + e.getMessage());
        }
    }





    public List<Reservation> getCardlist() {
        String sql = "SELECT id, nameE , nb_places , imageSrc , eventPrice FROM reservation ";
        List<Reservation> list = new ArrayList<>();
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setNameE(rs.getString("nameE"));
                res.setNb_places(rs.getInt("nb_places"));
                res.setImageSrc(rs.getString("imageSrc"));
                res.setEventPrice(rs.getDouble("eventPrice"));

                list.add(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list);

        return list;
    }
    public List<Reservation> getCardlist(int id) {
        String sql = "SELECT id, nameE , nb_places , imageSrc , eventPrice FROM reservation where id_user=? ";
        List<Reservation> list = new ArrayList<>();
        try {
            PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setNameE(rs.getString("nameE"));
                res.setNb_places(rs.getInt("nb_places"));
                res.setImageSrc(rs.getString("imageSrc"));
                res.setEventPrice(rs.getDouble("eventPrice"));
                list.add(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list);

        return list;
    }


    public List<Reservation> getlistBack() {
        String sql = "SELECT id, nameE , nb_places , name, email FROM reservation ";
        List<Reservation> list = new ArrayList<>();
        try {
            Statement statement = cnx.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setNameE(rs.getString("nameE"));
                res.setNb_places(rs.getInt("nb_places"));
                res.setName(rs.getString("name"));
                res.setEmail(rs.getString("email"));
                list.add(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list);

        return list;
    }

    public int getTotalReservedPlacesForEvent(int eventId) {
        String query = "SELECT SUM(nb_places) FROM reservation WHERE id_event = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sortByName(List<Reservation> reservationList)
    {
        Collections.sort(reservationList , Comparator.comparing(Reservation::getName));
    }

    public double getDiscountPercentageByCode(String code) {
        double discountPercentage = 0.0;
        String sql = "SELECT pourcentage FROM remiseentry WHERE code = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                discountPercentage = resultSet.getDouble("pourcentage");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discountPercentage;
    }

    public void addRemiseEntry(RemiseEntry entry) {
        String sql = "INSERT INTO remiseentry (code, pourcentage) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setString(1, entry.getCode());
            preparedStatement.setDouble(2, entry.getPourcentage());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Insertion RemiseEntry successful.");
            } else {
                System.out.println("Problem adding RemiseEntry");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error occurred while executing the SQL query: " + e.getMessage());
        }
    }

    public List<RemiseEntry> getAllRemiseEntries() {
        String sql = "SELECT * FROM remiseentry";
        List<RemiseEntry> remiseEntries = new ArrayList<>();
        try {
            Statement statement = cnx.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String code = resultSet.getString("code");
                double pourcentage = resultSet.getDouble("pourcentage");
                RemiseEntry entry = new RemiseEntry(code, pourcentage);
                remiseEntries.add(entry);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remiseEntries;
    }

    public String getRandomRemiseCode() {
        String sql = "SELECT code FROM remiseentry ORDER BY RAND() LIMIT 1";
        try {
            Statement statement = cnx.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getString("code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}






