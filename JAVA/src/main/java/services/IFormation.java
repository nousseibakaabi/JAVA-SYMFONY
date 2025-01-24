package services;


import models.Formation;

import java.sql.SQLException;
import java.util.List;
public interface IFormation<F> {



    public interface IFormationServices <F>{
        void addFormation(F f) throws SQLException;

        void updateFormation(F f) throws SQLException;

        void deleteFormation(int id_formation) throws SQLException;

        List<F> getAll() throws SQLException;

        F getById(int id_formation) throws  SQLException;

        public List<Formation> getFormationList();

        public List<Formation> trierParCategorie() throws SQLException;

        public List<Formation> trierParNiveau() throws SQLException;

    }
}
