package br.com.sharingan.ddd.orm;

import java.util.List;

import br.com.sharingan.ddd.orm.entidade.Entidade;

public interface Repositorio<T extends Entidade> {

	List<T> findAll();

	T findById(Long id);

	T create(T t);

	T update(T t);

	Boolean deleteAll();
}
