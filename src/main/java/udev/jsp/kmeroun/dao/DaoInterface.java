package udev.jsp.kmeroun.dao;

import java.io.Serializable;
import java.util.List;

public interface DaoInterface<T, Id extends Serializable> {

    public void save(T entity);

    public void update(T entity);

    public void delete(T entity);

    public T get(Id id);

    public List<T> findAll();

    public void deleteAll();

}
