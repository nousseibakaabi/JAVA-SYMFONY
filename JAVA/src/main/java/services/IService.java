package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void add(T t) throws SQLException;
    void delete(String email) throws SQLException;
    void update(T t ) throws SQLException;
    T getByEmail(String e) throws SQLException;
    List<T> getAll() throws SQLException;


}
