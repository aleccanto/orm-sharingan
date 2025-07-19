package br.com.sharingan.domain.orm.sql;

import br.com.sharingan.domain.orm.entidade.Entidade;

public interface GeradorSql<T extends Entidade> {

    Class<T> obterClasse();

    String gerarSelectTodos();

    String gerarSelectPorId(Long id);

    String gerarInsert(T t) throws IllegalArgumentException, IllegalAccessException;

    String gerarUpdate(T t) throws IllegalArgumentException, IllegalAccessException;

    String gerarDeleteTodos();

}
