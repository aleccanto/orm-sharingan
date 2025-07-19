package br.com.sharingan.orm;

public interface GeradorSql<T> {

    Class<T> obterClasse();

    String gerarSelectTodos();

    String gerarSelectPorId(Long id);

    String gerarInsert(T t) throws IllegalArgumentException, IllegalAccessException;

    String gerarUpdate(T t) throws IllegalArgumentException, IllegalAccessException;

    String gerarDeleteTodos();

}
