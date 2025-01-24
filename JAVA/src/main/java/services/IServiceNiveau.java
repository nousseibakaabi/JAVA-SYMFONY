package services;

import models.Niveau;

import java.sql.SQLException;
import java.util.List;

public interface IServiceNiveau {
    void add(Niveau niveau) throws SQLException;

    void update(Niveau niveau, int id) throws SQLException;


    void delete(int id) throws SQLException;

    List<Niveau> getAll() throws SQLException;
    Niveau getById(int id) throws  SQLException;
    //Niveau getByName(String name) throws  SQLException;
    //public List<String> getName() throws  SQLException;

}
