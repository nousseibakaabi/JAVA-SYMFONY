package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;
import utils.myBD;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserService implements IService<User> {

    private Connection connection;

    public UserService() {
        connection = myBD.getInstance().getConnection();
    }

    @Override
    public User getByEmail(String e) {
        User user = null;
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, e);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setPassword(rs.getString("password"));
                user.setRoles(rs.getString("roles"));
                user.setQuestion(rs.getString("question"));
                user.setAnswer(rs.getString("answer"));
                user.setStatus(rs.getString("Status"));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return user;
    }

    public boolean userExist(String e) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, e);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public void add(User t) {
        try {
            if (userExist(t.getEmail())) {
                System.out.println("User already exists");
                return;
            }

            String query = "INSERT INTO user (roles, name, email, password, address, question, answer, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, String.join(",", t.getRoles())); // Join roles into a single string
                ps.setString(2, t.getName());
                ps.setString(3, t.getEmail());
                ps.setString(4, t.getPassword());
                ps.setString(5, t.getAddress());
                ps.setString(6, t.getQuestion());
                ps.setString(7, t.getAnswer());
                ps.setString(8, t.getStatus());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User added successfully:");
                    System.out.println("Name: " + t.getName());
                    System.out.println("Email: " + t.getEmail());
                    // Print other user details as needed
                } else {
                    System.out.println("Failed to add user");
                }
            }

        } catch (SQLException exp) {
            // Handle the exception appropriately
            System.err.println("Error adding user: " + exp.getMessage());
            exp.printStackTrace(); // Log the full stack trace for debugging
        }
    }

    @Override
    public void delete(String email) throws SQLException {
        if (!userExist(email)) {
            System.out.println("User does not exist");
            return;
        }
        String query = "DELETE FROM user WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    @Override
    public void update(User t) throws SQLException {
        String query = "UPDATE user SET address = ?, password = ?, Status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, t.getAddress());
            ps.setString(2, t.getPassword());
            ps.setString(3, t.getStatus());
            ps.setInt(4, t.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public ObservableList<User> getAll() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT * FROM user";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setPassword(rs.getString(("password")));
                user.setRoles(rs.getString("roles"));
                user.setQuestion(rs.getString("question"));
                user.setAnswer(rs.getString("answer"));
                user.setStatus(rs.getString("Status"));
                users.add(user);
            }
        } catch (SQLException exp) {
            System.out.println(exp);
        }
        return users;
    }

    public User getUserByUsername(String e) throws SQLException {
        if (userExist(e)) {
            User user = new User();
            String query = "SELECT * FROM user WHERE name = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, e);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setAddress(rs.getString("address"));
                    user.setPassword(rs.getString("password"));
                    user.setRoles(rs.getString("roles"));
                    user.setQuestion(rs.getString("question"));
                    user.setAnswer(rs.getString("answer"));
                    user.setStatus(rs.getString("Status"));
                }
            }
            return user;
        }
        return new User();
    }

    public void updatePassword(User t, String email) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, t.getPassword());
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }

    public void deactivateAccount(String email) throws SQLException {
        String query = "UPDATE user SET Status = 'Inactive' WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    public void activateAccount(String email) throws SQLException {
        String query = "UPDATE user SET Status = 'Active' WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    public boolean isAccountInactive(String email) {
        User user = getByEmail(email);
        return user != null && "Inactive".equals(user.getStatus());
    }

    public Map<String, Long> getCountsByRoles() {
        Map<String, Long> rolesCounts = new HashMap<>();
        String query = "SELECT roles, COUNT(*) FROM user GROUP BY roles";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                String roles = rs.getString("roles");
                long count = rs.getLong("COUNT(*)");
                rolesCounts.put(roles, count);
            }
        } catch (SQLException exp) {
            System.out.println(exp);
        }
        return rolesCounts;
    }
}
