package br.com.sharingan.orm.impl;

import java.lang.reflect.Field;

import org.slf4j.Logger;

import br.com.sharingan.ORMSharinganApp;
import br.com.sharingan.orm.Entidade;
import br.com.sharingan.orm.GeradorSql;

public class GeradorSqlImpl<T extends Entidade> implements GeradorSql<T> {

    private static final Logger LOGGER = ORMSharinganApp.LOGGER;

    private final Class<T> classe;

    public GeradorSqlImpl(Class<T> classe) {
        this.classe = classe;
    }

    @Override
    public String gerarSelectTodos() {
        String nomeClasse = classe.getSimpleName().toLowerCase();
        String comandoSql = "SELECT * FROM " + nomeClasse;
        LOGGER.info("SQL: \n\t" + comandoSql);
        return comandoSql;
    }

    @Override
    public String gerarSelectPorId(Long id) {
        String nomeClasse = classe.getSimpleName().toLowerCase();
        String comandoSql = "SELECT * FROM " + nomeClasse + " WHERE id = ?";
        LOGGER.info("SQL: " + comandoSql);
        return comandoSql;
    }

    @Override
    public String gerarInsert(T t) throws IllegalArgumentException, IllegalAccessException {
        String nomeClasse = classe.getSimpleName().toLowerCase();
        String comandoSql = "INSERT INTO " + nomeClasse;
        String colunasDeclaradasSql = " (";
        String valores = ") VALUES (";
        String comandoSqlFinal = "";

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if ("id".equals(field.getName())) {
                LOGGER.info("id ignorado");
                continue;
            }
            if (field.get(t) != null) {
                colunasDeclaradasSql += field.getName().toLowerCase() + ", ";
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
        String nomeClasse = classe.getSimpleName().toLowerCase();
        StringBuilder comandoSqlUpdate = new StringBuilder();
        comandoSqlUpdate.append("UPDATE ").append(nomeClasse).append(" SET ");
        String clausulaWhereSql = "WHERE id = " + t.getId();
        String comandoSqlFinal = "";
        for (Field field : classe.getDeclaredFields()) {
            field.setAccessible(true);
            if (t.getId() == null) {
                LOGGER.info("Id nulo, criando o objeto: " + nomeClasse);
                return "";
            }
            if (field.get(t) != null && !"id".equals(field.getName())) {
                comandoSqlUpdate.append(field.getName()).append(" = ?, ");
            }
        }
        comandoSqlFinal = comandoSqlUpdate.substring(0, comandoSqlUpdate.length() - 2) + " " + clausulaWhereSql;
        LOGGER.info("SQLFINAL: {0}", comandoSqlFinal);

        return comandoSqlFinal;
    }

    @Override
    public String gerarDeleteTodos() {
        String comandoSqlDelete = "DELETE FROM " + classe.getSimpleName().toLowerCase();
        String clausulaWhereSql = "WHERE id > 0";
        String comandoSqlFinal = comandoSqlDelete + " " + clausulaWhereSql;
        LOGGER.info("SQL:  {0}", comandoSqlFinal);
        return comandoSqlFinal;
    }

    @Override
    public Class<T> obterClasse() {
        return classe;
    }

}
