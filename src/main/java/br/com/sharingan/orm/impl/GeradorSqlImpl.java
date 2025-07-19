package br.com.sharingan.orm.impl;

import java.lang.reflect.Field;

import org.slf4j.Logger;

import br.com.sharingan.ORMSharinganApp;
import br.com.sharingan.anotacoes.Coluna;
import br.com.sharingan.anotacoes.Entidade;
import br.com.sharingan.anotacoes.Id;
import br.com.sharingan.anotacoes.Tabela;
import br.com.sharingan.orm.GeradorSql;

public class GeradorSqlImpl<T> implements GeradorSql<T> {

    private static final Logger LOGGER = ORMSharinganApp.LOGGER;

    private final Class<T> classe;

    public GeradorSqlImpl(Class<T> classe) {
        if (!classe.isAnnotationPresent(Entidade.class)) {
            throw new IllegalArgumentException("A classe " + classe.getSimpleName() + " não está anotada com @Entidade.");
        }

        boolean hasId = false;
        for (Field field : classe.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                hasId = true;
                break;
            }
        }
        if (!hasId) {
            throw new IllegalArgumentException("A classe " + classe.getSimpleName() + " deve ter um campo anotado com @Id.");
        }

        this.classe = classe;
    }

    @Override
    public String gerarSelectTodos() {
        String nomeTabela = obterNomeTabela(classe);
        String comandoSql = "SELECT * FROM " + nomeTabela;
        LOGGER.info("SQL: \n\t" + comandoSql);
        return comandoSql;
    }

    @Override
    public String gerarSelectPorId(Long id) {
        String nomeTabela = obterNomeTabela(classe);
        String nomeColunaId = obterNomeColunaId(classe);
        String comandoSql = "SELECT * FROM " + nomeTabela + " WHERE " + nomeColunaId + " = ?";
        LOGGER.info("SQL: " + comandoSql);
        return comandoSql;
    }

    @Override
    public String gerarInsert(T t) throws IllegalArgumentException, IllegalAccessException {
        String nomeTabela = obterNomeTabela(classe);
        String comandoSql = "INSERT INTO " + nomeTabela;
        String colunasDeclaradasSql = " (";
        String valores = ") VALUES (";
        String comandoSqlFinal = "";

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                LOGGER.info("id ignorado");
                continue;
            }
            if (field.get(t) != null) {
                colunasDeclaradasSql += obterNomeColuna(field) + ", ";
                valores += "?, ";
            }
        }
        comandoSqlFinal = comandoSql + colunasDeclaradasSql.substring(0, colunasDeclaradasSql.length() - 2)
                + valores.substring(0, valores.length() - 2) + ")";

        LOGGER.info("SQL: " + comandoSqlFinal);
        return comandoSqlFinal;
    }

    @Override
    public String gerarUpdate(T t) throws IllegalArgumentException, IllegalAccessException {
        String nomeTabela = obterNomeTabela(classe);
        StringBuilder comandoSqlUpdate = new StringBuilder();
        comandoSqlUpdate.append("UPDATE ").append(nomeTabela).append(" SET ");
        String nomeColunaId = obterNomeColunaId(classe);
        Long idValor = obterValorId(t);
        String clausulaWhereSql = "WHERE " + nomeColunaId + " = " + idValor;
        String comandoSqlFinal = "";
        for (Field field : classe.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }
            if (idValor == null) {
                LOGGER.info("Id nulo, criando o objeto: " + nomeTabela);
                return "";
            }
            if (field.get(t) != null) {
                comandoSqlUpdate.append(obterNomeColuna(field)).append(" = ?, ");
            }
        }
        comandoSqlFinal = comandoSqlUpdate.substring(0, comandoSqlUpdate.length() - 2) + " " + clausulaWhereSql;
        LOGGER.info("SQLFINAL: {0}", comandoSqlFinal);

        return comandoSqlFinal;
    }

    @Override
    public String gerarDeleteTodos() {
        String nomeTabela = obterNomeTabela(classe);
        String comandoSqlDelete = "DELETE FROM " + nomeTabela;
        String clausulaWhereSql = "WHERE id > 0";
        String comandoSqlFinal = comandoSqlDelete + " " + clausulaWhereSql;
        LOGGER.info("SQL:  {0}", comandoSqlFinal);
        return comandoSqlFinal;
    }

    @Override
    public Class<T> obterClasse() {
        return classe;
    }

    private String obterNomeTabela(Class<?> classe) {
        if (classe.isAnnotationPresent(Tabela.class)) {
            Tabela tabela = classe.getAnnotation(Tabela.class);
            if (!tabela.value().isEmpty()) {
                return tabela.value();
            }
        }
        return classe.getSimpleName().toLowerCase();
    }

    private String obterNomeColunaId(Class<?> classe) {
        for (Field field : classe.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Id idAnnotation = field.getAnnotation(Id.class);
                if (!idAnnotation.value().isEmpty()) {
                    return idAnnotation.value();
                } else {
                    return field.getName();
                }
            }
        }
        return "id"; // Default to "id" if no @Id annotation is found
    }

    private String obterNomeColuna(Field field) {
        if (field.isAnnotationPresent(Coluna.class)) {
            Coluna coluna = field.getAnnotation(Coluna.class);
            if (!coluna.nome().isEmpty()) {
                return coluna.nome();
            } else {
                return field.getName().toLowerCase();
            }
        }
        return field.getName().toLowerCase();
    }

    private Long obterValorId(T t) throws IllegalArgumentException, IllegalAccessException {
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                return (Long) field.get(t);
            }
        }
        return null;
    }
}
