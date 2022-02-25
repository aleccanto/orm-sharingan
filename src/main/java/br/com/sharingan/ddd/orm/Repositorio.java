package br.com.sharingan.ddd.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import br.com.sharingan.ddd.orm.entidade.Entidade;

public interface Repositorio<T extends Entidade> {

	List<T> findAll(Class<T> clazz) throws SQLException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;

	T findById(Class<T> clazz, Long id) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException;

	T create(Class<T> clazz, T t) throws IllegalArgumentException, IllegalAccessException;

	T update(Class<T> clazz, T t) throws IllegalArgumentException, IllegalAccessException;

	Boolean deleteAll(Class<T> clazz);
}
