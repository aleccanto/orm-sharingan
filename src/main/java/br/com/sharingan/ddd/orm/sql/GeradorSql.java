package br.com.sharingan.ddd.orm.sql;

import br.com.sharingan.ddd.orm.entidade.Entidade;

public interface GeradorSql<T extends Entidade> {

    String gerarSelect(Class<T> clazz, T t);

    String gerarInsert(Class<T> clazz, T t);

    String gerarUpdate(T t) throws IllegalArgumentException, IllegalAccessException;

}
