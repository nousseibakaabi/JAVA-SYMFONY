package services;

import com.example.testuser1.data;
import models.Event;
import utils.myBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IServices<Event> {

    private Connection connection;

    public EventService() {
        connection = myBD.getInstance().getConnection();
    }

    @Override
    public void add(Event event) throws SQLException {
        String sql = "INSERT INTO `event`(`idPartnerCE`,`idEstab`,`nameEvent`,`dateEvent`,`nbrMax`,`prix`,`description`,`image`) VALUES(?,?,?,?,?,?,?,?)";
        System.out.println(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, event.getIdPartner());
        preparedStatement.setInt(2, event.getIdEstab());
        preparedStatement.setString(3, event.getNameEvent());
        preparedStatement.setDate(4, event.getDateEvent());
        preparedStatement.setInt(5, event.getNbrMax());
        preparedStatement.setDouble(6, event.getPrix());
        preparedStatement.setString(7, event.getDescription());
        String path = data.path;
        path = path.replace("\\", "\\\\");
        preparedStatement.setString(8, path);
            preparedStatement.executeUpdate();
    }

    @Override
    public void update(Event event, int id) throws SQLException {
        String sql = "update event set idPartnerCE = ?,idEstab = ?,  nameEvent = ?, dateEvent = ?, nbrMax = ?," +
                "prix = ?,description = ?,image = ? where idEvent = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, event.getIdPartner());
        preparedStatement.setInt(2, event.getIdEstab());
        preparedStatement.setString(3, event.getNameEvent());
        preparedStatement.setDate(4, event.getDateEvent());
        preparedStatement.setInt(5, event.getNbrMax());
        preparedStatement.setDouble(6, event.getPrix());
        preparedStatement.setString(7, event.getDescription());
        String path = data.path;
        if (path == null) {
            System.out.println("path is null");
            path = "C:\\Users\\user\\Downloads\\logo.png"; // Replace with your default path

        } else {
            path = path.replace("\\", "\\\\");
        }
        preparedStatement.setString(8, path);
        preparedStatement.setInt(9, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from event where idEvent = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

    }

    @Override
    public List<Event> getAll() throws SQLException {
        String sql = "select * from event";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Event> events = new ArrayList<>();
        while (rs.next()) {
            Event e = new Event();
            e.setIdEvent(rs.getInt("idEvent"));
            e.setIdPartner(rs.getInt("idPartnerCE"));
            e.setIdEstab(rs.getInt("idEstab"));
            e.setNameEvent(rs.getString("nameEvent"));
            e.setDateEvent(rs.getDate("dateEvent"));
            e.setNbrMax(rs.getInt("nbrMax"));
            e.setPrix(rs.getDouble("prix"));
            e.setDescription(rs.getString("description"));
            e.setImage(rs.getString("image"));

            events.add(e);
        }
        return events;
    }

    @Override
    public Event getById(int id) throws SQLException {
        String sql = "SELECT `idPartnerCE`,`idEstab`, `nameEvent`, `dateEvent`, `nbrMax`, `prix`, `description`,`image` " +
                "FROM `event` WHERE `idEvent` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int idPartner = resultSet.getInt("idPartner");
            int idEstab = resultSet.getInt("idEstab");
            String nameEvent = resultSet.getString("nameEvent");
            Date dateEvent = resultSet.getDate("dateEvent");
            int nbrMax = resultSet.getInt("nbrMax");
            double prix = resultSet.getDouble("prix");
            String description = resultSet.getString("description");
            String image = resultSet.getString("image");

            return new Event(id,idPartner, idEstab, nameEvent, dateEvent, nbrMax, prix,description, image);
        } else {
            // Handle the case when no matching record is found
            return null;
        }
    }

    @Override
    public List<String> getName() throws SQLException {
        return null;
    }

    @Override
    public int getIDByNom(String nomEtablissement) throws SQLException {
        return 0;
    }
    @Override
    public String getNameByID(int id) throws SQLException {
        return "";
    }
    @Override
    public List<Event> getByPartialName(String partialName) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event WHERE nameEvent  LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, partialName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Event e = new Event();
                    e.setIdEvent(rs.getInt("idEvent"));
                    e.setIdPartner(rs.getInt("idPartnerCE"));
                    e.setIdEstab(rs.getInt("idEstab"));
                    e.setNameEvent(rs.getString("nameEvent"));
                    e.setDateEvent(rs.getDate("dateEvent"));
                    e.setNbrMax(rs.getInt("nbrMax"));
                    e.setPrix(rs.getDouble("prix"));
                    e.setDescription(rs.getString("description"));
                    e.setImage(rs.getString("image"));

                    events.add(e);
                }
            }
        }

        return events;
    }

    @Override
    public List<Event> getAllDESC() throws SQLException {
        String sql = "select * from event ORDER BY nameEvent DESC";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Event> events = new ArrayList<>();
        while (rs.next()) {
            Event e = new Event();
            e.setIdEvent(rs.getInt("idEvent"));
            e.setIdPartner(rs.getInt("idPartnerCE"));
            e.setIdEstab(rs.getInt("idEstab"));
            e.setNameEvent(rs.getString("nameEvent"));
            e.setDateEvent(rs.getDate("dateEvent"));
            e.setNbrMax(rs.getInt("nbrMax"));
            e.setPrix(rs.getDouble("prix"));
            e.setDescription(rs.getString("description"));
            e.setImage(rs.getString("image"));

            events.add(e);
        }
        return events;
    }

    @Override
    public List<Event> getAllASC() throws SQLException {
        String sql = "select * from event ORDER BY nameEvent ASC";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Event> events = new ArrayList<>();
        while (rs.next()) {
            Event e = new Event();
            e.setIdEvent(rs.getInt("idEvent"));
            e.setIdPartner(rs.getInt("idPartnerCE"));
            e.setIdEstab(rs.getInt("idEstab"));
            e.setNameEvent(rs.getString("nameEvent"));
            e.setDateEvent(rs.getDate("dateEvent"));
            e.setNbrMax(rs.getInt("nbrMax"));
            e.setPrix(rs.getDouble("prix"));
            e.setDescription(rs.getString("description"));
            e.setImage(rs.getString("image"));

            events.add(e);
        }
        return events;
    }


    @Override
    public List<String> getByDate(Date date) throws SQLException {
        List<String> eventNames = new ArrayList<>();
        String sql = "SELECT nameEvent FROM event WHERE dateEvent = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    eventNames.add(resultSet.getString("nameEvent"));
                }
            }
        }

        return eventNames;
    }

}


