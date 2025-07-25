package br.com.sharingan.orm;

import java.util.List;

public interface Repositorio<T> {

	List<T> buscarTodos();

	T buscarPorId(Long id);

	T criar(T t);

	T atualizar(T t);

	Boolean excluirTodos();
}
