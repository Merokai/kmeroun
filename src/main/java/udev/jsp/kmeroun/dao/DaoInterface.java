package udev.jsp.kmeroun.dao;

import java.io.Serializable;
import java.util.List;

public interface DaoInterface<T, Id extends Serializable> {

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    T get(Id id);

    List<T> findAll();

    void deleteAll();

}
