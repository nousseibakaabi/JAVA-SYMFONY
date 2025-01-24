package services;
import java.sql.SQLException;
import java.util.List;

public interface ICertifServices <T>{
    void addCertificate(T t) throws SQLException;

    void updateCertificate(T t,int id) throws SQLException;

    void deleteCertificate(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws  SQLException;
}


