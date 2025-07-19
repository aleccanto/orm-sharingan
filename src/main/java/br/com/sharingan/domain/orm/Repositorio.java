package br.com.sharingan.domain.orm;

import java.util.List;

import br.com.sharingan.domain.orm.entidade.Entidade;

public interface Repositorio<T extends Entidade> {

	List<T> buscarTodos();

	T buscarPorId(Long id);

	T criar(T t);

	T atualizar(T t);

	Boolean excluirTodos();
}
