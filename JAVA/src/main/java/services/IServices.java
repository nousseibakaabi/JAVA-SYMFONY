package services;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T> {

    void add(T t) throws SQLException;

    void update(T t,int id) throws SQLException;

    void delete(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws  SQLException;

    List<String> getName() throws SQLException;

    int getIDByNom(String name) throws SQLException;
    String getNameByID(int id) throws SQLException;
    List<T> getByPartialName(String partialName) throws SQLException;

    List<T> getAllDESC() throws SQLException;
    List<T> getAllASC() throws SQLException;
    List<String> getByDate(java.sql.Date date) throws SQLException;

}
