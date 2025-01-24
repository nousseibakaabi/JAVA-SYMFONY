package services;

import models.Apprenant;

import java.sql.SQLException;
import java.util.List;

public interface IServiceApprenant<T> {
    void add(Apprenant apprenant) throws SQLException;

    void update(Apprenant apprenant, int id) throws SQLException;


     //void delete(String email) throws SQLException;
    void delete(int id) throws SQLException;

    List<Apprenant> getAll() throws SQLException;
    Apprenant getById(int id) throws  SQLException;
    //Apprenant getByEmail(String Email) throws  SQLException;
}
