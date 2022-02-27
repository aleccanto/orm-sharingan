package br.com.sharingan.ddd.orm.sql;

import br.com.sharingan.ddd.orm.entidade.Entidade;

public interface GeradorSql<T extends Entidade> {

    Class<T> classeUsada();

    String gerarSelectTodos();

    String gerarSelectFindById(Long id);

    String gerarInsert(T t) throws IllegalArgumentException, IllegalAccessException;

    String gerarUpdate(T t) throws IllegalArgumentException, IllegalAccessException;

    String deleteAll();

}
