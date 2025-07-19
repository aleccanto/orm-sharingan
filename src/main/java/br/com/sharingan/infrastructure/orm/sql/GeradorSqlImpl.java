package br.com.sharingan.infrastructure.orm.sql;

import java.lang.reflect.Field;

import org.slf4j.Logger;

import br.com.sharingan.ORMSharinganApp;
import br.com.sharingan.domain.orm.entidade.Entidade;
import br.com.sharingan.domain.orm.sql.GeradorSql;

public class GeradorSqlImpl<T extends Entidade> implements GeradorSql<T> {

    private static final Logger LOGGER = ORMSharinganApp.LOGGER;

    private final Class<T> clazz;

    public GeradorSqlImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String gerarSelectTodos() {
        String classeName = clazz.getSimpleName().toLowerCase();
        String sql = "SELECT * FROM " + classeName;
        LOGGER.info("SQL: \n\t" + sql);
        return sql;
    }

    @Override
    public String gerarSelectPorId(Long id) {
        String classeName = clazz.getSimpleName().toLowerCase();
        String sql = "SELECT * FROM " + classeName + " WHERE id = ?";
        LOGGER.info("SQL: " + sql);
        return sql;
    }

    @Override
    public String gerarInsert(T t) throws IllegalArgumentException, IllegalAccessException {
        String nomeClasse = clazz.getSimpleName().toLowerCase();
        String sql = "INSERT INTO " + nomeClasse;
        String sqlDeclaredValues = " (";
        String values = ") VALUES (";
        String sqlFinal = "";

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if ("id".equals(field.getName())) {
                LOGGER.info("id ignorado");
                continue;
            }
            if (field.get(t) != null) {
                sqlDeclaredValues += field.getName().toLowerCase() + ", ";
                values += "?, ";
            }
        }
        sqlFinal = sql + sqlDeclaredValues.substring(0, sqlDeclaredValues.length() - 2)
                + values.substring(0, values.length() - 2) + ")";

        LOGGER.info("SQL: \n\t" + sqlFinal);
        return sqlFinal;
    }

    @Override
    public String gerarUpdate(T t) throws IllegalArgumentException, IllegalAccessException {
        String className = clazz.getSimpleName().toLowerCase();
        StringBuilder sqlUpdate = new StringBuilder();
        sqlUpdate.append("UPDATE ").append(className).append(" SET ");
        String sqlWhere = "WHERE id = " + t.getId();
        String sqlFInal = "";
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (t.getId() == null) {
                LOGGER.info("Id nulo, criando o objeto: " + className);
                return "";
            }
            if (field.get(t) != null && !"id".equals(field.getName())) {
                sqlUpdate.append(field.getName()).append(" = ?, ");
            }
        }
        sqlFInal = sqlUpdate.substring(0, sqlUpdate.length() - 2) + " " + sqlWhere;
        LOGGER.info("SQLFINAL: {0}", sqlFInal);

        return sqlFInal;
    }

    @Override
    public String gerarDeleteTodos() {
        String sqlDelete = "DELETE FROM " + clazz.getSimpleName().toLowerCase();
        String sqlWhere = "WHERE id > 0";
        String sqlFinal = sqlDelete + " " + sqlWhere;
        LOGGER.info("SQL: \n\t{0}", sqlFinal);
        return sqlFinal;
    }

    @Override
    public Class<T> obterClasse() {
        return clazz;
    }

}
