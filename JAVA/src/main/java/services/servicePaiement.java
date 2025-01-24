package services;

import models.*;
import utils.myBD;

import java.sql.*;

public class servicePaiement {
    private Connection cnx;

    public servicePaiement() {
        cnx = myBD.getInstance().getConnection();
    }

    public void add(Paiement p, float montant, int reservationID) {
        String sql = "INSERT INTO `paiement` (`id_res`, `montant`) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setInt(1, reservationID); // Set the reservation ID
            preparedStatement.setFloat(2, montant);

            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Insertion Paiement successful.");
            } else {
                System.out.println("Problem Add Paiement");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while executing the SQL query: " + e.getMessage());
        }
    }

    public void updatePaiement(int reservationID, double newMontant) {
        String sql = "UPDATE paiement SET montant = ? WHERE id_res = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setDouble(1, newMontant);
            preparedStatement.setInt(2, reservationID);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Update Paiement successful.");
            } else {
                System.out.println("Problem updating Paiement");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while executing the SQL query: " + e.getMessage());
        }
    }

    public void updateHeure(int reservationId, Date time) {
        String sql = "UPDATE paiement SET heure_P = ? WHERE id_res = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(sql);
            preparedStatement.setDate(1, time);
            preparedStatement.setInt(2, reservationId);
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("Update heure_P successful.");
            } else {
                System.out.println("Problem updating heure_P");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while executing the SQL query: " + e.getMessage());
        }
    }

    public Paiement getPaiement(int reservationId) {
        String query = "SELECT id, id_res, montant, heure_P FROM paiement WHERE id_res = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, reservationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int id_res = resultSet.getInt("id_res");
                    float montant = resultSet.getFloat("montant");
                    Date heure_P = resultSet.getDate("heure_P");
                    return new Paiement(id, id_res, montant, heure_P);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}



