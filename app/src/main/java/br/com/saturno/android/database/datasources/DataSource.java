package br.com.saturno.android.database.datasources;

import java.util.List;

import br.com.saturno.android.database.entities.Entity;

/**
 * Created by andre on 30/11/2015.
 */
public interface DataSource <T extends Entity> {

    boolean add(T ds);

    T get(T ds);

    List<T> getAll();

    int update(T ds);

    int delete(T ds);

    int getCount();

}
