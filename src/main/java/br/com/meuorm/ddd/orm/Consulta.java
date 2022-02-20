package br.com.meuorm.ddd.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Consulta<T> {

	List<T> findAll(Class<T> t)
			throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException;

	T findById(Class<T> t, Long id) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException;
}
